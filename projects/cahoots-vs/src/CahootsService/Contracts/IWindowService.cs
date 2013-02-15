///
///
///

namespace Cahoots.Services.Contracts
{
    using Cahoots.Services.Models;
    using Microsoft.VisualStudio.Text.Editor;

    public interface IWindowService
    {
        /// <summary>
        /// Opens a chat window.
        /// </summary>
        /// <param name="user">The user.</param>
        void OpenChatWindow(Collaborator user);

        /// <summary>
        /// Opens a chat window.
        /// </summary>
        /// <param name="username">The username.</param>
        void OpenChatWindow(string username);
        
        /// <summary>
        /// Opens the document window.
        /// </summary>
        /// <param name="filePath">The file path.</param>
        IWpfTextView OpenDocumentWindow(string filePath);
    }
}
