///
///
///

namespace Cahoots.Services.ViewModels
{
    using System.Collections.ObjectModel;
    using Cahoots.Ext.View;

    public class CollaborationsViewModel : BaseViewModel
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="CollaborationsViewModel" /> class.
        /// </summary>
        public CollaborationsViewModel()
        {
            this.Collaborations = new ViewModelCollection<Collaboration>();
        }

        /// <summary>
        /// Gets or sets the collaborations.
        /// </summary>
        /// <value>
        /// The collaborations.
        /// </value>
        public ViewModelCollection<Collaboration> Collaborations { get; set; }

        /// <summary>
        /// Represents a collaboration
        /// </summary>
        public class Collaboration
        {

            public Collaboration()
            {
                this.Users = new ViewModelCollection<string>();
            }

            /// <summary>
            /// Gets or sets the document id.
            /// </summary>
            /// <value>
            /// The document id.
            /// </value>
            public string DocumentId { get; set; }

            /// <summary>
            /// Gets or sets the op id.
            /// </summary>
            /// <value>
            /// The op id.
            /// </value>
            public string OpId { get; set; }

            /// <summary>
            /// Gets or sets the users.
            /// </summary>
            /// <value>
            /// The users.
            /// </value>
            public ViewModelCollection<string> Users { get; set; }
        }
    }
}
