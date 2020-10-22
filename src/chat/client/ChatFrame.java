package chat.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import chat.entity.ChatStatus;
import chat.entity.FontStyle;
import chat.entity.TransInfo;
import chat.io.IOStream;
import chat.util.FontSupport;

/**
 * 聊天主界面
 * 
 * @author 14036
 *
 */
public class ChatFrame extends JFrame {
	private static final long serialVersionUID = -1991882152903774788L;
	private static final Integer FRAME_WIDTH = 750;
	private static final Integer FRAME_HEIGHT = 600;
	public JTextPane acceptPane; // 消息接收框
	public JList listUser; // 在线用户列表
	private String sender; // 消息发出人
	private Socket socket; // 通道
	private JComboBox receiverBox; // 接收人选项
	private JComboBox fontFamilyCmb; // 字体
	private JLabel lblEmoji;
	private EmojiFrame emojiFrame; // 表情框
	// private int clickedTimes = 0; // 表情标签点击次数，用来作用于表情框的显现或消失

	public JLabel getLblEmoji() {
		return lblEmoji;
	}

	public ChatFrame() {
	}

	public ChatFrame(Socket socket, String sender) {
		this.socket = socket;
		this.sender = sender;

		this.setTitle("聊天室主界面 当前用户：" + sender);
		this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = screenSize.width;
		int height = screenSize.height;

		this.setLocation((width - FRAME_WIDTH) / 2, (height - FRAME_HEIGHT) / 2);
		JLabel frameBg = new JLabel(new ImageIcon("src/image/chatBg.png"));
		frameBg.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		this.add(frameBg);
		// 接收框
		acceptPane = new JTextPane();
		acceptPane.setOpaque(false);
		acceptPane.setFont(new Font("宋体", 0, 16));
		JScrollPane scroPaneOne = new JScrollPane(acceptPane);
		scroPaneOne.setBounds(15, 20, 500, 332);
		scroPaneOne.setOpaque(false);
		scroPaneOne.getViewport().setOpaque(false);
		acceptPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});
		frameBg.add(scroPaneOne);
		// 当前用户在线列表
		listUser = new JList();
		listUser.setFont(new Font("宋体", 0, 14));
		listUser.setVisibleRowCount(17);
		listUser.setFixedCellWidth(180);
		listUser.setFixedCellHeight(18);
		// 点选用户菜单
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem privateChat = new JMenuItem("私聊"); // 私聊菜单项--按钮
		privateChat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object receiverObj = listUser.getSelectedValue();
				if (receiverObj != null) {
					String receiver = new String((String) receiverObj);
					receiverBox.removeAllItems();
					receiverBox.addItem("ALL");
					receiverBox.addItem(receiver);
					receiverBox.setSelectedItem(receiver);
				}
			}
		});
		popupMenu.add(privateChat);
		// 用户列表事件监听
		listUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				if (e.isMetaDown()) { // 右键点击
					if (listUser.getSelectedIndex() >= 0) {
						// 弹出菜单
						popupMenu.show(listUser, e.getX(), e.getY());
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});

		JScrollPane scroListUser = new JScrollPane(listUser);
		scroListUser.setFont(new Font("宋体", 0, 14));
		scroListUser.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroListUser.setBounds(530, 17, 200, 507);
		frameBg.add(scroListUser);

		// 输入框
		JTextPane sendPane = new JTextPane();
		sendPane.setOpaque(false);
		sendPane.setFont(new Font("宋体", 0, 16));
		JScrollPane scroSendPane = new JScrollPane(sendPane);
		scroSendPane.setBounds(15, 400, 500, 122);
		scroSendPane.getViewport().setOpaque(false);
		sendPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});
		frameBg.add(scroSendPane);

		// TODO 表情，暂定
		emojiFrame = new EmojiFrame(this, sendPane);
		lblEmoji = new JLabel(new ImageIcon("src/image/emoji.png"));
		lblEmoji.setBounds(14, 363, 25, 25);
		lblEmoji.addMouseListener(new MouseAdapter() {
			// @Override
			// public void mouseClicked(MouseEvent e) {
			// super.mouseClicked(e);
			//// emojiFrame = new EmojiFrame(ChatFrame.this, sendPane);
			// emojiFrame.setVisible(true);
			// }
			@Override
			public void mouseReleased(MouseEvent e) {
				super.mousePressed(e);
				if (e.getButton() != 1) {
					return;
				}
				if (e.getX() >= 0 && e.getX() <= ((JComponent) e.getSource()).getWidth() && e.getY() >= 0
						&& e.getY() <= ((JComponent) e.getSource()).getHeight()) {
					// if (clickedTimes % 2 == 0) {
					// emojiFrame.setVisible(true);
					// clickedTimes++;
					// } else {
					// emojiFrame.setVisible(false);
					// clickedTimes++;
					// }
					// if (clickedTimes == 1000) {
					// clickedTimes = 0;
					// }
					emojiFrame.setVisible(!emojiFrame.isVisible());
					// System.out.println(clickedTimes);
				} else {
					// System.out.println(e.getSource().getClass().getName()+"x:"+e.getX()+",y:"+e.getY());
					return;
				}
			}
		});
		frameBg.add(lblEmoji);

		this.addComponentListener(new ComponentAdapter() {
			// 窗口移动时调用
			@Override
			public void componentMoved(ComponentEvent e) {
				super.componentMoved(e);
				emojiFrame.setVisible(false);
			}
		});
		// TODO 
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});
		// 窗口抖动
		JLabel lblShake = new JLabel(new ImageIcon("src/image/shake.png"));
		lblShake.setBounds(43, 363, 25, 25);
		lblShake.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				TransInfo tif = new TransInfo();
				tif.setStatusEnum(ChatStatus.DD);
				tif.setSender(sender);
				String receiver = "ALL";
				Object receiverObj = receiverBox.getSelectedItem();
				if (receiverObj != null) {
					receiver = String.valueOf(receiverObj);
				}
				tif.setReceiver(receiver);
				IOStream.writeMsg(socket, tif);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});
		frameBg.add(lblShake);

		// 设置字体选择
		JLabel lblfontChoose = new JLabel(new ImageIcon("src/image/font.png"));
		lblfontChoose.setBounds(44, 363, 80, 25);
		frameBg.add(lblfontChoose);
		// 字体下拉选项
		fontFamilyCmb = new JComboBox();
		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] str = graphicsEnvironment.getAvailableFontFamilyNames();
		for (String string : str) {
			fontFamilyCmb.addItem(string);
		}
		fontFamilyCmb.setSelectedItem("宋体");
		fontFamilyCmb.setBounds(104, 363, 150, 25);
		fontFamilyCmb.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
