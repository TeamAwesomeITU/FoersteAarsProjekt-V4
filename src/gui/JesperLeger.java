package gui;

import java.awt.*;

import javax.swing.*;


public class JesperLeger {
	
	public static void main(String[] args) {
		JesperLeger medFuttesMor = new JesperLeger();
	}
	
	private JFrame frame;
	private Container contentPane;
	
	public JesperLeger(){
		makeFrame();
	}
	
	public void makeFrame(){
		frame = new JFrame();
		contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		contentPane.add(makeComboBox(), BorderLayout.CENTER);

		frame.setPreferredSize(new Dimension(200,100));
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);	
	}
	
	public JPanel makeComboBox(){
		String[] patterns = {"", "Nørregade", "Nørreport", "Nørrevej", "Nørrealle"};
		JComboBox<String> box = new JComboBox<String>(patterns);
		
		JPanel flow = new JPanel();
		flow.add(box);
		
		return flow;
	}
}
