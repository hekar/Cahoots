using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Cahoots.Services.Models
{
    public class ChatMessageModel
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="ChatMessageModel" /> class.
        /// </summary>
        /// <param name="name">The name.</param>
        /// <param name="message">The message.</param>
        /// <param name="time">The time.</param>
        public ChatMessageModel(string name, string message, DateTime time)
        {
            this.Name = name;
            this.Message = message;
            this.TimeStamp = time;
        }

        /// <summary>
        /// Gets or sets the name.
        /// </summary>
        /// <value>
        /// The name.
        /// </value>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets the message.
        /// </summary>
        /// <value>
        /// The message.
        /// </value>
        public string Message { get; set; }

        /// <summary>
        /// Gets or sets the time stamp.
        /// </summary>
        /// <value>
        /// The time stamp.
        /// </value>
        public DateTime TimeStamp { get; set; }
    }
}
