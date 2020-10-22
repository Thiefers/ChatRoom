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

	// ����������
	public JTextField txtServerName;
	// ������IP
	public JTextField txtIP;
	// ��������
	public JTextField txtNumber;
	// ��־��
	public JTextPane txtLogPane;

	/**
	 * 
	 * @return ��һ��ѡ���ȫ����Ϣ��������־����
	 */
	public JLabel getServerInfoPanel() {
		JPanel serverPane = new JPanel();
		serverPane.setOpaque(false);
		serverPane.setLayout(null);
		serverPane.setBounds(0, 0, ServerFrame.FRAME_WIDTH, ServerFrame.FRAME_HEIGHT);

		JLabel lblLog = new JLabel("[������������־]");
		lblLog.setFont(new Font("����", Font.BOLD, 16));
		lblLog.setBounds(130, 5, 140, 30);
		serverPane.add(lblLog);

		// ��־����
		txtLogPane = new JTextPane();
		txtLogPane.setOpaque(false);
		txtLogPane.setFont(new Font("����", 0, 13));

		JScrollPane logScroPane = new JScrollPane(txtLogPane);
		logScroPane.setBounds(130, 35, 340, 360);
		logScroPane.setOpaque(false);
		logScroPane.getViewport().setOpaque(false);
		serverPane.add(logScroPane);

		serverPane.add(stopBtn());
		serverPane.add(saveLogBtn());
		serverPane.add(getServerParam());

		// ����
		JLabel lblBg = new JLabel(new ImageIcon("src/image/beijing4.jpg"));
		lblBg.setBounds(0, 0, 300, 300);
		lblBg.add(serverPane);

		return lblBg;
	}

	/**
	 * 
	 * @return ������������
	 */
	private JPanel getServerParam() {
		// ������������Ϣ������־��
		JPanel serverParamPane = new JPanel();
		serverParamPane.setOpaque(false);
		serverParamPane.setBounds(5, 5, 100, 390);
		serverParamPane.setFont(new Font("����", 0, 14));
		serverParamPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));

		JLabel lblNumber = new JLabel("��ǰ��������");
		lblNumber.setFont(new Font("����", Font.BOLD, 14));
		serverParamPane.add(lblNumber);

		txtNumber = new JTextField("0��", 13);
		txtNumber.setFont(new Font("����", 0, 14));
		txtNumber.setEditable(false);
		serverParamPane.add(txtNumber);

		JLabel lblServerName = new JLabel("����������");
		lblServerName.setFont(new Font("����", Font.BOLD, 14));
		serverParamPane.add(lblServerName);

		txtServerName = new JTextField(13);
		txtServerName.setFont(new Font("����", 0, 14));
		txtServerName.setEditable(false);
		serverParamPane.add(txtServerName);

		JLabel lblIP = new JLabel("������IP");
		lblIP.setFont(new Font("����", Font.BOLD, 14));
		serverParamPane.add(lblIP);

		txtIP = new JTextField(13);
		txtIP.setFont(new Font("����", 0, 14));
		txtIP.setEditable(false);
		serverParamPane.add(txtIP);

		JLabel lblPort = new JLabel("�������˿�");
		lblPort.setFont(new Font("����", Font.BOLD, 14));
		serverParamPane.add(lblPort);

		JTextField txtPort = new JTextField(Constants.SERVER_PORT + "", 13);
		txtPort.setFont(new Font("����", 0, 14));
		txtPort.setEditable(false);
		serverParamPane.add(txtPort);

		return serverParamPane;
	}

	/**
	 * 
	 * @return ��ť--������־
	 */
	private JButton saveLogBtn() {
		JButton saveLogBtn = new JButton("������־");
		saveLogBtn.setFont(new Font("����", 0, 14));
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
						bw.write("��¼��==>" + dateStr);
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
	 * @return ��ť--�رշ�����
	 */
	private JButton stopBtn() {
		JButton stopBtn = new JButton("�رշ�����");
		stopBtn.setFont(new Font("����", 0, 14));
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
