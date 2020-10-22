package chat.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import chat.constants.Constants;
import chat.entity.ServerInfoBean;

public class ChatServer {
	
	private ServerFrame serverFrame;
	
	public ChatServer() {
		try {
			// 建立服务器socket监听
			ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
			serverFrame = new ServerFrame();
			// 初始化服务器信息
			ServerInfoBean serverInfo = getServerIP();
			loadServerInfo(serverInfo);
			// 连接
			while(true) {
				Socket socket = serverSocket.accept();
				ServerHandler serverHandler = new ServerHandler(socket, serverFrame);
//				new Thread(serverHandler).start();
				serverHandler.start();
				System.out.println("服务端收到连接：" + socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置显示服务器信息
	 * @param serverInfo
	 */
	private void loadServerInfo(ServerInfoBean serverInfo) {
		serverFrame.serverInfoPanel.txtIP.setText(serverInfo.getIp());
		serverFrame.serverInfoPanel.txtServerName.setText(serverInfo.getHostName());
		serverFrame.serverInfoPanel.txtLogPane.setText("服务器已经启动...");
	}

	/**
	 * 获取服务器的主机名和ip地址
	 * @return 返回服务器IP、主机名和端口号等
	 */
	private ServerInfoBean getServerIP() {
		ServerInfoBean serverInfo = null;
		try {
			InetAddress serverAddress = InetAddress.getLocalHost();
//			System.out.println(serverAddress); // DESKTOP-K11I070/192.168.80.1
//			System.out.println(serverAddress.getHostAddress()); // 192.168.80.1
//			System.out.println(serverAddress.getHostName()); // DESKTOP-K11I070
			serverInfo = new ServerInfoBean();
			serverInfo.setIp(serverAddress.getHostAddress());
			serverInfo.setHostName(serverAddress.getHostName());
			serverInfo.setPort(Constants.SERVER_PORT);
		} catch (UnknownHostException e) {
			System.out.println("###Cound not get Server IP." + e);
		}
		return serverInfo;
	}
	
	public static void main(String[] args) {
		new ChatServer();
	}
}
