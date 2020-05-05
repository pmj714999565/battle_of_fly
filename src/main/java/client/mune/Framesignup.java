package client.mune;
/*
    注册页面
 */
import client.ts.TCP_send;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static java.lang.Thread.sleep;

public class Framesignup extends Createframe {

    private int flag_sendmail=0;

    public Framesignup(String title, int zx, int zy) {
        super(title, zx, zy);
        final TCP_send tcp_send=new TCP_send();
        final HashMap<String,Object> mes=new HashMap<String, Object>();
        mes.put("ID","*");
        mes.put("password","*");
        mes.put("mail","*");
        mes.put("flag_status","*");
        mes.put("code","*");
        mes.put("lv","1");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
        Container c=getContentPane();
        final JTextField ID_text=new JTextField();
        final JPasswordField word_text=new JPasswordField();
        final JTextField mail_text=new JTextField();
        final JTextField code_text=new JTextField();
        JLabel ID_label=new JLabel("用户名：");
        JLabel word_label=new JLabel("密码");
        JLabel mail_label=new JLabel("邮箱地址");
        JLabel code_label=new JLabel("邮箱验证码");
        word_text.setEchoChar('*');
        JButton confirm=new JButton("确定");
        JButton cancel=new JButton("取消");
        final JButton sendmail=new JButton("发送邮件");
        confirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                mes.put("ID",ID_text.getText());
                mes.put("password",word_text.getPassword());
                mes.put("code",code_text.getText());
                mes.put("flag_status","verify");

                tcp_send.TCP_send_flag(mes);
                if(tcp_send.getStatus()==2){
                    JOptionPane.showMessageDialog(null, "注册成功", "sign up success", JOptionPane.PLAIN_MESSAGE);
                }
                else if(tcp_send.getStatus()==0)
                    JOptionPane.showMessageDialog(null, "验证码错误", "sign up fail", JOptionPane.PLAIN_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "用户名已经存在", "sign up fail", JOptionPane.PLAIN_MESSAGE);
            }
        });
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        sendmail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(flag_sendmail==0) {
                    sendmail.setText("发送中...");
                    sendmail.setBorder(BorderFactory.createLoweredBevelBorder());
                    flag_sendmail=1;
                    new Thread(new Runnable() {
                        public void run() {
                            tcp_send.TCP_send_flag(mes);
                            for (int i = 10; i >= 0; i--) {
                                sendmail.setText(i + "秒后可重新发送");
                                try {
                                    sleep(1000);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            flag_sendmail = 0;
                            sendmail.setText("重新发送");
                        }
                    }).start();
                    mes.put("mail",mail_text.getText());
                    mes.put("flag_status","Email");
                }
            }
        });
        c.add(ID_label);
        c.add(sendmail);
        c.add(ID_text);
        c.add(word_label);
        c.add(word_text);
        c.add(confirm);
        c.add(cancel);
        c.add(mail_label);
        c.add(mail_text);
        c.add(code_label);
        c.add(code_text);
        ID_label.setBounds(80, 20, 90, 30);
        ID_text.setBounds(150, 20, 180, 30);
        word_label.setBounds(80, 60, 90, 30);
        word_text.setBounds(150, 60, 180, 30);
        mail_label.setBounds(80,100,90,30);
        mail_text.setBounds(150,100,180,30);
        sendmail.setBounds(350,100,120,30);
        code_label.setBounds(80,140,90,30);
        code_text.setBounds(150,140,90,30);
        confirm.setBounds(160, 200, 70, 40);
        cancel.setBounds(256, 200, 70, 40);
    }
}
