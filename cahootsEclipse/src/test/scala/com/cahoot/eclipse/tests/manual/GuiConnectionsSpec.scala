package com.cahoot.eclipse.tests.manual
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.cahoot.eclipse.test.tools.SwtTestTools
import com.cahoot.eclipse.connection.ConnectionSelectionDialog
import com.cahoot.eclipse.connection.ManageConnectionsDialog
import org.scalatest.FeatureSpec

@RunWith(classOf[JUnitRunner])
class GuiConnectionsSpec extends FeatureSpec {

  feature("Manual Layout Connection GUI Dialogs") {
    scenario("Connection Selection Dialog") {
      val dialog = new ConnectionSelectionDialog();
      SwtTestTools.blockTillQuit(dialog);
      assert(true)
    }

    scenario("manual layout test of manage connection dialog") {
      val dialog = new ManageConnectionsDialog();
      SwtTestTools.blockTillQuit(dialog);
      pending
    }
  }

}