package demo.mirror.com.socketdemo;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhangzhuang on 17/10/25.
 */

public class ChangeUtil {
    /**
     * 将byte[]消息转化为SMS对象
     *
     * @param smsBytes
     * @return
     */
    public static SMS getSms(byte[] smsBytes) {

        int start = 0;
        int end = 0;

        SMS sms = new SMS();

		/* 处理编码格式 */
        start = 0;
        end = 4;
        byte[] formatBytes = new byte[4];
        for (int i = 0; i < (end - start); i++) {

            formatBytes[i] = smsBytes[start + i];
        }
        sms.setFormat(bytesToInt(formatBytes));

		/* 目的端端口号 */
        start = end;
        end = end + 4;
        byte[] serverPortBytes = new byte[4];
        for (int i = 0; i < (end - start); i++) {

            serverPortBytes[i] = smsBytes[start + i];
        }
        sms.setServerPort(bytesToInt(serverPortBytes));

		/* 时间戳 */
        start = end;
        end = end + 8;
        byte[] timeBytes = new byte[8];
        for (int i = 0; i < (end - start); i++) {

            timeBytes[i] = smsBytes[start + i];
        }
        sms.setTime(bytesToLong(timeBytes));

		/* 接收短消息的手机号 */
        start = end;
        end = end + 11;
        byte[] getPhoneBytes = new byte[11];
        for (int i = 0; i < (end - start); i++) {

            getPhoneBytes[i] = smsBytes[start + i];
        }
        sms.setReceivePhone(bytesToString(getPhoneBytes));

		/* 发送短消息的手机号 */
        start = end;
        end = end + 11;
        byte[] sendPhoneBytes = new byte[11];
        for (int i = 0; i < (end - start); i++) {

            sendPhoneBytes[i] = smsBytes[start + i];
        }
        sms.setSendPhone(bytesToString(sendPhoneBytes));

		/* 消息长度 */
        start = end;
        end = end + 4;
        byte[] messSizeBytes = new byte[4];
        for (int i = 0; i < (end - start); i++) {

            messSizeBytes[i] = smsBytes[start + i];
        }
        sms.setMessageSize(bytesToInt(messSizeBytes));

		/* 消息 */
        start = end;
        end = end + sms.getMessageSize();
        byte[] messageBytes = new byte[sms.getMessageSize()];
        for (int i = 0; i < (end - start); i++) {

            messageBytes[i] = smsBytes[start + i];
        }
        sms.setMessage(bytesToString(messageBytes));

		/* 发送端端口号 */
        start = end;
        end = end + 4;
        byte[] clientPortBytes = new byte[4];
        for (int i = 0; i < (end - start); i++) {

            clientPortBytes[i] = smsBytes[start + i];
        }
        sms.setClientPort(bytesToInt(clientPortBytes));

        return sms;
    }

    /**
     * 将SMS对象转化为 byte[]消息
     *
     * @param sms
     * @return
     */
    public static byte[] setSms(SMS sms) {

        byte[] smsBytes = new byte[2048];

        int start = 0;
        int end = 0;

		/* 处理编码格式 */
        start = 0;
        end = 4;
        byte[] formatBytes = intToBytes(sms.getFormat());
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = formatBytes[i];
        }

		/* 目的端端口号 */
        start = end;
        end = end + 4;

        byte[] serverPortBytes = intToBytes(sms.getServerPort());
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = serverPortBytes[i];
        }

		/* 时间戳 */
        start = end;
        end = end + 8;
        byte[] timeBytes = longToBytes(sms.getTime());
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = timeBytes[i];
        }

		/* 接收短消息的手机号 */
        start = end;
        end = end + 11;
        byte[] getPhoneBytes = sms.getReceivePhone().getBytes();
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = getPhoneBytes[i];
        }

		/* 发送短消息的手机号 */
        start = end;
        end = end + 11;
        byte[] sendPhoneBytes = sms.getSendPhone().getBytes();
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = sendPhoneBytes[i];
        }

		/* 消息长度 */
        start = end;
        end = end + 4;
        byte[] messSizeBytes = intToBytes(sms.getMessageSize());
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = messSizeBytes[i];
        }

		/* 消息 */
        start = end;
        end = end + sms.getMessageSize();
        byte[] messageBytes = sms.getMessage().getBytes();
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = messageBytes[i];
        }

		/* 发送端端口号 */
        start = end;
        end = end + 4;
        byte[] clientPortBytes = intToBytes(sms.getClientPort());
        for (int i = 0; i < (end - start); i++) {

            smsBytes[start + i] = clientPortBytes[i];
        }

        return smsBytes;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src
     *            byte数组
     * @param offset
     *            从数组的第offset位开始
     * @return int数值
     */
    private static int bytesToInt(byte[] src) {
        int value;
        value = (int) ((src[0] & 0xFF) | ((src[1] & 0xFF) << 8) | ((src[2] & 0xFF) << 16) | ((src[3] & 0xFF) << 24));
        return value;
    }

    /**
     * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
     *
     * @param value
     *            要转换的int值
     * @return byte数组
     */
    private static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    /**
     * byte[] 转 long
     *
     * @param b
     * @return
     */
    private static long bytesToLong(byte[] b) {

        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;

        return s;
    }

    /**
     * long to byte[]
     *
     * @param number
     * @return
     */
    private static byte[] longToBytes(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
            temp = temp >> 8;// 向右移8位
        }
        return b;
    }

    /**
     * bytes 转 String
     *
     * @param src
     * @return
     */
    private static String bytesToString(byte[] src) {

        String message = "";

        try {
            message = new String(src, 0, src.length, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return message;
    }

}
