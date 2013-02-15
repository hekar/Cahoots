///
///
///

namespace Cahoots
{
    using System;
    using Microsoft.VisualStudio.Editor;
    using Microsoft.VisualStudio.Shell;
    using Microsoft.VisualStudio.Shell.Interop;
    using Microsoft.VisualStudio.Text.Editor;
    using Microsoft.VisualStudio.TextManager.Interop;

    static class DTEExtensions
    {
        /// <summary>
        /// Gets the editor view.
        /// </summary>
        /// <param name="dte">The DTE.</param>
        /// <param name="fullPath">The full path.</param>
        /// <returns>The editor view.</returns>
        public static IWpfTextView GetEditorView(this EnvDTE._DTE dte, string fullPath)
        {
            // http://stackoverflow.com/questions/6751086/visual-studio-text-editor-extension
            // http://stackoverflow.com/questions/2413530/find-an-ivstextview-or-iwpftextview-for-a-given-projectitem-in-vs-2010-rc-exten

            Microsoft.VisualStudio.OLE.Interop.IServiceProvider sp =
                    (Microsoft.VisualStudio.OLE.Interop.IServiceProvider)dte;
            ServiceProvider serviceProvider = new ServiceProvider(sp);

            uint itemID;
            IVsUIHierarchy uiHierarchy;
            IVsWindowFrame windowFrame;

            var isOpen = VsShellUtilities.IsDocumentOpen(
                                serviceProvider,
                                fullPath,
                                Guid.Empty,
                                out uiHierarchy,
                                out itemID,
                                out windowFrame);

            if (!isOpen)
            {
                VsShellUtilities.OpenDocument(
                        serviceProvider,
                        fullPath,
                        Guid.Empty,
                        out uiHierarchy,
                        out itemID,
                        out windowFrame);
            }

            var frame = VsShellUtilities.GetTextView(windowFrame);

            IVsUserData userData = frame as IVsUserData;
            object holder;
            Guid guidViewHost = DefGuidList.guidIWpfTextViewHost;
            userData.GetData(ref guidViewHost, out holder);
            IWpfTextViewHost viewHost = (IWpfTextViewHost)holder;
            return viewHost.TextView;
        }
    }
}
