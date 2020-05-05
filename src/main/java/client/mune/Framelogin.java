package client.mune;
/*
    登录页面
 */
import client.RoomStart;
import client.ts.Host;
import client.ts.TCP_send;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Framelogin extends Createframe implements Host {
    private static String ID=null;

    public static String getID() {
        return ID;
    }

    public Framelogin(String title, int zx, int zy, final Createframe frame) {
        super(title, zx, zy);
        final TCP_send tcp_send=new TCP_send();
        final HashMap<String,Object> mes=new HashMap<String, Object>();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
        Container c=getContentPane();
        final JTextField ID_text=new JTextField();
        final JPasswordField word_text=new JPasswordField();
        JLabel ID_label=new JLabel("用户名：");
        JLabel word_label=new JLabel("密码");
        word_text.setEchoChar('*');
        JButton confirm=new JButton("确定");
        JButton cancel=new JButton("取消");
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ObjectMapper jos=new ObjectMapper();
                mes.put("ID",ID_text.getText());
                mes.put("password",word_text.getPassword());
                mes.put("flag_status","login");
                tcp_send.TCP_send_flag(mes);
                if(tcp_send.getStatus()==0){
                    JOptionPane.showMessageDialog(null, "账号或密码错误", "log in fail", JOptionPane.PLAIN_MESSAGE);
                }
                else{
                    ID=ID_text.getText();
                    JOptionPane.showMessageDialog(null, "登录成功", "log in success", JOptionPane.PLAIN_MESSAGE);
                    frame.setVisible(false);
                    setVisible(false);
                    RoomStart roomStart=new RoomStart();
                    roomStart.startroom();
                }
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    dispose();
            }
        });
        c.add(ID_label);
        c.add(ID_text);
        c.add(word_label);
        c.add(word_text);
        c.add(confirm);
        c.add(cancel);
        ID_label.setBounds(80, 20, 90, 30);
        ID_text.setBounds(120, 20, 210, 30);
        word_label.setBounds(85, 60, 90, 30);
        word_text.setBounds(120, 60, 210, 30);
        confirm.setBounds(140, 100, 70, 40);
        cancel.setBounds(220, 100, 70, 40);
    }

}
