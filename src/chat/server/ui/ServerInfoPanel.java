package chat.server.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import chat.constants.Constants;
import chat.server.ServerFrame;

public class ServerInfoPanel {

	// 服务器名称
	public JTextField txtServerName;
	// 服务器IP
	public JTextField txtIP;
	// 在线人数
	public JTextField txtNumber;
	// 日志区
	public JTextPane txtLogPane;

	/**
	 * 
	 * @return 第一个选项卡的全部信息，包括日志区域
	 */
	public JLabel getServerInfoPanel() {
		JPanel serverPane = new JPanel();
		serverPane.setOpaque(false);
		serverPane.setLayout(null);
		serverPane.setBounds(0, 0, ServerFrame.FRAME_WIDTH, ServerFrame.FRAME_HEIGHT);

		JLabel lblLog = new JLabel("[服务器在线日志]");
		lblLog.setFont(new Font("宋体", Font.BOLD, 16));
		lblLog.setBounds(130, 5, 140, 30);
		serverPane.add(lblLog);

		// 日志区域
		txtLogPane = new JTextPane();
		txtLogPane.setOpaque(false);
		txtLogPane.setFont(new Font("宋体", 0, 13));

		JScrollPane logScroPane = new JScrollPane(txtLogPane);
		logScroPane.setBounds(130, 35, 340, 360);
		logScroPane.setOpaque(false);
		logScroPane.getViewport().setOpaque(false);
		serverPane.add(logScroPane);

		serverPane.add(stopBtn());
		serverPane.add(saveLogBtn());
		serverPane.add(getServerParam());

		// 背景
		JLabel lblBg = new JLabel(new ImageIcon("src/image/beijing4.jpg"));
		lblBg.setBounds(0, 0, 300, 300);
		lblBg.add(serverPane);

		return lblBg;
	}

	/**
	 * 
	 * @return 服务器面板参数
	 */
	private JPanel getServerParam() {
		// 服务器参数信息，无日志区
		JPanel serverParamPane = new JPanel();
		serverParamPane.setOpaque(false);
		serverParamPane.setBounds(5, 5, 100, 390);
		serverParamPane.setFont(new Font("宋体", 0, 14));
		serverParamPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		JLabel lblNumber = new JLabel("当前在线人数");
		lblNumber.setFont(new Font("宋体", Font.BOLD, 14));
		serverParamPane.add(lblNumber);

		txtNumber = new JTextField("0人", 13);
		txtNumber.setFont(new Font("宋体", 0, 14));
		txtNumber.setEditable(false);
		serverParamPane.add(txtNumber);

		JLabel lblServerName = new JLabel("服务器名称");
		lblServerName.setFont(new Font("宋体", Font.BOLD, 14));
		serverParamPane.add(lblServerName);

		txtServerName = new JTextField(13);
		txtServerName.setFont(new Font("宋体", 0, 14));
		txtServerName.setEditable(false);
		serverParamPane.add(txtServerName);

		JLabel lblIP = new JLabel("服务器IP");
		lblIP.setFont(new Font("宋体", Font.BOLD, 14));
		serverParamPane.add(lblIP);

		txtIP = new JTextField(13);
		txtIP.setFont(new Font("宋体", 0, 14));
		txtIP.setEditable(false);
		serverParamPane.add(txtIP);

		JLabel lblPort = new JLabel("服务器端口");
		lblPort.setFont(new Font("宋体", Font.BOLD, 14));
		serverParamPane.add(lblPort);

		JTextField txtPort = new JTextField(Constants.SERVER_PORT + "", 13);
		txtPort.setFont(new Font("宋体", 0, 14));
		txtPort.setEditable(false);
		serverParamPane.add(txtPort);

		return serverParamPane;
	}

	/**
	 * 
	 * @return 按钮--保存日志
	 */
	private JButton saveLogBtn() {
		JButton saveLogBtn = new JButton("保存日志");
		saveLogBtn.setFont(new Font("宋体", 0, 14));
		saveLogBtn.setBounds(320, 400, 110, 30);
		saveLogBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtLogPane.getText().trim().length() != 0) {
					String curLog = txtLogPane.getText();
					BufferedWriter bw = null;
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String dateStr = sdf.format(date);
					try {
						bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/log.txt", true)));
						bw.write("记录于==>" + dateStr);
						bw.newLine();
						bw.write(curLog);
						bw.newLine();
						bw.flush();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						if (bw != null) {
							try {
								bw.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});
		return saveLogBtn;
	}

	/**
	 * 
	 * @return 按钮--关闭服务器
	 */
	private JButton stopBtn() {
		JButton stopBtn = new JButton("关闭服务器");
		stopBtn.setFont(new Font("宋体", 0, 14));
		stopBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		stopBtn.setBounds(200, 400, 110, 30);
		return stopBtn;
	}

}
