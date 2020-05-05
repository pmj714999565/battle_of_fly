package client.flyobject;

import client.Loadimg;
import client.fire.PlayerFire;

public class Enemy extends Flyobject{
    int sp;//敌机移动速度；
    int dir;//敌机移动方向
    public int hp;
    public int kind;
    public int ef=1;
    public Enemy(int rdkind,int dir,int px,int lv)
    {
        this.dir=dir;
        this.kind=rdkind;
        String path="/img/enemy/"+(rdkind<10?"0":"")+rdkind+".png";
        img = Loadimg.getImg(path);
        w = img.getWidth();
        h = img.getHeight();
        y=0;
        x=px;
        sp=8-rdkind+(int)Math.pow(lv,0.5);//敌机越大移动速度越慢
        hp= 3+lv;
    }

    public void MoveEnemy()
    {
        switch (dir){
            case 1:y=y+sp/2;x=x+sp/2;break;
            case 2:y=y+sp/2;x=x-sp/2;break;
            case 3:x=x+sp;if(x>=897) dir=4;break;
            case 4: x=x-sp;if(x<=0) dir=3;break;
            default:y=y+sp;break;
        }
    }
    /*
     * 判断敌机是否被子弹击中
     */
    public boolean shootBy(PlayerFire f) {
        boolean hit = f.x<=x+w-25 &&
                f.x>x+10 &&
                f.y<=y+h-25 &&
                f.y>y-10;
        return hit;
    }
    /*
     * 判断敌机是与英雄机相撞
     */
    public boolean hitBy(Player f) {
        boolean hit = f.x<=x+w-25 &&
                f.x>x+10 &&
                f.y<=y+h-25 &&
                f.y>y-10;
        return hit;
    }
}
