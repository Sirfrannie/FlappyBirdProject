package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Base extends Components
{
    int x = 0;
    public boolean outFrame = false;
    public Base(){
        try {
            img = ImageIO.read(new File("img/base.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
