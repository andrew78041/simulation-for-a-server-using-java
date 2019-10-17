package Client;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class GUIS extends JFrame {
	JFrame a;

	private JPanel panel;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new GUIS();

	}

	public GUIS() {
		
		JFrame a = new JFrame();
		a.setTitle("Server");
		a.setVisible(false);
		a.setBounds(50, 50, 1000, 1000);
		a.setResizable(false);
		JButton q = new JButton();
		JButton qa = new JButton();
		a.getContentPane().add(q);
		a.getContentPane().add(qa);

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 0, 500, 750);
		JPanel contentPane = new JPanel(null);
		contentPane.setPreferredSize(new Dimension(500, 300));
		contentPane.add(scrollPane);
		a.setContentPane(contentPane);
		a.pack();
		a.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		

	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
}

