package chat.server;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import chat.server.ui.OnlineUserPanel;
import chat.server.ui.ServerInfoPanel;

public class ServerFrame extends JFrame{

	public static final Integer FRAME_WIDTH = 550;
	public static final Integer FRAME_HEIGHT = 500;
	// 用户列表
	public OnlineUserPanel onlineUserPanel;
	// 服务器参数选项卡
	public ServerInfoPanel serverInfoPanel;
	
	public ServerFrame() {
		this.setTitle("网络聊天服务器");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setLocation((width-FRAME_WIDTH)/2,(height - FRAME_HEIGHT)/2);
		
		// 选项卡
		JTabbedPane tabServerPane = new JTabbedPane(JTabbedPane.TOP);
		tabServerPane.setFont(new Font("宋体", 0, 18));
		
		// 服务器选项卡
		serverInfoPanel = new ServerInfoPanel();
		tabServerPane.add("服务器信息", serverInfoPanel.getServerInfoPanel());
		// 在线用户选项卡
		onlineUserPanel = new OnlineUserPanel();
		tabServerPane.add("用户在线列表" , onlineUserPanel.getUserPanel());
		
		this.add(tabServerPane);
		this.setVisible(true);
	}
	
}