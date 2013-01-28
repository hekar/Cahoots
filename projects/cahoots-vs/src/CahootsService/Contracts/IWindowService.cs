
namespace Cahoots.Services.Contracts
{
    using Cahoots.Services.Models;

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
    }
}
