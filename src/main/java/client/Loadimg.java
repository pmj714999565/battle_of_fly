package client;
/*
    图片读取工具类
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Loadimg {
    public static BufferedImage getImg(String path) {
        try {
            return ImageIO.read(Loadimg.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ImageIcon getLconImg(String path) {
        return new ImageIcon(Loadimg.class.getResource(path));
    }
}
