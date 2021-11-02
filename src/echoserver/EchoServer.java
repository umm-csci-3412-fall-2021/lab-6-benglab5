package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class EchoServer {
	
	// REPLACE WITH PORT PROVIDED BY THE INSTRUCTOR
	public static final int PORT_NUMBER = 6013; 
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
		ExecutorService pool = Executors.newCachedThreadPool();

		while (true) {
			Socket socket = serverSocket.accept();

			ConnectionRunnable connection = new ConnectionRunnable(socket);

			pool.submit(connection); //Returns a future which should probably be tracked somehow?

			// Put your code here.
			// This should do very little, essentially:
			//   * Construct an instance of your runnable class
			//   * Construct a Thread with your runnable
			//      * Or use a thread pool
			//   * Start that thread
		}


	}

	public class ConnectionRunnable implements Runnable {

		Socket sock;

		public ConnectionRunnable(Socket s){
			this.sock = s;
		}

		@Override
		public void run() {
			try {
				InputStream sockIn = sock.getInputStream();
				OutputStream sockOut = sock.getOutputStream();

				try {
					while (true) {
						int nextByte = sockIn.read();
						if (nextByte == -1) {
							break;
						} else {
							sockOut.write(nextByte);
						}
						
					}
				} catch (IOException ioe) {
					System.err.println(ioe);
				} finally {
					sock.shutdownOutput();
				}
			} catch (IOException ioe) {
				System.err.println(ioe);
			}

		}
	}
}