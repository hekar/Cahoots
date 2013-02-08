/// Represents a server to connect to
/// Codeora 2013
///

namespace Cahoots.Services.Models
{
    public class Server
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="Server" /> class.
        /// </summary>
        public Server()
        {
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="Server" /> class.
        /// </summary>
        /// <param name="server">The server.</param>
        public Server(Server server)
        {
            this.Name = server.Name;
            this.Address = server.Address;
        }

        /// <summary>
        /// Gets or sets the name.
        /// </summary>
        /// <value>
        /// The name.
        /// </value>
        public string Name { get; set; }

        /// <summary>
        /// Gets or sets the address.
        /// </summary>
        /// <value>
        /// The address.
        /// </value>
        public string Address { get; set; }
    }
}
