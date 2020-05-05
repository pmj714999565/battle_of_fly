package client.flyobject;

import client.Loadimg;

public class Player extends Flyobject{
    public int hp;//血量
    public int power=1;//威力
    public Player() {
        img = Loadimg.getImg("/img/Hero1.png");
        w = img.getWidth();
        h = img.getHeight();
        hp=3;
        x=500;
        y=500;
    }
    public void moveHero(int mx,int my) {
        x=mx-25;
        y=my-25;
    }
}
