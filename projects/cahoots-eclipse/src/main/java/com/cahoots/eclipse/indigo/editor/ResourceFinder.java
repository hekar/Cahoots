package com.cahoots.eclipse.indigo.editor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.httpclient.util.ExceptionUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.ide.ResourceUtil;
import org.eclipse.ui.internal.Workbench;

import com.cahoots.eclipse.Activator;
import com.cahoots.java.lang.ExceptionUtils;

public class ResourceFinder {

	private final Activator activator;

	@Inject
	public ResourceFinder(final Activator activator) {
		this.activator = activator;
	}

	public IFile findFileByDocumentId(final String documentId) {
		try {

			final String documentPath = String.format("file://%s", documentId);
			final URL url = new URL(documentPath);
			final URL fileUrl = FileLocator.toFileURL(url);
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			final IWorkspaceRoot workspaceRoot = workspace.getRoot();

			final String eclipseDocumentPath = fileUrl.getPath();
			final Path path = new Path(eclipseDocumentPath);

			final IFile file = workspaceRoot.getFile(path);
			return file;
		} catch (final Exception e) {
			e.printStackTrace();
			throw ExceptionUtils.toRuntime(e);
		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	private IPath getProjectPath(final String eclipseDocumentPath,
			final IProject[] projects) {
		final String documentProjectName = getProjectNameFromDocumentPath(eclipseDocumentPath);
		for (final IProject project : projects) {
			final boolean closed = !project.isOpen();
			if (closed) {
				// TODO: Can closed projects have their locations retrieved?
			}

			final String projectName = project.getName();
			if (projectName.equals(documentProjectName)) {
				final IPath location = project.getLocation();
				return location;
			}
		}

		return null;
	}

	@Deprecated
	private String getProjectNameFromDocumentPath(
			final String eclipseDocumentPath) {
		try {
			final String projectName = eclipseDocumentPath.split("/")[1];

			assert !projectName.trim().isEmpty();

			return projectName;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new ArrayIndexOutOfBoundsException(
					"Failure to find project name in documentId");
		}
	}

	public IEditorPart findEditorByDocumentId(final String documentId) {
		if (documentId.trim().isEmpty()) {
			throw new IllegalArgumentException("DocumentId cannot be null");
		}

		final IFile file = findFileByDocumentId(documentId);

		if (file == null) {
			throw new IllegalStateException(
					"Could not find file for documentId: " + documentId);
		}

		return findEditorByFile(file);
	}

	public IEditorPart findEditorByFile(final IFile file) {
		// Go through all the workbench windows to find an active page with the
		// editor
		final IWorkbenchWindow[] wbs = activator.getWorkbench()
				.getWorkbenchWindows();
		for (final IWorkbenchWindow wb : wbs) {
			final IWorkbenchPage page = wb.getActivePage();

			final IEditorPart editor = ResourceUtil.findEditor(page, file);
			if (editor != null) {
				return editor;
			}
		}

		return null;
	}

}
