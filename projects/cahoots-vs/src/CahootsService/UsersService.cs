using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.ObjectModel;
using Cahoots.Services.Models;
using Cahoots.Services.MessageModels;
using Cahoots.Services.ViewModels;
using Cahoots.Ext;

namespace Cahoots.Services
{
    public class UsersService : AsyncService, IAsyncService
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="UsersService" /> class.
        /// </summary>
        public UsersService() : base("users")
        {
            this.ViewModel = new UsersViewModel();
        }

        /// <summary>
        /// Gets or sets the view model.
        /// </summary>
        /// <value>
        /// The view model.
        /// </value>
        private UsersViewModel ViewModel { get; set; }

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="type">The message type.</param>
        /// <param name="json">The json.</param>
        public override void ProcessMessage(string type, string json)
        {
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
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        /// <returns>The users view model.</returns>
        public override BaseViewModel GetViewModel(params object[] parameters)
        {
            return this.ViewModel;
        }

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        public override void Disconnect()
        {
            // clean up view model and such...
            this.ViewModel.Users.Clear();
        }

        /// <summary>
        /// Loads all users.
        /// </summary>
        /// <param name="message">The message.</param>
        public void LoadAllUsers(ReceiveAllUsersMessage message)
        {
            if (message.Users != null)
            {
                var users =
                        message.Users.Where(c => c.UserName != this.UserName);
                foreach (var user in users)
                {
                    this.UpdateCollaborator(user);
                }
            }
        }

        /// <summary>
        /// Gets the collaborators.
        /// </summary>
        /// <returns></returns>
        public IEnumerable<Collaborator> GetCollaborators()
        {
            return this.ViewModel.Users.ToArray();
        }

        /// <summary>
        /// Updates the user status.
        /// </summary>
        /// <param name="message">The message.</param>
        public void UpdateUserStatus(ReceiveUserStatusMessage message)
        {
            if (message.User.UserName != this.UserName)
            {
                this.UpdateCollaborator(message.User);
            }
        }

        /// <summary>
        /// Updates the collaborator.
        /// </summary>
        /// <param name="collaborator">The collaborator.</param>
        private void UpdateCollaborator(Collaborator collaborator)
        {
            var user = this.ViewModel.Users.FirstOrDefault(u => u.Name == collaborator.Name);

            if (user != null)
            {
                user.Status = collaborator.Status;
                user.ForceRefresh("Status");
            }
            else
            {
                this.ViewModel.Users.Add(collaborator);
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
