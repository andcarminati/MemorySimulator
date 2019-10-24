package simulator.gui;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import simulator.allocators.BestFit;
import simulator.allocators.FirstFit;
import simulator.allocators.NextFit;
import simulator.allocators.WorstFit;
import simulator.core.AbstractAllocator;
import simulator.core.Chunk;



public class GUI implements ActionListener{

	private static final int availableMemory = 4080;
	private int[] demands={100, 50, 20, 40, 70, 500, 4000, 30, 10, 120, 2000, 
			256, 500, 30, 100, 40, 700, 333, 900, 80, 150, 10, 5, 136, 100};
	private JFrame frame;
	private JTextPane textPane;
	private JTextArea textArea;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JTextField field1;
	private JTextField field2;
	private JComboBox<String> comboBox;
	private JComboBox<Double> comboBox2;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton button4;
	private JButton button5;
	private JButton button6;
	private JButton button7;
	private JButton button8;
	private Thread t;
	private volatile boolean finalizeThread = false;
	private volatile boolean stopThread = false;
	private volatile boolean alocFlag = false;
	private volatile boolean desalocFlag = false;
	
	private Lock mutex;
	private Condition condition;
	
	public GUI() {
		
		mutex = new ReentrantLock();
		condition = mutex.newCondition();
		
		frame = new JFrame("Memory allocation simulator by Prof. Andreu Carminati");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(680, 640);
		frame.setVisible(true);
		frame.setLayout(new FlowLayout());
		
		label3 = new JLabel("Select an allocator: ");
		frame.add(label3);
		comboBox = new JComboBox<String>();
		comboBox.addItem("First Fit");
		comboBox.addItem("Worst Fit");
		comboBox.addItem("Best Fit");
		comboBox.addItem("Next Fit");
		comboBox.setSelectedIndex(0);
		frame.add(comboBox);
		
		label4 = new JLabel("Simulation interval (sec.): ");
		frame.add(label4);
		comboBox2 = new JComboBox<Double>();
		comboBox2.addItem(0.5);
		comboBox2.addItem(1.0);
		comboBox2.addItem(2.0);
		comboBox2.addItem(4.0);
		comboBox2.setSelectedIndex(0);
		frame.add(comboBox2);
		
		button1 = new JButton ("Simulate");
		button1.addActionListener(this);
		frame.add(button1);
		button2 = new JButton ("Finalize");
		button2.addActionListener(this);
		button2.setEnabled(false);
		frame.add(button2);
		
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setPreferredSize(new Dimension(650,3));
        frame.add(sep);
		
		button3 = new JButton ("Stop");
		button3.addActionListener(this);
		button3.setEnabled(false);
		frame.add(button3);
		
		button4 = new JButton ("Next random");
		button4.addActionListener(this);
		button4.setEnabled(false);
		frame.add(button4);
		
		button6 = new JButton ("Next alloc.");
		button6.addActionListener(this);
		button6.setEnabled(false);
		frame.add(button6);
		
		button7 = new JButton ("Next dealloc.");
		button7.addActionListener(this);
		button7.setEnabled(false);
		frame.add(button7);
		
		button5 = new JButton ("Continue");
		button5.addActionListener(this);
		button5.setEnabled(false);
		frame.add(button5);
		
		sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setPreferredSize(new Dimension(650,3));
        frame.add(sep);
		
        
		label1 = new JLabel("Demands (bytes):");
		frame.add(label1);
		
		field1 = new JTextField(40);
		field1.setFont(new Font("Verdana", Font.BOLD, 10));
		field1.setEditable(false);
		frame.add(field1);
		
		label2 = new JLabel("Start addresses:");
		frame.add(label2);
		field2 = new JTextField(40);
		field2.setFont(new Font("Verdana", Font.BOLD, 10));
		field2.setEditable(false);
		frame.add(field2);
		
        button8 = new JButton("Load demands");
        button8.setActionCommand("Demandas");
        button8.addActionListener(this);
        frame.add(button8);
		
		sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setPreferredSize(new Dimension(650,3));
        frame.add(sep);
		
		textPane = new JTextPane();
		textPane.setPreferredSize(new Dimension(620, 215));
		textPane.setContentType("text/html");
		textPane.setEditable(false);
		frame.add(textPane);
		
		label5 = new JLabel(""+ availableMemory +" bytes of memory");
		frame.add(label5);
		
		sep = new JSeparator(SwingConstants.HORIZONTAL);
        sep.setPreferredSize(new Dimension(650,3));
        frame.add(sep);
		
		textArea = new JTextArea();
		//textArea.setPreferredSize(new Dimension(620, 160));
		textArea.setFont(new Font("Verdana", Font.BOLD, 11));
		textArea.setLineWrap(true); 
		
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(620, 160));
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane);
        frame.setResizable(false);
        textArea.setText("");
        textPane.setText(genHTML(null, availableMemory));
        updadeDemands(demands);
	}
	
	private void checkStop(){
		
		try {
			mutex.lock();
			if(stopThread){
				condition.await();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			mutex.unlock();
		}
	}
	
	private void stopThread(){	
		stopThread = true;
	}
	
	private void resumeThread(){
		stopThread = false;
		try {
			mutex.lock();
			condition.signal();		
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			mutex.unlock();
		}
	}
	
	private void stepThread(){
		try {
			mutex.lock();
			if(stopThread){
				condition.signal();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			mutex.unlock();
		}
	}
	
	private AbstractAllocator getAlocador(int memory){
		
		String name = comboBox.getItemAt(comboBox.getSelectedIndex());
		
		if(name.equals("First Fit")){
			return new FirstFit(memory);
		} else if (name.equals("Worst Fit")){
			return new WorstFit(memory);
		} else if (name.equals("Best Fit")){
			return new BestFit(memory);
		}else if (name.equals("Next Fit")){
			return new NextFit(memory);
		}
		return null;
	}
	
	private int getSimulationTime(){
		
		return (int) (1000*comboBox2.getItemAt(comboBox2.getSelectedIndex()));
	}
	
	public void runTests(){
		
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				Random randomGenerator = new Random();
				AbstractAllocator alocador = getAlocador(availableMemory);
				Queue<Integer> demanda = new LinkedList<>();
				Queue<Integer> enderecos = new LinkedList<>();
				
				for (int i = 0; i < demands.length; i++) {
					int j = demands[i];
					demanda.add(j);
				}
				
				//System.out.println("Testando alocador " + alocador.getClass().getName() + ":");
				updateGUI(null, "Simulating " + alocador.getClass().getName() + " allocator\n", null, null);
				
				while((!demanda.isEmpty() || !enderecos.isEmpty()) && !finalizeThread ){
					checkStop();
					
					boolean random = randomGenerator.nextBoolean();
					
					if(random && demanda.isEmpty()){
						random = false;
					} else if(enderecos.isEmpty()){
						random = true;
					}
					
					if(alocFlag){
						random = true;
					}
					
					if(desalocFlag){
						random = false;
					}
					
					if(random){
						if(!demanda.isEmpty()){
							int aDemanda = demanda.remove();
							int oEndereco = alocador.allocate(aDemanda);
							
							if(oEndereco == -1){
								JOptionPane.showMessageDialog(frame, "Not enough memory for demand: " + aDemanda + " bytes");
								updateGUI(null, "Not enough memory for demand: " + aDemanda + " bytes\n", null, null);
							}
							
							enderecos.add(oEndereco);
							alocador.dump();
							try {
								Thread.sleep(getSimulationTime());
							} catch (InterruptedException e) {							
								e.printStackTrace();
							}
							updateGUI(alocador.getInitialChunk(), 
									"Allocating memory for: " + aDemanda + " bytes" + "\n",
									(List<Integer>)demanda, (List<Integer>)enderecos);
						}
					} else {
						if(!enderecos.isEmpty()){
							int oEndereco = enderecos.remove();
							alocador.deallocate(oEndereco);
							alocador.dump();
							try {
								Thread.sleep(getSimulationTime());
							} catch (InterruptedException e) {
								// TODO Bloco catch gerado automaticamente
								e.printStackTrace();
							}
							updateGUI(alocador.getInitialChunk(), 
									"Deallocating memory for address: " + oEndereco + "\n",
									(List<Integer>)demanda, (List<Integer>)enderecos);
						}
					}
					alocFlag = false;
					desalocFlag = false;
				}
				stopThread = false;
				finalizeThread = false;
				resetSimulation();
				updateGUI(null, "Simulation finished\n", null, null);
			}
		});
		t.start();
	}
	
	private void resetSimulation() {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				button1.setEnabled(true);
				button2.setEnabled(false);
				button3.setEnabled(false);
				button4.setEnabled(false);
				button5.setEnabled(false);
				button6.setEnabled(false);
				button7.setEnabled(false);
				button8.setEnabled(true);
				updadeDemands(demands);
			}
		});
	}
	
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		
		SwingUtilities.invokeAndWait(new Runnable() {
			
			@Override
			public void run() {
				new GUI();
			}
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("Simulate")){
			button1.setEnabled(false);
			button2.setEnabled(true);
			button3.setEnabled(true);
			button8.setEnabled(false);
			runTests();
		} else if(e.getActionCommand().equals("Finalize")){
			finalizeThread = true;
			resumeThread();
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(false);
			button4.setEnabled(false);
			button5.setEnabled(false);
			button6.setEnabled(false);
			button7.setEnabled(false);
		} else if(e.getActionCommand().equals("Stop")){
			button3.setEnabled(false);
			button4.setEnabled(true);
			button5.setEnabled(true);
			button6.setEnabled(true);
			button7.setEnabled(true);
			stopThread();
		} else if(e.getActionCommand().equals("Continue")){
			button3.setEnabled(true);
			button4.setEnabled(false);
			button4.setEnabled(false);
			button5.setEnabled(false);
			button6.setEnabled(false);
			button7.setEnabled(false);
			resumeThread();
		} else if(e.getActionCommand().equals("Next random")){
			stepThread();
		} else if(e.getActionCommand().equals("Next alloc.")){
			alocFlag = true;
			stepThread();
		} else if(e.getActionCommand().equals("Next dealloc.")){
			desalocFlag = true;
			stepThread();
		} else if(e.getActionCommand().equals("Demandas")){
			int[] numbers;
	        DemandLoaderGUI d = new DemandLoaderGUI(frame);
	        d.setVisible(true);
	        numbers = d.getResult();
	        if(numbers != null){
	        	demands = numbers;
	        	updadeDemands(demands);
	        }
		}
	}
	
	private String genHTML(Chunk start, int blocks){
		StringBuilder builder = new StringBuilder();
		
		builder.append("<!DOCTYPE html>\n<html>\n<head>\n</head>\n<body>\n");
		int total = 1;
		
		while(start != null){
			
			int amt = start.size()/10;
			String img = start.isAvailable() ? getFreeImage() : getUsedImage();
			
			for (int i = 1; i <= amt; i++) {
				builder.append("<img src=" + img + "\" width=\"18\" height=\"12\">");
				if(total++%34==0){
					builder.append("<br>");
				}
			}
			start = start.getNext();
		}
		
		int amt = blocks/10;
		
		for (int i = 1; i <= amt; i++) {
			builder.append("<img src=" + getFreeImage() + "\" width=\"18\" height=\"12\">");
			if(total++%34==0){
				builder.append("<br>");
			}
		}
		builder.append("\n</body>\n</html>");
		return builder.toString();
	}
	
	public void updateGUI(Chunk start, String message, List<Integer> dema, List<Integer> end){	
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					
					if(dema != null){
						updadeDemands(dema);
					}
		
					if(end != null){
						StringBuilder builder2 = new StringBuilder();
						for (Iterator<Integer> iterator = end.iterator(); iterator.hasNext();) {
							int ende = iterator.next();
							builder2.append("" + ende + ", ");
						}
						field2.setText(builder2.toString());
						field2.setCaretPosition(0);
					}

					if(start != null){
						textPane.setText(genHTML(start, 0));
					}
					
					if(message != null){
						textArea.setText(textArea.getText()+message);
					}
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String getFreeImage(){
		return this.getClass().getClassLoader().getResource("free.png").toString();
	}

	private String getUsedImage(){
		return this.getClass().getClassLoader().getResource("used.png").toString();
	}
	
	private void updadeDemands(int[] dema) {
		List<Integer> list = new LinkedList<Integer>();
		
		for (int i = 0; i < dema.length; i++) {
			int j = dema[i];
			list.add(j);
		}
		updadeDemands(list);
	}
	
	private void updadeDemands(List<Integer> dema) {
		StringBuilder builder1 = new StringBuilder();
		for (Iterator<Integer> iterator = dema.iterator(); iterator.hasNext();) {
			int demanda = iterator.next();
			builder1.append("" + demanda + ", ");
		}
		field1.setText(builder1.toString());
		field1.setCaretPosition(0);
	}
}
