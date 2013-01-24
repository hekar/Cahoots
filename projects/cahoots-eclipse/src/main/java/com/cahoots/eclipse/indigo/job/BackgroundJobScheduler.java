package com.cahoots.eclipse.indigo.job;

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class BackgroundJobScheduler {
	public void schedule(String name, final BackgroundJob backgroundJob) {
		Job job = new Job(name) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				return backgroundJob.run(monitor);
			}
		};
		job.schedule();
	}

}
