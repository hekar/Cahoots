using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace Cahoots.Services.Models
{
    [DataContract]
    public class Collaborator
    {
        [DataMember(Name = "name")]
        public string Name { get; set; }

        [DataMember(Name = "role")]
        public string Role { get; set; }

        [DataMember(Name = "status")]
        public string Status { get; set; }
    }
}
