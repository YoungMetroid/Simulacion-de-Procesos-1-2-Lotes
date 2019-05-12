package com.elizalde.simulacion.tablas;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablaDeResultados {
	public DefaultTableModel defaultTable;
	public JTable tablaLotes;
	public JScrollPane scrollPane;

	public Vector<Vector<String>> allInfo = new Vector<Vector<String>>();
	private static String[] columnNames = { "ID", "Operacion", "Resultado" };

	public TablaDeResultados() {
		defaultTable = new DefaultTableModel();
		tablaLotes = new JTable();
		defaultTable.setColumnIdentifiers(columnNames);

		tablaLotes.setModel(defaultTable);
		tablaLotes.setPreferredScrollableViewportSize(new Dimension(270, 200));
		tablaLotes.setAutoscrolls(true);
		scrollPane = new JScrollPane(tablaLotes);
		scrollPane.setEnabled(false);
		tablaLotes.setCellSelectionEnabled(false);
		tablaLotes.setEnabled(false);
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setAllinfo(Vector<Vector<String>> allInfo) {
		this.allInfo = allInfo;
	}

	public void clearAll() {
		allInfo.removeAllElements();
		for (int i = 0; i < defaultTable.getRowCount();)
			defaultTable.removeRow(0);
	}
}
