///
///
///

namespace Cahoots.Services
{
    using System;
    using System.Collections.Generic;
    using System.Collections.ObjectModel;
    using System.Diagnostics;
    using System.Linq;
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
            this.Documents = new Dictionary<string, DocumentModel>();
            this.ViewModel = new CollaborationsViewModel();
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
        /// Gets or sets the view model.
        /// </summary>
        /// <value>
        /// The view model.
        /// </value>
        private CollaborationsViewModel ViewModel { get; set; }

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
        public void StartCollaboration(
                string user,
                string documentId,
                List<string> users)
        {

            // find/open the document
            var tuple = this.WindowService.OpenDocumentWindow(
                                documentId);

            var view = tuple.Item2;
            
            var model = new SendShareMessage()
            {
                Service = "op",
                MessageType = "share",
                User = user,
                DocumentId = documentId,
                Collaborators = new Collection<string>(users),
                Contents = view.TextBuffer.CurrentSnapshot.GetText()
            };

            this.SendMessage(model);
        }

        /// <summary>
        /// Leaves the collaboration.
        /// </summary>
        /// <param name="user">The user.</param>
        /// <param name="opId">The op id.</param>
        public void LeaveCollaboration(string user, string opId)
        {
            var model = new LeaveCollaborationMessage(){
                Service = "op",
                MessageType = "leave",
                User = user,
                OpId = opId
            };
            RemoveDocument(opId);
            this.SendMessage(model);
        }

        /// <summary>
        /// Removes the document.
        /// </summary>
        /// <param name="opId">The op id.</param>
        private void RemoveDocument(string opId)
        {
            var docs = (from c in this.Documents.Values where c.OpId == opId select c).ToList();
            foreach (var doc in docs)
            {
                doc.View.Closed -= doc.Closed;
                doc.View.TextBuffer.Changed -= doc.Changed;

                this.Documents.Remove(doc.DocumentId);
            }
        }

