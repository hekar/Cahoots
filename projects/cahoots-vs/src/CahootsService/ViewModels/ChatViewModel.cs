using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Cahoots.Services.Models;
using Cahoots.Services.MessageModels;
using Cahoots.Ext.View;

namespace Cahoots.Services.ViewModels
{
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
        /// Sends the message.
        /// </summary>
        /// <param name="message">The message.</param>
        public void SendMessage(string message)
        {
            this.Messages.Add(
                new ChatMessageModel(this.Me, message, DateTime.Now));

            var q = new SendChatMessage()
            {
                MessageType = "send",
                Service = "chat",
                Message = message,
                To = Chatee.UserName,
                From = Me
            };

            var str = JsonHelper.Serialize(q);
            this.RelayMessage(str);
        }
    }
}
