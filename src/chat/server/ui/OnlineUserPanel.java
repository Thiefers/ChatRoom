package chat.server.ui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class OnlineUserPanel {

	private JList listUser;

	public JList getListUser() {
		return listUser;
	}

	public JLabel getUserPanel() {
		JPanel userPane = new JPanel();
		userPane.setLayout(null);
		userPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(""),
				BorderFactory.createEmptyBorder(1, 1, 1, 1)));
		userPane.setBounds(50,5,300,400);
		userPane.setOpaque(false);
		
		JLabel lblUser = new JLabel("[在线用户列表]");
		lblUser.setFont(new Font("宋体", Font.BOLD, 16));
		lblUser.setBounds(50, 10, 200, 30);
		userPane.add(lblUser);
		
		listUser = new JList();
		listUser.setFont(new Font("宋体", 0, 14));
		listUser.setVisibleRowCount(17);
		listUser.setFixedCellWidth(180);
		listUser.setFixedCellHeight(18);
		listUser.setOpaque(false);

		JScrollPane scroUser = new JScrollPane(listUser);
		scroUser.setFont(new Font("宋体", 0, 14));
		scroUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroUser.setBounds(50, 35, 200, 360);
		scroUser.setOpaque(false);
//		#BBBBBB
//		scroUser.getViewport().setBackground(new Color(187,187,187));
//		scroUser.getViewport().setOpaque(false);
		userPane.add(scroUser);
		
		// 背景
		JLabel lblBg = new JLabel(new ImageIcon("src/image/beijing4.jpg"));
		lblBg.setBounds(0,0,300,300);
		lblBg.add(userPane);
		
		return lblBg;
	}

}
