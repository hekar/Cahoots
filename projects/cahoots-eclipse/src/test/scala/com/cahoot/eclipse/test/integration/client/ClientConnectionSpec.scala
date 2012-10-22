package com.cahoot.eclipse.test.integration.client

import collection.mutable.Stack
import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import com.cahoot.client.etherpad.EtherpadClient
import com.cahoot.eclipse.swt.SwtTools

@RunWith(classOf[JUnitRunner])
class ClientConnectionSpec extends FunSuite {

  test("connect") {
    synchronized {
      val success = (json: String, values: Map[String, String]) => {
        println(json)
        this.notify()
      }
      
      val failure = { (t: Throwable) =>
        t.printStackTrace()
        this.notify()
      }

      val client = new EtherpadClient
      client.call("127.0.0.1:9000", "", "echo",
        Map[String, String](),
        success,
        failure)

      this.wait()
    }
  }

}