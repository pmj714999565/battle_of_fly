package client.flyobject;

import client.Loadimg;

public class Tools extends Flyobject{
    public int type;//
    public Tools(int rd,int px){
        String path="/img/tools/"+(rd<10?"0":"")+rd+".png";
        img = Loadimg.getImg(path);
        w = img.getWidth();
        h = img.getHeight();
        y=0;
        x=px;
        type=rd;
    }
    public void MoveTools()
    {
        y+=5;
    }
    public boolean hitBy(Player f) {
        boolean hit = x<=f.x+f.w &&
                x>f.x-w &&
                y<=f.y+f.h &&
                y>f.y-h;
        return hit;
    }
}
