/// XML Encoding Helper class
/// Codeora 2013
///

namespace Cahoots.Ext
{
    using System.IO;
    using System.Text;
    using System.Xml.Serialization;

    public static class XmlHelper
    {
        /// <summary>
        /// XML Serialization
        /// </summary>
        /// <typeparam name="T">The model type.</typeparam>
        /// <param name="entity">The object to serialize.</param>
        /// <returns>The serialized object.</returns>
        public static string Serialize<T>(T entity)
        {
            var ser = new XmlSerializer(typeof(T));
            using (var mem = new MemoryStream())
            {
                ser.Serialize(mem, entity);
                string xml = Encoding.UTF8.GetString(mem.ToArray());
                return xml;
            }
        }

        /// <summary>
        /// XML Deserialization
        /// </summary>
        /// <typeparam name="T">The model type.</typeparam>
        /// <param name="xml">The xml to deserialize.</param>
        /// <returns>The deserialized object.</returns>
        public static T Deserialize<T>(string xml)
        {
            var ser = new XmlSerializer(typeof(T));
            using (var mem = new MemoryStream(Encoding.UTF8.GetBytes(xml)))
            {
                T obj = (T)ser.Deserialize(mem);
                return obj;
            }
        }
    }
}