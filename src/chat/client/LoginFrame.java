package chat.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import chat.entity.ChatStatus;
import chat.entity.TransInfo;
import chat.io.IOStream;

public class LoginFrame extends JFrame {

	// ��¼�����ȡ��߶�
	private static final Integer FRAME_WIDTH = 400;
	private static final Integer FRAME_HEIGHT = 300;
	
	public LoginFrame() {
		this.setTitle("��¼");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		
		// ��ȡ��Ļ��Ϣ����λ
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);
		
		this.setLayout(null);
		JLabel loginBg = new JLabel(new ImageIcon("src/image/loginBg2.jpg"));
		loginBg.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.add(loginBg);
		
		// ������ǩ����Ӧ���ı���
		JLabel lblUid = new JLabel("�˺ţ�");
		lblUid.setBounds(80, 50, 120, 30);
		lblUid.setFont(new Font("����", 0, 16));
		lblUid.setForeground(Color.BLACK);
		loginBg.add(lblUid);
		JTextField txtUid = new JTextField();
		txtUid.setBounds(130, 50, 160, 25);
		this.add(txtUid);
		
		JLabel lblPwd = new JLabel("���룺");
		lblPwd.setBounds(80, 90, 120, 30);
		lblPwd.setFont(new Font("����", 0, 16));
		lblPwd.setForeground(Color.BLACK);
		loginBg.add(lblPwd);
		JPasswordField txtPwd = new JPasswordField();
		txtPwd.setBounds(130, 90, 160, 25);
		this.add(txtPwd);
		
		// ��ť
		JButton enter = new JButton("��¼");
		enter.setBounds(100, 150, 80, 25);
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = txtUid.getText();
				String pwd = txtPwd.getText();
				BufferedReader br = null;
				boolean flag = false;
				if ("".equals(uid) || "".equals(pwd) || uid == null || pwd == null) {
					new FailedFrame2();
				} else {
					try {
						br = new BufferedReader(new InputStreamReader(new FileInputStream("src/user.txt")));
						String line = null;
						while ((line = br.readLine()) != null) {
							if ((uid.equals(line.split("[|]")[0]) && (pwd.equals(line.split("[|]")[1])))) {
								flag = true;
								break;
							} else {
								flag = false;
							}
						}
						br.close();
						if (!flag) {
							new FailedFrame2();
						}else {
							TransInfo tif = new TransInfo();
							tif.setUsername(uid);
							tif.setPassword(pwd);
							tif.setStatusEnum(ChatStatus.LOGIN);
//							tif.setLoginSuccessFlag(true);
							connectServer(tif);
						}
					} catch (FileNotFoundException e2) {
						e2.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		loginBg.add(enter);
		
		JButton regit = new JButton("ע��");
		regit.setBounds(200, 150, 80, 25);
		regit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new RegitFrame();
				LoginFrame.this.dispose();
			}
		});
		loginBg.add(regit);
		
		this.setVisible(true);
	}
	
	/**
	 * ���ӷ�����
	 * @param args
	 */
	public void connectServer(TransInfo transInfo) {
		try {
			System.out.println("����֮ǰ");
			Socket socket = new Socket("localhost", 8888);
			IOStream.writeMsg(socket, transInfo);
			ClientHandler clientHandler = new ClientHandler(socket, this);
			new Thread(clientHandler).start();
			System.out.println("�ͻ����߳�����֮��");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		new LoginFrame();
	}

}
class FailedFrame2 extends JFrame {
	public FailedFrame2() {
		JFrame jFrame = new JFrame("Tip");
		JPanel panel = new JPanel();
		jFrame.add(panel);
		JLabel tip = new JLabel("��¼ʧ��");
		panel.add(tip);
		JButton check = new JButton("ȷ��");
		panel.add(check);
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jFrame.dispose();
			}
		});

		jFrame.setResizable(false);
		jFrame.setSize(new Dimension(190, 66));
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
	}
}