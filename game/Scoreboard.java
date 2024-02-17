import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.lang.Math;
public class Scoreboard extends Components
{
    // array of icon that represent number 0-9
    private BufferedImage number[] = new BufferedImage[10];  
    public int positionX = 1280-100; // screen width -10
    public int positionY = 10;
    public int numWidth;
    public int score=0;
    public Scoreboard(){
        score = 0;
        for (int i=0; i<number.length; ++i){
            try {
                number[i] = ImageIO.read(new File("img/number/"+i+".png"));
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        numWidth= number[0].getWidth();
    }
    public void plus(int n){
        score += n;
    }
    public void setZero(){
        score = 0;
    }
    int cur;
    public void update(Graphics g){
        int s = score;
        if ( s > 9999999 ) s = 9999999;
        for (int i=6; i>=1; --i){
            cur = (int)(Math.pow(10, i));
            g.drawImage(number[0+(int)(s/cur)], positionX-((numWidth-1)*i), positionY, null);
            s -= ((int)(s/cur))*cur;
        }
        if ( s < 10 ){
            g.drawImage(number[s], positionX, positionY, null);
        }
    }
}
