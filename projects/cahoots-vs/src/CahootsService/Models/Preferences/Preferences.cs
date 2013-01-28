/// Preferences Model
/// Codeora 2013
///

namespace Cahoots.Services.Models
{
    using System.Collections.ObjectModel;

    public class Preferences
    {
        /// <summary>
        /// Gets or sets the servers.
        /// </summary>
        /// <value>
        /// The servers.
        /// </value>
        public Collection<Server> Servers { get; set; }

        /// <summary>
        /// Gets or sets a value indicating whether to [save chat logs].
        /// </summary>
        /// <value>
        ///   <c>true</c> to [save chat logs]; otherwise, <c>false</c>.
        /// </value>
        public bool SaveChatLogs { get; set; }
    }
}
