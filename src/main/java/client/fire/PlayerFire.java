package client.fire;

import client.Loadimg;
import client.flyobject.Flyobject;

public class PlayerFire extends Flyobject {
    public PlayerFire(int hx, int hy) {
        img = Loadimg.getImg("/img/fire.png");
        x = hx + 25;
        y = hy - 25;
    }

    @Override
    public void move() {
        y -= 5;
    }
}
