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
        /// Gets or sets the menu service.
        /// </summary>
        /// <value>
        /// The menu service.
        /// </value>
        private OleMenuCommandService MenuService { get; set; }

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
                this.GetMenuCommand(
                        PkgCmdIDList.ConnectToolbarButton).Enabled = false;
                this.GetMenuCommand(
                        PkgCmdIDList.DisconnectToolbarButton).Enabled = true;
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
                this.GetMenuCommand(
                    PkgCmdIDList.ConnectToolbarButton).Enabled = true;
                this.GetMenuCommand(
                    PkgCmdIDList.DisconnectToolbarButton).Enabled = false;
            }
        }
    }
}