package Client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Canvas;

public class GUI extends JFrame {
	private static int i=1;
	JFrame a;
	JButton connect;
	boolean sent = false;
	boolean connected = false;
	String cname;
	private JPanel panel;

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel1) {
		this.panel1 = panel1;
	}

	public JScrollPane getScrollPane1() {
		return scrollPane1;
	}

	public boolean isSent() {
		return sent;
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	private static final long serialVersionUID = 1L;
	private JTextField text;
	private JButton btnNewButton;
	private JPanel panel1;
	private JScrollPane scrollPane1;
	private JLabel lblClient;
	private boolean start = true;
	public JSpinner getSpinner() {
		return spinner;
	}

	public void setSpinner(JSpinner spinner) {
		this.spinner = spinner;
	}

	private JSpinner spinner;

	public static void main(String[] args) {
		GUI frame=new GUI();

		

		    frame.pack();
		    frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
		    frame.setVisible( true );
		System.out.println(i);

	}
boolean ab=false;
	@SuppressWarnings("deprecation")
	public GUI() {
		i++;
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		a = new JFrame();
		a.setBackground(Color.BLUE);
		a.setResizable(false);
		a.setTitle("Client\r\n");
		a.getContentPane().setBackground(new Color(0, 0, 51));
		SwingUtilities.updateComponentTreeUI(a);
		a.setPreferredSize(new Dimension(600, 500));
		setConnected(false);
		a.pack();
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		a.getContentPane().setLayout(null);
		panel1 = new JPanel();
		panel1.setFont(new Font("Segoe UI Historic", Font.PLAIN, 12));
		panel1.setBorder(new LineBorder(new Color(51, 0, 51), 2, true));
		panel1.setForeground(new Color(0, 0, 0));
		panel1.setBackground(new Color(0, 0, 51));

		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		scrollPane1 = new JScrollPane(panel1);
		scrollPane1.setBorder(null);
		
		
		scrollPane1.setAutoscrolls(true);
		scrollPane1.setBounds(10, 31, 572, 341);
		scrollPane1.getViewport().revalidate();

		a.getContentPane().add(scrollPane1);
		text = new JTextField();
		text.setBorder(new EmptyBorder(5, 20, 5, 5));
		text.setBackground(new Color(0, 0, 51));

		text.setText("Start Typing here:");
		text.setForeground(Color.LIGHT_GRAY);
		text.setSelectionColor(new Color(192, 192, 192));
		text.setLocation(new Point(4, 4));

//		text.setWrapStyleWord(true);
//		text.setLineWrap(true);
		text.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
		text.setBounds(32, 386, 441, 55);
		text.addMouseListener(new MouseAdapter() {

			
			public void mouseClicked(MouseEvent e) {
				if (start) {
				
					text.setText("");
					text.setForeground(Color.white);
					start = false;
					
				}
			}
		});
		a.getContentPane().add(text);
		text.setColumns(10);

		btnNewButton = new JButton("Send");
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(null);
		btnNewButton.setIconTextGap(10);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setBorder(null);
		btnNewButton.setAutoscrolls(true);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setBackground(new Color(51, 0, 51));
		btnNewButton.setBounds(521, 386, 50, 55);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// display/center the jdialog when the button is pressed
				setSent(true);
				if (text.getText().toLowerCase().startsWith("join(") && text.getText().toLowerCase().endsWith(")"))
					setConnected(true);
			}
		});
		a.getContentPane().add(btnNewButton);
		
			

		lblClient = new JLabel("Client not connected");
		lblClient.setIconTextGap(80);
		lblClient.setIcon(null);
		lblClient.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		lblClient.setBorder(null);
		lblClient.setHorizontalAlignment(SwingConstants.CENTER);
		lblClient.setOpaque(true);
		lblClient.setFont(new Font("Segoe UI Semibold", Font.BOLD | Font.ITALIC, 13));
		lblClient.setBackground(new Color(51, 0, 51));
		lblClient.setForeground(new Color(255, 255, 255));
		lblClient.setBounds(0, 6, 594, 23);
		a.getContentPane().add(lblClient);
		
		 spinner = new JSpinner();
		 spinner.setForeground(new Color(0, 0, 51));
		 spinner.setBorder(null);
		 spinner.setToolTipText("Time to Live");
		 spinner.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 12));
		 spinner.setBackground(new Color(0, 0, 51));
		spinner.setBounds(472, 386, 45, 55);
		spinner.setValue(2);
		
		a.getContentPane().add(spinner);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBackground(new Color(51, 0, 51));
		lblNewLabel.setBounds(0, 375, 594, 81);
		a.getContentPane().add(lblNewLabel);
		a.pack();
		a.setVisible(true);

	}

	public JLabel getLblClient() {
		return lblClient;
	}

	public void setLblClient(JLabel lblClient) {
		this.lblClient = lblClient;
	}


	public void setScrollPane(JScrollPane scrollPane1) {
		this.scrollPane1 = scrollPane1;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public JPanel getPanel1() {
		repaint();
		revalidate();
		return panel1;
	}

	public void setPanel1(JPanel panel1) {
		this.panel1 = panel1;
	}

	public JTextField getText() {
		repaint();
		revalidate();
		return text;

	}

	public void setText(JTextField text) {
		this.text = text;
	}

	public String send() {

		repaint();
		revalidate();
		return getText().getText();

	}

	public static int getI() {
		return i;
	}

	public static void setI(int i) {
		GUI.i = i;
	}
}
