package chat.entity;

import java.io.Serializable;
import java.util.List;

public class TransInfo implements Serializable {

	private static final long serialVersionUID = -123238207743968368L;
	private String username;
	private String password;
	// ϵͳ��Ϣ
	private String notice;
	// ��¼�ɹ���־
	private Boolean loginSuccessFlag = false;
	// ��Ϣ����ö��
	private ChatStatus statusEnum;
	// ���ߵ��û��б�
	private String[] userOnlineArray;
	// ������
	private String sender;
	// ������
	private String receiver;
	// ������Ϣ
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
