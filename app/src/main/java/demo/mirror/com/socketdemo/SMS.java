package demo.mirror.com.socketdemo;

/**
 * Created by zhangzhuang on 17/10/25.
 */

public class SMS {
    /**
     *编码格式
     */
    private int format;
    /**
     * 服务端口号
     */
    private int serverPort;
    /**
     *时间
     */
    private long time;
    /**
     * 发送短信手机号
     */
    private String sendPhone;
    /**
     * 接收短信手机号
     */
    private String receivePhone;
    /**
     * 内容大小
     */
    private int messageSize;
    /**
     * 消息内容
     */
    private String message;
    /**
     * 客户端端口号
     */
    private int clientPort;

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }
}
