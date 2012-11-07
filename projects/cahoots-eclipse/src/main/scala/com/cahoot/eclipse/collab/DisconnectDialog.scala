package com.cahoot.eclipse.collab

import scala.collection.mutable.ListBuffer
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.NameValuePair
import org.apache.commons.httpclient.methods.PostMethod
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Label
import com.cahoot.eclipse.core.PluginConstants
import com.cahoot.eclipse.widget.CahootMigOkCancelDialog
import com.cahoot.eclipse.widget.listener.SwtListeners
import com.cahoot.models.PersonModel
import com.cahoot.eclipse.Activator
import net.miginfocom.swt.MigLayout
import com.cahoot.eclipse.ActiveUser
import com.cahoot.eclipse.ActiveUser

/**
 * Modal dialog for selecting which collaborators the user wishes to share with
 */
class DisconnectDialog() extends CahootMigOkCancelDialog {

  var help: Label = _
  var manage: Button = _

  val collaborators = new ListBuffer[PersonModel]

  override def setupContents(panel: Composite): Unit = {

    panel.setLayout(new MigLayout("fill", "",
      "[growprio 0][growprio 0][growprio 100][growprio 0]"))
      
    help = new Label(panel, SWT.NONE)
    help.setText("Are you sure you would like to disconnect from the server?")
    help.setLayoutData("")
  }

  override def setupDefaults(panel: Composite): Unit = {
    val shell = getShell()
    shell.setText("%s Collaborator".format(PluginConstants.APP_TITLE))
    shell.setSize(800, 600)
  }

  override def addListeners(): Unit = {

    ok.addSelectionListener(SwtListeners.selectionListener({ e =>
      var server: String = "http://localhost:9000";
      
      var client: HttpClient = new HttpClient();
      var method: PostMethod = new PostMethod( server + "/app/logout");
      
      var  data: Array[NameValuePair] = Array(
          new NameValuePair("auth_token", ActiveUser.auth_token)
        )
      method.setRequestBody(data)
      
      var statusCode: Int = client.executeMethod(method)
      
      if(statusCode == 200)
      {
    	ActiveUser.auth_token = ""
        var token:String = new String(method.getResponseBody())
        
      	close
      } 
      else 
      {
        //TODO better message on error
        
	    MessageDialog.openInformation(
				null,
				"Disconnect",
				"Error disconnecting from the server");
      }
      
    }))

    cancel.addSelectionListener(SwtListeners.selectionListener({ e =>
      close
    }))
  }

  def getCollaborators(): List[PersonModel] = collaborators.toList
  

}