package client.ts;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.util.HashMap;


public class UDP_send implements Host {
    ObjectMapper jos;
    int ex;
    int px;
    int py;
    int tx;
    int ttype;
    int etype;
    int dir;
    int hp;
    int power;
    ObjectMapper mapper;
    boolean gameover=false;

    public UDP_send() {
        mapper = new ObjectMapper();
        jos = new ObjectMapper();
        ex=500;
        px=500;
        py=500;
        tx=500;
        ttype=1;
        etype=1;
        dir=0;
    }

    public void UDP_sends(String mes) {
        try {
            byte[] data = new byte[512];
            DatagramPacket datagramPacketget = new DatagramPacket(data, data.length);
            DatagramSocket datagramSocket = new DatagramSocket();
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket datagramPacket = new DatagramPacket(mes.getBytes(), mes.getBytes().length, address, 2334);
            datagramSocket.send(datagramPacket);
            datagramSocket.setSoTimeout(500);
            datagramSocket.receive(datagramPacketget);
            String me=new String (datagramPacketget.getData(),0,datagramPacketget.getLength());
            HashMap<String, Object> tmpMap = mapper.readValue(me, HashMap.class);
            ex=Integer.parseInt(tmpMap.get("ex").toString());
            px=Integer.parseInt(tmpMap.get("px").toString());
            py=Integer.parseInt(tmpMap.get("py").toString());
            tx=Integer.parseInt(tmpMap.get("tx").toString());
            ttype=Integer.parseInt(tmpMap.get("ttype").toString());
            etype=Integer.parseInt(tmpMap.get("etype").toString());
            hp=Integer.parseInt(tmpMap.get("hp").toString());
            power=Integer.parseInt(tmpMap.get("power").toString());
            datagramSocket.close();
        }
        catch (SocketTimeoutException e){
            gameover=true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  int getEx() {
        return ex;
    }

    public  int getPx() {
        return px;
    }

    public  int getPy() {
        return py;
    }

    public  int getTtype() {
        return ttype;
    }

    public  int getEtype() {
        return etype;
    }

    public  int getTx() {
        return tx;
    }

    public  int getDir() {
        return dir;
    }

    public  boolean isGameover() {
        return gameover;
    }

    public int getHp() {
        return hp;
    }

    public int getPower() {
        return power;
    }
}