package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Client2 {

	final static int ServerPort = 1235;
	private static Socket s;
public  void updatemembers(GUI gui) {
	for(ClientHandler mc : Server.ar) {
		gui.getPanel().removeAll();
		gui.getPanel().add(new JButton(mc.getName()));
		gui.repaint();
		gui.getPanel().validate();
	}
}
	public static void main(String args[]) throws UnknownHostException, IOException {

		GUI gui = new GUI();
		

		while (!gui.isConnected()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		InetAddress ip = InetAddress.getByName("localhost");
		s = new Socket(ip, ServerPort);

		gui.getLblClient().setText("Client "+s.getLocalPort());
		
	// obtaining input and out streams
		DataInputStream dis = new DataInputStream(s.getInputStream());
		DataOutputStream dos = new DataOutputStream(s.getOutputStream());

		// sendMessage thread
		Thread sendMessage = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// read the message to deliver.
					String msg = gui.send();

					try {
						// write on the output stream
						if (gui.isSent()) {
							if (msg.contains(":")){
								dos.writeUTF(msg+","+gui.getSpinner().getValue());
								}
								else
									dos.writeUTF(msg);
							gui.getPanel1().add(new JLabel("You: " + msg,SwingConstants.RIGHT));
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
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						// read the message sent to this client
						while (dis.available() > 0) {
							String msg = dis.readUTF();

							gui.getPanel1().add(new JLabel(msg));
							gui.getPanel1().validate();
							gui.getScrollPane1().validate();
							gui.repaint();
							System.out.println(msg);
						}
					} catch (IOException e) {

						e.printStackTrace();
					}

				}
			}
		});

		sendMessage.start();
		readMessage.start();

	}

}
