﻿/// ChatViewModel.cs
/// Codeora 2013
///
/// View model for driving chat windows.
///

namespace Cahoots.Services.ViewModels
{
    using System;
    using Cahoots.Ext.View;
    using Cahoots.Services.MessageModels;
    using Cahoots.Services.Models;

    public class ChatViewModel : BaseViewModel
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="ChatViewModel" /> class.
        /// </summary>
        public ChatViewModel()
        {
            this.Messages = new ViewModelCollection<ChatMessageModel>();
        }

        /// <summary>
        /// Gets or sets the chatee.
        /// </summary>
        /// <value>
        /// The chatee.
        /// </value>
        public Collaborator Chatee { get; set; }

        /// <summary>
        /// Gets or sets me.
        /// </summary>
        /// <value>
        /// Me.
        /// </value>
        public string Me { get; set; }

        /// <summary>
        /// Gets or sets the messages.
        /// </summary>
        /// <value>
        /// The messages.
        /// </value>
        public ViewModelCollection<ChatMessageModel> Messages { get; set; }

        /// <summary>
        /// Gets or sets the send.
        /// </summary>
        /// <value>
        /// The send.
        /// </value>
        public Action<SendChatMessage> Send { get; set; }

        /// <summary>
        /// Sends the message.
        /// </summary>
        /// <param name="message">The message.</param>
        public void SendMessage(string message)
        {
            this.Messages.Add(
                new ChatMessageModel("You", message, DateTime.Now));

            var model = new SendChatMessage()
            {
                MessageType = "send",
                Service = "chat",
                Message = message,
                To = Chatee.UserName,
                From = Me,
                TimeStamp = DateTime.Now.ToString("yyyy-MM-ddTHH:mm:ss") + DateTime.Now.ToString("zzz")
            };

            if (this.Send != null)
            {
                this.Send(model);
            }
        }
    }
}