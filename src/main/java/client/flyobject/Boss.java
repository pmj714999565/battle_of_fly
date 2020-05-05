package client.flyobject;

import client.Loadimg;
import client.fire.PlayerFire;

import java.awt.image.BufferedImage;

public class Boss extends Flyobject{
    public int hp;//boss的血量
    int dir=1;
    public BufferedImage hpimg1;
    public BufferedImage hpimg2;
    public Boss() {
        img = Loadimg.getImg("/img/enemy/boss.png");
        hpimg1=Loadimg.getImg("/img/bosshp1.png");
        hpimg2=Loadimg.getImg("/img/bosshp2.png");
        w = img.getWidth();
        h = img.getHeight();
        y=30;
        hp=500;
    }
    public void move()
    {
        if(dir==1)
        {
            x=x+5;
            if(x>=970-w)
                dir=2;
        }
        else if(dir==2)
        {
            x=x-5;
            if(x<=0)
                dir=1;
        }
    }
    public boolean shootBy(PlayerFire f) {
        boolean hit = f.x<=x+w &&
                f.x>x &&
                f.y<=y+h &&
                f.y>y;
        return hit;
    }
}
