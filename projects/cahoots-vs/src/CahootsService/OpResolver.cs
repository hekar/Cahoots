///
///
///

namespace Cahoots.Services
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;
    using Cahoots.Services.Models;
    using Cahoots.Services.MessageModels.Ops;
    using System.Diagnostics;

    public class OpResolver
    {
        /// <summary>
        /// Resolves the operations.
        /// </summary>
        /// <param name="doc">The doc.</param>
        /// <param name="remote">The remote.</param>
        /// <param name="local">The local.</param>
        public static void Resolve(DocumentModel doc, BaseOpMessage remoteOp, BaseOpMessage localOp)
        {
            if (remoteOp is OpDeleteMessage)
            {
                var remote = remoteOp as OpDeleteMessage;
                Debug.WriteLine("Remote: {0} Delete", remote.User);
                if (localOp is OpDeleteMessage)
                {
                    var local = localOp as OpDeleteMessage;
                    int noOpLength = 0;

                    if (remote.Index < local.Index)
                    {
                        // no overlap, remote OK as is, local needs modifications
                        local.Index = local.Index - remote.ReplacementLength;
                    }
                    else if (remote.Index + remote.ReplacementLength < local.Index + local.ReplacementLength)
                    {
                        // remote del at lower index reaches into locally
                        // applied del, local del reaches further out
                        // --> shorten remote del appropriately, move and
                        // shorten local del left
                        remote.Length = (remote.Index + remote.ReplacementLength) - local.Index;
                        local.Length = (remote.Index + remote.ReplacementLength) - local.Index;
                        local.Index = remote.Index;
                    }
                    else if ((remote.Index + remote.ReplacementLength >= local.Index + local.ReplacementLength))
                    {
                        // remote del at lower index, remote del fully extends
                        // over local del
                        // --> shorten remote by local length
                        // make local no-op
                        remote.ReplacementLength = remote.ReplacementLength - local.ReplacementLength;

                        // TODO: verify this...
                        local.Index = remote.Index;
                        local.ReplacementLength = noOpLength;
                    }
                    else if (remote.Index == local.Index)
                    {
                        if (remote.Index + remote.ReplacementLength < local.Index + local.ReplacementLength)
                        {
                            // start indeces are equal, remote is shorter
                            // --> make remote no-op
                            remote.ReplacementLength = noOpLength;
                            // --> shorten local to only delete
                            // non-overlapping region
                            local.ReplacementLength = local.ReplacementLength - remote.ReplacementLength;
                        }
                        else if (remote.Index + remote.ReplacementLength == local.Index + local.ReplacementLength)
                        {
                            // same endIndex, same deletion
                            // --> make remote op be no-op
                            remote.ReplacementLength = noOpLength;
                            // --> make local op appear as no-op
                            local.ReplacementLength = noOpLength;
                        }
                        else if (remote.Index + remote.ReplacementLength > local.Index + local.ReplacementLength)
                        {
                            // remote del extends over local del
                            // --> shorten remote del by length of local del,
                            // index/offset does not need to be updated
                            remote.ReplacementLength = remote.ReplacementLength - local.ReplacementLength;
                            // --> make local del appear as no-op
                            local.ReplacementLength = noOpLength;
                        }
                    }
                    else if (remote.Index > local.Index)
                    {
                        if (remote.Index > local.Index + local.ReplacementLength)
                        {
                            // move remote delete left by length of local deletion
                            remote.Index = remote.Index - local.ReplacementLength;
                        }
                        else if (remote.Index + remote.ReplacementLength < local.Index + local.ReplacementLength)
                        {
                            //remote is fully contained in/overlapping with local del
                            // --> remote is no-op
                            remote.ReplacementLength = noOpLength;
                            // --> local needs to be shortened by length of remote
                            local.ReplacementLength = local.ReplacementLength - remote.ReplacementLength;
                        }
                        else if (remote.Index < local.Index + local.ReplacementLength)
                        {
                            // remote del starts within local del, but extends further
                            // --> shorten remote by overlap and move left
                            // to index of local del
                            remote.ReplacementLength = remote.ReplacementLength - (local.Index + local.ReplacementLength) - remote.Index;
                        }
                    }
                }
                else if (localOp is OpInsertMessage)
                {
                    var local = localOp as OpInsertMessage;
                    if (remote.Index < localOp.Index)
                    {
                        if (remote.Index + remote.ReplacementLength < local.Index)
                        {
                            // remote remains unchanged, deletion happens fully
                            // before local insertion
                            // local insert needs to be moved left by full length of deletion
                            local.Index -= remote.ReplacementLength;
                        }
                        else if (remote.Index + remote.ReplacementLength >= local.Index)
                        {
                            // TODO ???
                        }
                        else if (remote.Index >= local.Index)
                        {
                            // remote del needs to be moved right by full length of insertion
                            remote.Index += local.Content.Length;
                        }
                    }
                }
            }
            else if (remoteOp is OpInsertMessage)
            {
                var remote = remoteOp as OpInsertMessage;
                if (localOp is OpInsertMessage)
                {
                    var local = localOp as OpInsertMessage;
                    if (remote.Index < local.Index)
                    {
                        local.Index += remote.Content.Length;
                    }
                    else if (remote.Index == local.Index)
                    {
                        var compare = local.User.CompareTo(remote.User);
                        if (compare < 0)
                        {
                            remote.Index += local.Content.Length;
                        }
                        else
                        {
                            local.Index += remote.Content.Length;
                        }
                    }
                    else if (remote.Index > local.Index)
                    {
                        remote.Index += local.Content.Length;
                    }
                }
                else if (localOp is OpDeleteMessage)
                {
                    var local = localOp as OpDeleteMessage;
                    if (remote.Index <= localOp.Index)
                    {
                        localOp.Index += remote.Content.Length;
                    }
                    else if (remote.Index > localOp.Index)
                    {
                        if (remote.Index > local.Index + local.ReplacementLength)
                        {
                            remote.Index -= local.ReplacementLength;
                        }
                        else if (remote.Index <= local.Index + local.ReplacementLength)
                        {
                            // TODO ???
                        }
                    }
                }
            }
        }
    }
}
