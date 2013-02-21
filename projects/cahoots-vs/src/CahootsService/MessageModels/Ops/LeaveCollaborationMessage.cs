using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Collections.ObjectModel;
    using System.Runtime.Serialization;
    [DataContract]
    class LeaveCollaborationMessage : MessageBase
    {
        /// <summary>
        /// Gets or sets the user.
        /// </summary>
        /// <value>
        /// The user.
        /// </value>
        [DataMember(Name = "user", IsRequired = true)]
        public string User { get; set; }

        /// <summary>
        /// Gets or sets the op id.
        /// </summary>
        /// <value>
        /// The op id.
        /// </value>
        [DataMember(Name = "opId", IsRequired = true)]
        public string OpId { get; set; }
    }
}
