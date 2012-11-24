using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.ObjectModel;
using Cahoots.Services.Models;
using Cahoots.Services.MessageModels;

namespace Cahoots.Services
{
    public class UsersService : AsyncService, IAsyncService
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="UsersService" /> class.
        /// </summary>
        /// <param name="messageSender">The message sender.</param>
        public UsersService(SendMessage messageSender)
                                        : base("users", messageSender, null)
        {
            this.Users = new ObservableCollection<Collaborator>();
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
        /// <param name="type">The message type.</param>
        /// <param name="json">The json.</param>
        public override void ProcessMessage(string type, string json)
        {
            // TODO: Implement...
            switch(type)
            {
                case "all":
                    var all =
                        JsonHelper.Deserialize<ReceiveAllUsersMessage>(json);
                    this.LoadAllUsers(all);
                    break;

                case "status":
                    var update =
                        JsonHelper.Deserialize<ReceiveUserStatusMessage>(json);
                    this.UpdateUserStatus(update);
                    break;
            }
        }

        /// <summary>
        /// Loads all users.
        /// </summary>
        /// <param name="message">The message.</param>
        public void LoadAllUsers(ReceiveAllUsersMessage message)
        {
            if (message.Users != null)
            {
                this.Users.Clear();

                foreach (var user in message.Users)
                {
                    this.Users.Add(user);
                }
            }
        }

        /// <summary>
        /// Updates the user status.
        /// </summary>
        /// <param name="message">The message.</param>
        public void UpdateUserStatus(ReceiveUserStatusMessage message)
        {
            var user = this.Users.FirstOrDefault(u => u.Name == message.User.Name);

            if (user != null)
            {
                user.Status = message.User.Status;
            }
            else
            {
                this.Users.Add(new Collaborator()
                    {
                        Name = message.User.Name,
                        Status = message.User.Status
                    });
            }
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
