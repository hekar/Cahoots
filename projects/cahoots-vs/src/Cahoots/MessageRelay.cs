using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebSocketSharp;
using Cahoots.Services;

namespace Cahoots
{
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
        private Dictionary<string, IAsyncService> Services { get; set; }

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
            // find identifier
            string identifier = "user"; // e.Data, blah blah blah

            if (this.Services.ContainsKey(identifier))
            {
                var service = this.Services[identifier];
                service.ProcessMessage(e.Data);
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
