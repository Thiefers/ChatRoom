package chat.server;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JTextPane;

import chat.entity.ChatStatus;
import chat.entity.FontStyle;
import chat.entity.TransInfo;
import chat.io.IOStream;
import chat.util.FontSupport;

public class ServerHandler extends Thread {

	private Socket socket;
	ServerFrame serverFrame;

	public ServerHandler(Socket socket, ServerFrame serverFrame) {
		this.socket = socket;
		this.serverFrame = serverFrame;
	}

	private static List<String> onlineUsers = new ArrayList<>();
	private static List<Socket> onlineSockets = new ArrayList<>();
	private static Map<String, Socket> userSocketMap = new HashMap<String, Socket>();

	@Override
	public void run() {
		while (true) {
			try {
				Object obj = IOStream.readMsg(socket);
				if (obj instanceof TransInfo) {
					TransInfo tif = (TransInfo) obj;
					if (tif.getStatusEnum() == ChatStatus.LOGIN) {
						loginHandler(tif);
					} else if (tif.getStatusEnum() == ChatStatus.CHAT) {
						chatHandler(tif);
					} else if (tif.getStatusEnum() == ChatStatus.DD) {
						doudong(tif);
					} else if (tif.getStatusEnum() == ChatStatus.QUIT) {
						loginOut(tif);
						Thread.sleep(1000);
						socket.close();
						this.interrupt();
						break;
					}
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理客户端的登录请求
	 * 
	 * @param tif
	 */
	private void loginHandler(TransInfo tif) {
		boolean flag = checkUserLogin(tif);
		tif.setLoginSuccessFlag(false);
		if (flag) {
			tif.setLoginSuccessFlag(true);
			tif.setStatusEnum(ChatStatus.LOGIN);
			IOStream.writeMsg(socket, tif);
			String username = tif.getUsername();

			onlineUsers.add(username);
			onlineSockets.add(socket);
			userSocketMap.put(username, socket);

			tif = new TransInfo();
			tif.setStatusEnum(ChatStatus.NOTICE);
			String notice = ">>> " + username + "上线。";
			tif.setNotice(notice);
			sendAll(tif);

			tif = new TransInfo();
			tif.setUserOnlineArray(onlineUsers.toArray(new String[onlineUsers.size()]));
			tif.setStatusEnum(ChatStatus.ULIST);
			sendAll(tif);

			flushOnlineUserList();
			log(notice);
		} else {
			IOStream.writeMsg(socket, tif);
			log(tif.getUsername() + "登录失败");
		}
	}

	/**
	 * 聊天处理
	 * @param tif
	 */
	private void chatHandler(TransInfo tif) {
		String receiver = tif.getReceiver();
		if ("ALL".equals(receiver)) {
			sendAll(tif);
			List<FontStyle> contents = tif.getContent();
			FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLogPane, contents, tif.getSender(), "所有人", 0);
		} else {
			send(tif);
			List<FontStyle> contents = tif.getContent();
			FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLogPane, contents, tif.getSender(), tif.getReceiver(), 0);
		}
	}

	/**
	 * 发送shake到客户端
	 * 
	 * @param tif
	 */
	private void doudong(TransInfo tif) {
		String receiver = tif.getReceiver();
		if ("ALL".equals(receiver)) {
			sendAll(tif);
			// 记录到服务端日志区域
			log(tif.getSender() + "给所有人发抖动");
		} else {
			send(tif);
			log(tif.getSender() + "给" + receiver + "发抖动");
		}
	}

	/**
	 * 用户退出，修改在线人数，刷新用户列表，通知
	 * 
	 * @param tif
	 */
	private void loginOut(TransInfo tif) {
		String username = tif.getUsername();
		// 将用户从集合中除去
		Iterator<String> userIter = onlineUsers.iterator();
		while (userIter.hasNext()) {
			if (username.equals(userIter.next())) {
				userIter.remove();
			}
		}
		// 从socket集合中除去
		Iterator<Socket> socketIter = onlineSockets.iterator();
		while (socketIter.hasNext()) {
			if (socket == socketIter.next()) {
				socketIter.remove();
			}
		}
		// 将user与socket的关系从Map中除去
		userSocketMap.remove(username);
		// 刷新服务器面板用户列表
		flushOnlineUserList();
		// 通知其他用户
		tif.setStatusEnum(ChatStatus.NOTICE);
		sendAll(tif);
		log(">>> " + username + "下线。");
		// 通知其他人刷新用户列表
		tif.setUserOnlineArray(onlineUsers.toArray(new String[onlineUsers.size()]));
		tif.setStatusEnum(ChatStatus.ULIST);
		sendAll(tif);
	}

	/**
	 * 群聊
	 * 
	 * @param tif
	 */
	private void sendAll(TransInfo tif) {
		for (int i = 0; i < onlineSockets.size(); i++) {
			Socket tempSocket = onlineSockets.get(i);
			IOStream.writeMsg(tempSocket, tif);
		}
	}

	/**
	 * 私聊
	 * 
	 * @param tif
	 */
	private void send(TransInfo tif) {
		String receiver = tif.getReceiver();
		String sender = tif.getSender();
		Socket receiverSocket = userSocketMap.get(receiver);
		IOStream.writeMsg(receiverSocket, tif);
		Socket senderSocket = userSocketMap.get(sender);
		IOStream.writeMsg(senderSocket, tif);
	}

	/**
	 * 刷新服务端在线用户列表
	 */
	private void flushOnlineUserList() {
		JList listUser = serverFrame.onlineUserPanel.getListUser();
		String[] userArr = onlineUsers.toArray(new String[onlineUsers.size()]);
		listUser.setListData(userArr);
		serverFrame.serverInfoPanel.txtNumber.setText(userArr.length + "");
	}

	/**
	 * 日志记录
	 * 
	 * @param log
	 */
	private void log(String log) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		JTextPane txtLog = serverFrame.serverInfoPanel.txtLogPane;
		String oldLog = txtLog.getText();
		String newLog = oldLog + "\n" + dateStr + " " + log;
		txtLog.setText(newLog);
		txtLog.setCaretPosition(newLog.length());
	}

	/**
	 * 登录许可
	 */
	public boolean checkUserLogin(TransInfo tif) {
		String username = tif.getUsername();
		String password = tif.getPassword();
		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new FileInputStream("src/user.txt"));
			String line = null;
			while ((line = dis.readLine()) != null) {
				if ((username + "|" + password).equals(line.split("[|]")[0] + "|" + line.split("[|]")[1])) {
					return true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
