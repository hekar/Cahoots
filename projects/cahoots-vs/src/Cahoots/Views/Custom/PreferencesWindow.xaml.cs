/// PreferencesWindow.xaml.cs
/// Codeora 2013
///
/// Preferences window class.
///

namespace Cahoots
{
    using System.Windows;
    using Cahoots.Ext;
    using Cahoots.Services.Models;

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
            InitializeComponent();
            this.Preferences = preferences;
            var pref = new Preferences();
        }

        /// <summary>
        /// Gets or sets the preferences.
        /// </summary>
        /// <value>
        /// The preferences.
        /// </value>
        public Preferences Preferences { get; set; }

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
    }
}
