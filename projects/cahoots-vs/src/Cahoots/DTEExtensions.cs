using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.VisualStudio.Text.Editor;
using Microsoft.VisualStudio.Shell;
using Microsoft.VisualStudio.Shell.Interop;
using Microsoft.VisualStudio.TextManager.Interop;
using Microsoft.VisualStudio.Editor;

namespace Cahoots
{
    static class DTEExtensions
    {
        /// <summary>
        /// Gets the editor view.
        /// </summary>
        /// <param name="dte">The DTE.</param>
        /// <param name="fullPath">The full path.</param>
        /// <returns></returns>
        public static IWpfTextView GetEditorView(this EnvDTE._DTE dte, string fullPath)
        {

            // http://stackoverflow.com/questions/6751086/visual-studio-text-editor-extension
            // http://stackoverflow.com/questions/2413530/find-an-ivstextview-or-iwpftextview-for-a-given-projectitem-in-vs-2010-rc-exten

            Microsoft.VisualStudio.OLE.Interop.IServiceProvider sp = (Microsoft.VisualStudio.OLE.Interop.IServiceProvider)dte;
            ServiceProvider serviceProvider = new ServiceProvider(sp);

            uint itemID;
            IVsUIHierarchy uiHierarchy;
            IVsWindowFrame windowFrame;

            if (VsShellUtilities.IsDocumentOpen(serviceProvider, fullPath, Guid.Empty, out uiHierarchy, out itemID, out windowFrame))
            {
                var frame = VsShellUtilities.GetTextView(windowFrame);

                IWpfTextView view = null;
                IVsUserData userData = frame as IVsUserData;

                IWpfTextViewHost viewHost;
                object holder;
                Guid guidViewHost = DefGuidList.guidIWpfTextViewHost;
                userData.GetData(ref guidViewHost, out holder);
                viewHost = (IWpfTextViewHost)holder;
                return viewHost.TextView;
            }
            else
            {
                return null;
            }
        }
    }
}
