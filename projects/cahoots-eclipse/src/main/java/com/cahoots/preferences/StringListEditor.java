package com.cahoots.preferences;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.cahoots.eclipse.collab.ServerDialog;

public class StringListEditor extends ListEditor {
	
	public StringListEditor(String name, String labelText, Composite parent )
	{
		super(name, labelText, parent);
	}
	
	@Override
	protected String createList(String[] arg0) {
		StringBuilder sb = new StringBuilder();
		for(String s : getList().getItems())
		{
			sb.append(s)
			  .append(",");
		}
		return sb.toString();
	}

	@Override
	protected String getNewInputObject() {
		ServerDialog dia = new ServerDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell());
		dia.setBlockOnOpen(true);
		
		dia.open();

		return dia.getServer();
	}

	@Override
	protected String[] parseString(String arg0) {
		String[] a = arg0.split(",");
		return a;
	}
	
}
