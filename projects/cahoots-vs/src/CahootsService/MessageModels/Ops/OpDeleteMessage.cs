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
        /// Gets or sets the start.
        /// </summary>
        /// <value>
        /// The start.
        /// </value>
        [DataMember(Name = "start", IsRequired = true)]
        public int Start { get; set; }

        /// <summary>
        /// Gets or sets the end.
        /// </summary>
        /// <value>
        /// The end.
        /// </value>
        [DataMember(Name = "end", IsRequired = true)]
        public int End{ get; set; }
    }
}
