// ----------------------------------------------------------------------
// <copyright file="ViewModelBase.cs" company="Codeora">
//     Copyright 2012. All rights reserved
// </copyright>
// ------------------------------------------------------------------------

namespace Cahoots.Services.ViewModels
{
    using System;
    using System.Collections.Generic;
    using System.Linq;
    using System.Text;

    public abstract class BaseViewModel
    {
        /// <summary>
        /// Relays the command.
        /// </summary>
        /// <param name="command">The command.</param>
        public abstract void RelayCommand(string command);
    }
}
