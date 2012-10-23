using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Cahoots
{
    static class GuidList
    {
        public const string guidCahootsPkgString = "de677c38-48e7-4cff-bb96-1c88dc58f367";
        public const string guidCommandSetString = "e6ae0a49-a43a-4b9a-9519-c841bf09e800";

        public static readonly Guid guidCahootsCmdSet = new Guid(guidCommandSetString);
    };
}
