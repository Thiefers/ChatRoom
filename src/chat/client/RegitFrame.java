package chat.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegitFrame extends JFrame {

	private static final Integer FRAME_WIDTH = 400;
	private static final Integer FRAME_HEIGHT = 300;

	public RegitFrame() {
		this.setTitle("注册");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

		// 获取屏幕信息并定位
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;
		this.setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);

		this.setLayout(null);
		JLabel regitBg = new JLabel(new ImageIcon("src/image/loginBg2.jpg"));
		regitBg.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.add(regitBg);

		// 创建标签及相应的文本框
		JLabel lblUid = new JLabel("账号：");
		lblUid.setBounds(80, 50, 120, 30);
		lblUid.setFont(new Font("宋体", 0, 16));
		lblUid.setForeground(Color.BLACK);
		regitBg.add(lblUid);
		JTextField txtUid = new JTextField();
		txtUid.setBounds(130, 50, 160, 25);
		this.add(txtUid);

		JLabel lblPwd = new JLabel("密码：");
		lblPwd.setBounds(80, 90, 120, 30);
		lblPwd.setFont(new Font("宋体", 0, 16));
		lblPwd.setForeground(Color.BLACK);
		regitBg.add(lblPwd);
		JPasswordField txtPwd = new JPasswordField();
		txtPwd.setBounds(130, 90, 160, 25);
		this.add(txtPwd);

		JLabel lblRePwd = new JLabel("确认密码：");
		lblRePwd.setBounds(48, 130, 120, 30);
		lblRePwd.setFont(new Font("宋体", 0, 16));
		lblRePwd.setForeground(Color.BLACK);
		regitBg.add(lblRePwd);
		JPasswordField txtRePwd = new JPasswordField();
		txtRePwd.setBounds(130, 130, 160, 25);
		this.add(txtRePwd);

		JButton regitBtn = new JButton("注册");
		regitBtn.setBounds(100, 180, 80, 25);
		regitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String uid = txtUid.getText();
				String pwd = txtPwd.getText();
				String rePwd = txtRePwd.getText();
				System.out.println(uid);
				BufferedReader br = null;
				BufferedWriter bw = null;
				boolean flag = true;
				boolean infoFlag = false;
				if ("".equals(uid) || "".equals(pwd) || "".equals(rePwd) || uid == null || pwd == null || rePwd == null) {
					infoFlag = false;
				}else if (!pwd.equals(rePwd)){
					infoFlag = false;
				}else {
					infoFlag = true;
				}
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream("src/user.txt")));
					String line = null;
					while ((line = br.readLine()) != null) {
						if (uid.equals(line.split("[|]")[0])) {
							flag = false;
							break;
						} else {
							flag = true;
						}
					}
					br.close();
					if (flag && infoFlag) {
						bw = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream("src/user.txt", true)));
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
						bw.append(uid + "|" + pwd + "|注册于:" + sdf.format(date));
						bw.newLine();
						bw.flush();
						new SuccessFrame();
						bw.close();
						txtUid.setText("");
						txtPwd.setText("");
						txtRePwd.setText("");
					} else {
						new FailedFrame();
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		regitBg.add(regitBtn);

		JButton backBtn = new JButton("返回");
		backBtn.setBounds(220, 180, 80, 25);
		backBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginFrame();
				RegitFrame.this.dispose();
			}
		});
		regitBg.add(backBtn);
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new RegitFrame();
	}
}

class SuccessFrame extends JFrame {
	public SuccessFrame() {
		JFrame jFrame = new JFrame("Tip");
		JPanel panel = new JPanel();
		jFrame.add(panel);
		JLabel tip = new JLabel("注册成功");
		panel.add(tip);
		JButton check = new JButton("确认");
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

class FailedFrame extends JFrame {
	public FailedFrame() {
		JFrame jFrame = new JFrame("Tip");
		JPanel panel = new JPanel();
		jFrame.add(panel);
		JLabel tip = new JLabel("注册失败");
		panel.add(tip);
		JButton check = new JButton("确认");
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
