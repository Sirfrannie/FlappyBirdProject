import java.io.IOException; // for Image reading exception
import java.awt.image.BufferedImage; // handling Image
import javax.imageio.ImageIO; // reading Image
import java.io.File; 
class Bird 
{
    BufferedImage birdImage;
    
    public Bird(){
        try {
            birdImage = ImageIO.read(new File("img/bluebird-midflap.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(){
        return birdImage;
    }
    public void finalize(){
        System.out.println("Destroyed");
    }
}
