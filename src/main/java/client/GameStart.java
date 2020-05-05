package client;

import client.mune.Createframe;

public class GameStart {
    public void startgame(String roomnum,String otherid) {
        GamePanel gamePanel = new GamePanel();
        Createframe frame_game = new Createframe("Battle", 1024, 683);
        frame_game.add(gamePanel);
        frame_game.setVisible(true);
        gamePanel.action(frame_game,roomnum,otherid);
    }
}

