/// PostRequest.cs
/// 26 October 2012
///
/// Represents the response from a PostRequest.
///

namespace Cahoots.Ext.Net
{
    using System;
    using System.IO;
    using System.Net;

    /// <summary>
    /// Represents a respones from a PostRequest.
    /// </summary>
    public class PostResponse
    {
        /// <summary>
        /// Initializes a new instance of the 
        /// <see cref="PostResponse" /> class.
        /// </summary>
        /// <param name="response">The response.</param>
        internal PostResponse(HttpWebResponse response)
        {
            if (response == null)
            {
                throw new ArgumentNullException("response");
            }

            this.Status = (int)response.StatusCode;

            using (var stream = response.GetResponseStream())
            using (var reader = new StreamReader(stream))
            {
                this.Content = reader.ReadToEnd();
            }
        }

        /// <summary>
        /// Gets the status.
        /// </summary>
        /// <value>
        /// The status.
        /// </value>
        public int Status { get; private set; }

        /// <summary>
        /// Gets the content.
        /// </summary>
        /// <value>
        /// The content.
        /// </value>
        public string Content { get; private set; }
    }
}
