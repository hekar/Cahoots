///
///
///

namespace Cahoots
{
    using System;
    using System.Runtime.InteropServices;
    using System.Windows.Forms;
    using System.ComponentModel.Design;
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
        /// Gets or sets the menu service.
        /// </summary>
        /// <value>
        /// The menu service.
        /// </value>
        private OleMenuCommandService MenuService { get; set; }

        private MenuCommand Connect { get; set; }
        private MenuCommand Disconnect { get; set; }
        private MenuCommand Host { get; set; }
        private MenuCommand Stop { get; set; }

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
                // connect to the server here...
                this.Connect.Enabled = false;
                this.Disconnect.Enabled = true;
                this.Host.Enabled = true;
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