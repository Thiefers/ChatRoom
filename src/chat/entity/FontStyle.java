package chat.entity;

import java.awt.Color;
import java.io.Serializable;

public class FontStyle implements Serializable {
	private static final long serialVersionUID = 5579466148010208361L;
	private String content; // 聊天内容
	private String fontFamily; // 字体
	private int fontSize; // 字体大小
	private Color fontColor; // 字体颜色
	private int fontStyle; // 字体样式
	private String emojiPath; // 图片路径

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
