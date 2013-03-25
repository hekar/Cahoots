///
///
///

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Runtime.Serialization;

    [DataContract]
    class OpReplaceMessage : BaseOpMessage
    {
        /// <summary>
        /// Gets or sets the end.
        /// </summary>
        /// <value>
        /// The end.
        /// </value>
        [DataMember(Name = "end", IsRequired = true)]
        public int End { get; set; }

        /// <summary>
        /// Gets or sets the content.
        /// </summary>
        /// <value>
        /// The content.
        /// </value>
        [DataMember(Name = "content", IsRequired = true)]
        public string Content { get; set; }

        /// <summary>
        /// Gets or sets the old content.
        /// </summary>
        /// <value>
        /// The old content.
        /// </value>
        [DataMember(Name = "oldContent", IsRequired = true)]
        public string OldContent { get; set; }
    }
}
