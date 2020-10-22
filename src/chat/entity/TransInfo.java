package chat.entity;

import java.io.Serializable;
import java.util.List;

public class TransInfo implements Serializable {

	private static final long serialVersionUID = -123238207743968368L;
	private String username;
	private String password;
	// 系统消息
	private String notice;
	// 登录成功标志
	private Boolean loginSuccessFlag = false;
	// 消息类型枚举
	private ChatStatus statusEnum;
	// 在线的用户列表
	private String[] userOnlineArray;
	// 发送人
	private String sender;
	// 接收人
	private String receiver;
	// 聊天消息
	private List<FontStyle> content;

	public List<FontStyle> getContent() {
		return content;
	}

	public void setContent(List<FontStyle> content) {
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public Boolean getLoginSuccessFlag() {
		return loginSuccessFlag;
	}

	public void setLoginSuccessFlag(Boolean loginSuccessFlag) {
		this.loginSuccessFlag = loginSuccessFlag;
	}

	public ChatStatus getStatusEnum() {
		return statusEnum;
	}

	public void setStatusEnum(ChatStatus statusEnum) {
		this.statusEnum = statusEnum;
	}

	public String[] getUserOnlineArray() {
		return userOnlineArray;
	}

	public void setUserOnlineArray(String[] userOnlineArray) {
		this.userOnlineArray = userOnlineArray;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
}
