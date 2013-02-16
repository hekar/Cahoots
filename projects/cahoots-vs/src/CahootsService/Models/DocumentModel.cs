///
///
///

namespace Cahoots.Services.Models
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using Microsoft.VisualStudio.Text.Editor;

    public class DocumentModel
    {
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
        public long TickStamp
        {
            get
            {
                // TODO: actually implement this...
                return 0;
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
