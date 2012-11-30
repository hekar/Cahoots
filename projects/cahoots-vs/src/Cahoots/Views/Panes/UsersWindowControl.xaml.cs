// ----------------------------------------------------------------------
// <copyright file="UsersWindowControl.xaml.cs" company="Codeora">
//     Copyright 2012. All rights reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots
{
    using System.Windows.Controls;
    using Cahoots.Services.ViewModels;

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
            this.ViewModel = CahootsPackage.Instance.GetViewModel("users") as UsersViewModel;
            this.DataContext = this.ViewModel;
            this.dataGrid1.ItemsSource = this.ViewModel.Users;  
        }

        /// <summary>
        /// Gets or sets the view model.
        /// </summary>
        /// <value>
        /// The view model.
        /// </value>
        private UsersViewModel ViewModel { get; set; }
    }
}
