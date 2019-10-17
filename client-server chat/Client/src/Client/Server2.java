package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JLabel;

// Server class 
public class Server2 {

	// Vector to store active clients
	static Vector<ClientHandler2> ar = new Vector<>();

	// counter for clients
	static int i = 0;
	private static Socket s1;
	static ServerSocket ss;

	private static DataInputStream dis1;

	private static DataOutputStream dos1;

	public static void main(String[] args) throws IOException {

		ss = new ServerSocket(1235);
		InetAddress ip = InetAddress.getByName("localhost");
		setS1(new Socket(ip, 1234));
		setDis1(new DataInputStream(s1.getInputStream()));
		setDos1(new DataOutputStream(s1.getOutputStream()));
		Server.sercon = true;
		Socket s;
		GUIS a = new GUIS();

		// running infinite loop for getting
		// client request
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Accept the incoming request
			s = ss.accept();
			System.out.println("New client request received : " + s);
			a.getPanel().add(new JLabel(("Server" + ss.getLocalPort())));
			a.getPanel().add(new JLabel(("New client request received : " + s)));
			a.getPanel().revalidate();
			a.repaint(50);
			// obtain input and output streams
			DataInputStream dis = new DataInputStream(s.getInputStream());
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());

			System.out.println("Creating a new handler for this client...");
			a.getPanel().add(new JLabel(("Creating a new handler for this client...")));
			a.getPanel().revalidate();
			a.repaint(50);
			// Create a new handler object for handling this request.
			ClientHandler2 mtch = new ClientHandler2(s, (i == 0 ? "Server A" : i + ""), dis, dos);

			// Create a new Thread with this object.
			Thread t = new Thread(mtch);

			System.out.println("Adding this client to active client list");
			a.getPanel().add(new JLabel(("Adding this client to active client list")));
			a.getPanel().revalidate();
			a.repaint(50);
			// add this client to active clients list
			ar.add(mtch);

			// start the thread.
			t.start();
			a.repaint();
			a.revalidate();
			// increment i for new client.
			// i is used for naming only, and can be replaced
			// by any naming scheme
			i++;

		}
	}

	public static Socket getS1() {
		return s1;
	}

	public static void setS1(Socket s1) {
		Server2.s1 = s1;
	}

	public static DataInputStream getDis1() {
		return dis1;
	}

	public static void setDis1(DataInputStream dis1) {
		Server2.dis1 = dis1;
	}

	public static DataOutputStream getDos1() {
		return dos1;
	}

	public static void setDos1(DataOutputStream dos1) {
		Server2.dos1 = dos1;
	}
}

// ClientHandler class 
class ClientHandler2 implements Runnable {
	Scanner scn = new Scanner(System.in);
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	DataInputStream dis;
	final DataOutputStream dos;
	Socket s;
	boolean isloggedin;

	// constructor
	public ClientHandler2(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.name = name;
		this.s = s;
		this.isloggedin = false;
	}

