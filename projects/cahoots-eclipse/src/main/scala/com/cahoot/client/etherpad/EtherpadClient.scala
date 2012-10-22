package com.cahoot.client.etherpad

import scala.util.parsing.json._
import com.cahoot.eclipse.scala._
import akka.dispatch._
import java.util.concurrent.Executors
import org.apache.commons.httpclient._
import org.apache.commons.httpclient.methods._

object EtherpadClient {
  val DefaultFailure = { (t: Throwable) =>
    t.printStackTrace();
  }

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())
}

class EtherpadClient {

  import EtherpadClient._

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
    args: Map[String, String]): Promise[(String, Map[String, String])] = {

    val finalUrl = "http://%s/api/1/%s/".format(url, function)

    val client = new HttpClient
    val method = new PostMethod(finalUrl)

    method.setParameter("apikey", apiKey)
    for ((k, v) <- args) method.setParameter(k, v)

    // Execute the asynchronous request
    Promise().completeWith(Future {
      client.executeMethod(method)
      val json = new String(method.getResponseBody())

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
        throw new EtherpadClientException(parsed("message"))
      } else {
        (json, parsed)
      }
    })
  }

}