package com.cahoot.client.services
import com.cahoot.client.connection.Connection
import com.cahoot.client.etherpad.EtherpadClient
import com.cahoot.models.DocumentModel
import com.google.inject.Inject
import com.google.inject.Provider
import com.cahoot.models.DocumentModel
import com.cahoot.eclipse.scala.FunctionTypes
import com.cahoot.eclipse.scala.FunctionTypes

class EtherpadServices {

  private[this] val apikey = "45SPoCQHWcIJ3FZNKIhg80t4irP6B49D"
    
  @Inject
  private[this] var client: EtherpadClient = _

  @Inject
  private[this] var conn: Provider[Connection] = _

  def createPad(padid: BigInt,
                success: (DocumentModel) => Unit,
                failure: FunctionTypes.Failure): Unit = {

    val connection = conn.get()

    val clientSuccess = { (json: String, parsed: Map[String, String]) =>
      success(new DocumentModel(padid))
    }

    client.call(connection.address + ":" + connection.port, apikey,
      "createPad", Map("padID" -> padid.toString()), clientSuccess, failure)

  }

}