///
///
///

namespace Cahoots.Services.Models
{
    using System;
    using Microsoft.VisualStudio.Text.Editor;

    public class DocumentModel
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="DocumentModel" /> class.
        /// </summary>
        /// <param name="tick">The tick.</param>
        public DocumentModel(long tick)
        {
            this.tickStart = tick;
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
        /// Gets the tick stamp.
        /// </summary>
        /// <value>
        /// The tick stamp.
        /// </value>
        private long tickStart { get; set; }
        public long TickStamp
        {
            get
            {
                var now = (long)DateTime.Now.TimeOfDay.TotalMilliseconds;
                return now - tickStart;
            }
        }


        private object locker = new object();

        private int blocks = 0;
        public bool BlockEvent
        {
            get
            {
                lock (locker)
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
                    lock (locker)
                    {
                        blocks++;
                    }
                }
            }
        }
    }
}
