/**
 * 
 */
package com.elizalde.simulacion;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.elizalde.simulacion.input.Keyboard;
import com.elizalde.simulacion.tablas.TablaDeResultados;
import com.elizalde.simulacion.tablas.TablaEnEjecucion;
import com.elizalde.simulacion.tablas.TablaLotes;
import com.elizalde.simulacion.timer.Timers;

/**
 * @author Elizalde Loera Felipe de Jesus
 *
 */

public class Simulacion extends JFrame implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	private GridBagLayout layout;
	private GridBagConstraints constraints;

	// Threads
	public Thread ResultsThread;
	boolean running = false;

	// Gui Components
	public JLabel lotesPendientes;
	public JButton buttonAdd;
	public JButton buttonProcesar;
	public JFrame process;

	// Gui Specs
	public static int width = 300;
	public static int height = 168;
	public static int scale = 3;
	public static String title = "Simulacion";
	public boolean empty = false;

	// Classes
	public TablaLotes tablaLotes;
	public TablaEnEjecucion segundaTabla;
	public TablaDeResultados tercerTabla;
	public Timers timer;

	public boolean isPaused = false;
	public boolean isContinued = false;
	public boolean rowRemoved = false;
	Keyboard key;
	// Data Structures
	public Vector<Vector<String>> info = new Vector<Vector<String>>(), mainTable = new Vector<Vector<String>>();

	public Vector<String> ids = new Vector<String>();

	public int processCounter = 0;
	public int lotCounter = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Simulacion simulacion = new Simulacion();
		simulacion.setResizable(false);
		simulacion.setTitle(Simulacion.title);
		simulacion.pack();
		simulacion.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		simulacion.setLocationRelativeTo(null);
		simulacion.setVisible(true);

	}

	public Simulacion() {
		key = new Keyboard();
		addKeyListener(key);
		int isEmpty = ids.capacity();
		if (isEmpty == 0) {
			System.out.println("Empty");
		}
		setPreferredSize(new Dimension(width * scale, height * 2));

		layout = new GridBagLayout();
		setLayout(layout);
		constraints = new GridBagConstraints();

		tablaLotes = new TablaLotes(width, height);

		segundaTabla = new TablaEnEjecucion();
		segundaTabla.defaultTable.addRow(new Object[] { "Programa" });
		segundaTabla.defaultTable.addRow(new Object[] { "Operacion" });
		segundaTabla.defaultTable.addRow(new Object[] { "Tiempo Maximo Estimado" });
		segundaTabla.defaultTable.addRow(new Object[] { "Tiempo Transcurrido" });
		segundaTabla.defaultTable.addRow(new Object[] { "Tiempo Restante" });
		segundaTabla.defaultTable.addColumn("Datos");

		segundaTabla.tablaLotes.setRowHeight(35);
		tercerTabla = new TablaDeResultados();
		lotesPendientes = new JLabel("Lotes Pendientes: 0");
		buttonAdd = new JButton("Agregar Proceso");
		buttonProcesar = new JButton("Procesar Lotes");
		timer = new Timers();
		buttonProcesar.addActionListener(this);
		buttonAdd.addActionListener(this);

		// Put all your components in the JPanel
		constraints.fill = GridBagConstraints.ABOVE_BASELINE;
		addComponent(lotesPendientes, 0, 0, 3, 3);

		constraints.fill = GridBagConstraints.EAST;
		addComponent(timer, 0, 6, 1, 4);
		constraints.fill = GridBagConstraints.NONE;
		addComponent(new JPanel().add(tablaLotes.getScrollPane()), 8, 1, 2, 2);

		constraints.fill = GridBagConstraints.NONE;
		addComponent(new JPanel().add(segundaTabla.getScrollPane()), 8, 3, 2, 2);

		constraints.fill = GridBagConstraints.NONE;
		addComponent(new JPanel().add(tercerTabla.getScrollPane()), 8, 5, 2, 2);

		constraints.fill = GridBagConstraints.WEST;
		addComponent(buttonAdd, 10, 2, 1, 4);

		constraints.fill = GridBagConstraints.CENTER;
		addComponent(buttonProcesar, 10, 4, 1, 4);
	}

	public void addComponent(Component component, int row, int column, int width1, int height1) {
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width1;
		constraints.gridheight = height1;
		layout.setConstraints(component, constraints);
		add(component);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == buttonAdd) {
			this.setVisible(false);
			tercerTabla.clearAll();
			// Preguntas preguntas = new Preguntas();
			Generador generador = new Generador();
			/*
			 * process = new JFrame("Agregar Proceso"); process.setSize(new
			 * Dimension(350,400)); process.setResizable(true);
			 * process.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 * process.setLocationRelativeTo(null); process.setVisible(true);
			 * process.add(preguntas); preguntas.setID(ids);
			 * preguntas.getButton().addActionListener( new ActionListener() {
			 * public void actionPerformed(ActionEvent event) {
			 * if(event.getSource() == preguntas.getButton()) { info =
			 * preguntas.getTable(); copyTable(); process.dispose();
			 * 
			 * } } });
			 */
			generador.question();
			info = generador.getTable();
			copyTable();
		}
		if (e.getSource() == buttonProcesar) {
			if (lotCounter > 0) {
				System.out.println("Lote Counter: " + lotCounter);
				timer.startTimer();
				segundaTabla.setAllinfo(mainTable);

				segundaTabla.startTimer();
				startTimer();
				buttonProcesar.setEnabled(false);
				buttonAdd.setEnabled(false);

			}
		}

	}

	public void copyTable() {
		mainTable = info;
		if (mainTable.size() % 5 == 0) {
			updateNumberLotes(mainTable.size() / 5);
		} else {
			updateNumberLotes(mainTable.size() / 5 + 1);
		}
		// updateTable();
		this.setVisible(true);
	}

	public void updateTable() {
		if (processCounter < info.size()) {
			int sectionCounter = 0;
			while (processCounter < info.size() && sectionCounter < 5) {
				tablaLotes.defaultTable.addRow(new Object[] { info.get(processCounter).get(0),
						info.get(processCounter).get(4), info.get(processCounter).get(6)});
								
				processCounter++;
				sectionCounter++;
			}
		}
	}

	public void updateNumberLotes(int numeroLotes) {
		lotCounter = numeroLotes;
		lotesPendientes.setText("Lotes Pendientes: " + numeroLotes);
	}

	public synchronized void startTimer() {
		running = true;
		ResultsThread = new Thread(this, "Results");
		ResultsThread.start();
	}

	public synchronized void stop() {
		running = false;
		processCounter = 0;
		info.removeAllElements();
		ids.removeAllElements();
		mainTable.removeAllElements();
		buttonProcesar.setEnabled(true);
		buttonAdd.setEnabled(true);
		try {
			timer.stop();
			segundaTabla.stop();
			ResultsThread.join(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void update() {
		key.update();
		if (key.p == true && isPaused == false) {
			isPaused = true;
			pauseProgram();
		} else if (key.c == true && isPaused == true) {
			isPaused = false;
			continueProgram();
		} else if (key.w == true && isPaused == false) {
			segundaTabla.Error();
		} else if (key.e == true) {
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(rowRemoved == false)
				{
					if(segundaTabla.Entrada_Salida())
					
					{
						Object[] program= {tablaLotes.defaultTable.getValueAt(0, 0),
							tablaLotes.defaultTable.getValueAt(0, 1),
							segundaTabla.realTimeLeft};
					tablaLotes.defaultTable.removeRow(0);
					tablaLotes.defaultTable.addRow(program);
					}
				}
			else rowRemoved = false;
			
		}
	}

	@SuppressWarnings("deprecation")
	public void pauseProgram() {
		timer.timerThread.suspend();
		segundaTabla.executionThread.suspend();
	}

	@SuppressWarnings("deprecation")
	public void continueProgram() {
		timer.timerThread.resume();
		segundaTabla.executionThread.resume();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int lotesProcessed = 0;
		boolean updatingCheck = true;

		// tercerTabla.defaultTable.addRow(new Object[]{"Lote:
		// "+String.valueOf((lotesProcessed/5)+1)});
		requestFocus();
		while (running) {
			update();
			try {
				// TimeUnit.SECONDS.sleep(0.5);
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (updatingCheck) {
				if (lotesProcessed % 5 == 0) {
					updatingCheck = false;
					updateTable();
				}
			}
			if (segundaTabla.getProcessChanged()) {
				if (lotesProcessed % 5 == 0) {
					updatingCheck = true;
					tercerTabla.defaultTable
							.addRow(new Object[] { "Lote: " + String.valueOf((lotesProcessed / 5) + 1) });
				}
				tercerTabla.defaultTable.addRow(new Object[] {
						info.get(lotesProcessed).get(0), info.get(lotesProcessed).get(1)
								+ info.get(lotesProcessed).get(2) + info.get(lotesProcessed).get(3),
						segundaTabla.getResult() });
				segundaTabla.setProcessChanged();

				if (tablaLotes.defaultTable.getRowCount() > 0) {
					try {
						for(int i = 0; i < tablaLotes.defaultTable.getRowCount();i++)
						if(Integer.parseInt((String) tablaLotes.defaultTable.getValueAt(i, 0)) ==
						Integer.parseInt((String) tercerTabla.defaultTable.getValueAt(tercerTabla.defaultTable.getRowCount()-1,0)))
							tablaLotes.defaultTable.removeRow(i);
						rowRemoved = true;
					} catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}
				}
				lotesProcessed++;

				if (lotesProcessed % 5 == 0 || tablaLotes.defaultTable.getRowCount() == 0) {
					updateNumberLotes(lotCounter - 1);
				}
				if (lotesProcessed == info.size()) {
					stop();
				}

			}

		}
	}

}
