/// ReceiveMessage.cs
/// Codeora 2013
///
/// JSON model for receiving chat messages from the server.
///

namespace Cahoots.Services.MessageModels.Chat
{
    using System;
    using System.Runtime.Serialization;

    [DataContract]
    public class ReceiveMessage
    {
        /// <summary>
        /// Gets or sets from.
        /// </summary>
        /// <value>
        /// From.
        /// </value>
        [DataMember(Name = "from", IsRequired = true)]
        public string From { get; set; }

        /// <summary>
        /// Gets or sets the message.
        /// </summary>
        /// <value>
        /// The message.
        /// </value>
        [DataMember(Name = "message", IsRequired = true)]
        public string Message { get; set; }

        /// <summary>
        /// Gets or sets the time stamp.
        /// </summary>
        /// <value>
        /// The time stamp.
        /// </value>
        [DataMember(Name = "timestamp", IsRequired = true)]
        public DateTime TimeStamp { get; set; }
    }
}
