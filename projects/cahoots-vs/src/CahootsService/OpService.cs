///
///
///

namespace Cahoots.Services
{
    using System;
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Diagnostics;
    using System.Windows;
    using System.Linq;
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
            this.Documents = new Dictionary<string, DocumentModel>();
            this.WindowService = windowService;
        }

        /// <summary>
        /// Gets or sets the editors.
        /// </summary>
        /// <value>
        /// The editors.
        /// </value>
        private Dictionary<string, DocumentModel> Documents { get; set; }

        /// <summary>
        /// Gets or sets the window service.
        /// </summary>
        /// <value>
        /// The window service.
        /// </value>
        private IWindowService WindowService { get; set; }

        public string[] GetOpIds()
        {
            return this.Documents.Values.Select(i => i.OpId).ToArray();
        }

        /// <summary>
        /// Starts the collaboration.
        /// </summary>
        /// <param name="user">The user.</param>
        /// <param name="document">The document.</param>
        /// <param name="users">The users.</param>
        public void StartCollaboration(
                string user,
                string documentId,
                List<string> users)
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

        public void LeaveCollaboration(string user, string opId) {
            var model = new LeaveCollaborationMessage(){
                Service = "op",
                MessageType = "leave",
                User = user,
                OpId = opId
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
                    if (insert.User != this.UserName)
                    {
                        this.Insert(insert);
                    }
                    break;

                case "delete":
                    var delete = JsonHelper.Deserialize<OpDeleteMessage>(json);
                    if (delete.User != this.UserName)
                    {
                        this.Delete(delete);
                    }
                    break;

                case "replace":
                    var replace =
                            JsonHelper.Deserialize<OpReplaceMessage>(json);
                    if (replace.User != this.UserName)
                    {
                        this.Replace(replace);
                    }
                    break;

                case "shared":
                    var share =
                            JsonHelper.Deserialize<ReceiveShareMessage>(json);
                    this.ReceiveShare(share);
                    break;
                case "left":
                    var left = JsonHelper.Deserialize<CollaboratorLeftMessage>(json);
                    this.CollaboratorLeft(left);
                    break;
            }
        }

        private void CollaboratorLeft(CollaboratorLeftMessage left)
        {
            //TODO
        }

        /// <summary>
        /// Performs an insert in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Insert(OpInsertMessage model)
        {
            if (this.Documents.ContainsKey(model.DocumentId))
            {
                var doc = this.Documents[model.DocumentId];
                doc.BlockEvent = true;
                var view = doc.View;
                this.WindowService.InvokeOnUI(
                    () => view.TextBuffer.Insert(model.Start, model.Content));
            }
        }

        /// <summary>
        /// Performs a delete in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Delete(OpDeleteMessage model)
        {
            if (this.Documents.ContainsKey(model.DocumentId))
            {
                var doc = this.Documents[model.DocumentId];
                doc.BlockEvent = true;
                var view = doc.View;
                var span = new Span(model.Start, model.End - model.Start);
                this.WindowService.InvokeOnUI(
                    () => view.TextBuffer.Delete(span));
            }
        }

        /// <summary>
        /// Performs a replace in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Replace(OpReplaceMessage model)
        {
            if (this.Documents.ContainsKey(model.DocumentId))
            {
                var doc = this.Documents[model.DocumentId];
                doc.BlockEvent = true;
                var view = doc.View;
                var span = new Span(model.Start, model.End - model.Start);
                this.WindowService.InvokeOnUI(
                    () => view.TextBuffer.Replace(span, model.Content));
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
                var msg = string.Format(
                    "{0} would like to share a document with you.  Accept?",
                    model.Sharer);

                var result = MessageBox.Show(
                        msg,
                        "Collaboration Invite",
                        MessageBoxButton.YesNo);

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
                var tuple = this.WindowService.OpenDocumentWindow(
                                    model.DocumentId);

                var view = tuple.Item2;

                var tick = this.WindowService.GetCurrentTick(model.OpId);

                var doc = new DocumentModel(tick)
                {
                    DocumentId = model.DocumentId,
                    FullPath = tuple.Item1,
                    OpId = model.OpId,
                    View = view
                };

                this.Documents.Add(model.DocumentId, doc);

                // attach events and stuff
                view.TextBuffer.Changed += (s, e) => TextChanged(s, e, model.DocumentId);
                view.Closed += (s, e) => EndCollaboration(s, e, model.DocumentId);
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
        /// <param name="docId">The document id.</param>
        private void TextChanged(object sender, TextContentChangedEventArgs e, string docId)
        {
            var doc = this.Documents[docId];

            foreach (var change in e.Changes)
            {
                if (doc.BlockEvent)
                {
                    continue;
                }

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
                        Content = change.NewText,
                        DocumentId = docId,
                        OpId = doc.OpId,
                        TickStamp = doc.TickStamp
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

                    var model = new OpDeleteMessage()
                    {
                        Service = "op",
                        MessageType = "delete",
                        User = this.UserName,
                        Start = change.OldPosition,
                        End = change.OldPosition + change.OldLength,
                        DocumentId = docId,
                        OpId = doc.OpId,
                        TickStamp = doc.TickStamp
                    };

                    this.SendMessage(model);
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

                    var model = new OpReplaceMessage()
                    {
                        Service = "op",
                        MessageType = "replace",
                        User = this.UserName,
                        Start = change.OldPosition,
                        End = change.OldPosition + change.OldLength,
                        Content = change.NewText,
                        DocumentId = docId,
                        OpId = doc.OpId,
                        TickStamp = doc.TickStamp
                    };

                    this.SendMessage(model);
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
        /// <param name="docId">The document id.</param>
        private void EndCollaboration(object sender, EventArgs e, string documentId)
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