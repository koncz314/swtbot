/*******************************************************************************
 * Copyright (c) 2017 Cadence Design Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Aparna Argade - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class SelectTableItemRule extends GenerationSimpleRule {

	private int index;
	boolean useIndices = false;
	private TableItem[] selectedItems;
	private int[] selectedIndices;
	private Table table;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof Table && event.type == SWT.Selection && event.detail != SWT.CHECK;
	}

	@Override
	public void initializeForEvent(Event event) {
		this.table = (Table) event.widget;
		this.index = WidgetUtils.getIndex(this.table);
		this.selectedIndices = this.table.getSelectionIndices();
		this.selectedItems = this.table.getSelection();
		for (TableItem selectedItem : selectedItems) {
			int nbOccurrences = 0;
			for (TableItem item : this.table.getItems()) {
				if (item.getText().equals(selectedItem.getText())) {
					nbOccurrences++;
				}
			}
			if (nbOccurrences > 1) {
				this.useIndices = true;
			}
		}
	}

	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder code = new StringBuilder();

		code.append("bot.table("); //$NON-NLS-1$
		if (this.index != 0) {
			code.append(this.index);
		}
		code.append(")"); //$NON-NLS-1$

		code.append(".select("); //$NON-NLS-1$
		if (this.useIndices) {
			for (int i = 0; i < this.selectedIndices.length; i++) {
				code.append(this.selectedIndices[i]);
				if (i < this.selectedIndices.length - 1) {
					code.append(", "); //$NON-NLS-1$
				}
			}
		} else {
			for (int i = 0; i < this.selectedItems.length; i++) {
				code.append('"');
				code.append(this.selectedItems[i].getText());
				code.append('"');
				if (i < this.selectedIndices.length - 1) {
					code.append(", "); //$NON-NLS-1$
				}
			}
		}
		code.append(')');

		actions.add(code.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Table getWidget() {
		return this.table;
	}

}
