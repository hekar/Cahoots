using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebSocketSharp;
using Cahoots.Services;
using Cahoots.Services.MessageModels;

namespace Cahoots
{
    /// <summary>
    /// Class for relaying JSON messages to other services.
    /// </summary>
    public class MessageRelay : IDisposable
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="MessageRelay" /> class.
        /// </summary>
        /// <param name="socket">The socket.</param>
        /// <param name="services">The async message services.</param>
        public MessageRelay(WebSocket socket, params IAsyncService[] services)
        {
            if (socket == null)
            {
                throw new ArgumentNullException("socket");
            }

            this.Socket = socket;
            this.Socket.OnMessage += RelayMessage;
            this.Services = new Dictionary<string, IAsyncService>();

            if (services != null)
            {
                foreach (var service in services)
                {
                    this.Services.Add(service.ServiceIdentifier, service);
                }
            }
        }

        /// <summary>
        /// Gets or sets the web socket.
        /// </summary>
        /// <value>
        /// The web socket.
        /// </value>
        private WebSocket Socket { get; set; }

        /// <summary>
        /// Gets or sets the services.
        /// </summary>
        /// <value>
        /// The services.
        /// </value>
        public Dictionary<string, IAsyncService> Services { get; private set; }

        /// <summary>
        /// Relays the message.
        /// </summary>
        /// <param name="sender">The sender.</param>
        /// <param name="e">
        ///   The <see cref="MessageEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void RelayMessage(object sender, MessageEventArgs e)
        {
            var msg = JsonHelper.Deserialize<MessageBase>(e.Data);
            var identifier = msg.Service;
            var type = msg.MessageType;

            if (this.Services.ContainsKey(identifier))
            {
                var service = this.Services[identifier];
                service.ProcessMessage(type, e.Data);
            }
        }

        /// <summary>
        /// Performs application-defined tasks associated with
        /// freeing, releasing, or resetting unmanaged resources.
        /// </summary>
        public void Dispose()
        {
            foreach (var service in this.Services.Values)
            {
                service.Dispose();
            }
        }
    }
}
