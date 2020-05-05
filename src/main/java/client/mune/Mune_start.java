package client.mune;
/*
    开始菜单功能编写
 */
import client.Loadimg;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Mune_start extends JPanel {
    BufferedImage bankgd;
    BufferedImage image;
    Createframe frame;
    BufferedImage bot;
    public Mune_start(final Createframe frame)
    {
        this.frame=frame;
        Container c= frame.getContentPane();
        JButton login=new JButton("登录");
        JButton signup=new JButton("注册");
        JButton ESC=new JButton("退出");
        final Framelogin framelogin=new Framelogin("登录",512,256, frame);
        final Framesignup framesignup=new Framesignup("注册",512,316);
        login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                        framesignup.setVisible(false);
                        framelogin.setVisible(true);
            }
        });
        signup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                        framelogin.setVisible(false);
                        framesignup.setVisible(true);
            }
        });
        ESC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        bankgd= Loadimg.getImg("/img/start.png");
        image = Loadimg.getImg("/img/mouse.png");
        bot = Loadimg.getImg("/img/mouse_touch1.png");
        login.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(bot,new Point(0, 0), null));
        signup.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(bot,new Point(0, 0), null));
        ESC.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(bot,new Point(0, 0), null));
        login.setBounds(650,350,150,80);
        signup.setBounds(650,450,150,80);
        ESC.setBounds(650,550,150,80);
        c.add(login);
        c.add(signup);
        c.add(ESC);
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image,new Point(0, 0), null));
    }
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        g.drawImage(bankgd, 0, 0, 1024, 683, null);
    }

}
