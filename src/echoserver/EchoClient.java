package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {
	public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException, InterruptedException {
		Socket socket = new Socket("localhost", PORT_NUMBER);
		// InputStream socketInputStream = socket.getInputStream();
		// OutputStream socketOutputStream = socket.getOutputStream();

		// Put your code here.

		WriterThread writer = new WriterThread(socket);
		ReaderThread reader = new ReaderThread(socket);

		writer.start();
		reader.start();
		writer.join();
		reader.join();
	}

	protected class WriterThread extends Thread {

		OutputStream output;
		Socket sock;

		protected WriterThread (String name, Socket s) throws IOException {
			super(name);
			this.sock = s;
			this.output = s.getOutputStream();
		}

		protected WriterThread (Socket s) throws IOException {
			super();
			this.sock = s;
			this.output = s.getOutputStream();
		}

		@Override
		public void run() {
			try {
                while(true){
                    int nextByteOut;
                    try {
                        nextByteOut = System.in.read();
                    } catch (IOException ioe) {
                        System.out.println("Error reading from System.in");
                        throw(ioe);
                    }
                    if(nextByteOut == -1) {
                        try {
                            sock.shutdownOutput();
                        } catch (IOException ioe){
                            System.out.println("Error closing output");
                            throw(ioe);
                        } finally {
							break;
						}
                    } else {
                        try {
                            output.write(nextByteOut);
                        } catch (IOException ioe) {
                            System.out.println("Error writing to socket");
                            throw(ioe);
                        }
                    }
				}
			} catch (IOException ioe) {
				System.out.println("IOException encountered creating the server or a stream.");
				System.err.println(ioe);
				this.interrupt();
			}
		}
	}



	protected class ReaderThread extends Thread {
		InputStream input;
		Socket sock;

		protected ReaderThread (String name, Socket s) throws IOException {
			super(name);
			this.sock = s;
			this.input = s.getInputStream();
		}

		protected ReaderThread (Socket s) throws IOException {
			super();
			this.sock = s;
			this.input = s.getInputStream();
		}

		@Override
		public void run() {
			try {
				while(true){
					int nextByteIn;
					try {
						nextByteIn = input.read();
					} catch (IOException ioe){
						System.out.println("Error reading from socket");
						throw(ioe);
					}
					if(nextByteIn == -1){
						break;
					} else {
						System.out.write(nextByteIn);
					}
				}
			} catch (IOException ioe) {
				System.out.println("IOException encountered creating the server or a stream.");
				System.err.println(ioe);
			} finally {
				System.out.flush();
				try {
					sock.close();
				} catch (IOException ioe){
					System.out.println("Error closing the socket being read");
					System.err.println(ioe);
				}
			}
		}
	}
}