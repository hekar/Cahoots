using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace Cahoots.Services.MessageModels
{
    [DataContract]
    public class MessageBase
    {
        /// <summary>
        /// Gets or sets the service.
        /// </summary>
        /// <value>
        /// The service.
        /// </value>
        [DataMember(Name = "service", IsRequired = true)]
        public string Service { get; set; }

        /// <summary>
        /// Gets or sets the type of the message.
        /// </summary>
        /// <value>
        /// The type of the message.
        /// </value>
        [DataMember(Name = "type", IsRequired = true)]
        public string MessageType { get; set; }
    }
}
