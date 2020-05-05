package client.flyobject;

import client.Loadimg;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;


public class Mouse extends JPanel implements MouseListener {
    private int xs;
    private int xe;
    private int ys;
    private int ye;
    private Object lock;

    public Mouse(int xs, int xe, int ys, int yz){
        this.xs=xs;
        this.xe=xe;
        this.ys=ys;
        this.ye=yz;
        }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {
        for (int i = 1; i < 4; i++) {
            BufferedImage image = Loadimg.getImg("/img/mouse_touch" + i + ".png");
            for (int j = 0; j < 5000; j++) {
            }
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(0, 0), null));
        }
    }
    public void mouseExited(MouseEvent e) {
        BufferedImage image = Loadimg.getImg("/img/mouse.png");
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(image,new Point(0, 0), null));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