	@Override
	public void run() {

		String received;
		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				// receive the string
//				if(dis.readUTF()==null) {
//					this.isloggedin=false;
//					this.s.close();
//					break;
//				}
//				if(!(Server2.getDis1().available()>0)) {
//					
//				}
				while (dis.available() > 0) {
					received = dis.readUTF();
					int ttl = 99;
					if (received.contains(":")) {
						ttl = Integer.parseInt(received.split(",")[1]);
						received = received.split(",")[0];
						ttl--;
					}
					if (ttl <= 0) {
						System.out.println("Timeout");

						String names = "";
						boolean found = false;
						boolean f = false;
						StringTokenizer st = new StringTokenizer(received, ":");
						String MsgToSend = st.nextToken();

						String recipient = st.nextToken();
						if (recipient.contains("-")) {
							StringTokenizer st2 = new StringTokenizer(recipient, "-");
							recipient = st2.nextToken();
							names = st2.nextToken();
							f = true;

						} else {
							this.dos.writeUTF("Message not sent! increase TTL or change receipent");
							this.dos.flush();
							break;
						}
						// search for the recipient in the connected devices list.
						// ar is the vector storing client of active users
						for (ClientHandler2 mc : Server2.ar) {
							// if the recipient is found, write on its
							// output stream
							if (mc.name.equals(names) && mc.isloggedin == true) {
								if (f) {
									mc.dos.writeUTF(names + ":" + "Message not sent! increase TTL or change receipent");
									mc.dos.flush();
									found = true;
									f = true;
									break;
								} else {
									mc.dos.writeUTF(
											this.name + ":" + "Message not sent! increase TTL or change receipent");
									mc.dos.flush();
									found = true;
									break;
								}
							}

						}
						if (!found) {
							if (!(this.name.equals("Server A")&&names.equals("Server B"))) {
								Server2.getDos1().writeUTF("Message not sent! increase TTL or change receipent" + ":"
										+ names + "-" + this.name + "," + 3);
								Server2.getDos1().flush();
							}
							break;
						}
						break;
					} else {

					}
					System.out.println(received);
					System.out.println(ttl);

					if (!received.contains(":")) {
						if (received.equals("logout")) {

							for (int i = 0; i < Server2.ar.size(); i++) {
								if (Server2.ar.get(i).name.equals(this.name)) {
									Server2.ar.remove(i);
								}
							}
							this.isloggedin = false;
							this.dis.close();
							this.dos.close();
							this.dis.close();
							return;
						} else if (received.toLowerCase().startsWith("join(") && received.endsWith(")")) {
							StringTokenizer n = new StringTokenizer(received, "(");
							String n1 = n.nextToken();
							String n2 = n.nextToken();
							n2 = n2.substring(0, n2.length() - 1);
							try {
								for (ClientHandler2 m : Server2.ar) {
									if (m.name.equals(n2)) {
										this.dos.writeUTF("This Username is already taken!");
										this.dos.flush();
										break;
									}
									if (m.name.equals(this.name) && this.isloggedin == false) {
										m.setName(n2);
										this.isloggedin = true;
										break;
									}
								}
							} catch (ConnectException e) {
								this.dos.writeUTF("Cannot Connect, Server is down!");
							}
						} else if (received.toLowerCase().equals("getmemberslist()")) {
							for (ClientHandler2 m : Server2.ar) {

								if (m.name.equals(this.name) && m.isloggedin == true) {
									Server2.getDos1().writeUTF("getmemberslist()-" + this.name);
									m.dos.writeUTF("connected members are : ");
									for (int i = 0; i < Server2.ar.size(); i++) {
										m.dos.writeUTF(Server2.ar.get(i).getName());
										m.dos.flush();
									}
									break;
								}
							}
						} else if (received.toLowerCase().contains("getmemberslist()-")) {
							String receipents = received.split("-")[1];
							for (int i = 0; i < Server2.ar.size(); i++) {
								Server2.getDos1().writeUTF(Server2.ar.get(i).name + ":" + receipents + ",5");
								Server2.getDos1().flush();
							}
							break;
						}
					}

					else {
						boolean found = false;
						boolean f = false;
						StringTokenizer st = new StringTokenizer(received, ":");
						String MsgToSend = st.nextToken();

						String recipient = st.nextToken();
						String names = "";
						if (recipient.contains("-")) {
							StringTokenizer st2 = new StringTokenizer(recipient, "-");
							recipient = st2.nextToken();
							names = st2.nextToken();
							f = true;
						}
						// search for the recipient in the connected devices list.
						// ar is the vector storing client of active users
						for (ClientHandler2 mc : Server2.ar) {
							// if the recipient is found, write on its
							// output stream
							if (mc.name.equals(recipient) && mc.isloggedin == true) {
								if (f) {
									mc.dos.writeUTF(names + ":" + MsgToSend);
									mc.dos.flush();
									found = true;
									f = true;
									break;
								} else {
									mc.dos.writeUTF(this.name + ":" + MsgToSend);
									mc.dos.flush();
									found = true;
									break;
								}
							}
						}
						if (!found) {
							Server2.getDos1().writeUTF(MsgToSend + ":" + recipient + "-" + this.name + "," + ttl);
							Server2.getDos1().flush();
							break;
						}
					}
				}
			} catch (IOException e) {

				e.printStackTrace();
				break;
			}
		}
		try {
			// closing resources

			this.dis.close();
			this.dos.close();
			this.s.close();
			Server2.getDis1().close();
			Server2.getDos1().close();
			Server2.getS1().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}