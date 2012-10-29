package com.cahoot.client.services
import com.cahoot.client.connection.Connection
import com.cahoot.client.etherpad.EtherpadClient
import com.cahoot.models.DocumentModel
import com.cahoot.models.DocumentModel
import com.cahoot.eclipse.scala.FunctionTypes
import com.cahoot.eclipse.scala.FunctionTypes
import com.cahoot.client.connection.ConnectionComponent
import com.cahoot.client.etherpad.EtherpadClientComponent

class EtherpadServices extends ConnectionComponent with EtherpadClientComponent {

  private[this] val apikey = "45SPoCQHWcIJ3FZNKIhg80t4irP6B49D"
    
  def createPad(padid: BigInt): Unit = {

    etherpadClient.call(connection.address + ":" + connection.port, apikey,
      "createPad", Map("padID" -> padid.toString()))

  }

}