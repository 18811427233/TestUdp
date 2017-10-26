package demo.mirror.com.socketdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**
 * socket连接
 */
public class MainActivity extends AppCompatActivity {

    /*服务区*/
    private TextView tvServerIp;
    private EditText etServerPort;
    private Button btnServerOperate;
    private TextView tvServerShow;

    /*客户区*/
    private EditText etClientIp;
    private EditText etClientPort;

    private EditText etClientSend;
    private EditText etSendPort;

    private EditText etClientGet;
    private EditText etGetPort;

    private EditText etFormat;
    private EditText etClientContent;
    private Button btnClientPost;
    private Button btnClear;
    private TextView tvClientShow;

    private MyHandler myHandler = new MyHandler(this);
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private static UdpServer udpServer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BindReceiver();
        initView();
        initData();
        initEvent();
    }

    public void initView() {

        /*服务区*/
        tvServerIp = (TextView) findViewById(R.id.tv_server_ip);
        etServerPort = (EditText) findViewById(R.id.et_server_port);
        btnServerOperate = (Button) findViewById(R.id.btn_server_operate);
        tvServerShow = (TextView) findViewById(R.id.tv_server_show);

        /*客户区*/
        etClientIp = (EditText) findViewById(R.id.et_client_ip);
        etClientPort = (EditText) findViewById(R.id.et_client_port);

        etClientSend = (EditText) findViewById(R.id.et_client_send);
        etSendPort = (EditText) findViewById(R.id.et_send_port);

        etClientGet = (EditText) findViewById(R.id.et_client_get);
        etGetPort = (EditText) findViewById(R.id.et_get_port);
        etFormat = (EditText) findViewById(R.id.et_format);

        etClientContent = (EditText) findViewById(R.id.et_client_content);
        btnClientPost = (Button) findViewById(R.id.btn_client_post);
        btnClear = (Button) findViewById(R.id.btn_clear);
        tvClientShow = (TextView) findViewById(R.id.tv_client_show);
    }

    public void initData() {

        if (udpServer != null) {
            btnServerOperate.setText("断开");
        }

        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);

        tvServerIp.setText("ip地址：" + ip);

        String serverPort = SharedPrefsUtil.getValue(this, "ServerPort", "");

        String clientIp = SharedPrefsUtil.getValue(this, "ClientIp", "");
        String clientPort = SharedPrefsUtil.getValue(this, "ClientPort", "");
        String clientSendNumber = SharedPrefsUtil.getValue(this, "ClientSendNumber", "");
        String clientGetNumber = SharedPrefsUtil.getValue(this, "ClientGetNumber", "");
        String clientSendPort = SharedPrefsUtil.getValue(this, "ClientSendPort", "");
        String clientGetPort = SharedPrefsUtil.getValue(this, "ClientGetPort", "");
        String clientFormat = SharedPrefsUtil.getValue(this, "ClientFormat", "");
        String clientContent = SharedPrefsUtil.getValue(this, "ClientContent", "");
        if (!serverPort.isEmpty()) {
            etServerPort.setText(serverPort);
        }

        if (!clientIp.isEmpty()) {
            etClientIp.setText(clientIp);
        }
        if (!clientPort.isEmpty()) {
            etClientPort.setText(clientPort);
        }
        if (!clientSendNumber.isEmpty()) {
            etClientSend.setText(clientSendNumber);
        }
        if (!clientGetNumber.isEmpty()) {
            etClientGet.setText(clientGetNumber);
        }
        if (!clientSendPort.isEmpty()) {
            etSendPort.setText(clientSendPort);
        }
        if (!clientGetPort.isEmpty()) {
            etGetPort.setText(clientGetPort);
        }
        if (!clientFormat.isEmpty()) {
            etFormat.setText(clientFormat);
        }
        if (!clientContent.isEmpty()) {
            etClientContent.setText(clientContent);
        }

    }

    public void initEvent() {

        /*启动/断开*/
        btnServerOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (udpServer == null) {

                    if (etServerPort.getText().toString().isEmpty()) {

                        Toast.makeText(MainActivity.this, "请输入端口号", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int mPort = Integer.parseInt(etServerPort.getText().toString());
                    udpServer = new UdpServer(MainActivity.this, mPort);
                    new Thread(udpServer).start();

                    btnServerOperate.setText("断开");
                    SharedPrefsUtil.putValue(MainActivity.this, "ServerPort", etServerPort.getText().toString());

                } else {

                    udpServer.close();
                    udpServer = null;
                    btnServerOperate.setText("启动");
                }

            }
        });

        /*发送短信*/
        btnClientPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (udpServer == null) {
                    Toast.makeText(MainActivity.this, "请开启UDP Server", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(etClientIp)) {
                    Toast.makeText(MainActivity.this, "请输入IP地址", Toast.LENGTH_SHORT).show();
                } else if (isEmpty(etClientPort)) {
                    Toast.makeText(MainActivity.this, "请输入端口号", Toast.LENGTH_SHORT).show();

                } else if (isEmpty(etClientSend)) {
                    Toast.makeText(MainActivity.this, "请输入发送手机号", Toast.LENGTH_SHORT).show();

                } else if (isEmpty(etClientGet)) {
                    Toast.makeText(MainActivity.this, "请输入接收手机号", Toast.LENGTH_SHORT).show();

                } else if (isEmpty(etSendPort)) {
                    Toast.makeText(MainActivity.this, "请输入发送端口号", Toast.LENGTH_SHORT).show();

                } else if (isEmpty(etGetPort)) {
                    Toast.makeText(MainActivity.this, "请输入接收端口号", Toast.LENGTH_SHORT).show();

                } else if (isEmpty(etFormat)) {
                    Toast.makeText(MainActivity.this, "请输入编码格式", Toast.LENGTH_SHORT).show();

                } else if (isEmpty(etClientContent)) {
                    Toast.makeText(MainActivity.this, "请输入短信内容", Toast.LENGTH_SHORT).show();

                } else {
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            SMS sms = new SMS();
                            sms.setServerPort(Integer.parseInt(etGetPort.getText().toString()));
                            sms.setClientPort(Integer.parseInt(etSendPort.getText().toString()));
                            sms.setMessage(etClientContent.getText().toString());
                            sms.setMessageSize(etClientContent.getText().toString().getBytes().length);
                            sms.setReceivePhone(etClientGet.getText().toString());
                            sms.setSendPhone(etClientSend.getText().toString());
                            sms.setTime(System.currentTimeMillis());
                            sms.setFormat(Integer.parseInt(etFormat.getText().toString()));

                            udpServer.Send(etClientIp.getText().toString(), Integer.parseInt(etClientPort.getText().toString()), ChangeUtil.setSms(sms));
                            Message message = new Message();
                            message.what = 2;
                            message.obj = sms;
                            myHandler.sendMessage(message);

                            SharedPrefsUtil.putValue(MainActivity.this, "ClientIp", etClientIp.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientPort", etClientPort.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientSendNumber", etClientSend.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientGetNumber", etClientGet.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientSendPort", etSendPort.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientGetPort", etGetPort.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientFormat", etFormat.getText().toString());
                            SharedPrefsUtil.putValue(MainActivity.this, "ClientContent", etClientContent.getText().toString());

                        }
                    });

                    thread1.start();
                }
            }
        });

        /*清空*/
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etClientSend.setText("");
                etClientGet.setText("");
                etSendPort.setText("");
                etGetPort.setText("");
                etFormat.setText("");
                etClientContent.setText("");
                tvServerShow.setText("");
                tvClientShow.setText("");
            }
        });
    }

    /*注册广播*/
    private void BindReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("udpReceiver");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("udpReceiver")) {
                byte[] types = intent.getByteArrayExtra("udpReceiver");

                Message message = new Message();
                message.what = 1;
                message.obj = types;
                myHandler.sendMessage(message);
            }
        }
    }

    private class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 1:
                        SMS sms = ChangeUtil.getSms((byte[]) msg.obj);
                        tvServerShow.setText("时间戳:" + sms.getTime() + "\n发送短消息的手机号:" + sms.getSendPhone() + "\n发送短消息的端口号:" + sms.getClientPort() + "\n接受短消息的手机号:" + sms.getReceivePhone() + "\n接受短消息的端口号:" + sms.getServerPort() + "\n消息内容:" + sms.getMessage() + "\n消息长度:" + sms.getMessageSize() + "\n短信编码格式:" + sms.getFormat());
                        break;
                    case 2:
                        SMS sms1 = (SMS) msg.obj;
                        tvClientShow.setText("时间戳:" + sms1.getTime() + "\n发送短消息的手机号:" + sms1.getSendPhone() + "\n发送短消息的端口号:" + sms1.getClientPort() + "\n接受短消息的手机号:" + sms1.getReceivePhone() + "\n接受短消息的端口号:" + sms1.getServerPort() + "\n消息内容:" + sms1.getMessage() + "\n消息长度:" + sms1.getMessageSize() + "\n短信编码格式:" + sms1.getFormat());

                        break;
                    case 3:
                        break;
                }
            }
        }
    }

    /**
     * 转换IP地址为String
     *
     * @param i
     * @return
     */
    private String intToIp(int i) {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public boolean isEmpty(EditText view) {
        String s = view.getText().toString();
        return s.isEmpty();
    }

}
