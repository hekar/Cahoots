/// JSON Encoding Helper class
/// Codeora 2013
///

namespace Cahoots.Ext
{
    using System.IO;
    using System.Runtime.Serialization.Json;
    using System.Text;

    public static class JsonHelper
    {
        /// <summary>
        /// JSON Serialization
        /// </summary>
        /// <typeparam name="T">The model type.</typeparam>
        /// <param name="entity">The object to serialize.</param>
        /// <returns>The serialized object.</returns>
        public static string Serialize<T>(T entity)
        {
            var ser = new DataContractJsonSerializer(typeof(T));
            using (var mem = new MemoryStream())
            {
                ser.WriteObject(mem, entity);
                string json = Encoding.UTF8.GetString(mem.ToArray());
                return json;
            }
        }

        /// <summary>
        /// JSON Deserialization
        /// </summary>
        /// <typeparam name="T">The model type.</typeparam>
        /// <param name="json">The json to deserialize.</param>
        /// <returns>The deserialized object.</returns>
        public static T Deserialize<T>(string json)
        {
            var ser = new DataContractJsonSerializer(typeof(T));
            using (var mem = new MemoryStream(Encoding.UTF8.GetBytes(json)))
            {
                T obj = (T)ser.ReadObject(mem);
                return obj;
            }
        }
    }
}