package socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import constant.AppConstants;
import constant.TextToSpeechContent;
import util.Tools;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author zhangrui.i
 */
public class AppSocketThread extends Thread {

    private DatagramSocket datagramSocket;

    private JTextArea jTextArea;

    public AppSocketThread(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

    public void close() {
        datagramSocket.close();
    }

    /**
     * 实时接受数据
     */
    @Override
    public void run() {
        System.out.println("服务已正常启动...");
        try {
            //接收端监听指定端口
            if (datagramSocket == null) {
                datagramSocket = new DatagramSocket(AppConstants.PORT);
            }
            while (true) {
                //定义数据包,用于存储数据
                byte[] buf = new byte[64 * 1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                //通过服务的receive方法将收到数据存入数据包中,receive()为阻塞式方法
                datagramSocket.receive(dp);
                //通过数据包的方法获取其中的数据
                String ip = dp.getAddress().getHostAddress();
                String data = new String(dp.getData(), 0, dp.getLength(), "gbk");
                System.out.println(data);

                /***** 返回ACK消息数据报*/
                // 组装数据报
                byte[] bufback = "success".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(bufback, bufback.length, dp.getAddress(), dp.getPort());
                // 发送消息
                datagramSocket.send(sendPacket);

                // 页面更新
                jTextArea.append("\r\n" + Tools.getInfosByJsonStr(data));

                // 语音播报
                if (!Tools.isblank(data)) {
                    JSONObject jsonObject = JSONObject.parseObject(data);
                    TextToSpeechContent textToSpeechContent = JSON.parseObject(
                            jsonObject.getString("textToSpeechContent"), TextToSpeechContent.class);
                    jTextArea.append("-->" + Tools.textToSpeech(Tools.getTextToSpeechContentForTemplate(textToSpeechContent)));
                    jTextArea.setCaretPosition(jTextArea.getDocument().getLength());
                }
            }
        } catch (SocketException e) {
            System.out.println("服务端口被占用...");
            System.out.println("处理方式-->\r\n1.打开控制台;\r\n2.输入 netstat -ano|findstr 3000 ,回车;\r\n3.输入 taskkill /pid (进程号) /f ,回车.");
        } catch (IOException e) {
            System.out.println("服务异常...");
            e.printStackTrace();
        }
    }
}