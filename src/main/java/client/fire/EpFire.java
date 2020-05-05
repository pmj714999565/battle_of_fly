package client.fire;

import client.Loadimg;
import client.flyobject.Flyobject;
import client.flyobject.Player;

public class EpFire extends Flyobject {
    double k;
    public EpFire(int ex,int ey,int hx,int hy){
        img= Loadimg.getImg("/img/epfire.png");
        h=img.getHeight(null);
        w=img.getWidth(null);
        x=ex+25;
        y=ey+55;
        if(hx!=ex){
            k=1.0*(hy-ey)/(hx-ex);
            if(hx>ex) dir=1;
            else dir=0;
        }
        else k=0;
    }
    @Override
    public void move(){
        if(k==0){
            x=x-(int)Math.pow(-1,dir);
        }
        else if(k>1){
            y=y-(int)Math.pow(-1,dir)*5;
            x=(int)(x-((int)Math.pow(-1,dir)*5)/k);
        }
        else if(k>0){
            y=y-(int)Math.pow(-1,dir);
            x=(int)(x-((int)Math.pow(-1,dir))/k);
        }
        else if(k<-1){
            y=y+(int)Math.pow(-1,dir)*5;
            x=(int)(x+((int)Math.pow(-1,dir)*5)/k);
        }
        else {
            y=y+(int)Math.pow(-1,dir);
            x=(int)(x+((int)Math.pow(-1,dir))/k);
        }
    }
    public boolean shootBy(Player f) {
        boolean hit = x<=f.x+f.w-14 &&
                x>f.x-w+16 &&
                y<=f.y+f.h-14 &&
                y>f.y-h+16;
        return hit;
    }
}