        /// <summary>
        /// Gets a view model for the service.
        /// </summary>
        /// <param name="parameters">The parameters.</param>
        /// <returns></returns>
        public override BaseViewModel GetViewModel(params object[] parameters)
        {
            return this.ViewModel;
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
                case "delete":
                    this.ApplyOp(json, type);
                    break;

                case "replace":
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

                case "joined":
                    var joined = JsonHelper.Deserialize<CollaboratorJoinedMessage>(json);
                    this.CollaboratorJoined(joined);
                    break;

                case "collaborators":
                    var collaborators = JsonHelper.Deserialize<CollaboratorsListMessage>(json);
                    this.CollaboratorsList(collaborators);
                    break;
            }
        }

        /// <summary>
        /// Applies the op.
        /// </summary>
        /// <param name="json">The json.</param>
        /// <param name="type">The type.</param>
        private void ApplyOp(string json, string type)
        {
            // incoming remote change
            BaseOpMessage remote = null;

            switch (type)
            {
                case "insert":
                    remote = JsonHelper.Deserialize<OpInsertMessage>(json);
                    break;

                case "delete":
                    remote = JsonHelper.Deserialize<OpDeleteMessage>(json);
                    break;

                case "replace":
                    remote = JsonHelper.Deserialize<OpReplaceMessage>(json);
                    break;

                default:
                    throw new InvalidOperationException("Invalid op type");
            }

            if (this.Documents.ContainsKey(remote.DocumentId) && remote.User != this.UserName)
            {
                var doc = this.Documents[remote.DocumentId];

                this.WindowService.InvokeOnUI(() =>
                {
                    lock (doc.OpLocker)
                    {
                        var edit = doc.View.TextBuffer.CreateEdit();
                        
                        // do op

                        doc.BlockEvent = true;
                        edit.Apply();
                    }
                });
            }
        }

        private int MoveOp(BaseOpMessage winner, BaseOpMessage loser)
        {
            var offset = 0;

            // the loser only needs to move if its position is after the winner
            if (loser.Index >= winner.Index)
            {
                switch (winner.MessageType)
                {
                    case "insert":
                        var i = winner as OpInsertMessage;
                        offset = i.Content.Length;
                        break;

                    case "delete":
                        var d = winner as OpDeleteMessage;
                        offset = d.Index - d.End;
                        break;

                    case "replace":
                        var r = winner as OpReplaceMessage;
                        offset = r.Content.Length - r.OldContent.Length;
                        break;
                }

                switch (loser.MessageType)
                {
                    case "insert":
                        loser.Index += offset;
                        break;

                    case "delete":
                        var d = winner as OpDeleteMessage;
                        d.Index += offset;
                        d.End += offset;
                        break;

                    case "replace":
                        var r = winner as OpReplaceMessage;
                        r.Index += offset;
                        r.End += offset;
                        break;
                }
            }

            return offset;
        }

        private void UndoOp(DocumentModel doc, BaseOpMessage op, ITextEdit edit)
        {
            switch (op.MessageType)
            {
                case "insert":
                    var insert = (OpInsertMessage)op;
                    this.UndoInsert(doc, insert, edit);
                    break;

                case "delete":
                    var delete = (OpDeleteMessage)op;
                    this.UndoDelete(doc, delete, edit);
                    break;

                case "replace":
                    var replace = (OpReplaceMessage)op;
                    this.UndoReplace(doc, replace, edit);
                    break;
            }
        }

        private int RedoOp(DocumentModel doc, BaseOpMessage op, ITextEdit edit)
        {
            var len = 0;
            switch (op.MessageType)
            {
                case "insert":
                    var insert = (OpInsertMessage)op;
                    len = insert.Content.Length;
                    this.Insert(doc, insert, edit);
                    break;

                case "delete":
                    var delete = (OpDeleteMessage)op;
                    len = delete.Index - delete.End;
                    this.Delete(doc, delete, edit);
                    break;

                case "replace":
                    var replace = (OpReplaceMessage)op;
                    len = replace.OldContent.Length - replace.Content.Length;
                    this.Replace(doc, replace, edit);
                    break;
            }

            return len;
        }

        private int DoOp(DocumentModel doc, ITextEdit edit, BaseOpMessage op)
        {
            var len = 0;
            switch (op.MessageType)
            {
                case "insert":
                    var insert = op as OpInsertMessage;
                    len = insert.Content.Length;
                    doc.Changes.Add(insert);
                    this.Insert(doc, insert, edit);
                    break;

                case "delete":
                    var delete = op as OpDeleteMessage;
                    len = delete.Index - delete.End;
                    doc.Changes.Add(delete);
                    this.Delete(doc, delete, edit);
                    break;

                case "replace":
                    var replace = op as OpReplaceMessage;
                    len = replace.OldContent.Length - replace.Content.Length;
                    doc.Changes.Add(replace);
                    this.Replace(doc, replace, edit);
                    break;
            }

            return len;
        }

        /// <summary>
        /// Invites the user.
        /// </summary>
        /// <param name="user">The user.</param>
        /// <param name="OpId">The op id.</param>
        public void InviteUser(String user, String OpId)
        {
            var message = new InviteUserMessage()
            {
                MessageType="invite",
                OpId=OpId,
                Service="op",
                Sharer=UserName,
                User=user
            };

            this.SendMessage(message);
        }

        /// <summary>
        /// Collaboratorses the list.
        /// </summary>
        /// <param name="collaborators">The collaborators.</param>
        private void CollaboratorsList(CollaboratorsListMessage collaborators)
        {
            var collab = this.ViewModel.Collaborations.First(c => c.OpId == collaborators.OpId);
            foreach (var c in collaborators.Collaborators)
            {
                collab.Users.Add(c);
            }
        }

        /// <summary>
        /// Collaborators the joined.
        /// </summary>
        /// <param name="joined">The joined.</param>
        private void CollaboratorJoined(CollaboratorJoinedMessage joined)
        {
            var collab = this.ViewModel.Collaborations.First(c => c.OpId == joined.OpId);
            collab.Users.Add(joined.User);
        }

        /// <summary>
        /// Collaborators the left.
        /// </summary>
        /// <param name="left">The left.</param>
        private void CollaboratorLeft(CollaboratorLeftMessage left)
        {
            var collab = this.ViewModel.Collaborations.First(c => c.OpId == left.OpId);
            if (left.User == this.UserName)
            {
                this.ViewModel.Collaborations.Remove(collab);
                RemoveDocument(left.OpId);
            }
            else
            {
                collab.Users.Remove(left.User);
            }
        }

        /// <summary>
        /// Performs an insert in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Insert(DocumentModel doc, OpInsertMessage model, ITextEdit edit)
        {
            //doc.BlockEvent = true;
            var start = model.Index > edit.Snapshot.Length ? edit.Snapshot.Length : model.Index;
            edit.Insert(start, model.Content);
        }

        /// <summary>
        /// Undoes an insert operation.
        /// </summary>
        /// <param name="model">The model.</param>
        private void UndoInsert(DocumentModel doc, OpInsertMessage model, ITextEdit edit)
        {
            //doc.BlockEvent = true;
            var span = new Span(model.Index, model.Content.Length);
            edit.Delete(span);
        }

        /// <summary>
        /// Performs a delete in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Delete(DocumentModel doc, OpDeleteMessage model, ITextEdit edit)
        {
            //doc.BlockEvent = true;
            var len = model.End - model.Index;

            if (len > doc.View.TextBuffer.CurrentSnapshot.Length)
            {
                len = edit.Snapshot.Length;
            }

            var span = new Span(model.Index, len);
            edit.Delete(span);
        }

        /// <summary>
        /// Undoes a delete operation.
        /// </summary>
        /// <param name="model">The model.</param>
        private void UndoDelete(DocumentModel doc, OpDeleteMessage model, ITextEdit edit)
        {
            //doc.BlockEvent = true;
            edit.Insert(model.Index, model.OldContent);
        }

        /// <summary>
        /// Performs a replace in a document.
        /// </summary>
        /// <param name="model">The model.</param>
        private void Replace(DocumentModel doc, OpReplaceMessage model, ITextEdit edit)
        {
            //doc.BlockEvent = true;
            var start = model.Index;
            var length = model.OldContent.Length;

            if (start > edit.Snapshot.Length - length)
            {
                start = edit.Snapshot.Length - length;
            }

            if (model.TickStamp == 0)
            {
                length = edit.Snapshot.Length;
            }

            var span = new Span(start, length);
            edit.Replace(span, model.Content);
        }

        /// <summary>
        /// Undoes a replace operation.
        /// </summary>
        /// <param name="model">The model.</param>
        private void UndoReplace(DocumentModel doc, OpReplaceMessage model, ITextEdit edit)
        {
            //doc.BlockEvent = true;
            var span = new Span(model.Index, model.Content.Length);
            edit.Replace(span, model.OldContent);
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
                    View = view,
                    Changed = (s, e) => TextChanged(s, e, model.DocumentId),
                    Closed = (s, e) => EndCollaboration(s, e, model.DocumentId)
                };

                this.Documents.Add(model.DocumentId, doc);

                this.ViewModel.Collaborations.Add(
                    new CollaborationsViewModel.Collaboration()
                    {
                        DocumentId = model.DocumentId,
                        OpId = model.OpId
                    });
                
                var joinMessage = new JoinCollaborationMessage()
                {
                    Service = "op",
                    MessageType = "join",
                    OpId = model.OpId,
                    User = this.UserName
                };

                this.SendMessage(joinMessage);

                // attach events and stuff
                view.TextBuffer.Changed += doc.Changed;
                view.Closed += doc.Closed;
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

            if (doc.BlockEvent)
            {
                return;
            }
            
            foreach (var change in e.Changes)
            {
                var tick = (long)doc.TickStamp;

                lock (doc.OpLocker)
                {
                    if (!change.OldText.IsNullOrEmpty())
                    {
                        var model = new OpDeleteMessage()
                        {
                            Service = "op",
                            MessageType = "delete",
                            User = this.UserName,
                            Index = change.OldPosition,
                            ReplacementLength = change.OldLength,
                            OldContent = change.OldText,
                            DocumentId = docId,
                            OpId = doc.OpId,
                            TickStamp = tick,
                            IsApplied = true,
                            LocalCount = doc.LocalCount,
                            RemoteCount = doc.RemoteCount
                        };

                        doc.LocalCount++;
                        doc.Changes.Add(model);
                        this.SendMessage(model);
                    }

                    if (!change.NewText.IsNullOrEmpty())
                    {
                        var model = new OpInsertMessage()
                        {
                            Service = "op",
                            MessageType = "insert",
                            User = this.UserName,
                            Index = change.NewPosition,
                            Content = change.NewText,
                            DocumentId = docId,
                            OpId = doc.OpId,
                            TickStamp = tick,
                            IsApplied = true,
                            LocalCount = doc.LocalCount,
                            RemoteCount = doc.RemoteCount
                        };

                        doc.LocalCount++;
                        doc.Changes.Add(model);
                        this.SendMessage(model);
                    }
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
            this.LeaveCollaboration(this.UserName, this.Documents[documentId].OpId);
        }

        /// <summary>
        /// Cleans up the service if the user disconnects.
        /// </summary>
        public override void Disconnect()
        {
        }
    }
}