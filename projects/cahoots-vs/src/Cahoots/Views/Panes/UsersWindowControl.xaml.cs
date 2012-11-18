using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Cahoots.Services;
using System.Collections.Specialized;

namespace Cahoots
{
    /// <summary>
    /// Interaction logic for UsersWindowControl.xaml
    /// </summary>
    public partial class UsersWindowControl : UserControl
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="UsersWindowControl" /> class.
        /// </summary>
        public UsersWindowControl()
        {
            InitializeComponent();
            if (CahootsPackage.Instance != null)
            {
                this.dataGrid1.ItemsSource = CahootsPackage.Instance.ActiveUsers;
                CahootsPackage.Instance.ActiveUsers.CollectionChanged += PropertyChanged;
            }
        }

        /// <summary>
        /// Properties the changed.
        /// </summary>
        /// <param name="sender">The sender.</param>
        /// <param name="e">The <see cref="NotifyCollectionChangedEventArgs" /> instance containing the event data.</param>
        public void PropertyChanged(object sender, NotifyCollectionChangedEventArgs e)
        {
            this.dataGrid1.ItemsSource = CahootsPackage.Instance.ActiveUsers;
        }
    }
}