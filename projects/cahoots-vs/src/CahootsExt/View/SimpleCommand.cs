using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Input;

namespace Cahoots.Ext.View
{
    public class SimpleCommand : ICommand
    {
        /// <summary>
        /// Gets or sets the execute delegate.
        /// </summary>
        /// <value>
        /// The execute delegate.
        /// </value>
        public Action ExecuteDelegate { get; set; }

        /// <summary>
        /// Gets or sets the can execute delegate.
        /// </summary>
        /// <value>
        /// The can execute delegate.
        /// </value>
        public Func<bool> CanExecuteDelegate { get; set; }

        public bool CanExecute(object parameter)
        {
            if (this.CanExecuteDelegate != null)
            {
                return this.CanExecuteDelegate();
            }

            return true;
        }

        public event EventHandler CanExecuteChanged;

        public void Execute(object parameter)
        {
            if (this.ExecuteDelegate != null)
            {
                this.ExecuteDelegate();
            }
        }
    }
}
