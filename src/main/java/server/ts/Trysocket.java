package server.ts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import server.Sendmail;
import server.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Trysocket extends Thread {

    private ServerSocket server = null;
    private byte[] temp;
    private String status = "0";
    private dao d;
    private ExecutorService executorService;
    private Socket socket0;
    private final Object key;
    private ObjectMapper mapper;
    private JedisPool jedisPool;

    public Trysocket(int port) {
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(1000);
        jedisPool=new JedisPool(config,"localhost", 6379,100,"123456");
        d = new dao();
        key = new Object();
        mapper = new ObjectMapper();
        executorService = new ThreadPoolExecutor(100, 1000, 100, TimeUnit.HOURS, new ArrayBlockingQueue<Runnable>(512));
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        temp = new byte[1024];
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                socket0 = server.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService.execute(new Runnable() {
                public void run() {
                    try {
                        Socket socket = socket0;
                        Jedis jedis=jedisPool.getResource();
                        InputStream in = socket.getInputStream();
                        int len;
                        String s = "";
                        while ((len = in.read(temp)) != -1) {
                            s = new String(temp, 0, len, "UTF-8");
                        }
                        socket.shutdownInput();
                        if ("connect is Ok".equals(s)) {
                            socket.close();
                            in.close();
                        } else {
                            HashMap<String, Object> tmpMap = mapper.readValue(s, HashMap.class);
                            if ("Email".equals(tmpMap.get("flag_status"))) {
                                String dic = "abcdefghigklmnopqrstuvwsyzABCDEFGHIGKLMNOPQRSTUVWSYZ0123456789";
                                StringBuilder key = new StringBuilder();
                                for (int i = 0; i < 4; i++) {
                                    Random h = new Random();
                                    key.append(dic.charAt(h.nextInt(58)));
                                }
                                Sendmail sendmail = new Sendmail(tmpMap.get("mail").toString(), key.toString());
                                sendmail.send();
                                jedis.set(tmpMap.get("mail").toString(), key.toString());
                                jedis.set(tmpMap.get("mail").toString(), key.toString(), "XX", "EX", 60 * 60 * 30);
                            }
                            else if ("verify".equals(tmpMap.get("flag_status").toString())) {
                                status = jedis.exists(tmpMap.get("mail").toString()) && jedis.get(tmpMap.get("mail").toString()).equals(tmpMap.get("code")) ? "1" : "0";
                                if ("1".equals(status)) {
                                    status = String.valueOf(d.dao_signup("select * from battle_of_fly where ID='" + tmpMap.get("ID").toString() + "'",
                                            "INSERT INTO battle_of_fly VALUES('" + tmpMap.get("ID").toString() + "','" + tmpMap.get("password").toString() +
                                                    "','" + tmpMap.get("mail").toString() + "','" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "',1)"));
                                }
                            }
                            else if ("login".equals(tmpMap.get("flag_status").toString())) {
                                status = String.valueOf(d.dao_login(tmpMap.get("ID").toString(), tmpMap.get("password").toString()));
                            }
                            else if ("enterroom".equals(tmpMap.get("flag_status").toString())) {
                                String num = tmpMap.get("roomnum").toString();
                                synchronized (key) {
                                    int n = jedis.lrange("roomID" + num, 0, -1).size() / 2;
                                    if (n < 2) {
                                        jedis.rpush("roomID" + num,tmpMap.get("ID").toString());
                                        jedis.rpush("roomID" + num,String.valueOf(d.find_info(tmpMap.get("ID").toString())));
                                        status="1";
                                    }
                                    else status="0";
                                }
                            }
                            else if("outroom".equals(tmpMap.get("flag_status").toString())){
                                String num = tmpMap.get("roomnum").toString();
                                jedis.lrem("roomID"+num,1,tmpMap.get("ID").toString());
                                jedis.lrem("roomID"+num,1,String.valueOf(d.find_info(tmpMap.get("ID").toString())));
                            }
                            else if("ready".equals(tmpMap.get("flag_status").toString())){
                                String num = tmpMap.get("roomnum").toString();
                                jedis.sadd("readyroom"+num,tmpMap.get("ID").toString());
                            }
                            else if("readycancel".equals(tmpMap.get("flag_status").toString())){
                                String num = tmpMap.get("roomnum").toString();
                                jedis.srem("readyroom"+num,tmpMap.get("ID").toString());
                            }
                            else if ("gameover".equals(tmpMap.get("flag_status").toString())){
                                String num = tmpMap.get("roomnum").toString();
                                jedis.srem("readyroom"+num,tmpMap.get("ID").toString());
                                jedis.ltrim("roomID"+num,1,0);
                                d.update_info(tmpMap.get("ID").toString(),tmpMap.get("score").toString());
                            }
                            jedis.close();
                            OutputStream wt = socket.getOutputStream();
                            wt.write(status.getBytes());
                            socket.close();
                            in.close();
                            wt.close();
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}

