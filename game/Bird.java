import java.io.IOException; // for Image reading exception
import java.awt.image.BufferedImage; // handling Image
import javax.imageio.ImageIO; // reading Image
import java.io.File; 
class Bird extends Components
{
    public int x=100;
    public Bird(){
        try {
            img = ImageIO.read(new File("img/bluebird-midflap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        img = setScale(img, getWidth()+20, getHeight()+20);
    }
    public void finalize(){
        System.out.println("Bird Destroyed");
    }
}
