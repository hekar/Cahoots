using System;
using Cahoots.Services.ViewModels;

namespace Cahoots.Services
{
    /// <summary>
    /// Interface for async services.
    /// </summary>
    public interface IAsyncService : IDisposable
    {
        /// <summary>
        /// Gets or sets the service identifier.
        /// </summary>
        /// <value>
        /// The service identifier.
        /// </value>
        string ServiceIdentifier { get; }

        /// <summary>
        /// Gets or sets the name of the user.
        /// </summary>
        /// <value>
        /// The name of the user.
        /// </value>
        string UserName { get; set; }
        
        /// <summary>
        /// Initializes the service senders.
        /// </summary>
        /// <param name="messageSender">The message sender.</param>
        void Initialize(SendMessage messageSender);

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="json">The json.</param>
        void ProcessMessage(string type, string json);

        /// <summary>
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        BaseViewModel GetViewModel(params object[] parameters);

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        void Disconnect();
    }
}
