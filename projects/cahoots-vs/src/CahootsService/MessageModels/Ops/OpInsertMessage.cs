///
///
///

namespace Cahoots.Services.MessageModels.Ops
{
    using System.Runtime.Serialization;

    [DataContract]
    public class OpInsertMessage : BaseOpMessage
    {
        /// <summary>
        /// Gets or sets the content.
        /// </summary>
        /// <value>
        /// The content.
        /// </value>
        [DataMember(Name = "content", IsRequired = true)]
        public string Content { get; set; }
    }
}
