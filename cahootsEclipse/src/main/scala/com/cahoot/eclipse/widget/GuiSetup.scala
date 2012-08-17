package com.cahoot.eclipse.widget
import org.eclipse.swt.widgets.Composite

/**
 * Default events for a Window, Dialog or panel setup
 */
abstract trait GuiSetup {

  def setupContents(panel: Composite): Unit
 
  def setupDefaults(panel: Composite): Unit
  
  def addListeners(): Unit 
  
}