package com.cahoot.eclipse.tests.manual
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.cahoot.eclipse.test.tools.SwtTestTools
import com.cahoot.eclipse.connection.ConnectionSelectionDialog
import com.cahoot.eclipse.connection.ManageConnectionsDialog
import org.scalatest.FeatureSpec
import com.cahoot.eclipse.swt.SwtTools
import com.cahoot.models.DocumentModel
import com.cahoot.eclipse.guice.InjectorFactory
import com.cahoot.client.services.EtherpadServices
import com.cahoot.client.etherpad.EtherpadClient

@RunWith(classOf[JUnitRunner])
class GuiConnectionsSpec extends FeatureSpec {

  feature("Manual Layout Connection GUI Dialogs") {

    scenario("bob ") {
      val injector = InjectorFactory.injector()
      val es = injector.getInstance(classOf[EtherpadServices])
      
      val createSuccess = { (dm: DocumentModel) =>
      	println(dm.docId)
      }

      es.createPad(23, createSuccess, EtherpadClient.DefaultFailure)
      
      while(true) {}
      
      pending
    }
  }

}