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
        [DataMember(Name = "length", IsRequired = true)]
        public int Length { get; set; }

        /// <summary>
        /// Gets or sets the old content.
        /// </summary>
        /// <value>
        /// The old content.
        /// </value>
        [DataMember(Name = "oldContent", IsRequired = true)]
        public string OldContent { get; set; }

        /// <summary>
        /// Gets the length of the replacement.
        /// </summary>
        /// <value>
        /// The length of the replacement.
        /// </value>
        public int ReplacementLength
        {
            get
            {
                return 0;
            }
            set
            {
                // TODO: do this...
            }
        }
    }
}
