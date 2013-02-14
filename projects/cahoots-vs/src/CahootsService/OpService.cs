using System;
using System.Collections.Generic;
using System.Diagnostics;
using Microsoft.VisualStudio.Text.Editor;
using Microsoft.VisualStudio.Text;

namespace Cahoots.Services
{
    public class OpService : AsyncService
    {
        public OpService() : base("op")
        {
            this.Editors = new Dictionary<string, IWpfTextView>();
        }

        /// <summary>
        /// Gets or sets the editors.
        /// </summary>
        /// <value>
        /// The editors.
        /// </value>
        private Dictionary<string, IWpfTextView> Editors { get; set; }

        public void AddSharedDocument(string name, IWpfTextView view)
        {
            view.TextBuffer.Changed += TextBuffer_Changed;
        }

        /// <summary>
        /// Handles the Changed event of the TextBuffer control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="TextContentChangedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void TextBuffer_Changed(object sender, TextContentChangedEventArgs e)
        {
            foreach (var change in e.Changes)
            {
                if (string.IsNullOrEmpty(change.OldText) && !string.IsNullOrEmpty(change.NewText))
                {
                    // insert
                    Debug.WriteLine("INSERT {0} to {1}", change.NewPosition, change.NewPosition + change.NewLength);
                    Debug.WriteLine(change.NewText);
                    Debug.WriteLine("");
                }
                else if (!string.IsNullOrEmpty(change.OldText) && string.IsNullOrEmpty(change.NewText))
                {
                    // delete
                    Debug.WriteLine("DELETE {0} to {1}", change.OldPosition, change.OldPosition + change.OldLength);
                    Debug.WriteLine(change.OldText);
                    Debug.WriteLine("");
                }
                else
                {
                    // replace
                    Debug.WriteLine(
                        "REPLACE {0} to {1} with {2} to {3}",
                        change.OldPosition,
                        change.OldPosition + change.OldLength,
                        change.NewPosition,
                        change.NewPosition + change.NewLength);
                    Debug.WriteLine(change.OldText);
                    Debug.WriteLine("-----------------------------------------------------");
                    Debug.WriteLine(change.NewText);
                    Debug.WriteLine("");
                }
            }
        }

        public override void ProcessMessage(string type, string json)
        {
        }

        public override ViewModels.BaseViewModel GetViewModel(params object[] parameters)
        {
            throw new NotImplementedException();
        }

        public override void Disconnect()
        {
        }
    }
}
