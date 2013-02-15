///
///
///

namespace Cahoots.Services
{
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Diagnostics;
    using System.Windows;
    using Cahoots.Ext;
    using Cahoots.Ext.String;
    using Cahoots.Services.Contracts;
    using Cahoots.Services.MessageModels.Ops;
    using Cahoots.Services.Models;
    using Cahoots.Services.ViewModels;
    using Microsoft.VisualStudio.Text;

    public class OpService : AsyncService
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="OpService" /> class.
        /// </summary>
        public OpService(IWindowService windowService) : base("op")
        {
            this.Editors = new Dictionary<string, DocumentModel>();
            this.WindowService = WindowService;
        }

        /// <summary>
        /// Gets or sets the editors.
        /// </summary>
        /// <value>
        /// The editors.
        /// </value>
        private Dictionary<string, DocumentModel> Editors { get; set; }

        /// <summary>
        /// Gets or sets the window service.
        /// </summary>
        /// <value>
        /// The window service.
        /// </value>
        private IWindowService WindowService { get; set; }

        /// <summary>
        /// Starts the collaboration.
        /// </summary>
        /// <param name="user">The user.</param>
        /// <param name="document">The document.</param>
        /// <param name="users">The users.</param>
        public void StartCollaboration(string user, string documentId, List<string> users)
        {
            var model = new SendShareMessage()
            {
                Service = "op",
                MessageType = "share",
                User = user,
                DocumentId = documentId,
                Collaborators = new Collection<string>(users)
            };

            this.SendMessage(model);
        }

        /// <summary>
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        /// <returns></returns>
        public override BaseViewModel GetViewModel(params object[] parameters)
        {
            return null;
        }

        /// <summary>
        /// Processes the JSON message.
        /// </summary>
        /// <param name="type">The message type.</param>
        /// <param name="json">The json.</param>
        public override void ProcessMessage(string type, string json)
        {
            switch (type)
            {
                case "insert":
                    var insert = JsonHelper.Deserialize<OpInsertMessage>(json);
                    this.Insert(insert);
                    break;

                case "delete":
                    var delete = JsonHelper.Deserialize<OpDeleteMessage>(json);
                    this.Delete(delete);
                    break;

                case "replace":
                    var replace = JsonHelper.Deserialize<OpReplaceMessage>(json);
                    this.Replace(replace);
                    break;

                case "shared":
                    var share = JsonHelper.Deserialize<ReceiveShareMessage>(json);
                    this.ReceiveShare(share);
                    break;
            }
        }

        /// <summary>
        /// Performs an insert in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Insert(OpInsertMessage model)
        {
            if (this.Editors.ContainsKey(model.DocumentId))
            {
                var view = this.Editors[model.DocumentId].View;
                view.TextBuffer.Insert(model.Start, model.Content);
            }
        }

        /// <summary>
        /// Performs a delete in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Delete(OpDeleteMessage model)
        {
            if (this.Editors.ContainsKey(model.DocumentId))
            {
                var view = this.Editors[model.DocumentId].View;
                var span = new Span(model.Start, model.End - model.Start);
                view.TextBuffer.Delete(span);
            }
        }

        /// <summary>
        /// Performs a replace in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Replace(OpReplaceMessage model)
        {
            if (this.Editors.ContainsKey(model.DocumentId))
            {
                var view = this.Editors[model.DocumentId].View;
                var span = new Span(model.Start, model.End - model.Start);
                view.TextBuffer.Replace(span, model.Content);
            }
        }

        /// <summary>
        /// Receives a share request.
        /// </summary>
        /// <param name="model">The model.</param>
        public void ReceiveShare(ReceiveShareMessage model)
        {
            bool accept = false;

            if (model.Sharer != this.UserName)
            {
                var msg = string.Format("{0} would like to share a document with you.  Accept?", model.Sharer);
                var result = MessageBox.Show(msg, "Collaboration Invite", MessageBoxButton.YesNo);

                if (result == MessageBoxResult.Yes)
                {
                    accept = true;
                }
            }
            else
            {
                accept = true;
            }

            if (accept)
            {
                // find/open the document
                var view = this.WindowService.OpenDocumentWindow("");

                var doc = new DocumentModel()
                {
                    DocumentId = model.DocumentId,
                    FullPath = "",
                    OpId = model.OpId,
                    View = view
                };

                // attach events and stuff
                view.TextBuffer.Changed += TextBuffer_Changed;
                view.Closed += new System.EventHandler(view_Closed);
            }
        }

        /// <summary>
        /// Handles the Changed event of the TextBuffer control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="TextContentChangedEventArgs" />
        ///   instance containing the event data.
        /// </param>
        private void TextBuffer_Changed(
                object sender,
                TextContentChangedEventArgs e)
        {
            foreach (var change in e.Changes)
            {
                if (change.OldText.IsNullOrEmpty() && !change.NewText.IsNullOrEmpty())
                {
                    // insert
                    Debug.WriteLine(
                            "INSERT {0} to {1}",
                            change.NewPosition,
                            change.NewPosition + change.NewLength);
                    Debug.WriteLine(change.NewText);
                    Debug.WriteLine("");

                    var model = new OpInsertMessage()
                    {
                        Service = "op",
                        MessageType = "insert",
                        User = this.UserName,
                        Start = change.NewPosition,
                        Content = change.NewText
                    };

                    this.SendMessage(model);
                }
                else if (!change.OldText.IsNullOrEmpty() && change.NewText.IsNullOrEmpty())
                {
                    // delete
                    Debug.WriteLine(
                            "DELETE {0} to {1}",
                            change.OldPosition,
                            change.OldPosition + change.OldLength);
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
                    Debug.WriteLine("-------------------------------------------");
                    Debug.WriteLine(change.NewText);
                    Debug.WriteLine("");
                }
            }
        }

        /// <summary>
        /// Handles the Closed event of the view control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">
        ///   The <see cref="System.EventArgs" />
        ///   instance containing the event data.
        /// </param>
        void view_Closed(object sender, System.EventArgs e)
        {
            // end collaboration
        }

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        public override void Disconnect()
        {
        }
    }
}
