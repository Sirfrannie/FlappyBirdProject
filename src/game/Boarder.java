package game;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Boarder extends Components
{
    public Boarder(int width, int height){
        try {
            img = ImageIO.read(new File("img/frame.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = setScale(img, width, height);
    }
}
