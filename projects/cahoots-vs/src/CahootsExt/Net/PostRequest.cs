// ----------------------------------------------------------------------
// <copyright file="PostRequest.cs" company="My Company">
//     Copyright statement. All right reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Ext.Net
{
    using System;
    using System.Collections.Generic;
    using System.Net;
    using System.Text;
    using System.Web;

    /// <summary>
    /// Static class used to make simple POST requests.
    /// </summary>
    public static class PostRequest
    {
        /// <summary>
        /// Sends a request to the specified URI.
        /// </summary>
        /// <param name="uri">The URI.</param>
        /// <param name="parameters">The parameters.</param>
        /// <returns>The response.</returns>
        public static PostResponse Send(
                Uri uri,
                Dictionary<string, string> parameters)
        {
            if (uri == null)
            {
                throw new ArgumentNullException("uri");
            }

            if (parameters == null)
            {
                throw new ArgumentNullException("parameters");
            }

            // create and configure base request
            var req = HttpWebRequest.Create(uri) as HttpWebRequest;
            req.Method = "POST";
            req.ContentType = "application/x-www-form-urlencoded";

            // build parameter string
            var sb = new StringBuilder();
            foreach (var kvp in parameters)
            {
                sb.Append(
                    string.Format(
                        "{0}={1}&",
                        HttpUtility.UrlEncode(kvp.Key),
                        HttpUtility.UrlEncode(kvp.Value)));
            }

            try
            {
                // write parameter string
                byte[] data = Encoding.ASCII.GetBytes(sb.ToString());
                req.ContentLength = data.LongLength;
                using (var stream = req.GetRequestStream())
                {
                    stream.Write(data, 0, data.Length);
                }

                // return a post response
                return new PostResponse(req.GetResponse() as HttpWebResponse);
            }
            catch (WebException ex)
            {
                if (ex.Response != null)
                {
                    // if the exception has a valid resonse object,
                    // make it into a post response
                    return new PostResponse(ex.Response as HttpWebResponse);
                }

                throw ex;
            }
        }
    }
}
