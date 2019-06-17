
// Ricardo Santiago
// 4/29/16
// CSCI-112
// Threaded Server for Quadratic Client

import java.io.*;
import java.net.*;

public class ThreadedServer {
	static final int PORTNUMBER = 5013;

	static void pStr(String p) {
		System.out.println(p);
	}

	void runServer() {
		ServerSocket server;
		Socket connection;

		try {
			pStr("Creating Server Socket " + 5013 + " . . . ");
			server = new ServerSocket(5013);
			pStr("SUCCESS!!!");

			while (true) {
				pStr("Waiting for connection.");
				connection = server.accept();
				pStr("Done");
				ThreadConnect t = new ThreadConnect(connection);
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class ThreadConnect extends Thread {
		private Socket connection;

		ThreadConnect(Socket x) {
			connection = x;
		}

		public void run() {
			DataOutputStream output = null;
			DataInputStream input = null;

			try {

				input = new DataInputStream(connection.getInputStream());
				output = new DataOutputStream(connection.getOutputStream());

				String message;

				try {

					double a, b, c, root1, root2;

					c = input.readDouble();
					b = input.readDouble();
					a = input.readDouble();

					double discriminant = b * b - 4.0 * a * c;

					String statusStr;
					int statusNum;

					if (discriminant > 0) {

						root1 = (-b + Math.sqrt(discriminant)) / (2 * a);
						root2 = (-b - Math.sqrt(discriminant)) / (2 * a);
						statusNum = 2;

						output.writeDouble(root1);
						output.writeDouble(root2);
						output.writeInt(statusNum);
					}

					if (discriminant == 0) {

						statusNum = 1;
						root1 = -b / (2 * a);
						root2 = -b / (2 * a);

						output.writeDouble(root1);
						output.writeDouble(root2);
						output.writeInt(statusNum);

					}
					if (discriminant < 0) {
						statusNum = 0;
						output.writeInt(statusNum);
					}

					else {
						statusNum = -1;
						statusStr = ("The server encountered an error doing the calculation");
						output.writeInt(statusNum);
						output.writeUTF(statusStr);
					}

				} catch (Exception e) {
					message = e.getMessage();
				}

				output.flush();
			} catch (Exception e) {
				pStr(e.getMessage());
			} finally {
				try {
					input.close();
				} catch (Exception e) {
				}
				try {
					output.close();
				} catch (Exception e) {
				}
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void main(String args[]) {
		ThreadedServer s = new ThreadedServer();
		s.runServer();
	}
}