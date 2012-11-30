
namespace Cahoots.Services.Models
{
    using System.ComponentModel;
    using System.Runtime.Serialization;

    [DataContract]
    public abstract class ModelBase : INotifyPropertyChanged
    {
        /// <summary>
        /// Occurs when [property changed].
        /// </summary>
        public event PropertyChangedEventHandler PropertyChanged;

        /// <summary>
        /// Forces the refresh.
        /// </summary>
        /// <param name="property">The property.</param>
        public void ForceRefresh(string property)
        {
            if (this.PropertyChanged != null)
            {
                this.PropertyChanged(
                        this,
                        new PropertyChangedEventArgs(property));
            }
        }
    }
}
