package client.mune;

import client.Loadimg;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Mune_room_paint extends JPanel {
    BufferedImage image;
    BufferedImage image2;
    int room_status;

    public void setRoom_status(int room_status) {
        this.room_status = room_status;
    }

    public Mune_room_paint(){
        image= Loadimg.getImg("/img/room.png");
        image2= Loadimg.getImg("/img/roomin.png");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image,0,0,1251,724,null);
        if(room_status==1){
            g.drawImage(image2,400,100,null);
        }

    }
}
