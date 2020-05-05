package client.mune;

import javax.swing.*;

public class Createframe extends JFrame{
    public Createframe(String title, int zx, int zy) {
        setTitle(title);
        setSize(zx,zy);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
