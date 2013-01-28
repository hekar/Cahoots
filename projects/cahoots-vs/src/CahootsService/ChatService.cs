///
///
///

namespace Cahoots.Services
{
    using System;
    using System.Collections.Generic;
    using Cahoots.Services.Models;
    using Cahoots.Services.ViewModels;
    using Cahoots.Services.MessageModels.Chat;
using Cahoots.Services.Contracts;
    using System.Threading;
    using System.ComponentModel;

    public class ChatService : AsyncService, IAsyncService
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="ChatService" /> class.
        /// </summary>
        public ChatService(IWindowService windowService) : base("chat")
        {
            this.ViewModels = new Dictionary<string, ChatViewModel>();
            this.WindowService = windowService;
        }

        /// <summary>
        /// Gets or sets the view models.
        /// </summary>
        /// <value>
        /// The view models.
        /// </value>
        private Dictionary<string, ChatViewModel> ViewModels { get; set; }

        /// <summary>
        /// Gets or sets the window service.
        /// </summary>
        /// <value>
        /// The window service.
        /// </value>
        private IWindowService WindowService { get; set; }

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="type">The message type.</param>
        /// <param name="json">The json.</param>
        public override void ProcessMessage(string type, string json)
        {
            switch(type)
            {
                case "receive":
                    var model = JsonHelper.Deserialize<ReceiveMessage>(json);
                    this.ReceiveMessage(model);
                    break;
            }
        }

        /// <summary>
        /// Receives the message.
        /// </summary>
        /// <param name="model">The model.</param>
        private void ReceiveMessage(ReceiveMessage model)
        {
            if (!this.ViewModels.ContainsKey(model.From))
            {
                var bg = new BackgroundWorker();
                bg.DoWork += (s, e) => this.WindowService.OpenChatWindow(model.From);
                bg.RunWorkerAsync();
            }

            if (!this.ViewModels.ContainsKey(model.From))
            {
                while (!this.ViewModels.ContainsKey(model.From))
                {
                };

                var vm = this.ViewModels[model.From];

                vm.Messages.Add(
                    new ChatMessageModel(
                        vm.Chatee.Name,
                        model.Message,
                        DateTime.Now));
            }
        }

        /// <summary>
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        /// <returns>A view model.</returns>
        public override BaseViewModel GetViewModel(params object[] parameters)
        {
            if (parameters.Length == 2 && parameters[0] is Collaborator && parameters[1] is string)
            {
                var user = parameters[0] as Collaborator;

                if (this.ViewModels.ContainsKey(user.UserName))
                {
                    return this.ViewModels[user.UserName];
                }

                var vm = new ChatViewModel()
                {
                    Chatee = user,
                    Me = parameters[1] as string,
                    RelayMessage = this.SendMessage
                };

                this.ViewModels.Add(user.UserName, vm);

                return vm;
            }

            return null;
        }

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        public override void Disconnect()
        {
            this.ViewModels.Clear();
        }
    }
}
