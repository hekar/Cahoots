using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Cahoots.Ext
{
    public static class GenericExt
    {
        /// <summary>
        /// Ases the specified obj.
        /// </summary>
        /// <typeparam name="T">The cast type.</typeparam>
        /// <param name="obj">The obj.</param>
        /// <returns>The object as a T.</returns>
        public static T As<T>(this object obj) where T : class
        {
            return obj as T;
        }
    }
}
