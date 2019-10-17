package Client;

import java.awt.Color;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class Client {

	static int ServerPort = 1234;
	private static Socket s;

	public static void updatemembers(GUI gui) {
		for (ClientHandler mc : Server.ar) {
			gui.getPanel().removeAll();
			gui.getPanel().add(new JButton(mc.getName()));
			gui.repaint();
			gui.getPanel().validate();
		}
	}

	public static void main(String args[]) throws UnknownHostException, IOException {
		String[] choises = { "server A : port number 1234", "Server B : port number 1235" };
		String picked = (String) JOptionPane.showInputDialog(null, "Please select your Port Number :", "Selecting Port",
				JOptionPane.QUESTION_MESSAGE, null, choises, choises[0]);
		System.out.println(picked);
		GUI gui = new GUI();

		if (picked.equals("server A : port number 1234")) {

			ServerPort = 1234;
		} else
			ServerPort = 1235;

		while (!gui.isConnected()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		InetAddress ip = InetAddress.getByName("localhost");
		try {
			s = new Socket(ip, ServerPort);
			gui.getLblClient().setText("Connected to ServerPort: " + ServerPort);
			if (ServerPort == 1234) {
				gui.getLblClient().setForeground(new Color(50, 50, 250));
			} else {
				gui.getLblClient().setForeground(new Color(250, 50, 50));
			}
		} catch (java.net.ConnectException e) {
			gui.getPanel1().add(new JLabel("Cannot Connect to Server"));
		}
		// obtaining input and out streams
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());

		// sendMessage thread
		Thread sendMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				gui.getPanel1().add(Box.createVerticalStrut(10));
				while (true) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// read the message to deliver.
					String msg = gui.send();

					try {
						// write on the output stream
						if (gui.isSent()&msg.length()>0) {
							if (msg.contains(":")) {
								dos.writeUTF(msg + "," + gui.getSpinner().getValue());
							} else
								dos.writeUTF(msg);
							String dat= DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now());
							gui.getText().setText("");
							JLabel fo = new JLabel( msg+"       "+dat);
							fo.setBackground(new Color(50,0, 51));
							fo.setForeground(new Color(255,255,255));
							fo.setBorder(new EmptyBorder(10, 25, 10, 7));
							fo.setBounds(0, 0, 250, 250);
							fo.setOpaque(true);
							fo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
							gui.getPanel1().add(fo);
							gui.getPanel1().add(Box.createVerticalStrut(10));
							gui.getPanel1().validate();
							gui.getScrollPane1().validate();
							gui.repaint();
							gui.setSent(false);
							dos.flush();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					finally {

					}
				}
			}
		});

		// readMessage thread
		Thread readMessage = new Thread(new Runnable() {
			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						// read the message sent to this client
						while (dis.available() > 0) {
							String msg = dis.readUTF();
							if (msg.toLowerCase().startsWith("server")) {
								msg=msg.substring(8);
							if (msg.toLowerCase().startsWith(":")) {
								msg=msg.substring(1);
								if (msg.toLowerCase().startsWith("server")){
									msg=msg.substring(8);
								}
							}
							
								}
							if(msg.length()!=0) {
							JLabel fo = new JLabel();
							fo = new JLabel("" + msg+"       "+ DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now()));
							fo.setBackground(new Color(255, 255, 255));
							fo.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
							fo.setBorder(new EmptyBorder(10, 25, 10, 7));
							fo.setOpaque(true);
							gui.getPanel1().add(fo);
							gui.getPanel1().add(Box.createVerticalStrut(10));
							gui.getPanel1().validate();
							gui.getScrollPane1().validate();
							gui.repaint();
							}
						}
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
			}
		});

		sendMessage.start();
		readMessage.start();

		updatemembers(gui);
	}

}
