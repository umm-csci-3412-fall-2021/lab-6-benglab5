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

	protected ConnectionRunnable implements Runnable {

		Socket sock;

		public ConnectionRunnable(Socket s){
			this.sock = s;
		}

		void run() {

		}
	}
}