//				sendPane.setFont(new Font(fontFamilyCmb.getSelectedItem().toString(), 0, 16));
			}
		});
		fontFamilyCmb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				sendPane.setFont(new Font(e.getItem().toString(), 0, 16));
//				FontSupport.setFont(sendPane, e.getItem().toString());
			}
		});
		frameBg.add(fontFamilyCmb);

		// 聊天对象选项
		JLabel receiverLabel = new JLabel("聊天对象");
		receiverLabel.setBounds(304, 363, 80, 25);
		frameBg.add(receiverLabel);
		// 下拉选择框
		receiverBox = new JComboBox();
		receiverBox.setSelectedItem("ALL");
		receiverBox.addItem("ALL");
		receiverBox.setBounds(374, 363, 150, 25);
		receiverBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});
		frameBg.add(receiverBox);

		// 发送按钮
		JButton sendBtn = new JButton("发 送");
		sendBtn.setBounds(15, 533, 125, 25);
		sendBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TransInfo tif = new TransInfo();
				// 包装了所有文字对应的属性
				List<FontStyle> fontStyles = FontSupport.fontEncode(sendPane);
				tif.setContent(fontStyles);
				// 发送人
				tif.setSender(sender);
				String receiver = "ALL";
				// 获取到当前要发送给谁
				Object receiverObj = receiverBox.getSelectedItem();
				if (receiverObj != null) {
					receiver = String.valueOf(receiverObj);
				}
				// 接收人
				tif.setReceiver(receiver);
				tif.setStatusEnum(ChatStatus.CHAT);
				IOStream.writeMsg(socket, tif);
				sendPane.setText("");
			}
		});
		sendBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				if (e.getSource() != lblEmoji) {
					emojiFrame.setVisible(false);
				}
			}
		});
		frameBg.add(sendBtn);

		this.addWindowListener(new WindowAdapter() {
			// 当前聊天窗口关闭
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.out.println(sender + "窗口关闭");
				TransInfo tif = new TransInfo();
				tif.setStatusEnum(ChatStatus.QUIT);
				tif.setUsername(sender);
				tif.setNotice(sender + "离开聊天室.");
				IOStream.writeMsg(socket, tif);
			}

			// 窗口最小化时调用
			@Override
			public void windowIconified(WindowEvent e) {
				super.windowIconified(e);
				emojiFrame.setVisible(false);
			}
		});

		this.setVisible(true);
	}
}