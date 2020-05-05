package client.flyobject;

import java.awt.image.BufferedImage;

public abstract class Flyobject {
    public BufferedImage img;
    public int x;
    public int y;
    public int w;
    public int h;
    public int dir;
    public void move(){};
}
