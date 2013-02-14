/// SendShareMessage.cs
/// Codeora 2013
///
/// JSON model for sending a share request.
///

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Collections.ObjectModel;
    using System.Runtime.Serialization;

    [DataContract]
    public class SendShareMessage : MessageBase
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
        /// Gets or sets the document id.
        /// </summary>
        /// <value>
        /// The document id.
        /// </value>
        [DataMember(Name = "documentId", IsRequired = true)]
        public string DocumentId { get; set; }

        /// <summary>
        /// Gets or sets the collaborators.
        /// </summary>
        /// <value>
        /// The collaborators.
        /// </value>
        [DataMember(Name = "collaborators", IsRequired = true)]
        public Collection<string> Collaborators { get; set; }
    }
}
