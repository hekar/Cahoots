
using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.Shell;

namespace Cahoots
{
	/// <summary>
    /// This class implements the tool window UsersWindowToolWindow exposed by this package and hosts a user control.
    ///
    /// In Visual Studio tool windows are composed of a frame (implemented by the shell) and a pane, 
    /// usually implemented by the package implementer.
    ///
    /// This class derives from the ToolWindowPane class provided from the MPF in order to use its 
    /// implementation of the IVsUIElementPane interface.
    /// </summary>
    [Guid("c5d2c22e-28af-4fe9-87ed-aee833b7f3d4")]
    public class UsersWindowToolWindow : UsersWindowToolWindowBase
    {

        /// <summary>
        /// Standard constructor for the tool window.
        /// </summary>
        public UsersWindowToolWindow()
        {
            base.Content = new UsersWindowControl();
        }

	}
}