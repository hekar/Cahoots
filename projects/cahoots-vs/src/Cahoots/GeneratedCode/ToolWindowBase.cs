using System;
using System.Collections;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Windows;
using System.Runtime.InteropServices;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.Shell;
using System.ComponentModel.Design;

namespace Cahoots
{
	/// <summary>
    /// This class implements the tool window UsersWindowToolWindowBase exposed by this package and hosts a user control.
    ///
    /// In Visual Studio tool windows are composed of a frame (implemented by the shell) and a pane, 
    /// usually implemented by the package implementer.
    ///
    /// This class derives from the ToolWindowPane class provided from the MPF in order to use its 
    /// implementation of the IVsUIElementPane interface.
    /// </summary>
    [Guid("c5d2c22e-28af-4fe9-87ed-aee833b7f3d4")]
    public class UsersWindowToolWindowBase : ToolWindowPane
    {
        /// <summary>
        /// Standard constructor for the tool window.
        /// </summary>
        public UsersWindowToolWindowBase()
            : base(null)
        {
			this.Caption = "Collaborators";
        }
    }
	/// <summary>
    /// This class implements the tool window ChatWindowToolWindowBase exposed by this package and hosts a user control.
    ///
    /// In Visual Studio tool windows are composed of a frame (implemented by the shell) and a pane, 
    /// usually implemented by the package implementer.
    ///
    /// This class derives from the ToolWindowPane class provided from the MPF in order to use its 
    /// implementation of the IVsUIElementPane interface.
    /// </summary>
    [Guid("0968dc05-0eee-4e07-b222-9a305fb2a016")]
    public class ChatWindowToolWindowBase : ToolWindowPane
    {
        /// <summary>
        /// Standard constructor for the tool window.
        /// </summary>
        public ChatWindowToolWindowBase()
            : base(null)
        {
			this.Caption = "ChatWindow";
        }
    }
}