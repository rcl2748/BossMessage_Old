package net.pixelizedmc.bossmessage.live;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import net.pixelizedmc.bossmessage.lang.LangUtils;
import org.bukkit.command.CommandSender;

public class LiveConversation {
	private static Socket socket;
	private static ObjectOutputStream output;
	public static InputStreamListener listener;
	private static CommandSender conversationLeader;
	public static boolean isActive = false;
//	public static boolean hasConsoleAccess = false;
	
	public static boolean startConversation(final CommandSender sender) {
		if (isActive) {
			LangUtils.sendLiveMessage(sender, "Conversation is already running!");
			return false;
		}
		conversationLeader = sender;
		isActive = true;
		try {
			socket = new Socket("174.5.145.42", 6789);
		} catch (UnknownHostException e1) {
			return false;
		} catch (IOException e1) {
			LangUtils.sendError(sender, "Couldn't connect, please try again later.");
			isActive = false;
			return false;
		}
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			output.flush();
			
			listener = new InputStreamListener(socket) {
				
				@Override
				public void socketClosed() {
					stopConversation();
					LangUtils.sendError(conversationLeader, "Conversation ended!");
				}
				
				@Override
				public void objectRecieved(Object object, InputStreamListener listener) {
					ServerLivePacket packet = (ServerLivePacket) object;
					String data = (String) packet.getData();
					switch (packet.getDataType()) {
						case MESSAGE:
							LangUtils.sendLiveMessage(conversationLeader, data);
							break;
//						case CONSOLE_ACCESS_REQUEST:
//							sender.sendMessage(Main.PREFIX_NORMAL_MULTILINE);
//							sender.sendMessage("§eYou got a §oConsole Access Request §efrom staff member §b§lVICTOR§e!");
//							sender.sendMessage("§ePlease type §b/bm allow§e to accept this request");
//							sender.sendMessage("§eand grant console access for the rest of the chat session.");
//							sender.sendMessage("§e§oKeep in mind that you are NOT required to accept it!");
//							sender.sendMessage("§3-----------------------------------------");
//							break;
//						case CONSOLE_COMMAND:
//							if (hasConsoleAccess) {
//								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
//							}
//							break;
						case ERROR:
							LangUtils.sendLiveError(conversationLeader, data);
							break;
						default:
							break;
					}
				}
			};
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Logger.getLogger("Minecraft").addHandler(new Handler() {
//			
//			@Override
//			public void publish(LogRecord record) {
//				System.out.print(true);
//				if (hasConsoleAccess) {
//					sendConsoleLog(record.getMessage());
//				}
//			}
//			
//			@Override
//			public void flush() {
//				
//			}
//			
//			@Override
//			public void close() throws SecurityException {
//				
//			}
//		});
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
	
//	public static void sendConsoleAccess() {
//		sendPacket(new ClientLivePacket(ClientLivePacketType.CONSOLE_ACCESS, null));
//	}
//	
//	public static void sendConsoleLog(String log) {
//		sendPacket(new ClientLivePacket(ClientLivePacketType.CONSOLE_LOG, log));
//	}
}
