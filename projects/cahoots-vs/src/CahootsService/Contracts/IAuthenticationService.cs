/// IAuthenticationService.cs
/// 26 October 2012
///
/// The interface responsible for authenticating against a Cahoots server.
///

namespace Cahoots.Services
{
    using System;

    public interface IAuthenticationService
    {
        /// <summary>
        /// Gets the server URL.
        /// </summary>
        /// <value>
        /// The server URL.
        /// </value>
        Uri ServerUrl { get; }

        /// <summary>
        /// Gets the name of the user.
        /// </summary>
        /// <value>
        /// The name of the user.
        /// </value>
        string UserName { get; }

        /// <summary>
        /// Gets the password.
        /// </summary>
        /// <value>
        /// The password.
        /// </value>
        string Password { get; }

        /// <summary>
        /// Gets the token.
        /// </summary>
        /// <value>
        /// The token.
        /// </value>
        string Token { get; }

        /// <summary>
        /// Gets the error message.
        /// </summary>
        /// <value>
        /// The error message.
        /// </value>
        string ErrorMessage { get; }

        /// <summary>
        /// Gets a value indicating whether this instance is authenticated.
        /// </summary>
        /// <value>
        ///   <c>true</c> if this instance is authenticated;
        ///   otherwise, <c>false</c>.
        /// </value>
        bool IsAuthenticated { get; }

        /// <summary>
        /// Authenticates this instance.
        /// </summary>
        /// <returns>
        ///   <c>true</c> if this instance was authenticated;
        ///   otherwise, <c>false</c>.
        /// </returns>
        bool Authenticate();

        /// <summary>
        /// Deauthenticates this instance.
        /// </summary>
        /// <returns>
        ///   <c>true</c> if this instance was deauthenticated;
        ///   otherwise, <c>false</c>.
        /// </returns>
        bool Deauthenticate();
    }
}
