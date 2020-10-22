package chat.client;

import java.net.Socket;
import java.util.List;

import chat.entity.ChatStatus;
import chat.entity.FontStyle;
import chat.entity.TransInfo;
import chat.io.IOStream;
import chat.util.FontSupport;

public class ClientHandler implements Runnable {

	Socket socket;
	LoginFrame loginFrame;
	ChatFrame chatFrame;

	public ClientHandler() {
	}

	public ClientHandler(Socket socket, LoginFrame loginFrame) {
		this.socket = socket;
		this.loginFrame = loginFrame;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Object object = IOStream.readMsg(socket);
				if (object instanceof TransInfo) {
					TransInfo tif = (TransInfo) object;
					if (tif.getStatusEnum() == ChatStatus.LOGIN) {
						login(tif);
					} else if (tif.getStatusEnum() == ChatStatus.CHAT) {
						chat(tif);
					} else if (tif.getStatusEnum() == ChatStatus.NOTICE) {
						notice(tif);
					} else if (tif.getStatusEnum() == ChatStatus.ULIST) {
						onlineUser(tif);
					} else if (tif.getStatusEnum() == ChatStatus.DD) {
						dd(tif);
					}
				}
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// ��¼
	private void login(TransInfo tif) {
		if (tif.getLoginSuccessFlag()) {
			String username = tif.getUsername();
			chatFrame = new ChatFrame(socket, username);
			loginFrame.dispose();
		} else {
			System.out.println("��¼ʧ��");
		}
	}

	// ����
	private void chat(TransInfo tif) {
		String sender = tif.getSender();
		String receiver = tif.getReceiver();
		if (receiver != null && "ALL".equals(receiver)) {
			receiver = "������";
		}
		List<FontStyle> contents = tif.getContent();
		// ���ֽ�������н�������
		FontSupport.fontDecode(chatFrame.acceptPane, contents, sender, receiver, 1);
	}

	// ֪ͨ
	private void notice(TransInfo tif) {
		FontSupport.contendAppend(chatFrame.acceptPane, "\n" + tif.getNotice());
	}

	// �����û�
	private void onlineUser(TransInfo tif) {
		String[] userOnlineArr = tif.getUserOnlineArray();
		chatFrame.listUser.setListData(userOnlineArr);
	}

	// ���ڶ���
	private void dd(TransInfo tif) {
		ShakeWindow sw = new ShakeWindow(chatFrame);
		FontSupport.contendAppend(chatFrame.acceptPane, "\n" + tif.getSender() + "������һ�����ڶ���......");
		new Thread(sw).start();
	}

}