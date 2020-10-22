package chat.entity;

import java.awt.Color;
import java.io.Serializable;

public class FontStyle implements Serializable {
	private static final long serialVersionUID = 5579466148010208361L;
	private String content; // ��������
	private String fontFamily; // ����
	private int fontSize; // �����С
	private Color fontColor; // ������ɫ
	private int fontStyle; // ������ʽ
	private String emojiPath; // ͼƬ·��

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getEmojiPath() {
		return emojiPath;
	}

	public void setEmojiPath(String emojiPath) {
		this.emojiPath = emojiPath;
	}

}
