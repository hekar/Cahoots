using System.Runtime.Serialization;
using Cahoots.Services.Models;

namespace Cahoots.Services.MessageModels
{
    [DataContract]
    public class ReceiveAllUsersMessage : MessageBase
    {
        [DataMember(Name = "users")]
        public Collaborator[] Users { get; set; }
    }
}
