
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
    /// This class implements the tool window CollaborationsWindowToolWindow exposed by this package and hosts a user control.
    ///
    /// In Visual Studio tool windows are composed of a frame (implemented by the shell) and a pane, 
    /// usually implemented by the package implementer.
    ///
    /// This class derives from the ToolWindowPane class provided from the MPF in order to use its 
    /// implementation of the IVsUIElementPane interface.
    /// </summary>
    [Guid("bcca61a3-a7c9-4423-abd0-28ecb548c11a")]
    public class CollaborationsWindowToolWindow : CollaborationsWindowToolWindowBase
    {

        /// <summary>
        /// Standard constructor for the tool window.
        /// </summary>
        public CollaborationsWindowToolWindow()
        {
            base.Content = new CollaborationsWindowControl();
        }

	}
}