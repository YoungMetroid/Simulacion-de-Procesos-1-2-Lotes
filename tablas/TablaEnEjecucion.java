package com.elizalde.simulacion.tablas;

import java.awt.Dimension;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablaEnEjecucion implements Runnable {

	public DefaultTableModel defaultTable;
	public JTable tablaLotes;
	public JScrollPane scrollPane;
	private static String[] columnNames = { "Proceso en Ejecucion", };
	public Object[][] info;
	public boolean running = false;
	boolean processChanged = false;
	boolean operated = false;
	public Thread executionThread;
	public Vector<Vector<String>> allInfo = new Vector<Vector<String>>();
	int counter = 0;
	int globalCounter;
	String result;
	int programCounter = 0;
	public String realTimeLeft = "0";
	boolean error = false;
	int process;

	public TablaEnEjecucion() {
		defaultTable = new DefaultTableModel();
		tablaLotes = new JTable();
		defaultTable.setColumnIdentifiers(columnNames);

		tablaLotes.setModel(defaultTable);
		tablaLotes.setPreferredScrollableViewportSize(new Dimension(270, 200));
		tablaLotes.setAutoscrolls(true);
		scrollPane = new JScrollPane(tablaLotes);
		scrollPane.setEnabled(false);
		tablaLotes.setCellSelectionEnabled(false);
		tablaLotes.setColumnSelectionAllowed(false);
		tablaLotes.setRowSelectionAllowed(false);
		tablaLotes.setEnabled(false);

	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public synchronized void startTimer() {
		running = true;
		executionThread = new Thread(this, "Execution");
		executionThread.start();

	}

	@Override
	public void run() {

		counter = allInfo.size();
		for (int i = 0; i < allInfo.size(); i++) {
			allInfo.get(i).add(Integer.toString(i + 1));

		}

		process = 0;
		while (running && counter != 0) {
			counter--;
			setValues(process);
			try {
				while (Integer.parseInt(defaultTable.getValueAt(4, 1).toString()) > 0) {
					TimeUnit.SECONDS.sleep(1);
					if (!operated) {
						doOperation(process);
					}
					int timeLeft = Integer.parseInt(defaultTable.getValueAt(4, 1).toString());
					int currentTime = Integer.parseInt(defaultTable.getValueAt(3, 1).toString());
					// This part simulates a copy of the time decreasing
					defaultTable.setValueAt(timeLeft - 1, 4, 1);
					defaultTable.setValueAt(currentTime + 1, 3, 1);
					allInfo.get(process).set(5, String.valueOf(currentTime+1));
					allInfo.get(process).set(6, String.valueOf(timeLeft-1));
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (error == true) {
				setResult("?");
				error = false;
			}
			setProcessChanged();
			process++;
		}

	}

	public synchronized void stop() {
		running = false;
		allInfo.removeAllElements();
		try {
			executionThread.join(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void Error() {
		defaultTable.setValueAt("0", 4, 1);
		error = true;
	}

	public boolean Entrada_Salida() {
		//realTimeLeft = "0";
		System.out.println("Process: "+ process);
		for (int copyProcess = process; copyProcess < allInfo.size(); copyProcess++) {
			
			if(copyProcess % 5 == 0 && copyProcess != 0 ) {
				if(copyProcess != process)
				{
					System.out.println("First");
					if (Integer.parseInt(allInfo.get(process).get(6)) > 0) {
						Vector<String> v = allInfo.get(process);	
						allInfo.insertElementAt(v, copyProcess);
						allInfo.remove(process);
						setValues(process);
						realTimeLeft = allInfo.get(copyProcess-1).get(6);
						return true;
					}
					else return false;
				}
				
			}
			
		}
		if (Integer.parseInt(allInfo.get(process).get(6)) > 1) {
			System.out.println("Second");
			Vector<String> v = allInfo.get(process);
			allInfo.add(v);
			allInfo.remove(process);
			setValues(process);
			realTimeLeft = allInfo.get(allInfo.size()-1).get(6);
			return true;
		}		
		return false; 


	}

	public void setAllinfo(Vector<Vector<String>> allInfo) {
		this.allInfo = allInfo;
	}

	public void setValues(int process) {
		defaultTable.setValueAt(allInfo.get(process).get(0), 0, 1);
		defaultTable.setValueAt(allInfo.get(process).get(1) + allInfo.get(process).get(2)
		+ allInfo.get(process).get(3),1, 1);
		defaultTable.setValueAt(allInfo.get(process).get(4), 2, 1);
		defaultTable.setValueAt(allInfo.get(process).get(5), 3, 1);
		defaultTable.setValueAt(allInfo.get(process).get(6), 4, 1);
	}

	public void clearTable() {
		defaultTable.setValueAt(new String(""), 0, 1);
		defaultTable.setValueAt(new String(""), 1, 1);
		defaultTable.setValueAt(new String(""), 2, 1);
		defaultTable.setValueAt(new String(""), 3, 1);
		defaultTable.setValueAt(new String(""), 4, 1);
	}

	public boolean getProcessChanged() {
		return processChanged;
	}

	public void setProcessChanged() {
		processChanged = (!processChanged);
		setOperated();
	}

	public void doOperation(int process) {
		double a = Integer.parseInt(allInfo.get(process).get(1));
		double b = Integer.parseInt(allInfo.get(process).get(3));
		String operator = new String(allInfo.get(process).get(2));
		switch (operator) {
		case "+":
			result = Double.toString(a + b);
			break;
		case "-":
			result = Double.toString(a - b);
			break;
		case "*":
			result = Double.toString(a * b);
			break;
		case "/":
			result = Double.toString(a / b);
			break;
		case "%":
			result = Double.toString(a % b);
			break;
		case "^":
			result = Double.toString((int) Math.pow(a, b));
			break;
		default:
			result = "0";
			break;
		}
	}

	public boolean operated() {
		return operated;
	}

	public void setOperated() {
		operated = (!operated);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String newResult) {
		result = newResult;
	}
}
