package com.cahoot.eclipse
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

class Activator extends AbstractUIPlugin {

	// The shared instance
	var plugin: Activator = _;
	
	override def start(context: BundleContext): Unit = {
		super.start(context);
		plugin = this;
	}

	override def stop(context: BundleContext): Unit = {
		plugin = null;
		super.stop(context);
	}
	
}