using System.Runtime.Serialization;

namespace Cahoots.Services.MessageModels.Ops
{
    class JoinCollaborationMessage
    {
        /// <summary>
        /// Gets or sets the opId
        /// </summary>
        /// <value>
        /// The opId.
        /// </value>
        [DataMember(Name = "opId", IsRequired = true)]
        public string opId { get; set; }

        /// <summary>
        /// Gets or sets the user
        /// </summary>
        /// <value>
        /// The user.
        /// </value>
        [DataMember(Name = "user", IsRequired = true)]
        public string user { get; set; }
    }
}
