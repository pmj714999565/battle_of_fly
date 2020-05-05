package server.ts;

import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import server.RandomRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TrysocketUDP extends Thread {
    private ExecutorService executorService;
    final Jedis jedis;
    private DatagramSocket datagramSocket;
    private ObjectMapper jos;

    public TrysocketUDP() {
        executorService=new ThreadPoolExecutor(10,1000,100, TimeUnit.HOURS,new ArrayBlockingQueue<Runnable>(512));
        jedis=new Jedis("127.0.0.1");
        jedis.auth("123456");
        jos=new ObjectMapper();
        try {
            datagramSocket= new DatagramSocket(2334);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            final byte[] buff = new byte[1024];
            final DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
            try {
                datagramSocket.receive(datagramPacket);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService.execute(new Runnable() {
                public void run() {
                    String ori = new String(buff, 0, datagramPacket.getLength());
                    String s=String.valueOf(ori.charAt(0));
                    String id=ori.substring(1);
                    double count=0;
                    InetAddress address = datagramPacket.getAddress();
                    int port = datagramPacket.getPort();
                    HashMap<String,Object> hashMap;
                    synchronized (jedis){
                        hashMap=RandomRequest.getRandom(ori);
                        List<String> list=jedis.hmget(id,"px","py","hp","power");
                        hashMap.put("px",list.get(0));
                        hashMap.put("py",list.get(1));
                        hashMap.put("hp",list.get(2));
                        hashMap.put("power",list.get(3));
                    }
                    while (RandomRequest.getStatistics(Integer.parseInt(s))<2&&count<800){
                        count++;
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                    if(count<5000) {
                        try {
                            String m = jos.writeValueAsString(hashMap);
                            byte[] data2 = m.getBytes();
                            DatagramPacket datagramPacket2 = new DatagramPacket(data2, data2.length, address, port);
                            datagramSocket.send(datagramPacket2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        RandomRequest.setStatistics(Integer.parseInt(s));
                    }
                }
            });
        }
    }
}
