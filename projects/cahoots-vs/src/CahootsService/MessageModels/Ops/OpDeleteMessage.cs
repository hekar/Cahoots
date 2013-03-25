///
///
///

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Runtime.Serialization;

    [DataContract]
    public class OpDeleteMessage : BaseOpMessage
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
        /// Gets or sets the old content.
        /// </summary>
        /// <value>
        /// The old content.
        /// </value>
        [DataMember(Name = "oldContent", IsRequired = true)]
        public string OldContent { get; set; }
    }
}
