
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


    }
}
