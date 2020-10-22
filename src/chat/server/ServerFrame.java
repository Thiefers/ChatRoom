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
	// �û��б�
	public OnlineUserPanel onlineUserPanel;
	// ����������ѡ�
	public ServerInfoPanel serverInfoPanel;
	
	public ServerFrame() {
		this.setTitle("�������������");
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setLocation((width-FRAME_WIDTH)/2,(height - FRAME_HEIGHT)/2);
		
		// ѡ�
		JTabbedPane tabServerPane = new JTabbedPane(JTabbedPane.TOP);
		tabServerPane.setFont(new Font("����", 0, 18));
		
		// ������ѡ�
		serverInfoPanel = new ServerInfoPanel();
		tabServerPane.add("��������Ϣ", serverInfoPanel.getServerInfoPanel());
		// �����û�ѡ�
		onlineUserPanel = new OnlineUserPanel();
		tabServerPane.add("�û������б�" , onlineUserPanel.getUserPanel());
		
		this.add(tabServerPane);
		this.setVisible(true);
	}
	
}