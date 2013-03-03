/// ChatService.cs
/// Codeora 2013
///
/// Service for sending and receiving chat messages
/// 

namespace Cahoots.Services
{
    using System;
    using System.Collections.Generic;

    using Cahoots.Ext;
    using Cahoots.Services.Contracts;
    using Cahoots.Services.MessageModels.Chat;
    using Cahoots.Services.Models;
    using Cahoots.Services.ViewModels;
    using Cahoots.Services.MessageModels;
    using System.IO;

    public class ChatService : AsyncService, IAsyncService
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="ChatService" /> class.
        /// </summary>
        public ChatService(
                IWindowService windowService,
                Preferences preferences)
                        : base("chat")
        {
            this.ViewModels = new Dictionary<string, ChatViewModel>();
            this.WindowService = windowService;
            this.Preferences = preferences;
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
        /// Gets or sets the preferences.
        /// </summary>
        /// <value>
        /// The preferences.
        /// </value>
        private Preferences Preferences { get; set; }

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
                this.WindowService.OpenChatWindow(model.From);
            }

            if (this.ViewModels.ContainsKey(model.From))
            {
                var vm = this.ViewModels[model.From];
                var dt = DateTime.Parse(model.TimeStamp);

                vm.Messages.Add(
                    new ChatMessageModel(
                        vm.Chatee.Name,
                        model.Message,
                        dt));

                if (this.Preferences.SaveChatLogs)
                {
                    // log
                    this.Log(
                            vm.Chatee.UserName,
                            model.Message,
                            vm.Chatee.Name,
                            dt);
                }
            }
        }

        /// <summary>
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        /// <remarks>
        /// [0] - Collaborator
        /// [1] - Local user name
        /// </remarks>
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
                    Send = this.Send
                };

                this.ViewModels.Add(user.UserName, vm);

                return vm;
            }

            return null;
        }

        /// <summary>
        /// Sends the specified message.
        /// </summary>
        /// <param name="message">The message.</param>
        public void Send(SendChatMessage message)
        {
            this.SendMessage(message);

            if (this.Preferences.SaveChatLogs)
            {
                // log
                this.Log(
                        message.To,
                        message.Message,
                        "You",
                        DateTime.Parse(message.TimeStamp));
            }
        }

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        public override void Disconnect()
        {
            this.ViewModels.Clear();
        }

        /// <summary>
        /// Logs the specified file.
        /// </summary>
        /// <param name="file">The file.</param>
        /// <param name="msg">The MSG.</param>
        /// <param name="sender">The sender.</param>
        /// <param name="dt">The dt.</param>
        private void Log(string file, string msg, string sender, DateTime dt)
        {
            var path = this.Preferences.ChatLogsDirectory + file + ".log";
            using (var stream = new FileStream(path, FileMode.Append | FileMode.OpenOrCreate))
            using (var writer = new StreamWriter(stream))
            {
                writer.WriteLine("{0} {1}: {2}", dt, sender, msg);
            }
        }
    }
}
