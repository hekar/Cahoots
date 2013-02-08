/// SendChatMessage.
/// Codeora 2013
///
/// JSON model for sending chat messages to the server.
///

namespace Cahoots.Services.MessageModels
{
    using System;
    using System.Runtime.Serialization;

    [DataContract]
    public class SendChatMessage : MessageBase
    {
        /// <summary>
        /// Gets or sets to.
        /// </summary>
        /// <value>
        /// To.
        /// </value>
        [DataMember(Name = "to", IsRequired = true)]
        public string To { get; set; }

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
        public string TimeStamp { get; set; }
    }
}