package com.cahoot.eclipse.guice
import com.google.inject.Injector
import com.google.inject.Guice

/**
 * Injector for the plugin
 */
object InjectorFactory {

  /**
   * Create the main injector to be used by the rest of the plugin
   */
  def injector(): Injector = {
    Guice.createInjector(new GuiModule());
  }
}