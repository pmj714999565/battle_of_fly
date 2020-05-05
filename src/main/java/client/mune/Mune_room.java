package client.mune;

import client.GameStart;
import client.Loadimg;
import client.ts.Host;
import client.ts.TCP_send;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

public class Mune_room extends Createframe implements Host {
    JLabel room1_text;
    JLabel room2_text;
    JLabel room3_text;
    JLabel room4_text;
    JLabel room5_text;
    Jedis jedis;
    JButton room1;
    JButton room2;
    JButton room3;
    JButton room4;
    JButton room5;
    HashMap<String, Object> mes;
    JButton exit;
    TCP_send tcp_send;
    String roomnum;
    Mune_room_paint mune_room_paint;
    JLabel playerone_ID;
    JLabel playertwo_ID;
    JLabel playerone_lv;
    JLabel playertwo_lv;
    JLabel playerone_ready;
    JLabel playertwo_ready;
    ArrayList<JLabel> readyFlag;
    JButton ready;
    JButton unready;
    int flag_ready = 0;
    String otherid = null;
    int roomin = 0;

    public Mune_room(String title, int zx, int zy) {
        super(title, zx, zy);
        mes = new HashMap<String, Object>();
        Container container = getContentPane();
        mune_room_paint = new Mune_room_paint();
        tcp_send = new TCP_send();
        mes.put("mail", "*");
        mes.put("flag_status", "*");
        mes.put("numroom", "*");

        exit = new JButton("退出房间");
        setbutton(exit, 335);
        exit.setEnabled(false);
        container.add(exit);

        room1 = new JButton("房间1");
        room2 = new JButton("房间2");
        room3 = new JButton("房间3");
        room4 = new JButton("房间4");
        room5 = new JButton("房间5");
        room_set(room1, 15, 70);
        room_set(room2, 15, 100);
        room_set(room3, 15, 130);
        room_set(room4, 15, 160);
        room_set(room5, 15, 190);

        room1_text = new JLabel("0/2");
        room2_text = new JLabel("0/2");
        room3_text = new JLabel("0/2");
        room4_text = new JLabel("0/2");
        room5_text = new JLabel("0/2");
        playerone_ID = new JLabel("ID1");
        playertwo_ID = new JLabel("ID2");
        playerone_lv = new JLabel("lv1");
        playertwo_lv = new JLabel("lv2");
        playerone_ID.setForeground(Color.GREEN);
        playerone_lv.setForeground(Color.GREEN);
        playertwo_ID.setForeground(Color.GREEN);
        playertwo_lv.setForeground(Color.GREEN);
        playerone_ready = new JLabel("已准备");
        playertwo_ready = new JLabel("已准备");
        playerone_ready.setBackground(Color.CYAN);
        playertwo_ready.setBackground(Color.CYAN);
        playerone_ready.setVisible(false);
        playertwo_ready.setVisible(false);
        playerone_ID.setVisible(false);
        playertwo_ID.setVisible(false);
        playerone_lv.setVisible(false);
        playertwo_lv.setVisible(false);
        playerone_ID.setBounds(750, 150, 234, 39);
        playerone_lv.setBounds(1040, 150, 150, 39);
        playerone_ready.setBounds(1150, 150, 150, 39);
        playertwo_ID.setBounds(750, 200, 234, 39);
        playertwo_lv.setBounds(1040, 200, 150, 39);
        playertwo_ready.setBounds(1150, 200, 150, 39);
        readyFlag = new ArrayList<JLabel>();
        readyFlag.add(playerone_ready);
        readyFlag.add(playertwo_ready);
        ready = new JButton("准备");
        setbutton(ready, 250);
        unready = new JButton("取消准备");
        setbutton(unready, 250);
        unready.setVisible(false);
        ready.setVisible(false);
        room1_text.setForeground(Color.GREEN);
        room2_text.setForeground(Color.GREEN);
        room3_text.setForeground(Color.GREEN);
        room4_text.setForeground(Color.GREEN);
        room5_text.setForeground(Color.GREEN);
        room1_text.setBounds(250, 60, 335, 40);
        room2_text.setBounds(250, 90, 335, 40);
        room3_text.setBounds(250, 120, 335, 40);
        room4_text.setBounds(250, 150, 335, 40);
        room5_text.setBounds(250, 180, 335, 40);
        ready.setBounds(400, 320, 335, 40);
        unready.setBounds(400, 320, 335, 40);
        container.add(playerone_ID);
        container.add(playerone_lv);
        container.add(playertwo_ID);
        container.add(playertwo_lv);
        container.add(playerone_ready);
        container.add(playertwo_ready);
        container.add(ready);
        container.add(unready);
        container.add(room1_text);
        container.add(room2_text);
        container.add(room3_text);
        container.add(room4_text);
        container.add(room5_text);

        container.add(room1);
        container.add(room2);
        container.add(room3);
        container.add(room4);
        container.add(room5);
        add(mune_room_paint);

    }

    public void set() {
        jedis = new Jedis(host);
        jedis.auth("123456");
        mes.put("ID", Framelogin.getID());
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int[] num = new int[6];
                    synchronized (jedis) {
                        for (int i = 1; i <= 5; i++) {
                            num[i - 1] = jedis.lrange("roomID" + i, 0, -1).size() / 2;
                        }
                    }
                    room1_text.setText(num[0] + "/2");
                    room2_text.setText(num[1] + "/2");
                    room3_text.setText(num[2] + "/2");
                    room4_text.setText(num[3] + "/2");
                    room5_text.setText(num[4] + "/2");
                    try {
                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        }).start();

