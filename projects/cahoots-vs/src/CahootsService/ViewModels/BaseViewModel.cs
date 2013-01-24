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
        /// Gets or sets the relay message.
        /// </summary>
        /// <value>
        /// The relay message.
        /// </value>
        public Action<string> RelayMessage { get; set; }
    }
}
