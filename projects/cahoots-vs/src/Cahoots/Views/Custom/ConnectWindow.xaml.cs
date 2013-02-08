/// Cahoots Package.cs
/// Codeora 2013
///
/// The class for the Connect window.
///

namespace Cahoots
{
    using System;
    using System.Linq;
    using System.Windows;
    using System.Windows.Controls;
    using System.Windows.Input;
    using System.Collections.Generic;

    /// <summary>
    /// Interaction logic for ConnectWindow.xaml
    /// </summary>
    public partial class ConnectWindow : Window
    {
        /// <summary>
        /// Initializes a new instance of the 
        /// <see cref="ConnectWindow" /> class.
        /// </summary>
        public ConnectWindow(IEnumerable<string> servers)
        {
            InitializeComponent();
            foreach (var server in servers)
            {
                cbServer.Items.Add(new ComboBoxItem() { Content = server });
            }
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
        public string Password { get; private set; }

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
            this.Password = pwPassword.Password;

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

        /// <summary>
        /// Handles the Input event of the Handle control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="EventArgs" /> instance containing the event data.
        /// </param>
        private void Handle_Input(
                object sender,
                EventArgs e)
        {
            if (btnConnect != null)
            {
                btnConnect.IsEnabled = (!string.IsNullOrEmpty(txtUsername.Text)
                    && !string.IsNullOrEmpty(pwPassword.Password)
                    && cbServer.SelectedIndex != -1);
            }
        }

        /// <summary>
        /// Handles the KeyDown event of the txtUsername control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="KeyEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void txtUsername_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && btnConnect.IsEnabled)
            {
                Connect_Click(sender, new RoutedEventArgs());
            }
        }

        /// <summary>
        /// Handles the KeyDown event of the pwPassword control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="KeyEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void pwPassword_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && btnConnect.IsEnabled)
            {
                Connect_Click(sender, new RoutedEventArgs());
            }
        }

        /// <summary>
        /// Handles the Click event of the Button control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void Button_Click(object sender, RoutedEventArgs e)
        {
            if (CahootsPackage.Instance.ShowPreferences())
            {
                cbServer.Items.Clear();
                foreach (var server in CahootsPackage.Instance.Preferences.Servers.Select(s => s.Name))
                {
                    cbServer.Items.Add(new ComboBoxItem() { Content = server });
                }

                cbServer.SelectedIndex = 0;
            }
        }
    }
}
