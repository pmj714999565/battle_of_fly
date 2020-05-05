package client.fire;

import client.Loadimg;
import client.flyobject.Flyobject;
import client.flyobject.Player;

public class BossFire extends Flyobject {
    public BossFire(int bx,int by,int dir)
    {
        img = Loadimg.getImg("/img/bossfire.png");
        w =img.getWidth();
        h =img.getHeight();
        x=bx+95;
        y=by+240;
        this.dir=dir;
    }
    public void move()
    {
        switch (dir){
            case 0: x -=5;break;
            case 1: x -= 4;y +=1;break;
            case 2: x -= 3;y +=2;break;
            case 3: x -= 2;y +=3;break;
            case 4: x -= 1;y +=4;break;
            case 5: y +=5;break;
            case 6: x += 1;y +=4;break;
            case 7: x += 2;y +=3;break;
            case 8: x += 3;y +=2;break;
            case 9: x += 4;y +=1;break;
            case 10: x += 5;break;
        }
    }
    public boolean shootBy(Player f) {
        boolean hit = x<=f.x+f.w-15 &&
                x>f.x-w+15 &&
                y<=f.y+f.h-15 &&
                y>f.y-h+15;
        return hit;
    }
}
