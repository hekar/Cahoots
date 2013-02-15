///
///
///

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Runtime.Serialization;

    [DataContract]
    public class ReceiveShareMessage : MessageBase
    {
        /// <summary>
        /// Gets or sets the sharer.
        /// </summary>
        /// <value>
        /// The sharer.
        /// </value>
        [DataMember(Name = "sharer", IsRequired = true)]
        public string Sharer { get; set; }

        /// <summary>
        /// Gets or sets the document id.
        /// </summary>
        /// <value>
        /// The document id.
        /// </value>
        [DataMember(Name = "documentId", IsRequired = true)]
        public string DocumentId { get; set; }

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
