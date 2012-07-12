package com.cahoot.eclipse.tests.manual
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class GuiConnectionsTests {

test("manual layout test of connection selection dialog") {
    val dialog = new ConnectionSelectionDialog();
    SwtTestTools.blockTillQuit(dialog);
  }

  test("manual layout test of manage connection dialog") {

    val dialog = new ManageConnectionsDialog();
    SwtTestTools.blockTillQuit(dialog);
  }

}