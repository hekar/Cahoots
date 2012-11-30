// ----------------------------------------------------------------------
// <copyright file="Collaborator.cs" company="Codeora">
//     Copyright 2012. All rights reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Services.Models
{
    using System.ComponentModel;
    using System.Runtime.Serialization;

    [DataContract]
    public class Collaborator : ModelBase
    {
        [DataMember(Name = "name")]
        public string Name { get; set; }

        [DataMember(Name = "role")]
        public string Role { get; set; }

        [DataMember(Name = "status")]
        public string Status { get; set; }
    }
}
