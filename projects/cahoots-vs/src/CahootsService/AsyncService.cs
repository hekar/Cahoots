using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebSocketSharp;

namespace Cahoots.Services
{
    /// <summary>
    /// Delegate for sending a text/json message.
    /// </summary>
    /// <param name="json">The json.</param>
    public delegate void SendMessage(string json);

    /// <summary>
    /// Delegate for sending a binary data message.
    /// </summary>
    /// <param name="data">The data.</param>
    public delegate void SendData(byte[] data);

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
        public AsyncService(
                string identifer,
                SendMessage messageSender,
                SendData dataSender)
        {
            if (string.IsNullOrWhiteSpace(identifer))
            {
                throw new ArgumentNullException("identifer");
            }

            this.ServiceIdentifier = identifer;
            this.MessageSender = messageSender;
            this.DataSender = dataSender;
        }

        /// <summary>
        /// Gets or sets the message sender.
        /// </summary>
        /// <value>
        /// The message sender.
        /// </value>
        private SendMessage MessageSender { get; set; }

        /// <summary>
        /// Gets or sets the data sender.
        /// </summary>
        /// <value>
        /// The data sender.
        /// </value>
        private SendData DataSender { get; set; }

        /// <summary>
        /// Gets or sets the service identifier.
        /// </summary>
        /// <value>
        /// The service identifier.
        /// </value>
        public string ServiceIdentifier { get; private set; }

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="type">The message type.</param>
        /// <param name="json">The json.</param>
        public abstract void ProcessMessage(string type, string json);

        /// <summary>
        /// Sends the data.
        /// </summary>
        /// <param name="data">The data.</param>
        protected void SendData(byte[] data)
        {
            if (this.DataSender == null)
            {
                throw new InvalidOperationException(
                    "This instance is not configured to send binary data.");
            }

            this.DataSender(data);
        }

        /// <summary>
        /// Sends the text/json.
        /// </summary>
        /// <param name="json">The json.</param>
        protected void SendMessage(string json)
        {
            if (this.MessageSender == null)
            {
                throw new InvalidOperationException(
                    "This instance is not configured to send text/json messages");
            }

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
