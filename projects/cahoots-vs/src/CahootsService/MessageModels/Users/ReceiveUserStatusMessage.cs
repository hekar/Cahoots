using System.Runtime.Serialization;
using Cahoots.Services.Models;

namespace Cahoots.Services.MessageModels
{
    [DataContract]
    public class ReceiveUserStatusMessage : MessageBase
    {
        [DataMember(Name = "user")]
        public Collaborator User { get; set; }
    }
}
