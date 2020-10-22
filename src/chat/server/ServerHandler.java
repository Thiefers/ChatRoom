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
	 * ����ͻ��˵ĵ�¼����
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
			String notice = ">>> " + username + "���ߡ�";
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
			log(tif.getUsername() + "��¼ʧ��");
		}
	}

	/**
	 * ���촦��
	 * @param tif
	 */
	private void chatHandler(TransInfo tif) {
		String receiver = tif.getReceiver();
		if ("ALL".equals(receiver)) {
			sendAll(tif);
			List<FontStyle> contents = tif.getContent();
			FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLogPane, contents, tif.getSender(), "������", 0);
		} else {
			send(tif);
			List<FontStyle> contents = tif.getContent();
			FontSupport.fontDecode(serverFrame.serverInfoPanel.txtLogPane, contents, tif.getSender(), tif.getReceiver(), 0);
		}
	}

	/**
	 * ����shake���ͻ���
	 * 
	 * @param tif
	 */
	private void doudong(TransInfo tif) {
		String receiver = tif.getReceiver();
		if ("ALL".equals(receiver)) {
			sendAll(tif);
			// ��¼���������־����
			log(tif.getSender() + "�������˷�����");
		} else {
			send(tif);
			log(tif.getSender() + "��" + receiver + "������");
		}
	}

	/**
	 * �û��˳����޸�����������ˢ���û��б�֪ͨ
	 * 
	 * @param tif
	 */
	private void loginOut(TransInfo tif) {
		String username = tif.getUsername();
		// ���û��Ӽ����г�ȥ
		Iterator<String> userIter = onlineUsers.iterator();
		while (userIter.hasNext()) {
			if (username.equals(userIter.next())) {
				userIter.remove();
			}
		}
		// ��socket�����г�ȥ
		Iterator<Socket> socketIter = onlineSockets.iterator();
		while (socketIter.hasNext()) {
			if (socket == socketIter.next()) {
				socketIter.remove();
			}
		}
		// ��user��socket�Ĺ�ϵ��Map�г�ȥ
		userSocketMap.remove(username);
		// ˢ�·���������û��б�
		flushOnlineUserList();
		// ֪ͨ�����û�
		tif.setStatusEnum(ChatStatus.NOTICE);
		sendAll(tif);
		log(">>> " + username + "���ߡ�");
		// ֪ͨ������ˢ���û��б�
		tif.setUserOnlineArray(onlineUsers.toArray(new String[onlineUsers.size()]));
		tif.setStatusEnum(ChatStatus.ULIST);
		sendAll(tif);
	}

	/**
	 * Ⱥ��
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
	 * ˽��
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
	 * ˢ�·���������û��б�
	 */
	private void flushOnlineUserList() {
		JList listUser = serverFrame.onlineUserPanel.getListUser();
		String[] userArr = onlineUsers.toArray(new String[onlineUsers.size()]);
		listUser.setListData(userArr);
		serverFrame.serverInfoPanel.txtNumber.setText(userArr.length + "");
	}

	/**
	 * ��־��¼
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
	 * ��¼���
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
