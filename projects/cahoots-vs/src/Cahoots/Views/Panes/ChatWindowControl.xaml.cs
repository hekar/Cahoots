/// Chat Window
/// Codeora 2013
///

namespace Cahoots
{
    using System.Windows;
    using System.Windows.Controls;
    using Cahoots.Services.ViewModels;

    public delegate void Send(string to, string message);

    /// <summary>
    /// Interaction logic for ChatWindowControl.xaml
    /// </summary>
    public partial class ChatWindowControl : UserControl
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="ChatWindowControl" /> class.
        /// </summary>
        public ChatWindowControl()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Gets or sets the view model.
        /// </summary>
        /// <value>
        /// The view model.
        /// </value>
        private ChatViewModel viewModel { get; set; }
        public ChatViewModel ViewModel
        {
            get
            {
                return this.viewModel;
            }
            set
            {
                this.viewModel = value;

                if (value != null)
                {
                    lstHistory.ItemsSource = this.viewModel.Messages;
                }
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
            if (this.viewModel != null)
            {
                this.ViewModel.SendMessage(txtMessage.Text);
                txtMessage.Clear();
                txtMessage.Focus();
            }
        }

        /// <summary>
        /// Handles the TextChanged event of the txtMessage control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="TextChangedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void txtMessage_TextChanged(object sender, TextChangedEventArgs e)
        {
            btnSend.IsEnabled = txtMessage.Text.Length != 0;
        }
    }
}
