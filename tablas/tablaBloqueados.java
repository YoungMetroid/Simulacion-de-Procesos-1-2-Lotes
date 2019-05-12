package com.elizalde.simulacion.tablas;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class tablaBloqueados {

	public DefaultTableModel defaultTable;
	public JTable tablaBlock;
	public JScrollPane scrollPane;
	private static String[] columnNames = {"ID", "Tiempo Transcurido en Bloqueado"};
	
	public tablaBloqueados(int width, int height)
	{
		defaultTable = new DefaultTableModel();
		tablaBlock = new JTable();
		defaultTable.setColumnIdentifiers(columnNames);
		
		tablaBlock.setModel(defaultTable);
		tablaBlock.setPreferredScrollableViewportSize(new Dimension(270,200));
		tablaBlock.setAutoscrolls(true);
		
		scrollPane = new JScrollPane(tablaBlock);
		scrollPane.setEnabled(true);
		
		tablaBlock.setCellSelectionEnabled(false);
		tablaBlock.setEnabled(true);
		
		
		
		
	}
}
