package net.pixelizedmc.bossmessage.live;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;


public abstract class InputStreamListener {
	
	public boolean run = true;
	public Thread t;
	public InputStreamListener instance = this;
	public ObjectInputStream input;
	public Socket socket;
	
	public InputStreamListener(final Socket s) {
		t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					socket = s;
					input = new ObjectInputStream(socket.getInputStream());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				while (run) {
					try {
						objectRecieved(input.readObject(), instance);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SocketException e) {
						try {
	                        input.close();
                        } catch (IOException e1) {
	                        e1.printStackTrace();
                        }
						socketClosed(e);
						run = false;
					} catch (EOFException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
	public abstract void objectRecieved(Object object, InputStreamListener listener);
	public abstract void socketClosed(SocketException e);
}
