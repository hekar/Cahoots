package com.cahoots.eclipse.indigo.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface BackgroundJob {
	IStatus run(IProgressMonitor monitor);
}
