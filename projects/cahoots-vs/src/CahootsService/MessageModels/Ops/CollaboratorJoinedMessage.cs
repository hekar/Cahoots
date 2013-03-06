using System.Runtime.Serialization;

namespace Cahoots.Services.MessageModels.Ops
{
    [DataContract]
    class CollaboratorJoinedMessage : MessageBase
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
        /// Gets or sets the opId
        /// </summary>
        /// <value>
        /// The opId.
        /// </value>
        [DataMember(Name = "opId", IsRequired = true)]
        public string OpId { get; set; }

    }
}
