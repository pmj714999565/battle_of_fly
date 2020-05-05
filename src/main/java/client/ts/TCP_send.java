package client.ts;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class TCP_send implements Host{
    private Socket client;
    byte[] temp;
    private int status=0;
    int port=2333;
    ObjectMapper jos;

    public TCP_send() {
        temp=new byte[1024];
        jos=new ObjectMapper();
    }

    public int getStatus() {
        return status;
    }

    public void TCP_send_flag(HashMap<String,Object> mes)  {
        try {
            String m = jos.writeValueAsString(mes);
            client = new Socket(host, port);
            OutputStream in = client.getOutputStream();
            in.write(m.getBytes());
            in.flush();
            client.shutdownOutput();

            InputStream input = client.getInputStream();
            int len;
            String s="";
            while ((len = input.read(temp)) != -1) {
                s = new String(temp, 0, len, "UTF-8");
            }
            status=Integer.parseInt(s);
            in.flush();
            client.shutdownInput();
            in.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "error connection", "无法连接到服务器", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
            e.printStackTrace();
        }
    }
    public void TCP_send_reback(HashMap<String,Object> mes)  {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String m = jos.writeValueAsString(mes);
            client = new Socket(host, port);
            OutputStream in = client.getOutputStream();
            in.write(m.getBytes());
            in.flush();
            client.shutdownOutput();

            InputStream input = client.getInputStream();
            int len;
            String s="";
            while ((len = input.read(temp)) != -1) {
                s = new String(temp, 0, len, "UTF-8");
            }
            client.shutdownInput();
            HashMap<String, Object> tmpMap = mapper.readValue(s, HashMap.class);
            status=Integer.parseInt(tmpMap.get("status").toString());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "error connection", "无法连接到服务器", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
            e.printStackTrace();
        }

    }
}
