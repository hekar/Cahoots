///
///
///

namespace Cahoots.Services.Models
{
    using System;
    using System.Collections.ObjectModel;
    using Cahoots.Services.MessageModels.Ops;
    using Microsoft.VisualStudio.Text;
    using Microsoft.VisualStudio.Text.Editor;
    using System.Threading;

    public class DocumentModel
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="DocumentModel" /> class.
        /// </summary>
        /// <param name="tick">The tick.</param>
        public DocumentModel(double tick)
        {
            this.tickStart = DateTime.Now.Subtract(new TimeSpan((long)tick));
            this.Changes = new Collection<BaseOpMessage>();
        }

        /// <summary>
        /// Gets or sets the view.
        /// </summary>
        /// <value>
        /// The view.
        /// </value>
        public IWpfTextView View { get; set; }

        /// <summary>
        /// Gets or sets the op id.
        /// </summary>
        /// <value>
        /// The op id.
        /// </value>
        public string OpId { get; set; }

        /// <summary>
        /// Gets or sets the document id.
        /// </summary>
        /// <value>
        /// The document id.
        /// </value>
        public string DocumentId { get; set; }

        /// <summary>
        /// Gets or sets the full path.
        /// </summary>
        /// <value>
        /// The full path.
        /// </value>
        public string FullPath { get; set; }

        /// <summary>
        /// Gets or sets the changed.
        /// </summary>
        /// <value>
        /// The changed.
        /// </value>
        public EventHandler<TextContentChangedEventArgs> Changed { get; set; }

        /// <summary>
        /// Gets or sets the closed.
        /// </summary>
        /// <value>
        /// The closed.
        /// </value>
        public EventHandler Closed { get; set; }

        /// <summary>
        /// Gets or sets the changes.
        /// </summary>
        /// <value>
        /// The changes.
        /// </value>
        public Collection<BaseOpMessage> Changes { get; set; }

        /// <summary>
        /// Gets the tick stamp.
        /// </summary>
        /// <value>
        /// The tick stamp.
        /// </value>
        private DateTime tickStart { get; set; }
        public double TickStamp
        {
            get
            {
                return DateTime.Now.Subtract(this.tickStart).TotalMilliseconds;
            }
        }

        /// <summary>
        /// The op lock object.
        /// </summary>
        public object OpLocker = new object();

        /// <summary>
        /// The event lock object.
        /// </summary>
        private object eventLocker = new object();

        /// <summary>
        /// Gets or sets a value indicating whether [block event].
        /// </summary>
        /// <value>
        ///   <c>true</c> if [block event]; otherwise, <c>false</c>.
        /// </value>
        private int blocks = 0;
        public bool BlockEvent
        {
            get
            {
                lock (eventLocker)
                {
                    var b = blocks > 0;
                    if (b)
                    {
                        blocks--;
                    }

                    return b;
                }
            }
            set
            {
                if (value)
                {
                    lock (eventLocker)
                    {
                        blocks++;
                    }
                }
            }
        }
    }
}
