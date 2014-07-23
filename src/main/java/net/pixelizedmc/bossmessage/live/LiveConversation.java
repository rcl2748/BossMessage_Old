package net.pixelizedmc.bossmessage.live;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.bukkit.command.CommandSender;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.lang.LangUtils;

public class LiveConversation {
	
	public static Socket socket;
	public static InputStreamListener listener;
	public static String summary;
	public static CommandSender user;
	public static boolean isActive = false;
	
	public static void startConversation(String question) {
		summary = question;
		isActive = true;
		try {
	        socket = new Socket("localhost", 6789);
        } catch (UnknownHostException e) {
	        e.printStackTrace();
	        Main.logger.severe(Main.PREFIX_CONSOLE + "THIS SHOULD NEVER OCCUR! PLEASE REPORT THIS STACKTRACE TO THE AUTHOR!");
        } catch (IOException e) {
        	System.out.println(1);
        } finally {
    		isActive = false;
        	if (socket != null && !socket.isClosed()) {
        		try {
	                socket.close();
                } catch (IOException e) {
                	System.out.println(1);
	                e.printStackTrace();
                }
        	}
        }
		listener = new InputStreamListener(socket) {
			
			@Override
			public void socketClosed(SocketException e) {
				isActive = false;
				LangUtils.sendError(user, "Conversation ended!");
			}
			
			@Override
			public void objectRecieved(Object object, InputStreamListener listener) {
				LangUtils.sendLiveMessage(user, (String) object);
			}
		};
	}
	
	public static void stopConversation() {
		if (isActive) {
			isActive = false;
			if (!socket.isClosed()) {
				try {
	                socket.close();
                } catch (IOException e) {
	                e.printStackTrace();
                }
			}
		}
	}
}
