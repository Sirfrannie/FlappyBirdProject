package game;

import java.io.IOException; // for Image reading exception
import java.awt.image.BufferedImage; // handling Image
import javax.imageio.ImageIO; // reading Image
import java.io.File; 
import java.lang.Math;
class Pipe extends Components 
{
    public int x = 1000; // initial Pipe Position (at the end of window)
    public int yTop; // topside pipe position
    public int yBot; // bottomside pipe position
    public int gap;
    public BufferedImage pipeImg[] = new BufferedImage[2];
    public int width, height;
    public static int i;
    
    public Pipe(Pipe p){
        this.x = p.x;
        this.yTop = p.yTop;
        this.yBot = p.yBot;
        this.gap = p.gap;
        System.arraycopy(p.pipeImg, 0, this.pipeImg, 0, p.pipeImg.length);
        this.width = p.width;
        this.height = p.height;
        this.i = p.i;
    }
    public Pipe(){
        for (int i=0; i<pipeImg.length; ++i){
            try {
                pipeImg[i] = ImageIO.read(new File("game/img/pipe-green.png"));
                pipeImg[i] = setScale(pipeImg[i], pipeImg[i].getWidth()+80, pipeImg[i].getHeight()+200);
                if ( i == 1 ){
                    flip(pipeImg[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gap = pipeGap();
        width = pipeImg[0].getWidth();
        height = pipeImg[0].getHeight();
        yTop = randYTop();
        yBot = yTop+height+gap;
        // System.out.println(gap);

    }
    public int pipeGap(){
        int min = 100;
        int max = 300;
        int range = max-min+1; 
        return (int)(Math.random()*range) + min;
    } 
    public int randYTop(){
        int min = -1*height+20;
        int max = -200;
        int range = max-min+1; 
        return (int)(Math.random()*range) + min;
    }
    public int randYBot(){
        int min = 720-height+10;
        int max = 720+height-10;
        int range = max-min+1; 
        return (int)(Math.random()*range) + min;
    }
    public BufferedImage getTop(){
        return pipeImg[1];
    }
    public BufferedImage getBot(){
        return pipeImg[0]; 
    }
    @Override
    public int getWidth(){
        return width;
    }

    @Override
    public int getHeight(){
        return height;
    }
}
