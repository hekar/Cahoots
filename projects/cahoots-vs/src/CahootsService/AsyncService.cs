using System;
using Cahoots.Services.ViewModels;
using Cahoots.Ext;
using Cahoots.Services.MessageModels;

namespace Cahoots.Services
{
    /// <summary>
    /// Delegate for sending a text/json message.
    /// </summary>
    /// <param name="json">The json.</param>
    public delegate void SendMessage(string json);

    /// <summary>
    /// Abstract class for async services.
    /// </summary>
    public abstract class AsyncService : IAsyncService, IDisposable
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="AsyncService" /> class.
        /// </summary>
        /// <param name="identifer">The identifer.</param>
        /// <param name="messageSender">The message sender.</param>
        /// <param name="dataSender">The data sender.</param>
        public AsyncService(string identifer)
        {
            if (string.IsNullOrWhiteSpace(identifer))
            {
                throw new ArgumentNullException("identifer");
            }

            this.ServiceIdentifier = identifer;
        }

        /// <summary>
        /// Gets or sets the message sender.
        /// </summary>
        /// <value>
        /// The message sender.
        /// </value>
        private SendMessage MessageSender { get; set; }

        /// <summary>
        /// Gets or sets the service identifier.
        /// </summary>
        /// <value>
        /// The service identifier.
        /// </value>
        public string ServiceIdentifier { get; private set; }

        /// <summary>
        /// Gets or sets the name of the user.
        /// </summary>
        /// <value>
        /// The name of the user.
        /// </value>
        public string UserName { get; set; }

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="type">The message type.</param>
        /// <param name="json">The json.</param>
        public abstract void ProcessMessage(string type, string json);

        /// <summary>
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        public abstract BaseViewModel GetViewModel(params object[] parameters);

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        public abstract void Disconnect();

        /// <summary>
        /// Initializes the service senders.
        /// </summary>
        /// <param name="messageSender">The message sender.</param>
        public void Initialize(SendMessage messageSender)
        {
            this.MessageSender = messageSender;
        }

        /// <summary>
        /// Sends the text/json.
        /// </summary>
        /// <typeparam name="T">The entity type.</typeparam>
        /// <param name="entity">The entity.</param>
        protected void SendMessage<T>(T entity) where T : MessageBase
        {
            if (this.MessageSender == null)
            {
                throw new InvalidOperationException(
                    "This instance is not configured to send text/json messages");
            }

            var json = JsonHelper.Serialize(entity);
            this.MessageSender(json);
        }

        /// <summary>
        /// Performs application-defined tasks associated with
        /// freeing, releasing, or resetting unmanaged resources.
        /// </summary>
        public virtual void Dispose()
        {
        }
    }
}
