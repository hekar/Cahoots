/// Cahoots Package.cs
/// 23 October 2012
///
/// The master class for the Visual Studio extension package.
///

namespace Cahoots
{
    using System;
    using System.ComponentModel.Design;
    using System.Runtime.InteropServices;
    using System.Windows.Forms;
    using Cahoots.Services;
    using Microsoft.VisualStudio.Shell;

    [Guid(GuidList.guidCahootsPkgString)]
    public class CahootsPackage : CahootsPackageBase
    {
        /// <summary>
        /// Initializes a new instance of the 
        /// <see cref="CahootsPackage" /> class.
        /// </summary>
        public CahootsPackage() : base()
        {
            this.MenuService =
                    GetService(typeof(IMenuCommandService))
                        as OleMenuCommandService;
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

            // find references to the toolbar buttons
            FindToolbarButtons();
        }

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
        private MenuCommand Connect { get; set; }

        /// <summary>
        /// Gets or sets the disconnect menu button.
        /// </summary>
        /// <value>
        /// The disconnect menu button.
        /// </value>
        private MenuCommand Disconnect { get; set; }

        /// <summary>
        /// Gets or sets the host menu button.
        /// </summary>
        /// <value>
        /// The host menu button.
        /// </value>
        private MenuCommand Host { get; set; }

        /// <summary>
        /// Gets or sets the stop menu button.
        /// </summary>
        /// <value>
        /// The stop menu button.
        /// </value>
        private MenuCommand Stop { get; set; }

        /// <summary>
        /// Finds the toolbar buttons.
        /// </summary>
        private void FindToolbarButtons()
        {
            this.Connect =
                    this.GetMenuCommand(PkgCmdIDList.ConnectToolbarButton);
            this.Disconnect =
                    this.GetMenuCommand(PkgCmdIDList.DisconnectToolbarButton);
            this.Host = this.GetMenuCommand(PkgCmdIDList.HostToolbarButton);
            this.Stop = this.GetMenuCommand(PkgCmdIDList.StopToolbarButton);

            // we have to set this stuff again, 
            // otherwise it all gets reset to true.
            this.Stop.Enabled = false;
            this.Disconnect.Enabled = false;
            this.Host.Enabled = false;
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
        /// <param name="e">
        ///   The <see cref="EventArgs" /> instance containing the event data.
        /// </param>
        protected override void ConnectToolbarButtonExecuteHandler(
                object sender,
                EventArgs e)
        {
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
                    this.Connect.Enabled = false;
                    this.Disconnect.Enabled = true;
                    this.Host.Enabled = true;
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
                        ConnectToolbarButtonExecuteHandler(sender, e);
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
                    MessageBoxIcon.Asterisk,
                    MessageBoxDefaultButton.Button1);

            if (result == DialogResult.Yes)
            {
                this.Connect.Enabled = true;
                this.Disconnect.Enabled = false;
                this.Host.Enabled = false;
                this.Stop.Enabled = false;
            }
        }
    }
}