package com.elizalde.simulacion;

import java.util.Random;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Generador {

	int procesos = -1;
	private Vector<String> copyID;
	private Vector<Vector<String>> tableInfo = new Vector<Vector<String>>();

	public Generador() {

	}
	void question() {
		String s = JOptionPane.showInputDialog("Numero de Procesos");
		procesos = Integer.parseInt(s);
		for (int i = 0; i < procesos; i++) {
			copyID = new Vector<String>();
			Random randomGenerator = new Random();

			int var1;
			var1 = randomGenerator.nextInt(10 - 01 + 1) + 01;

			randomGenerator = new Random();
			int var2;
			var2 = randomGenerator.nextInt(10 - 01 + 1) + 01;

			randomGenerator = new Random();
			int time;
			time = randomGenerator.nextInt(10 - 01 + 1) + 01;

			randomGenerator = new Random();
			int op;
			op = randomGenerator.nextInt(5 - 01 + 1) + 01;
			String operation;
			switch (op) {
			case 0:
				operation = "+";
				break;
			case 1:
				operation = "-";
				break;
			case 2:
				operation = "*";
				break;
			case 3:
				operation = "/";
				break;
			case 4:
				operation = "^";
				break;
			case 5:
				operation = "%";
				break;
			default:
				operation = "+";
				break;
			}
			copyID.addElement(Integer.toString(i));
			copyID.addElement(Integer.toString(var1));
			copyID.addElement(operation);
			copyID.addElement(Integer.toString(var2));
			copyID.addElement(Integer.toString(time));
			copyID.addElement("0");
			copyID.addElement(Integer.toString(time));
			tableInfo.addElement(copyID);

		}
	}

	public Vector<Vector<String>> getTable() {
		return tableInfo;
	}
}
