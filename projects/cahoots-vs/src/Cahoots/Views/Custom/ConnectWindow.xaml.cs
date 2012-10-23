///
///
///

namespace Cahoots
{
    using System.Security;
    using System.Windows;
    using System.Windows.Controls;

    /// <summary>
    /// Interaction logic for ConnectWindow.xaml
    /// </summary>
    public partial class ConnectWindow : Window
    {
        /// <summary>
        /// Initializes a new instance of the 
        /// <see cref="ConnectWindow" /> class.
        /// </summary>
        public ConnectWindow()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Gets the name of the user.
        /// </summary>
        /// <value>
        /// The name of the user.
        /// </value>
        public string UserName { get; private set; }

        /// <summary>
        /// Gets the password.
        /// </summary>
        /// <value>
        /// The password.
        /// </value>
        public SecureString Password { get; private set; }

        /// <summary>
        /// Gets the server.
        /// </summary>
        /// <value>
        /// The server.
        /// </value>
        public string Server { get; private set; }

        /// <summary>
        /// Handles the Click event of the Connect Button.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" /> 
        ///   instance containing the event data.
        /// </param>
        private void Connect_Click(object sender, RoutedEventArgs e)
        {
            this.UserName = txtUsername.Text;
            this.Password = pwPassword.SecurePassword;

            var server = ((ComboBoxItem)cbServer.SelectedItem);
            if (server != null)
            {
                this.Server = (string)server.Content;
            }

            this.DialogResult = true;
            this.Close();
        }

        /// <summary>
        /// Handles the Click event of the Cancel Button.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" /> 
        ///   instance containing the event data.
        /// </param>
        private void Cancel_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = false;
            this.Close();
        }
    }
}
