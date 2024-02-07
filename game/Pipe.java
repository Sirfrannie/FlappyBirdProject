import java.io.IOException; // for Image reading exception
import java.awt.image.BufferedImage; // handling Image
import javax.imageio.ImageIO; // reading Image
import java.io.File; 
class Pipe 
{
    BufferedImage pipeImage;
    int x = 1280;
    
    public Pipe(){
        try {
            pipeImage = ImageIO.read(new File("img/pipe-green.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(){
        return pipeImage;
    }
    public void finalize(){
        System.out.println("Dested");
    }
}
