using System;

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
        /// Processes the JSON message.
        /// </summary>
        /// <param name="json">The json.</param>
        void ProcessMessage(string json);
    }
}
