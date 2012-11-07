package com.cahoot.eclipse.collab
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Control
import org.eclipse.swt.widgets.Label
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.widgets.Text
import org.eclipse.swt.widgets.Table
import org.eclipse.swt.SWT
import net.miginfocom.swt.MigLayout
import com.cahoot.models.PersonModel
import java.util.ArrayList
import scala.collection.mutable.ListBuffer
import org.eclipse.jface.viewers.TableViewerColumn
import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.swt.widgets.Button
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.events.SelectionEvent
import com.cahoot.eclipse.widget.listener.SwtListeners
import com.cahoot.eclipse.core.PluginConstants
import com.cahoot.eclipse.widget.CahootOkCancelDialog
import com.cahoot.eclipse.widget.CahootMigOkCancelDialog
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.PostMethod
import org.eclipse.jface.dialogs.MessageDialog
import org.apache.commons.httpclient.NameValuePair
import org.eclipse.ui.IWorkbenchWindow
import org.eclipse.ui.internal.Workbench
import org.eclipse.ui.internal.WorkbenchWindow
import org.eclipse.jface.action.MenuManager
import org.eclipse.swt.widgets.Menu
import org.eclipse.jface.action.IContributionItem
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.IWorkbench
import org.eclipse.swt.widgets.MenuItem
import com.cahoot.eclipse.ActiveUser
import com.cahoot.eclipse.ActiveUser

/**
 * Modal dialog for selecting which collaborators the user wishes to share with
 */
class ConnectDialog() extends CahootMigOkCancelDialog{

  var help: Label = _
  var tb_username: Text = _
  var tb_password: Text = _
  var tb_server: Text = _
  var manage: Button = _

  val collaborators = new ListBuffer[PersonModel]

  override def setupContents(panel: Composite): Unit = {

    panel.setLayout(new MigLayout("fill", "",
      "[growprio 0][growprio 0][growprio 100][growprio 0]"))
      
      
    tb_server = new Text(panel, SWT.NONE)
    tb_server.setText("http://localhost:9000")
    tb_server.setLayoutData("")
      
    tb_username = new Text(panel, SWT.NONE)
    tb_username.setLayoutData("")
    
    
    tb_password = new Text(panel, SWT.SINGLE | SWT.PASSWORD)
    tb_password.setLayoutData("")
    
    help = new Label(panel, SWT.NONE)
    help.setText("Cahoots")
    help.setLayoutData("")
  }

  override def setupDefaults(panel: Composite): Unit = {
    val shell = getShell()
    shell.setText("%s Collaborator".format(PluginConstants.APP_TITLE))
    shell.setSize(800, 600)
  }

  override def addListeners(): Unit = {

    ok.addSelectionListener(SwtListeners.selectionListener({ e =>
      var username: String = tb_username.getText()
      var password: String = tb_password.getText()
      var server: String = tb_server.getText();
      
      var client: HttpClient = new HttpClient();
      var method: PostMethod = new PostMethod( server + "/app/login");
      var  data: Array[NameValuePair] = Array(
          new NameValuePair("username", username),
          new NameValuePair("password", password)
        )
      method.setRequestBody(data)
      
      var statusCode: Int = client.executeMethod(method)
      
      if(statusCode == 200)
      {
        var token:String = new String(method.getResponseBody())
        ActiveUser.auth_token = token
      	close
      } 
      else 
      {
        //TODO better message on error
        
	    MessageDialog.openInformation(
				null,
				"Connected",
				"Error connecting to server");
      }
      
    }))

    cancel.addSelectionListener(SwtListeners.selectionListener({ e =>
      close
    }))
  }

  def getCollaborators(): List[PersonModel] = collaborators.toList
  

}