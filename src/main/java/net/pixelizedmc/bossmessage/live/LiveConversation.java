package net.pixelizedmc.bossmessage.live;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import net.pixelizedmc.bossmessage.Main;
import net.pixelizedmc.bossmessage.lang.LangUtils;

public class LiveConversation {
	private static Socket socket;
	private static ObjectOutputStream output;
	public static InputStreamListener listener;
	private static CommandSender conversationLeader;
	public static boolean isActive = false;
	
	public static boolean startConversation(CommandSender sender) {
		if (isActive) {
			LangUtils.sendLiveMessage(sender, "Conversation is already running!");
			return false;
		}
		conversationLeader = sender;
		isActive = true;
		try {
	        socket = new Socket("localhost", 6789);
        } catch (UnknownHostException e1) {
        	return false;
        } catch (IOException e1) {
        	LangUtils.sendError(sender, "Couldn't connect, please try again later.");
        	return false;
        }
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			
			listener = new InputStreamListener(socket) {
				
				@Override
				public void socketClosed(SocketException e) {
					isActive = false;
					LangUtils.sendError(conversationLeader, "Conversation ended!");
				}
				
				@Override
				public void objectRecieved(Object object, InputStreamListener listener) {
					LangUtils.sendLiveMessage(Bukkit.getConsoleSender(), (String) object);
				}
			};
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static void stopConversation() {
		if (isActive) {
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			isActive = false;
		} else {
			LangUtils.sendLiveMessage(conversationLeader, "Conversation is not running!");
		}
	}
	
	public static void sendPacket(ClientLivePacket packet) {
		try {
			output.writeObject(packet);
	        output.flush();
        } catch (IOException e) {
	        e.printStackTrace();
        }
	}
	
	public static void sendMessage(String msg) {
		sendPacket(new ClientLivePacket(ClientLivePacketType.MESSAGE, msg));
	}
	
	public static void sendQuestion(String question) {
		sendPacket(new ClientLivePacket(ClientLivePacketType.QUESTION, question));
	}
}
