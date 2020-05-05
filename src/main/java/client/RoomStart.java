package client;

import client.mune.Mune_room;

public class RoomStart {
    public void startroom(){
        Mune_room mune_room=new Mune_room("房间列表",1251,724);
        mune_room.set();
        mune_room.setVisible(true);
    }
}
