/// Package class
/// Codeora 2013
///

namespace Cahoots
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.ComponentModel.Design;
    using System.Linq;
    using System.Runtime.InteropServices;
    using System.Threading;
    using System.Windows.Forms;

    using Cahoots.Services;
    using Cahoots.Services.Contracts;
    using Cahoots.Services.Models;
    using Cahoots.Services.ViewModels;
    using Microsoft.VisualStudio.Shell;
    using Microsoft.VisualStudio.Shell.Interop;
    using WebSocketSharp;
    using System.Collections.ObjectModel;

    /// <summary>
    /// Cahoots VSPackage Extension class.
    /// </summary>
    [Guid(GuidList.guidCahootsPkgString)]
    public class CahootsPackage : CahootsPackageBase, IWindowService
    {
        #region Initialization

        /// <summary>
        /// Initializes a new instance of the 
        /// <see cref="CahootsPackage" /> class.
        /// </summary>
        public CahootsPackage() : base()
        {
            this.MenuService =
                    GetService(typeof(IMenuCommandService))
                        as OleMenuCommandService;

            Instance = this;
            this.Chats = new Dictionary<string, ToolWindowPane>();

            this.UIContext = SynchronizationContext.Current;
            this.WindowFrames = new Collection<IVsWindowFrame>();
        }

        /// <summary>
        /// Initialization of the package;
        /// this method is called right after the package is sited,
        /// so this is the place where you can put all the initilaization
        /// code that rely on services provided by VisualStudio.
        /// </summary>
        protected override void Initialize()
        {
            base.Initialize();
            this.InitializeMessagingSystem();

            // find references to the toolbar buttons
            FindToolbarButtons();
        }

        /// <summary>
        /// Initializes the messaging system.
        /// </summary>
        private void InitializeMessagingSystem()
        {
            this.CommunicationRelay = new MessageRelay(
                    new UsersService(),
                    new ChatService(this));
        }

        /// <summary>
        /// Gets the instance.
        /// </summary>
        /// <value>
        /// The instance.
        /// </value>
        public static CahootsPackage Instance { get; private set; }

        #endregion

        #region Members

        /// <summary>
        /// Gets or sets the socket.
        /// </summary>
        /// <value>
        /// The socket.
        /// </value>
        private WebSocket Socket { get; set; }

        /// <summary>
        /// Gets or sets the communication relay.
        /// </summary>
        /// <value>
        /// The communication relay.
        /// </value>
        public MessageRelay CommunicationRelay { get; private set; }

        /// <summary>
        /// Gets or sets me.
        /// </summary>
        /// <value>
        /// Me.
        /// </value>
        public string Me { get; set; }
        
        /// <summary>
        /// Gets or sets the chats.
        /// </summary>
        /// <value>
        /// The chats.
        /// </value>
        private Dictionary<string, ToolWindowPane> Chats { get; set; }

        /// <summary>
        /// Gets or sets the UI context.
        /// </summary>
        /// <value>
        /// The UI context.
        /// </value>
        private SynchronizationContext UIContext { get; set; }

        /// <summary>
        /// The window id index.
        /// </summary>
        private int WindowIndex = 0;

        #endregion

        #region Menu Buttons

        /// <summary>
        /// Gets or sets the menu service.
        /// </summary>
        /// <value>
        /// The menu service.
        /// </value>
        private OleMenuCommandService MenuService { get; set; }

        /// <summary>
        /// Gets or sets the connect menu button.
        /// </summary>
        /// <value>
        /// The connect menu button.
        /// </value>
        private MenuCommand ConnectButton { get; set; }

        /// <summary>
        /// Gets or sets the disconnect menu button.
        /// </summary>
        /// <value>
        /// The disconnect menu button.
        /// </value>
        private MenuCommand DisconnectButton { get; set; }

        /// <summary>
        /// Gets or sets the users menu button.
        /// </summary>
        /// <value>
        /// The users menu button.
        /// </value>
        private MenuCommand UsersButton { get; set; }

        /// <summary>
        /// Gets or sets the host menu button.
        /// </summary>
        /// <value>
        /// The host menu button.
        /// </value>
        private MenuCommand HostButton { get; set; }

        /// <summary>
        /// Gets or sets the stop menu button.
        /// </summary>
        /// <value>
        /// The stop menu button.
        /// </value>
        private MenuCommand StopButton { get; set; }

        /// <summary>
        /// Gets or sets the window frames.
        /// </summary>
        /// <value>
        /// The window frames.
        /// </value>
        private Collection<IVsWindowFrame> WindowFrames { get; set; }

        /// <summary>
        /// Finds the toolbar buttons.
        /// </summary>
        private void FindToolbarButtons()
        {
            this.ConnectButton =
                    this.GetMenuCommand(PkgCmdIDList.ConnectToolbarButton);
            this.DisconnectButton =
                    this.GetMenuCommand(PkgCmdIDList.DisconnectToolbarButton);
            this.UsersButton = this.GetMenuCommand(PkgCmdIDList.UsersToolbarButton);

            // we have to set this stuff again, 
            // otherwise it all gets reset to true.
            this.DisconnectButton.Enabled = false;
        }

        /// <summary>
        /// Gets the menu command.
        /// </summary>
        /// <param name="commandId">The command id.</param>
        /// <returns>The menu command.</returns>
        private MenuCommand GetMenuCommand(uint commandId)
        {
            var id = new CommandID(GuidList.guidCahootsCmdSet, (int)commandId);
            return this.MenuService.FindCommand(id);
        }

        #endregion

        #region Authentication

        /// <summary>
        /// Gets or sets the authentication service.
        /// </summary>
        /// <value>
        /// The authentication service.
        /// </value>
        private IAuthenticationService AuthenticationService { get; set; }

        /// <summary>
        /// Connects the toolbar button execute handler.
        /// </summary>
        /// <param name="sender">The sender.</param>
        /// <param name="evt">
        ///   The <see cref="EventArgs" /> instance containing the event data.
        /// </param>
        protected override void ConnectToolbarButtonExecuteHandler(
                object sender,
                EventArgs evt)
        {
            // TODO: make this async???
            var window = new ConnectWindow();
            if (window.ShowDialog() == true)
            {
                // create a new authentication service for this connection.
                var server = new Uri(window.Server);
                this.AuthenticationService =
                        new AuthenticationService(
                            server,
                            window.UserName,
                            window.Password);

                // authenticate
                if (this.AuthenticationService.Authenticate())
                {
                    this.ConnectButton.Enabled = false;
                    this.DisconnectButton.Enabled = true;
                    //this.Host.Enabled = true;

                    this.Me = this.AuthenticationService.UserName;

                    // TODO: make this based on something legit
                    this.Socket = new WebSocket(
                            "ws://localhost:9000/app/message?auth_token=" + this.AuthenticationService.Token);

                    this.CommunicationRelay.SetSocket(this.Socket);

                    this.Socket.Connect();
                }
                else
                {
                    // display error message.
                    var  retry = MessageBox.Show(
                            "An error occured authenticating with Cahoots:\r\n"
                            + this.AuthenticationService.ErrorMessage,
                            "Failed to authenticate with Cahoots.",
                            MessageBoxButtons.RetryCancel);

                    if (retry == DialogResult.Retry)
                    {
                        ConnectToolbarButtonExecuteHandler(sender, evt);
                    }
                }
            }
        }

        /// <summary>
        /// Disconnects the toolbar button execute handler.
        /// </summary>
        /// <param name="sender">The sender.</param>
        /// <param name="e">
        ///   The <see cref="EventArgs" /> instance containing the event data.
        /// </param>
        protected override void DisconnectToolbarButtonExecuteHandler(
                object sender,
                EventArgs e)
        {
            var result = MessageBox.Show(
                    "Are you sure you would like to disconnect from Cahoots?",
                    "Disconnect from Cahoots",
                    MessageBoxButtons.YesNo,
                    MessageBoxIcon.Question,
                    MessageBoxDefaultButton.Button1);

            if (result == DialogResult.Yes)
            {
                var bg = new BackgroundWorker();
                bg.DoWork +=new DoWorkEventHandler(
                    (s, ev) => this.AuthenticationService.Deauthenticate());
                bg.RunWorkerAsync();
                this.ConnectButton.Enabled = true;
                this.DisconnectButton.Enabled = false;

                foreach (var frame in this.WindowFrames)
                {
                    frame.CloseFrame((int)__FRAMECLOSE.FRAMECLOSE_NoSave);
                }

                this.Socket.Send(new byte[] { 0x1A });
                this.Socket.Close();
                this.Socket.Dispose();
                this.Socket = null;
                this.CommunicationRelay.SetSocket(null);
            }
        }

        #endregion

        /// <summary>
        /// Invokes an action on the UI thread.
        /// </summary>
        /// <param name="action">The action.</param>
        private void InvokeOnUI(Action action)
        {
            UIContext.Send(_ => action(), null);
        }

        /// <summary>
        /// Gets the view model.
        /// </summary>
        /// <param name="service">The service.</param>
        /// <param name="parameters">The parameters.</param>
        /// <returns>The service view model.</returns>
        public BaseViewModel GetViewModel(string service, params object[] parameters)
        {
            var keys = new List<string>(this.CommunicationRelay.Services.Keys);

            if (this.CommunicationRelay.Services.ContainsKey(service))
            {
                var serv = this.CommunicationRelay.Services[service];
                return serv.GetViewModel(parameters);
            }

            return null;
        }

        /// <summary>
        /// Sends to socket.
        /// </summary>
        /// <param name="message">The message.</param>
        public void SendToSocket(string message)
        {
            Socket.Send(message);
        }

        /// <summary>
        /// Opens a chat window.
        /// </summary>
        /// <param name="user">The user.</param>
        public void OpenChatWindow(Collaborator user)
        {
            if (!Chats.ContainsKey(user.Name))
            {
                var pane = CahootsPackage.Instance.FindToolWindow(typeof(ChatWindowToolWindow), this.WindowIndex++, true);
                pane.Caption = "Chat — " + user.Name;
                Chats.Add(user.Name, pane);
                var frame = (IVsWindowFrame)pane.Frame;
                frame.Show();
                this.WindowFrames.Add(frame);
                var win = (pane as ChatWindowToolWindow).Content as ChatWindowControl;
                var vm = this.GetViewModel("chat", user, this.Me) as ChatViewModel;
                win.ViewModel = vm;
            }
            else
            {
                ((IVsWindowFrame)Chats[user.Name].Frame).Show();
            }
        }

        /// <summary>
        /// Opens a chat window.
        /// </summary>
        /// <param name="username">The username.</param>
        public void OpenChatWindow(string username)
        {
            var service = this.CommunicationRelay.Services["users"] as UsersService;
            var user = service.GetCollaborators().FirstOrDefault(c => c.UserName == username);

            if (user != null)
            {
                this.InvokeOnUI(() => this.OpenChatWindow(user));
            }
        }

        /// <summary>
        /// Releases unmanaged and - optionally - managed resources.
        /// </summary>
        /// <param name="disposing">
        ///   <c>true</c> to release both managed and unmanaged resources;
        ///   <c>false</c> to release only unmanaged resources.
        /// </param>
        protected override void Dispose(bool disposing)
        {
            if (this.Socket != null)
            {
                if (this.Socket.IsAlive)
                {
                    this.Socket.Send(new byte[] { 0x1A });
                    this.Socket.Close();
                }

                this.Socket.Dispose();
            }

            base.Dispose(disposing);
        }
    }
}