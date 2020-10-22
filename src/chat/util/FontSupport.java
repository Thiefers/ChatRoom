package chat.util;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.AbstractDocument.LeafElement;

import chat.entity.FontStyle;

public class FontSupport {

	/**
	 * 包装字体
	 * 
	 * @param txtSend
	 *            作用域文本框
	 * @param fontFamily
	 *            字体类型
	 */
	public static void setFont(JTextPane txtSend, String fontFamily) {
		Document document = txtSend.getDocument();
		try {
			StyleContext sc = StyleContext.getDefaultStyleContext();
			Font font = new Font(fontFamily, 0, 16);
			AttributeSet aSet = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Family, font.getFamily());
			aSet = sc.addAttribute(aSet, StyleConstants.FontSize, 16);

			int start = txtSend.getSelectionStart();
			int end = txtSend.getSelectionEnd();
			String str = document.getText(start, end - start);
			document.remove(start, end - start);
			document.insertString(start, str, aSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 给某个text面板添加文字
	 * 
	 * @param textPane
	 * @param content
	 * @return
	 */
	public static Document contendAppend(JTextPane textPane, String content) {
		Document document = textPane.getDocument();
		StyleContext sContext = StyleContext.getDefaultStyleContext();
		AttributeSet aSet = sContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
		try {
			document.insertString(document.getLength(), content, aSet);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return document;
	}

	// 样式库容器
	static StyleContext sContext = null;

	/**
	 * 内容解码
	 * 
	 * @param textPane
	 * @param fontList
	 * @param sender
	 * @param receiver
	 */
	public static void fontDecode(JTextPane textPane, List<FontStyle> fontList, String sender, String receiver,
			int type) {
		// type = 0-->服务端
		// type = 1-->客户端
		Document document = contendAppend(textPane, "\n 用户" + sender + "对" + receiver + "说:");
		sContext = StyleContext.getDefaultStyleContext();
		for (FontStyle zi : fontList) {
			if (zi != null) {
				if (zi.getContent() != null) {
					Font font = new Font(zi.getFontFamily(), 0, 16);
					AttributeSet aSet = sContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Family,
							font.getFamily());
					aSet = sContext.addAttribute(aSet, StyleConstants.FontSize, zi.getFontSize());
					try {
						document.insertString(document.getLength(), zi.getContent(), aSet);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				} else {
					// 解析图片
					textPane.setCaretPosition(document.getLength());
					if (type == 0) {
						// 服务器只记录图片源地址，以便日志记录
						try {
							document.insertString(document.getLength(), zi.getEmojiPath(),
									sContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Family, "宋体"));
						} catch (BadLocationException e) {
							e.printStackTrace();
						}
					} else {
						textPane.insertIcon(new ImageIcon(zi.getEmojiPath()));
					}
				}
			}
		}
	}

	/**
	 * 将发送框的内容解析到实体类中
	 * 
	 * @param textPane
	 * @return
	 */
	public static List<FontStyle> fontEncode(JTextPane textPane) {
		Document document = textPane.getDocument();
		List<FontStyle> list = new ArrayList<FontStyle>();
		for (int i = 0; i < document.getLength(); i++) {
			try {
				StyledDocument sd = textPane.getStyledDocument();
				FontStyle font = new FontStyle();
				Element e = sd.getCharacterElement(i);
				if (e instanceof LeafElement) {
					// 匹配内容文字
					if (e.getName().equals("content")) {
						AttributeSet aSet = e.getAttributes();
						font.setContent(sd.getText(i, 1));
						font.setFontFamily(aSet.getAttribute(StyleConstants.Family).toString());
						font.setFontSize((Integer) aSet.getAttribute(StyleConstants.FontSize));
						font.setFontStyle((Integer) aSet.getAttribute(StyleConstants.FontSize));
						font.setFontColor((Color) aSet.getAttribute(StyleConstants.Foreground));
						if (StyleConstants.isBold(aSet)) {
							font.setFontStyle(Font.BOLD);
						} else if (StyleConstants.isItalic(aSet)) {
							font.setFontStyle(Font.ITALIC);
						} else {
							font.setFontStyle(Font.PLAIN);
						}
					} else if (e.getName().equals("icon")) {
						// 设置图片路径
						font.setEmojiPath(e.getAttributes().getAttribute(StyleConstants.IconAttribute).toString());
					}
				}
				list.add(font);
			} catch (Exception e) {

			}
		}
		return list;
	}
}
