///
///
///

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Runtime.Serialization;

    [DataContract]
    public abstract class BaseOpMessage : MessageBase
    {
        /// <summary>
        /// Gets or sets the tick stamp.
        /// </summary>
        /// <value>
        /// The tick stamp.
        /// </value>
        [DataMember(Name = "tickStamp", IsRequired = true)]
        public long TickStamp { get; set; }

        /// <summary>
        /// Gets or sets a value indicating whether this op is applied.
        /// </summary>
        /// <value>
        /// <c>true</c> if this op is applied; otherwise, <c>false</c>.
        /// </value>
        [IgnoreDataMember]
        public bool IsApplied { get; set; }

        /// <summary>
        /// Gets or sets the op id.
        /// </summary>
        /// <value>
        /// The op id.
        /// </value>
        [DataMember(Name = "opId", IsRequired = true)]
        public string OpId { get; set; }

        /// <summary>
        /// Gets or sets the document id.
        /// </summary>
        /// <value>
        /// The document id.
        /// </value>
        [DataMember(Name = "documentId", IsRequired = true)]
        public string DocumentId { get; set; }

        /// <summary>
        /// Gets or sets the user.
        /// </summary>
        /// <value>
        /// The user.
        /// </value>
        [DataMember(Name = "user", IsRequired = true)]
        public string User { get; set; }
    }
}