        room1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterroom("1");
            }
        });
        room2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterroom("2");
            }
        });
        room3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterroom("3");
            }
        });
        room4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterroom("4");
            }
        });
        room5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterroom("5");
            }
        });

        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                roomin = 0;
                mes.put("flag_status", "outroom");
                mes.put("roomnum", roomnum);
                mes.put("ID", Framelogin.getID());
                tcp_send.TCP_send_flag(mes);
                playerone_ID.setVisible(false);
                playertwo_ID.setVisible(false);
                playerone_lv.setVisible(false);
                playertwo_lv.setVisible(false);
                playerone_ready.setVisible(false);
                playertwo_ready.setVisible(false);
                ready.setVisible(true);
                room1.setEnabled(true);
                room2.setEnabled(true);
                room3.setEnabled(true);
                room4.setEnabled(true);
                room5.setEnabled(true);
                exit.setEnabled(false);
                unready.setVisible(false);
                ready.setVisible(false);
                mune_room_paint.setRoom_status(0);
                repaint();
            }
        });
        ready.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag_ready = 1;
                ready.setVisible(false);
                unready.setVisible(true);
                mes.put("flag_status", "ready");
                tcp_send.TCP_send_flag(mes);
            }
        });
        unready.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                flag_ready = 0;
                mes.put("flag_status", "readycancel");
                tcp_send.TCP_send_flag(mes);
                unready.setVisible(false);
                ready.setVisible(true);
            }
        });
    }

    private void setbutton(JButton exit, int width) {
        ImageIcon image = Loadimg.getLconImg("/img/exit.png");
        Image img = image.getImage();
        img = img.getScaledInstance(width, 40, Image.SCALE_DEFAULT);
        image.setImage(img);
        exit.setIcon(image);
        ImageIcon image2 = Loadimg.getLconImg("/img/exitdisable.png");
        Image img2 = image.getImage();
        img2 = img2.getScaledInstance(width, 40, Image.SCALE_DEFAULT);
        image2.setImage(img2);
        exit.setDisabledIcon(image2);
        exit.setBounds(10, 20, width, 40);
        exit.setForeground(Color.white);
        exit.setHorizontalTextPosition(SwingConstants.CENTER);
        exit.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
        exit.setContentAreaFilled(false);//设置图片填满按钮所在的区域
        exit.setFocusPainted(false);//设置这个按钮是不是获得焦点
        exit.setBorderPainted(false);//设置是否绘制边框
    }

    void room_set(JButton room, int x, int y) {
        room.setBounds(x, y, 300, 20);
        room.setForeground(Color.white);
        room.setOpaque(false);//设置控件是否透明，true为不透明，false为透明
        room.setContentAreaFilled(false);//设置图片填满按钮所在的区域
        room.setFocusPainted(false);//设置这个按钮是不是获得焦点
        room.setBorderPainted(false);//设置是否绘制边框
        room.setBorder(null);//设置边框
    }

    public String getRoomnum() {
        return roomnum;
    }

    private void enterroom(final String number) {
        roomin = 1;
        mes.put("flag_status", "enterroom");
        mes.put("roomnum", number);
        tcp_send.TCP_send_flag(mes);
        if (tcp_send.getStatus() == 1) {
            JOptionPane.showMessageDialog(null, "进入房间成功", "room in success", JOptionPane.PLAIN_MESSAGE);

            new Thread(new Runnable() {
                public void run() {
                    while (roomin == 1) {
                        synchronized (jedis) {
                            List<String> list = jedis.lrange("roomID" + number, 0, -1);
                            String[] s = list.toArray(new String[list.size()]);
                            playerone_lv.setText(s[1].substring(0, 1));
                            playerone_ID.setText(s[0]);
                            if (s.length > 3) {
                                playertwo_lv.setText(s[3].substring(0, 1));
                                playertwo_ID.setText(s[2]);
                                if (s[2].equals(Framelogin.getID())) {
                                    otherid = s[0];
                                } else otherid = s[2];
                            } else {
                                playertwo_ID.setText(" ");
                                playertwo_lv.setText(" ");
                            }
                            if (jedis.scard("readyroom" + number).intValue() == 2) {
                                new Thread(new Runnable() {
                                    public void run() {
                                        GameStart gameStart = new GameStart();
                                        gameStart.startgame(roomnum, otherid);
                                    }
                                }).start();
                                dispose();
                                break;
                            }
                            for (int i = 0; i < s.length; i = i + 2) {
                                if (jedis.sismember("readyroom" + number, s[i])) {
                                    readyFlag.get(i / 2).setVisible(true);
                                } else {
                                    readyFlag.get(i / 2).setVisible(false);
                                }
                            }

                        }
                    }
                }
            }).start();
            playerone_ID.setVisible(true);
            playertwo_ID.setVisible(true);
            playerone_lv.setVisible(true);
            playertwo_lv.setVisible(true);
            ready.setVisible(true);
            room1.setEnabled(false);
            room2.setEnabled(false);
            room3.setEnabled(false);
            room4.setEnabled(false);
            room5.setEnabled(false);
            exit.setEnabled(true);
            roomnum = number;
            mune_room_paint.setRoom_status(1);
            repaint();
        } else {
            JOptionPane.showMessageDialog(null, "房间已满", "sign up success", JOptionPane.PLAIN_MESSAGE);
        }
    }
}