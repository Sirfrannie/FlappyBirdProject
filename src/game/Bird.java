package game;
 
import java.io.IOException; // for Image reading exception
import java.awt.image.BufferedImage; // handling Image
import javax.imageio.ImageIO; // reading Image
import java.io.File; 
class Bird extends Components
{
    public int x=100; // initial bird position
    private BufferedImage action[] = new BufferedImage[5];
    public Bird(int id){
        try {
            for (int i=0; i<action.length; ++i){
                action[i] = ImageIO.read(new File("img/bird/bird0"+id+"-"+i+".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for ( int i=0; i<action.length; ++i) {
            action[i] = setScale(action[i], action[i].getWidth()+30, action[i].getHeight()+30);
        }
        img = action[0];
    }
    int counter=0;
    @Override 
    public BufferedImage getImage(){
        if (counter > 5 ){
            counter ++;
            if ( counter > 10 ){
                counter = 0;
            }
            return action[1];

        }
        counter ++;
        return action[0];
    } 
    public BufferedImage getFlapping(){
        return action[2];
    }
    int counter2=0;
    public BufferedImage getStunning(){
        if (counter2 > 2 ){
            counter2 ++;
            if ( counter2 > 4 ){
                counter2 = 0;
            }
            return action[3];
        }
        counter2 ++;
        return action[4];
    }
}
