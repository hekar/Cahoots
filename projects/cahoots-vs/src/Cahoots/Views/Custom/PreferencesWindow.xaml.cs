/// PreferencesWindow.xaml.cs
/// Codeora 2013
///
/// Preferences window class.
///

namespace Cahoots
{
    using System.Windows;
    using Cahoots.Ext.View;
    using Cahoots.Services.Models;
    using System.Windows.Controls;

    /// <summary>
    /// Interaction logic for PreferencesWindow.xaml
    /// </summary>
    public partial class PreferencesWindow : Window
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="PreferencesWindow" /> class.
        /// </summary>
        public PreferencesWindow(Preferences preferences)
        {
            this.InitializeComponent();
            this.Servers = new ViewModelCollection<Server>();
            this.lvServers.ItemsSource = this.Servers;
            this.Preferences = preferences;
            this.txtChatLogsDirectory.Text =
                        this.Preferences.ChatLogsDirectory;
            this.chkSaveChat.IsChecked = this.Preferences.SaveChatLogs;

            foreach (var server in this.Preferences.Servers)
            {
                this.Servers.Add(new Server(server));
            }
        }

        /// <summary>
        /// Gets or sets the preferences.
        /// </summary>
        /// <value>
        /// The preferences.
        /// </value>
        public Preferences Preferences { get; set; }

        /// <summary>
        /// Gets or sets the servers.
        /// </summary>
        /// <value>
        /// The servers.
        /// </value>
        private ViewModelCollection<Server> Servers { get; set; }

        /// <summary>
        /// Handles the Click event of the Button control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void btnSave_Click(object sender, RoutedEventArgs e)
        {
            // copy the preferences
            this.Preferences.SaveChatLogs =
                    this.chkSaveChat.IsChecked ?? false;
            this.Preferences.ChatLogsDirectory =
                    this.txtChatLogsDirectory.Text;

            this.Preferences.Servers.Clear();

            foreach (var server in this.Servers)
            {
                this.Preferences.Servers.Add(new Server(server));
            }

            this.DialogResult = true;
        }

        /// <summary>
        /// Handles the Click event of the btnCancel control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void btnCancel_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = false;
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
            var form = new System.Windows.Forms.FolderBrowserDialog();
            form.SelectedPath = this.txtChatLogsDirectory.Text;
            if (form.ShowDialog() == System.Windows.Forms.DialogResult.OK)
            {
                this.txtChatLogsDirectory.Text = form.SelectedPath;
            }
        }

        /// <summary>
        /// Handles the SelectionChanged event of the ServersList control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="SelectionChangedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void ServersList_SelectionChanged(
                object sender,
                SelectionChangedEventArgs e)
        {
            btnEditServer.IsEnabled = lvServers.SelectedIndex != -1;
            btnDeleteServer.IsEnabled = lvServers.SelectedIndex != -1;
        }

        /// <summary>
        /// Handles the Click event of the btnEditServer control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void btnEditServer_Click(object sender, RoutedEventArgs e)
        {
            var server = this.lvServers.SelectedItem as Server;
            if (server != null)
            {
                var window = new EditServerWindow();
                window.ServerName = server.Name;
                window.ServerAddress = server.Address;

                if (window.ShowDialog() == true)
                {
                    server.Name = window.ServerName;
                    server.Address = window.ServerAddress;
                    this.lvServers.ItemsSource = null;
                    this.lvServers.ItemsSource = this.Servers;
                }
            }
        }

        /// <summary>
        /// Handles the Click event of the btnDeleteServer control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="RoutedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void btnDeleteServer_Click(object sender, RoutedEventArgs e)
        {
            var server = this.lvServers.SelectedItem as Server;
            if (server != null)
            {
                this.Servers.Remove(server);
            }
        }

        /// <summary>
        /// Handles the Click event of the btnNewServer control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="RoutedEventArgs" /> instance containing the event data.</param>
        private void btnNewServer_Click(object sender, RoutedEventArgs e)
        {
            var window = new EditServerWindow();
            if (window.ShowDialog() == true)
            {
                this.Servers.Add(
                    new Server()
                    {
                        Name = window.ServerName,
                        Address = window.ServerAddress
                    });
            }
        }
    }
}
