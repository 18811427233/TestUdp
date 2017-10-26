package demo.mirror.com.socketdemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by zhangzhuang on 17/10/25.
 */
public class UdpServer implements Runnable {

    private int port;
    private Context context;
    private DatagramSocket datagramSocket;
    private boolean isCollect;

    public UdpServer(Context context,int port) {
        this.context = context;
        this.port = port;
        isCollect = true;
    }

    /**
     * 发送信息
     * @param ip
     * @param sendPort
     * @param data
     */
    public void Send(String ip, int sendPort , byte[] data) {

        try {

            InetAddress serverAddress = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, sendPort);
            datagramSocket.send(packet);


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){

        datagramSocket.close();
        isCollect = false;
    }

    @Override
    public void run() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    datagramSocket = new DatagramSocket(port);

                    byte data[] = new byte[1024];
                    DatagramPacket dpRcv = new DatagramPacket(data, data.length);
                    while (isCollect) {
                        try {
                            Log.i("SocketInfo", "UDP监听中");
                            datagramSocket.receive(dpRcv);

                            String string = new String(dpRcv.getData(), dpRcv.getOffset(), dpRcv.getLength());
                            Log.i("SocketInfo", "收到信息：" + string);
                            Log.e("========","=====000====");
                            Intent intent = new Intent();
                            intent.setAction("udpReceiver");
                            intent.putExtra("udpReceiver", dpRcv.getData());
                            context.sendBroadcast(intent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (SocketException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
