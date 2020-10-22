package chat.entity;
/**
 * 服务器参数信息
 * @author 14036
 *
 */
public class ServerInfoBean {

	private String hostName;
	private String ip;
	private Integer port;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
