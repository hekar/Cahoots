
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
using Cahoots.Services.ViewModels;
using Cahoots.Services;
using Cahoots.Views.Custom;

namespace Cahoots
{
    /// <summary>
    /// Interaction logic for CollaborationsWindowControl.xaml
    /// </summary>
    public partial class CollaborationsWindowControl : UserControl
    {
        public CollaborationsWindowControl()
        {
            InitializeComponent();
            var vm = CahootsPackage.Instance.GetViewModel("op") as CollaborationsViewModel;
            this.DataContext = vm;
        }

        private void LeaveItem_Click(object sender, RoutedEventArgs e)
        {
            
            MenuItem menu = sender as MenuItem;
            CollaborationsViewModel.Collaboration item = menu.DataContext as CollaborationsViewModel.Collaboration;

            var service = CahootsPackage.Instance.CommunicationRelay.Services["op"] as OpService;
            service.LeaveCollaboration(CahootsPackage.Instance.UserName, item.OpId);
        }

        private void InviteItem_Click(object sender, RoutedEventArgs e)
        {
            MenuItem menu = sender as MenuItem;
            CollaborationsViewModel.Collaboration item = menu.DataContext as CollaborationsViewModel.Collaboration;

            var service = CahootsPackage.Instance.CommunicationRelay.Services["op"] as OpService;

            var users = CahootsPackage.Instance.CommunicationRelay.Service<UsersService>();
            var window = new SelectCollaborators(users.GetCollaborators());

            if (window.ShowDialog() == true)
            {
                var collaborators = window.Selected.Select(c => c.UserName);
                foreach (var c in collaborators)
                {
                    service.InviteUser(c, item.OpId);
                }
            }
        }
    }
}
