using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.ObjectModel;
using Cahoots.Services.Models;

namespace Cahoots.Services
{
    public class UsersService : AsyncService, IAsyncService
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="UsersService" /> class.
        /// </summary>
        /// <param name="messageSender">The message sender.</param>
        public UsersService(SendMessage messageSender) : base(messageSender, null)
        {
        }

        /// <summary>
        /// Gets the users.
        /// </summary>
        /// <value>
        /// The users.
        /// </value>
        public ObservableCollection<Collaborator> Users { get; private set; }

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="json">The json.</param>
        public override void ProcessMessage(string json)
        {
            // TODO: Implement...
        }

        /// <summary>
        /// Performs application-defined tasks associated with
        /// freeing, releasing, or resetting unmanaged resources.
        /// </summary>
        public override void Dispose()
        {
            base.Dispose();
        }
    }
}
