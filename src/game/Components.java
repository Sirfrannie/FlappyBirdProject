package game;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Image;

// handle some image stuff
public class Components
{
    protected BufferedImage img;
    protected int width;
    protected int height;

    public BufferedImage getImage(){
        return this.img;
    }

    public int getWidth(){
        return img.getWidth();
    }

    public int getHeight(){
        return img.getHeight();
    }
    // resize image 
    public BufferedImage setScale(BufferedImage src, int w, int h){ 
        Image tmp = src.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    // image flip 
    public void flip(BufferedImage image)
    {
        for (int i=0;i<image.getWidth();i++)
            for (int j=0;j<image.getHeight()/2;j++)
            {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(i, image.getHeight()-j-1));
                image.setRGB(i, image.getHeight()-j-1, tmp);
            }
    }
}
