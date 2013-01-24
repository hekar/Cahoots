// ----------------------------------------------------------------------
// <copyright file="UsersViewModel.cs" company="Codeora">
//     Copyright 2012. All rights reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Services.ViewModels
{
    using System;
    using System.Windows;
    using System.Windows.Input;
    using Cahoots.Ext.View;
    using Cahoots.Services.Models;

    /// <summary>
    /// View model for the users service.
    /// </summary>
    public class UsersViewModel : BaseViewModel
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="UsersViewModel" /> class.
        /// </summary>
        public UsersViewModel()
        {
            this.Users = new ViewModelCollection<Collaborator>();
        }

        /// <summary>
        /// Gets the users.
        /// </summary>
        /// <value>
        /// The users.
        /// </value>
        public ViewModelCollection<Collaborator> Users { get; private set; }
    }
}
