package simulator.gui;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;



public class DemandLoaderGUI extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3499538205052265505L;
	private JButton button1;
	private JButton button2;
	private JLabel label;
	private JTextField field;
	private ArrayList<Integer> demandList;
	
	public DemandLoaderGUI(JFrame frame){
		
		super(frame, "Load demands", true);
		
		setLayout(new FlowLayout());
		setModal(true);
		setResizable(false);
		setLocationRelativeTo(frame);
		setSize(new Dimension(450, 140));
		
		label = new JLabel("Insert the demands (bytes) separated by spaces:");
		add(label);
		
		demandList = new ArrayList<Integer>();
		
		field = new JTextField(35);
		add(field);
		field.setText("");
		
		button1 = new JButton("Load");
		button1.addActionListener(this);
		add(button1);
		
		button2 = new JButton("Cancel");
		button2.addActionListener(this);
		add(button2);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Carregar")){
			String str = field.getText();
			StringTokenizer tokenizer = new StringTokenizer(str);
			
			try {
				while(tokenizer.hasMoreTokens()){
					String strNumber = tokenizer.nextToken();
					int number = Integer.parseInt(strNumber);
					demandList.add(number);
				}
				setVisible(false);
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "Formato invalido de numero!");
			}
			
		} else if(e.getActionCommand().equals("Cancelar")){
			setVisible(false);
		}
	}
	public int[] getResult(){
		int[] numbers;
		
		if(demandList.size() == 0){
			return null;
		}
		numbers = new int[demandList.size()];
		
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = demandList.get(i);
			
		}
		return numbers;
	}
}
