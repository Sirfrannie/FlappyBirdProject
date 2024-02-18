import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
public class Background extends Components
{
    int x = 0;
    public Background(){
        try {
            img = ImageIO.read(new File("img/background-day.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        width = img.getWidth();
        height = img.getHeight();
        img = setScale(img, (int)(width+(720-height)), 720);
    }
    
}
