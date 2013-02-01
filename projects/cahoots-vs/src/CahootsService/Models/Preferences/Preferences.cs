/// Preferences.cs
/// Codeora 2013
///
/// Preferences Model for saving and managing preferences.
///

namespace Cahoots.Services.Models
{
    using System.Collections.ObjectModel;

    public class Preferences
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="Preferences" /> class.
        /// </summary>
        public Preferences()
        {
            this.Servers = new Collection<Server>();
        }

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

        /// <summary>
        /// Gets or sets the chat logs path.
        /// </summary>
        /// <value>
        /// The chat logs path.
        /// </value>
        public string ChatLogsDirectory { get; set; }
    }
}
