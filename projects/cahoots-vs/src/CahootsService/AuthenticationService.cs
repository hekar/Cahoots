// ----------------------------------------------------------------------
// <copyright file="AuthenticationService.cs" company="My Company">
//     Copyright statement. All right reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Services
{
    using System;
    using System.Collections.Generic;
    using System.Net;
    using Cahoots.Ext;
    using Cahoots.Ext.String;
    using Cahoots.Ext.Net;

    /// <summary>
    /// The Authentication service.
    /// </summary>
    public class AuthenticationService : IAuthenticationService
    {
        /// <summary>
        /// Initializes a new instance of the
        /// <see cref="AuthenticationService" /> class.
        /// </summary>
        /// <param name="serverUrl">The server URL.</param>
        /// <param name="userName">Name of the user.</param>
        /// <param name="password">The password.</param>
        public AuthenticationService(
                Uri serverUrl,
                string userName,
                string password)
        {
            if (serverUrl == null)
            {
                throw new ArgumentNullException("serverUrl");
            }

            if (userName.IsNullOrEmpty())
            {
                throw new ArgumentException("username is null or empty.");
            }

            if (password.IsNullOrEmpty())
            {
                throw new ArgumentException("password is null or empty.");
            }

            this.ServerUrl = serverUrl;
            this.UserName = userName;
            this.Password = password;
        }

        /// <summary>
        /// Gets the server URL.
        /// </summary>
        /// <value>
        /// The server URL.
        /// </value>
        public Uri ServerUrl { get; private set; }

        /// <summary>
        /// Gets the name of the user.
        /// </summary>
        /// <value>
        /// The name of the user.
        /// </value>
        public string UserName { get; private set; }

        /// <summary>
        /// Gets the password.
        /// </summary>
        /// <value>
        /// The password.
        /// </value>
        public string Password { get; private set; }

        /// <summary>
        /// Gets the token.
        /// </summary>
        /// <value>
        /// The token.
        /// </value>
        public string Token { get; private set; }

        /// <summary>
        /// Gets the error message.
        /// </summary>
        /// <value>
        /// The error message.
        /// </value>
        public string ErrorMessage { get; private set; }

        /// <summary>
        /// Gets a value indicating whether this instance is authenticated.
        /// </summary>
        /// <value>
        ///   <c>true</c> if this instance is authenticated;
        ///   otherwise, <c>false</c>.
        /// </value>
        public bool IsAuthenticated { get; private set; }

        /// <summary>
        /// Authenticates this instance.
        /// </summary>
        /// <returns>
        ///   <c>true</c> if this instance was authenticated;
        ///   otherwise, <c>false</c>.
        /// </returns>
        public bool Authenticate()
        {
            // build parameters dictionary
            var dict = new Dictionary<string, string>()
            {
                { "username", this.UserName },
                { "password", this.Password }
            };

            PostResponse response = null;

            try
            {
                // send the request
                response = PostRequest.Send(
                    new Uri(this.ServerUrl, "/app/login"), dict);
            }
            catch (WebException ex)
            {
                // handle a web exception nicely...
                this.Token = null;
                this.IsAuthenticated = false;
                this.ErrorMessage = ex.Message;
                return false;
            }

            if (response.Status == 200)
            {
                // authenticated
                this.Token = response.Content;
                this.IsAuthenticated = true;
                this.ErrorMessage = null;
            }
            else if (response.Status == 401)
            {
                // failed to authenticate
                this.Token = null;
                this.IsAuthenticated = false;
                this.ErrorMessage = response.Content;
            }
            else
            {
                // unrecognized error code...
                this.Token = null;
                this.IsAuthenticated = false;
                this.ErrorMessage =
                    string.Format(
                        "Unrecognized response code {0}",
                        response.Status);
            }

            return this.IsAuthenticated;
        }

        /// <summary>
        /// Deauthenticates this instance.
        /// </summary>
        /// <returns>
        ///   <c>true</c> if this instance was deauthenticated;
        ///   otherwise, <c>false</c>.
        /// </returns>
        public bool Deauthenticate()
        {
            // build parameters dictionary
            var dict = new Dictionary<string, string>()
            {
                { "auth_token", this.Token }
            };

            PostResponse response = null;

            try
            {
                // send the request
                response = PostRequest.Send(
                    new Uri(this.ServerUrl, "/app/logout"), dict);
            }
            catch (WebException ex)
            {
                // handle a web exception nicely...
                this.Token = null;
                this.IsAuthenticated = false;
                this.ErrorMessage = ex.Message;
                return false;
            }

            // these will always be reset...
            this.Token = null;
            this.IsAuthenticated = false;

            if (response.Status == 200)
            {
                // deauthenticated
                this.ErrorMessage = null;
                return true;
            }
            else
            {
                // unrecognized error code...
                this.ErrorMessage =
                    string.Format(
                        "Unrecognized response code {0}",
                        response.Status);
                return false;
            }
        }
    }
}
