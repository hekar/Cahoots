package com.cahoot.eclipse.widget.listener
import org.eclipse.swt.events.SelectionListener
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Listener
import org.eclipse.swt.widgets.Event

object SwtListeners {
  /**
   * <p>
   * Standard SWT listener.
   * </p>
   *
   * Usually used with addListener(int, listener)
   */
  def listener(f: Event => Unit) = new Listener {
    def handleEvent(event: Event) = f(event)
  }

  /**
   * <p>
   * SWT Selection listener.
   * </p>
   *
   * Usually used with addSelectionListener(selectionListener)
   */
  def selectionListener(f: SelectionEvent => Unit) = new SelectionListener {
    def widgetDefaultSelected(event: SelectionEvent) = f(event)
    def widgetSelected(event: SelectionEvent) = f(event)
  }

}