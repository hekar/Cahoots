// ----------------------------------------------------------------------
// <copyright file="UsersViewModel.cs" company="Codeora">
//     Copyright 2012. All rights reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Services.ViewModels
{
    using System;
    using Cahoots.Ext.View;
    using Cahoots.Services.Models;
using System.Windows.Controls;
using System.Windows.Input;
    using System.Windows;

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
            this.InviteCommand = new SimpleCommand()
            {
                ExecuteDelegate = () => MessageBox.Show("asdf"),
                CanExecuteDelegate = () => { return true; }
            };
        }

        /// <summary>
        /// Gets the users.
        /// </summary>
        /// <value>
        /// The users.
        /// </value>
        public ViewModelCollection<Collaborator> Users { get; private set; }

        public ICommand InviteCommand { get; private set; }

        /// <summary>
        /// Relays the command.
        /// </summary>
        /// <param name="command">The command.</param>
        public override void RelayCommand(string command)
        {
            throw new NotImplementedException();
        }
    }
}
