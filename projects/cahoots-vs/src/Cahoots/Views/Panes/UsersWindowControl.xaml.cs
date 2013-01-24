// ----------------------------------------------------------------------
// <copyright file="UsersWindowControl.xaml.cs" company="Codeora">
//     Copyright 2012. All rights reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots
{
    using System.Collections.Generic;
    using System.Windows.Controls;
    using Cahoots.Services.Models;
    using Cahoots.Services.ViewModels;
    using Microsoft.VisualStudio.Shell;
    using Microsoft.VisualStudio.Shell.Interop;
    using Cahoots.Services;

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
            this.Chats = new Dictionary<string, ToolWindowPane>();
        }

        /// <summary>
        /// Gets or sets the view model.
        /// </summary>
        /// <value>
        /// The view model.
        /// </value>
        private UsersViewModel ViewModel { get; set; }

        /// <summary>
        /// Gets or sets the chats.
        /// </summary>
        /// <value>
        /// The chats.
        /// </value>
        private Dictionary<string, ToolWindowPane> Chats { get; set; }

        private int i;

        /// <summary>
        /// Handles the Click event of the MenuItem control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="System.Windows.RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void MenuItem_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            var user = this.dataGrid1.CurrentItem as Collaborator;

            if (!Chats.ContainsKey(user.Name))
            {
                var pane = CahootsPackage.Instance.FindToolWindow(typeof(ChatWindowToolWindow), i++, true);
                pane.Caption = "Chat � " + user.Name;
                Chats.Add(user.Name, pane);
                ((IVsWindowFrame)pane.Frame).Show();
                var win = (pane as ChatWindowToolWindow).Content as ChatWindowControl;
                var vm = CahootsPackage.Instance.GetViewModel("chat", user, CahootsPackage.Instance.Me) as ChatViewModel;
                win.ViewModel = vm;
            }
            else
            {
                ((IVsWindowFrame)Chats[user.Name].Frame).Show();
            }
        }
    }
}
