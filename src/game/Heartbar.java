import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;

public class Heartbar extends Components
{
    // current amouth of heart;
    public int numOfHeart;
    // maximum heart 
    private int max = 5;
    // heart out line for no heart 
    private BufferedImage noHeart;
    // position
    public int positionX;
    public int positionY = 10;
    public Heartbar(){
        try {
            img = ImageIO.read(new File("img/heart/heart.png"));
            noHeart = ImageIO.read(new File("img/heart/noheart.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        // img = setScale(img, 100, 100);
        // noHeart = setScale(noHeart, 100, 100);
        numOfHeart = max;
    }
    // draw heart bar 
    public void drawHeart(Graphics g){
        
        for (int i=1; i<=max; ++i){
            if ( i <= numOfHeart ){
                g.drawImage(img, positionX+((getWidth()+1)*(i-1)), positionY, null); 
            }else{ 
                g.drawImage(noHeart, positionX+((getWidth()+1)*(i-1)), positionY, null);
            }
        }
    }
    // decrease amount of heart 
    public void decrease(){
        --numOfHeart;
    }
}

