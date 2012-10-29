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
      
      val client = new EtherpadClient
      val promise = client.call("127.0.0.1:9000", "", "echo",
        Map[String, String]())

      promise.onSuccess {
        case x =>
        println(x)
        this.notify()
      }

      promise.onFailure {
        case e: Exception =>
          e.printStackTrace()
          this.notify()
      }

      this.wait()
    }
  }

}