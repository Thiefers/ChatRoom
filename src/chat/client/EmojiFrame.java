package chat.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class EmojiFrame extends JWindow {
	GridLayout gridLayout = new GridLayout(7, 15);
	JLabel[] lblEmojiArr = new JLabel[105]; // 存放表情
	int i = 0;
	ChatFrame owner;
	JTextPane sendPane;
	
	public EmojiFrame() {
	}
	
	public EmojiFrame(ChatFrame owner, JTextPane sendPane) {
		this.owner = owner;
		this.sendPane = sendPane;
		init();
		this.setAlwaysOnTop(true);
	}

	private void init() {
		this.setPreferredSize(new Dimension(28*15, 28*7));
		JPanel emojiPane = new JPanel();
		this.setContentPane(emojiPane);
		emojiPane.setOpaque(true);
		emojiPane.setLayout(gridLayout);
		emojiPane.setBackground(SystemColor.text);
		String fileName = "";
		for (i = 0; i < lblEmojiArr.length; i++) {
			fileName = "src/emoji/" + i + ".gif";
			lblEmojiArr[i] = new JLabel(new ImageIcon(fileName),SwingConstants.CENTER);
			lblEmojiArr[i].setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
			lblEmojiArr[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (e.getButton() == 1) {
						JLabel curLbl = (JLabel)e.getSource();
						Icon icon = curLbl.getIcon();
						sendPane.insertIcon(icon);
						curLbl.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225),1));
						EmojiFrame.this.dispose();
					}
				}
				@Override
				public void mouseEntered(MouseEvent e) {
					super.mouseEntered(e);
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));
				}
			});
			emojiPane.add(lblEmojiArr[i]);
		}
	}
	
	@Override
	public void setVisible(boolean show) {
		super.setVisible(show);
		if (show) {
			Point loc = owner.getLblEmoji().getLocationOnScreen();/* 控件相对于屏幕的位置 */
			setBounds(loc.x - getPreferredSize().width / 3, loc.y - getPreferredSize().height, getPreferredSize().width,
					getPreferredSize().height);
		}
	}
}
