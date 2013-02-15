// ----------------------------------------------------------------------
// <copyright file="MessageRelay.cs" company="Codeora">
//     Copyright 2012. All right reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using WebSocketSharp;
    using Cahoots.Services;
    using Cahoots.Services.MessageModels;
    using Cahoots.Ext;

    /// <summary>
    /// Class for relaying JSON messages to other services.
    /// </summary>
    public class MessageRelay : IDisposable
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="MessageRelay" /> class.
        /// </summary>
        /// <param name="services">The async message services.</param>
        public MessageRelay(params IAsyncService[] services)
        {
            this.Services = new Dictionary<string, IAsyncService>();

            if (services != null)
            {
                foreach (var service in services)
                {
                    service.Initialize(this.SendMessage);
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
        public WebSocket Socket { get; private set; }

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
        /// Sets the socket.
        /// </summary>
        /// <param name="socket">The socket.</param>
        public void SetSocket(WebSocket socket)
        {
            if (socket == null)
            {
                foreach (var service in this.Services)
                {
                    service.Value.Disconnect();
                }
            }
            else
            {
                socket.OnMessage += RelayMessage;
            }

            this.Socket = socket;
        }

        /// <summary>
        /// Sets the name of the user.
        /// </summary>
        /// <param name="userName">Name of the user.</param>
        public void SetUserName(string userName)
        {
            foreach (var service in this.Services.Values)
            {
                service.UserName = userName;
            }
        }

        /// <summary>
        /// Sends a to the server message.
        /// </summary>
        /// <param name="message">The message.</param>
        public void SendMessage(string message)
        {
            if (this.Socket != null && this.Socket.ReadyState == WsState.OPEN)
            {
                this.Socket.Send(message);
            }
        }

        /// <summary>
        /// Gets a service of a specific type.
        /// </summary>
        /// <typeparam name="T">The service type.</typeparam>
        /// <returns>The service of the type.</returns>
        public T Service<T>() where T : class, IAsyncService
        {
            foreach (var service in this.Services.Values)
            {
                if (service is T)
                {
                    return service as T;
                }
            }

            return null;
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
