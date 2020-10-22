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

	// 登录
	private void login(TransInfo tif) {
		if (tif.getLoginSuccessFlag()) {
			String username = tif.getUsername();
			chatFrame = new ChatFrame(socket, username);
			loginFrame.dispose();
		} else {
			System.out.println("登录失败");
		}
	}

	// 聊天
	private void chat(TransInfo tif) {
		String sender = tif.getSender();
		String receiver = tif.getReceiver();
		if (receiver != null && "ALL".equals(receiver)) {
			receiver = "所有人";
		}
		List<FontStyle> contents = tif.getContent();
		// 文字解析类进行解析处理
		FontSupport.fontDecode(chatFrame.acceptPane, contents, sender, receiver, 1);
	}

	// 通知
	private void notice(TransInfo tif) {
		FontSupport.contendAppend(chatFrame.acceptPane, "\n" + tif.getNotice());
	}

	// 在线用户
	private void onlineUser(TransInfo tif) {
		String[] userOnlineArr = tif.getUserOnlineArray();
		chatFrame.listUser.setListData(userOnlineArr);
	}

	// 窗口抖动
	private void dd(TransInfo tif) {
		ShakeWindow sw = new ShakeWindow(chatFrame);
		FontSupport.contendAppend(chatFrame.acceptPane, "\n" + tif.getSender() + "发送了一个窗口抖动......");
		new Thread(sw).start();
	}

}