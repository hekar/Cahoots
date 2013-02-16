/// Package class
/// Codeora 2013
///

namespace Cahoots
{
    using System;
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.ComponentModel;
    using System.ComponentModel.Design;
    using System.IO;
    using System.Linq;
    using System.Runtime.InteropServices;
    using System.Threading;
    using System.Windows.Forms;
    using Cahoots.Ext;
    using Cahoots.Services;
    using Cahoots.Services.Contracts;
    using Cahoots.Services.Models;
    using Cahoots.Services.ViewModels;
    using Cahoots.Views.Custom;
    using EnvDTE;
    using Microsoft.VisualStudio.CommandBars;
    using Microsoft.VisualStudio.Shell;
    using Microsoft.VisualStudio.Shell.Interop;
    using Microsoft.VisualStudio.Text.Editor;
    using WebSocketSharp;

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
            this.InitializePreferences();
            this.InitializeMessagingSystem();
            this.ApplicationObject = GetService(typeof(EnvDTE.DTE)) as _DTE;

            // find references to the toolbar buttons
            FindToolbarButtons();

            // setup context menus
            SetupContextMenus();
        }

        /// <summary>
        /// Initializes the messaging system.
        /// </summary>
        private void InitializeMessagingSystem()
        {
            this.CommunicationRelay = new MessageRelay(
                    new UsersService(),
                    new ChatService(this, this.Preferences),
                    new OpService(this));
        }
        
        /// <summary>
        /// Sets up the context menus.
        /// </summary>
        private void SetupContextMenus()
        {
            var menus = this.ApplicationObject.CommandBars as CommandBars;
            var editorMenu = menus["Code Window"];

            // add the cahoots menu item
            CommandBarPopup cahootsMenu = (CommandBarPopup)
                    editorMenu.Controls.Add(MsoControlType.msoControlPopup,
                    System.Reflection.Missing.Value,
                    System.Reflection.Missing.Value, 1, true);

            // Set the caption of the menuitem
            cahootsMenu.Caption = "Cahoots";

            // add the share submenu item
            ShareMenuCommand =
              cahootsMenu.Controls.Add(MsoControlType.msoControlButton,
              System.Reflection.Missing.Value,
              System.Reflection.Missing.Value, 1, true);

            // Set the caption of the submenuitem
            ShareMenuCommand.Caption = "Share";
            ShareMenuCommand.Enabled = false;

            this.ShareMenuHander =
                    this.ApplicationObject.Events.get_CommandBarEvents(
                                ShareMenuCommand) as CommandBarEventsClass;
            this.ShareMenuHander.Click += OpenShareStarter;
        }

        /// <summary>
        /// Initializes the preferences.
        /// </summary>
        private void InitializePreferences()
        {
            // get paths and stuff
            var path =
                Environment.GetFolderPath(Environment.SpecialFolder.Personal);
            var root = path + @"\Visual Studio 2010\Cahoots\";
            var prefs = root + @"preferences.xml";

            if (!Directory.Exists(root))
            {
                Directory.CreateDirectory(root);
            }

            if (File.Exists(prefs))
            {
                // read in preferences
                using (var stream = File.OpenRead(prefs))
                using (var reader = new StreamReader(stream))
                {
                    var xml = reader.ReadToEnd();
                    this.Preferences = XmlHelper.Deserialize<Preferences>(xml);
                }
            }
            else
            {
                // need to create default preferences...
                this.Preferences = new Preferences()
                {
                    ChatLogsDirectory = root + @"chat logs\"
                };

                this.Preferences.Servers.Add(new Server()
                {
                    Name = "Localhost",
                    Address = "http://localhost:9000/"
                });

                // write to file
                using (var stream = File.Create(prefs))
                using (var writer = new StreamWriter(stream))
                {
                    var xml = XmlHelper.Serialize(this.Preferences);
                    writer.Write(xml);
                }
            }

            if (!Directory.Exists(this.Preferences.ChatLogsDirectory))
            {
                Directory.CreateDirectory(this.Preferences.ChatLogsDirectory);
            }
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
        /// Gets or sets the application object.
        /// </summary>
        /// <value>
        /// The application object.
        /// </value>
        private _DTE ApplicationObject { get; set; }

        /// <summary>
        /// Gets or sets the share menu hander.
        /// </summary>
        /// <value>
        /// The share menu hander.
        /// </value>
        private CommandBarEventsClass ShareMenuHander { get; set; }

        /// <summary>
        /// Gets or sets the share menu command.
        /// </summary>
        /// <value>
        /// The share menu command.
        /// </value>
        private CommandBarControl ShareMenuCommand { get; set; }

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
        public string UserName { get; set; }

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

        /// <summary>
        /// Gets or sets the preferences.
        /// </summary>
        /// <value>
        /// The preferences.
        /// </value>
        public Preferences Preferences { get; private set; }

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
            this.UsersButton =
                    this.GetMenuCommand(PkgCmdIDList.UsersToolbarButton);

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
            var window = new ConnectWindow(
                        this.Preferences.Servers.Select(s => s.Name));
            if (window.ShowDialog() == true)
            {
                this.ApplicationObject.StatusBar.Text =
                        "Connecting to Cahoots...";

                // create a new authentication service for this connection.
                var server = new Uri(
                    this.Preferences.Servers
                            .Single(s => s.Name == window.Server).Address);
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

                    this.UserName = this.AuthenticationService.UserName;

                    var address = string.Format(
                            "ws://{0}:{1}/app/message?auth_token={2}",
                            server.Host,
                            server.Port.ToString(),
                            this.AuthenticationService.Token);

                    this.Socket = new WebSocket(address);

                    this.CommunicationRelay.SetSocket(this.Socket);
                    this.CommunicationRelay.SetUserName(this.UserName);

                    this.Socket.Connect();

                    this.ApplicationObject.StatusBar.Text =
                                            "Cahoots Connected!";
                    ShareMenuCommand.Enabled = true;
                }
                else
                {
                    this.ApplicationObject.StatusBar.Text =
                                            "Cahoots Connection Error...";

                    // display error message.
                    var retry = MessageBox.Show(
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
                bg.DoWork += new DoWorkEventHandler(
                    (s, ev) => this.AuthenticationService.Deauthenticate());
                bg.RunWorkerAsync();
                this.ConnectButton.Enabled = true;
                this.DisconnectButton.Enabled = false;

                foreach (var frame in this.WindowFrames)
                {
                    frame.CloseFrame((int)__FRAMECLOSE.FRAMECLOSE_NoSave);
                }

                this.WindowFrames.Clear();

                this.Socket.Close();
                this.Socket.Dispose();
                this.Socket = null;
                this.CommunicationRelay.SetSocket(null);

                this.ApplicationObject.StatusBar.Text = "Cahoots Connected!";
                this.ShareMenuCommand.Enabled = false;
            }
        }

        #endregion

        /// <summary>
        /// Preferences the button execute handler.
        /// </summary>
        /// <param name="sender">The sender.</param>
        /// <param name="e">
        ///   The <see cref="EventArgs" /> instance containing the event data.
        /// </param>
        protected override void PreferenceButtonExecuteHandler(
                object sender,
                EventArgs e)
        {
            this.ShowPreferences();
        }

        /// <summary>
        /// Shows the preferences.
        /// </summary>
        public bool ShowPreferences()
        {
            var window = new PreferencesWindow(this.Preferences);
            var b = window.ShowDialog() ?? false;
            if (b)
            {
                // save preferences
                var path = Environment.GetFolderPath(
                            Environment.SpecialFolder.Personal);
                var root = path + @"\Visual Studio 2010\Cahoots\";
                var prefs = root + @"preferences.xml";

                using (var stream = File.Create(prefs))
                using (var writer = new StreamWriter(stream))
                {
                    var xml = XmlHelper.Serialize(this.Preferences);
                    writer.Write(xml);
                }
            }

            return b;
        }

        /// <summary>
        /// Invokes an action on the UI thread.
        /// </summary>
        /// <param name="action">The action.</param>
        public void InvokeOnUI(Action action)
        {
            UIContext.Send(_ => action(), null);
        }

        /// <summary>
        /// Opens the share starter.
        /// </summary>
        /// <param name="control">The control.</param>
        /// <param name="handled">if set to <c>true</c> [handled].</param>
        /// <param name="cancelDefault">
        ///   if set to <c>true</c> [cancel default].
        /// </param>
        protected void OpenShareStarter(
                object control,
                ref bool handled,
                ref bool cancelDefault)
        {
            var users = this.CommunicationRelay.Service<UsersService>();
            var window = new SelectCollaborators(users.GetCollaborators());

            if (window.ShowDialog() == true)
            {
                var collaborators = window.Selected.Select(c => c.UserName);

                var solution = this.ApplicationObject.Solution.FullName;
                var solutionPath = solution.Substring(
                        0,
                        solution.LastIndexOf('\\'));

                var documentPath =
                        this.ApplicationObject.ActiveDocument.FullName;
                var documentId =
                    documentPath.Replace(solutionPath, "").Replace('\\', '/');

                this.CommunicationRelay.Service<OpService>()
                        .StartCollaboration(
                                this.UserName,
                                documentId,
                                collaborators.ToList());
                
                //var view = this.ApplicationObject.GetEditorView(active.FullName);

                //(this.CommunicationRelay.Services["op"] as OpService).AddSharedDocument(active.FullName, view);
            }
        }

        /// <summary>
        /// Gets the view model.
        /// </summary>
        /// <param name="service">The service.</param>
        /// <param name="parameters">The parameters.</param>
        /// <returns>The service view model.</returns>
        public BaseViewModel GetViewModel(
                    string service,
                    params object[] parameters)
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
        /// Opens a chat window.
        /// </summary>
        /// <param name="user">The user.</param>
        public void OpenChatWindow(Collaborator user)
        {
            if (!Chats.ContainsKey(user.Name))
            {
                var pane = CahootsPackage.Instance.FindToolWindow(
                            typeof(ChatWindowToolWindow),
                            this.WindowIndex++,
                            true);

                pane.Caption = "Chat — " + user.Name;
                Chats.Add(user.Name, pane);
                var frame = (IVsWindowFrame)pane.Frame;
                this.WindowFrames.Add(frame);
                var win = pane.As<ChatWindowToolWindow>().Content
                                                as ChatWindowControl;
                var vm = this.GetViewModel("chat", user, this.UserName)
                                                as ChatViewModel;
                win.ViewModel = vm;
                frame.Show();
            }
            else
            {
                Chats[user.Name].Frame.As<IVsWindowFrame>().Show();
            }
        }

        /// <summary>
        /// Opens a chat window.
        /// </summary>
        /// <param name="username">The username.</param>
        public void OpenChatWindow(string username)
        {
            var service =
                    this.CommunicationRelay.Services["users"] as UsersService;
            var user = service.GetCollaborators().FirstOrDefault(
                                c => c.UserName == username);

            if (user != null)
            {
                this.InvokeOnUI(() => this.OpenChatWindow(user));
            }
        }

        /// <summary>
        /// Opens the document window.
        /// </summary>
        /// <param name="filePath">The file path.</param>
        public Tuple<string, IWpfTextView> OpenDocumentWindow(string filePath)
        {
            try
            {
                var solution = this.ApplicationObject.Solution.FullName;
                var solutionPath = solution.Substring(
                        0,
                        solution.LastIndexOf('\\'));

                var path = solutionPath + filePath.Replace('/', '\\');

                return new Tuple<string, IWpfTextView>(path, this.ApplicationObject.GetEditorView(path));
            }
            catch
            {
                return null;
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
                if (this.Socket.ReadyState == WsState.OPEN)
                {
                    this.Socket.Close();
                }

                this.Socket.Dispose();
            }

            foreach (var frame in this.WindowFrames)
            {
                frame.CloseFrame((int)__FRAMECLOSE.FRAMECLOSE_NoSave);
            }

            base.Dispose(disposing);
        }
    }
}