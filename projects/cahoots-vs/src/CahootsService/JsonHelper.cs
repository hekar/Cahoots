using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization.Json;
using System.IO;

namespace Cahoots.Services
{
    public class JsonHelper
    {
        /// <summary>
        /// JSON Serialization
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="t">The t.</param>
        /// <returns></returns>
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
