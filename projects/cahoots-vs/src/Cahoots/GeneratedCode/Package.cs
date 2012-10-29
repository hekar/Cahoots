using System;
using System.Diagnostics;
using System.Globalization;
using System.Runtime.InteropServices;
using System.ComponentModel.Design;
using Microsoft.Win32;
using Microsoft.VisualStudio;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.OLE.Interop;
using Microsoft.VisualStudio.Shell;

namespace Cahoots
{
    /// <summary>
    /// This is the class that implements the package exposed by this assembly.
    ///
    /// The minimum requirement for a class to be considered a valid package for Visual Studio
    /// is to implement the IVsPackage interface and register itself with the shell.
    /// This package uses the helper classes defined inside the Managed Package Framework (MPF)
    /// to do it: it derives from the Package class that provides the implementation of the 
    /// IVsPackage interface and uses the registration attributes defined in the framework to 
    /// register itself and its components with the shell.
    /// </summary>
    // This attribute tells the PkgDef creation utility (CreatePkgDef.exe) that this class is
    // a package.
    [PackageRegistration(UseManagedResourcesOnly = true)]
    // This attribute is used to register the informations needed to show the this package
    // in the Help/About dialog of Visual Studio.
    [InstalledProductRegistration("#110", "#112", "1.0", IconResourceID = 400)]
    // This attribute is needed to let the shell know that this package exposes some menus.
    [ProvideMenuResource("Menus.ctmenu", 1)]
	[Guid(GuidList.guidCahootsPkgString)]
    public abstract class CahootsPackageBase : Package
    {
		/// <summary>
        /// Default constructor of the package.
        /// Inside this method you can place any initialization code that does not require 
        /// any Visual Studio service because at this point the package object is created but 
        /// not sited yet inside Visual Studio environment. The place to do all the other 
        /// initialization is the Initialize method.
        /// </summary>
        public CahootsPackageBase()
        {
            Trace.WriteLine(string.Format(CultureInfo.CurrentCulture, "Entering constructor for: {0}", this.ToString()));
        }

        /////////////////////////////////////////////////////////////////////////////
        // Overriden Package Implementation
        #region Package Members

        /// <summary>
        /// Initialization of the package; this method is called right after the package is sited, so this is the place
        /// where you can put all the initilaization code that rely on services provided by VisualStudio.
        /// </summary>
        protected override void Initialize()
        {
            Trace.WriteLine (string.Format(CultureInfo.CurrentCulture, "Entering Initialize() of: {0}", this.ToString()));
            base.Initialize();

			// Add our command handlers for menu (commands must exist in the .vsct file)
            OleMenuCommandService mcs = GetService(typeof(IMenuCommandService)) as OleMenuCommandService;
            if ( null != mcs )
            {
				CommandID commandId;
				OleMenuCommand menuItem;

				// Create the command for button ConnectToolbarButton
                commandId = new CommandID(GuidList.guidCahootsCmdSet, (int)PkgCmdIDList.ConnectToolbarButton);
                menuItem = new OleMenuCommand(ConnectToolbarButtonExecuteHandler, ConnectToolbarButtonChangeHandler, ConnectToolbarButtonQueryStatusHandler, commandId);
                mcs.AddCommand(menuItem);
				// Create the command for button HostToolbarButton
                commandId = new CommandID(GuidList.guidCahootsCmdSet, (int)PkgCmdIDList.HostToolbarButton);
                menuItem = new OleMenuCommand(HostToolbarButtonExecuteHandler, HostToolbarButtonChangeHandler, HostToolbarButtonQueryStatusHandler, commandId);
                mcs.AddCommand(menuItem);
				// Create the command for button DisconnectToolbarButton
                commandId = new CommandID(GuidList.guidCahootsCmdSet, (int)PkgCmdIDList.DisconnectToolbarButton);
                menuItem = new OleMenuCommand(DisconnectToolbarButtonExecuteHandler, DisconnectToolbarButtonChangeHandler, DisconnectToolbarButtonQueryStatusHandler, commandId);
                mcs.AddCommand(menuItem);
				// Create the command for button StopToolbarButton
                commandId = new CommandID(GuidList.guidCahootsCmdSet, (int)PkgCmdIDList.StopToolbarButton);
                menuItem = new OleMenuCommand(StopToolbarButtonExecuteHandler, StopToolbarButtonChangeHandler, StopToolbarButtonQueryStatusHandler, commandId);
                mcs.AddCommand(menuItem);

			}
		}
		
		#endregion

		#region Handlers for Button: ConnectToolbarButton

		protected virtual void ConnectToolbarButtonExecuteHandler(object sender, EventArgs e)
		{
			ShowMessage("ConnectToolbarButton clicked!");
		}
		
		protected virtual void ConnectToolbarButtonChangeHandler(object sender, EventArgs e)
		{
		}
		
		protected virtual void ConnectToolbarButtonQueryStatusHandler(object sender, EventArgs e)
		{
		}

		#endregion

		#region Handlers for Button: HostToolbarButton

		protected virtual void HostToolbarButtonExecuteHandler(object sender, EventArgs e)
		{
			ShowMessage("HostToolbarButton clicked!");
		}
		
		protected virtual void HostToolbarButtonChangeHandler(object sender, EventArgs e)
		{
		}
		
		protected virtual void HostToolbarButtonQueryStatusHandler(object sender, EventArgs e)
		{
		}

		#endregion

		#region Handlers for Button: DisconnectToolbarButton

		protected virtual void DisconnectToolbarButtonExecuteHandler(object sender, EventArgs e)
		{
			ShowMessage("DisconnectToolbarButton clicked!");
		}
		
		protected virtual void DisconnectToolbarButtonChangeHandler(object sender, EventArgs e)
		{
		}
		
		protected virtual void DisconnectToolbarButtonQueryStatusHandler(object sender, EventArgs e)
		{
		}

		#endregion

		#region Handlers for Button: StopToolbarButton

		protected virtual void StopToolbarButtonExecuteHandler(object sender, EventArgs e)
		{
			ShowMessage("StopToolbarButton clicked!");
		}
		
		protected virtual void StopToolbarButtonChangeHandler(object sender, EventArgs e)
		{
		}
		
		protected virtual void StopToolbarButtonQueryStatusHandler(object sender, EventArgs e)
		{
		}

		#endregion

        /// <summary>
        /// This function is the callback used to execute a command when the a menu item is clicked.
        /// See the Initialize method to see how the menu item is associated to this function using
        /// the OleMenuCommandService service and the MenuCommand class.
        /// </summary>
        protected void ShowMessage(string message)
        {
            // Show a Message Box to prove we were here
            IVsUIShell uiShell = (IVsUIShell)GetService(typeof(SVsUIShell));
            Guid clsid = Guid.Empty;
            int result;
            Microsoft.VisualStudio.ErrorHandler.ThrowOnFailure(uiShell.ShowMessageBox(
                       0,
                       ref clsid,
                       "Cahoots",
                       string.Format(CultureInfo.CurrentCulture, message, this.ToString()),
                       string.Empty,
                       0,
                       OLEMSGBUTTON.OLEMSGBUTTON_OK,
                       OLEMSGDEFBUTTON.OLEMSGDEFBUTTON_FIRST,
                       OLEMSGICON.OLEMSGICON_INFO,
                       0,        // false
                       out result));
        }
    }
}
