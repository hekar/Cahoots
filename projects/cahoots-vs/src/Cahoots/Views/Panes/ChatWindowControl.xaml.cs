
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
using Cahoots.Services.Models;

namespace Cahoots
{
    public delegate void Send(string to, string message);

    /// <summary>
    /// Interaction logic for ChatWindowControl.xaml
    /// </summary>
    public partial class ChatWindowControl : UserControl
    {
        public ChatWindowControl()
        {
            InitializeComponent();
        }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Globalization", "CA1300:SpecifyMessageBoxOptions")]
        private void button1_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show(string.Format(System.Globalization.CultureInfo.CurrentUICulture, "We are inside {0}.button1_Click()", this.ToString()),
                            "ChatWindow");

        }

        /// <summary>
        /// Gets or sets the send message.
        /// </summary>
        /// <value>
        /// The send message.
        /// </value>
        public Send SendMessage { get; set; }

        /// <summary>
        /// Gets or sets the chatee.
        /// </summary>
        /// <value>
        /// The chatee.
        /// </value>
        public Collaborator Chatee { get; set; }

        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (this.SendMessage != null)
            {
                this.SendMessage(Chatee.Name, txtMessage.Text);
            }
        }
    }
}
