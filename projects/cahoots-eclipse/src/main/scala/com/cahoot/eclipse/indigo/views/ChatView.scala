package com.cahoot.eclipse.indigo.views
import org.eclipse.ui.part.ViewPart
import org.eclipse.jface.viewers.TableViewer
import org.eclipse.swt.widgets.Composite
import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.jface.viewers.deferred.DeferredContentProvider
import org.eclipse.ui.PlatformUI
import org.eclipse.swt.SWT
import java.util.Comparator
import com.cahoot.client.services.ChatService
import com.google.inject.Inject

private class ChatViewComparator extends Comparator[String] {
  override def compare(x1: String, x2: String): Int = x1.compareTo(x2)
}

class ChatView extends ViewPart {

  @Inject
  private var chatService: ChatService = _

  private var viewer: TableViewer = _

  @Override
  def createPartControl(parent: Composite): Unit = {
    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(new DeferredContentProvider(new ChatViewComparator));
    viewer.setLabelProvider(new LabelProvider());
    viewer.setInput();
    

    // Create the help context id for the viewer's control
    // PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "cahoot.viewer");
  }

  /**
   * Passing the focus request to the viewer's control.
   */
  override def setFocus(): Unit = {
    viewer.getControl().setFocus();
  }
}