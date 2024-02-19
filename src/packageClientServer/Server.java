package packageClientServer;

//Java implementation of Server side 
//It contains two classes : Server and ClientHandler 
//Save file as Server.java 

import java.io.*;
import java.text.*;
import java.util.*;

import java.net.*;

//Server class 
public class Server {
	private static int clientCount = 1;

	public static void main(String[] args) throws IOException {
		// server is listening on port 5056
		ServerSocket ss = new ServerSocket(5056);

		// running infinite loop for getting
		// client request

		System.out.println("Server started");

		System.out.println("Waiting for a client ...");
		while (true) {
			Socket s = null;

			try {
				// socket object to receive incoming client requests
				s = ss.accept();
				// Displaying that a new client is connected to the server
				int clientId = clientCount++;

				System.out.println("A new client with id " + clientId + " is connected : " + s);

				// obtaining input and out streams
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				System.out.println("Assigning new thread for this client");

				// create a new thread object
				ClientHandlerRunnable t = new ClientHandlerRunnable(s, dis, dos);
				new Thread(t).start();

				// Invoking the start() method
				// t.start();

			} catch (Exception e) {
				ss.close();
				s.close();
				e.printStackTrace();
			}
		}
	}
}

//ClientHandler class 
class ClientHandler extends Thread {
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}

	@Override
	public void run() {
		String received;
		String toreturn;
		while (true) {
			try {

				// Ask user what he wants
				dos.writeUTF("What do you want?[Date | Time]..\n" + "Type Exit to terminate connection.");

				// receive the answer from client
				received = dis.readUTF();

				if (received.equals("Exit")) {
					System.out.println("Client " + this.s + " sends exit...");
					System.out.println("Closing this connection.");
					this.s.close();
					System.out.println("Connection closed");
					break;
				}

				// creating Date object
				Date date = new Date();

				// write on output stream based on the
				// answer from the client
				switch (received) {

				case "Date":
					toreturn = fordate.format(date);
					dos.writeUTF(toreturn);
					break;

				case "Time":
					toreturn = fortime.format(date);
					dos.writeUTF(toreturn);
					break;

				default:
					dos.writeUTF("Invalid input");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			// closing resources
			this.dis.close();
			this.dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// ClientHandlerRunnable class
class ClientHandlerRunnable implements Runnable {
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd");
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
	final DataInputStream inputStream;
	final DataOutputStream outputStream;
	final Socket socket;

	// Constructor
	public ClientHandlerRunnable(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.socket = s;
		this.inputStream = dis;
		this.outputStream = dos;
	}

	@Override
	public void run() {
		String received;
		String toreturn;
		while (true) {
			try {

				// Ask user what he wants
				outputStream.writeUTF("What do you want?[Date | Time]..\n" + "Type Exit to terminate connection.");

				// receive the answer from client
				received = inputStream.readUTF();

				if (received.equals("Exit")) {
					System.out.println("Client " + this.socket + " sends exit...");
					System.out.println("Closing this connection.");
					this.socket.close();
					System.out.println("Connection closed");
					break;
				}

				// creating Date object
				Date date = new Date();

				// write on output stream based on the
				// answer from the client
				switch (received) {
				// if client sent "Date", format the current date and send it to the client
				case "Date":
					toreturn = fordate.format(date);
					outputStream.writeUTF(toreturn);
					break;
				// if client sent "Time", format the current time and send it to the client
				case "Time":
					toreturn = fortime.format(date);
					outputStream.writeUTF(toreturn);
					break;

				default:
					outputStream.writeUTF("Invalid input");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			// closing resources
			this.inputStream.close();
			this.outputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
