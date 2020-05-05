package client.ts;

import client.mune.Createframe;
import client.mune.Mune_start;


public class domain2 {
    public static void main(String[] args) {
        new Trysocket_client(2333);
        Createframe frame_start=new Createframe("网络版",1024,683);
        Mune_start mune_start = new Mune_start(frame_start);
        frame_start.add(mune_start);
        frame_start.setVisible(true);
    }
}
