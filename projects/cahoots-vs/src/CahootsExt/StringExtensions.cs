// ----------------------------------------------------------------------
// <copyright file="StringExtensions.cs" company="My Company">
//     Copyright statement. All right reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Ext.String
{
    /// <summary>
    /// The string extensions class.
    /// </summary>
    public static class StringExtensions
    {
        /// <summary>
        /// Determines if the string is null or empty.
        /// </summary>
        /// <param name="value">The value.</param>
        /// <returns>
        ///   <c>true</c> if null or empty; otherwise, <c>false</c>.
        /// </returns>
        public static bool IsNullOrEmpty(this string value)
        {
            return string.IsNullOrEmpty(value);
        }

        /// <summary>
        /// Determines if the stringis null or white space.
        /// </summary>
        /// <param name="value">The value.</param>
        /// <returns>
        ///   <c>true</c> if null or white space; otherwise, <c>false</c>.
        /// </returns>
        public static bool IsNullOrWhiteSpace(this string value)
        {
            return string.IsNullOrWhiteSpace(value);
        }
    }
}
