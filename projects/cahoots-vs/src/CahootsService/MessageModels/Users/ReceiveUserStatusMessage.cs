using System.Runtime.Serialization;

namespace Cahoots.Services.MessageModels
{
    [DataContract]
    public class ReceiveUserStatusMessage : MessageBase
    {
        [DataMember(Name = "user")]
        public string User { get; set; }

        [DataMember(Name = "status")]
        public string Status { get; set; }
    }
}
