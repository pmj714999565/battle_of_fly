package client.ts;
/*
    开始任务时判断是否连接到服务器
 */
import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Trysocket_client implements Host{
    private Socket client;

    public Trysocket_client(int port) {
        try {
            client=new Socket(host,port);
            OutputStream in = client.getOutputStream();
            in.write("connect is Ok".getBytes());
            in.flush();
            in.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "error connection","无法连接到服务器",JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
            e.printStackTrace();
        }
    }
}
