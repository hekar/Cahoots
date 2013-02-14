using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using Cahoots.Services.Models;
using Cahoots.Ext.View;

namespace Cahoots.Views.Custom
{
    /// <summary>
    /// Interaction logic for SelectCollaborator.xaml
    /// </summary>
    public partial class SelectCollaborators : Window
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="SelectCollaborators" /> class.
        /// </summary>
        /// <param name="collaborators">The collaborators.</param>
        public SelectCollaborators(IEnumerable<Collaborator> collaborators)
        {
            InitializeComponent();

            TheList = new ViewModelCollection<Collab>();

            foreach (var co in collaborators.Where(c => c.Status == "online"))
            {
                var collab = new Collab() { Collaborator = co };
                TheList.Add(collab);
            }

            lvCollabs.ItemsSource = TheList;
        }

        /// <summary>
        /// Gets or sets the list.
        /// </summary>
        /// <value>
        /// The list.
        /// </value>
        private ViewModelCollection<Collab> TheList { get; set; }

        /// <summary>
        /// Gets or sets the selected.
        /// </summary>
        /// <value>
        /// The selected.
        /// </value>
        public Collection<Collaborator> Selected { get; set; }

        /// <summary>
        /// Handles the Click event of the OkButton control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void OkButton_Click(object sender, RoutedEventArgs e)
        {
            if (TheList.Any(c => c.Checked))
            {
                this.Selected = new Collection<Collaborator>();

                foreach (var c in TheList.Where(c => c.Checked))
                {
                    this.Selected.Add(c.Collaborator);
                }

                this.DialogResult = true;
                this.Close();
            }
        }

        /// <summary>
        /// Handles the Click event of the CancelButton control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void CancelButton_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = false;
            this.Close();
        }
    }

    class Collab
    {
        /// <summary>
        /// Gets or sets the collaborator.
        /// </summary>
        /// <value>
        /// The collaborator.
        /// </value>
        public Collaborator Collaborator { get; set; }

        /// <summary>
        /// Gets or sets a value indicating whether this <see cref="Collab" /> is checked.
        /// </summary>
        /// <value>
        ///   <c>true</c> if checked; otherwise, <c>false</c>.
        /// </value>
        public bool Checked { get; set; }
    }
}
