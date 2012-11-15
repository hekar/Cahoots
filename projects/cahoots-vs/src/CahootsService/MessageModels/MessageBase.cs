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
        [DataMember(Name = "service", IsRequired = true)]
        public string Service { get; set; }

        [DataMember(Name = "type", IsRequired = true)]
        public string MessageType { get; set; }
    }
}
