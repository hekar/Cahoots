package com.cahoot.client.etherpad
import scala.util.parsing.json.JSON
import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.AsyncCompletionHandler
import com.ning.http.client.Response
import com.cahoot.eclipse.scala.FunctionTypes

object EtherpadClient {
  val DefaultFailure = { (t:Throwable) =>
    t.printStackTrace();
  }
}

class EtherpadClient {

  private val apiVersion = 1

  /**
   * Call a method in the etherpad api
   * @param url
   * 	Url to server
   * @param apiKey
   * 	Key to access server
   * @param function
   * 	Function by whose name to call
   * @param args
   * 	Argument => Value
   * @param success (json:String) => Unit
   * @param failure (t:Throwable) => Unit:
   */
  def call(url: String,
           apiKey: String,
           function: String,
           args: Map[String, String],
           success: (String, Map[String, String]) => Unit,
           failure: FunctionTypes.Failure): Unit = {

    val finalUrl = "http://%s/api/1/%s/".format(url, function)
    val client = new AsyncHttpClient();
    val get = client.prepareGet(finalUrl)

    get.addQueryParameter("apikey", apiKey)
    for ((k, v) <- args) get.addQueryParameter(k, v)

    // Execute the asynchronous request
    get.execute(
      new AsyncCompletionHandler[Unit]() {

        override def onCompleted(response: Response): Unit = {
          val json = response.getResponseBody()

          val parsed = JSON.parseFull(json) match {
            case Some(m: Map[_, _]) => m collect {
              case (k: Any, v: Any) =>
                (k.toString(), v.toString())
              case (k: Any) =>
                (k._1.toString(), "")
            } toMap
            case _ => Map[String, String]()
          }

          if (parsed("message") != "ok") {
            failure(throw new EtherpadClientException(parsed("message")))
          } else {
        	  success(json, parsed)
          }
        }

        override def onThrowable(t: Throwable): Unit = {
          failure(t)
        }
      });
  }

}