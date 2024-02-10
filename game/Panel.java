import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;

public class Panel extends JPanel implements KeyListener, ActionListener{
    Background bg[];
    Pipe p[] = new Pipe[8];
    Bird b[] = new Bird[2];
    int numPlayer = 2;


    public final int WIDTH = 1280, HEIGHT = 720;

    int score = 0;
    //  components detail
    int pipeWidth;
    int bgWidth;
    int birdWidth;
    // bird
    int flappyheight = HEIGHT / 4;
    int flappyV[] = {0, 0};
    int flappyA[] = {7, 7};
    int flappyI[] = {1, 1};
    boolean gameOver = false;
    //
    boolean firstStage = true;
    boolean firstStageBg = true;

    public Panel() {
        setFocusable(true);
        addKeyListener(this);
        setDoubleBuffered(true);

        for (int i=0; i<numPlayer; ++i){
            b[i] = new Bird();
        }
        bg = new Background[6];
        new Timer(40, this).start();
    }

    @Override // Jpanel
    public void paintComponent(Graphics g) {
        if (!gameOver) {
            // g.drawImage(imagebg, 0, 0, null);
            bgpic(g);
            birdpic(g);
            drawpipe(g);
            logic();
        }            
    }

    public void birdpic(Graphics g) {
        // g.drawImage(imagefb, 100, flappyheight + flappyV, null);
        for (int i=0; i<b.length; ++i){
            g.drawImage(b[i].getImage(), 100, flappyheight + flappyV[i], null);
        }
    }
    public void bgpic(Graphics g){
        // g.drawImage(imagebg, bgwidth  , 0,null);
        if (firstStageBg){
            for (int i=0; i<bg.length; ++i){
                if (bg[i] == null){
                    bg[i] = new Background();
                    bg[i].x += bg[i].getWidth()*i;
                }
                g.drawImage(bg[i].getImage(), bg[i].x, 0, null);
            }    
            bgWidth = bg[0].getWidth();
            firstStageBg = false;
        }else{
            for (int i=0; i<bg.length; ++i){
                if (bg[i] == null){
                    bg[i] = new Background();
                    if ( i == 0){
                        bg[i].x = bg[bg.length-1].x+bgWidth;
                    }else{
                        bg[i].x = bg[i-1].x+bgWidth;
                    }
                }
                g.drawImage(bg[i].getImage(), bg[i].x, 0, null);
            }    

        }
    }

    private void drawpipe(Graphics g) {
        if (firstStage){
            for (int i=0; i<p.length; ++i){
                if (p[i] == null){
                    p[i] = new Pipe();
                    p[i].x += ((p[i].getWidth()*2)+200)*i;
                }
                g.drawImage(p[i].getTop(), p[i].x, p[i].yTop, null);
                g.drawImage(p[i].getBot(), p[i].x, p[i].yBot, null);
            }
            pipeWidth = p[0].getWidth();
            firstStage = false;
        }else{
            for (int i=0; i<p.length; ++i){
                if (p[i] == null){
                    p[i] = new Pipe();
                    if ( i == 0 ){
                        p[i].x = (p[p.length-1].x+pipeWidth)+((p[i].getWidth()*2));
                    }else{
                        p[i].x = (p[i-1].x+pipeWidth)+((p[i].getWidth()*2));
                    }
                }
                g.drawImage(p[i].getTop(), p[i].x, p[i].yTop, null);
                g.drawImage(p[i].getBot(), p[i].x, p[i].yBot, null);
            }    

        }
    }
    private void logic() {
        // for (int i = 0; i < 2; i++) {
            // if (pipe[i] <= 100 && pipe[i] + pipewidth >= 100) { 
                // if ((flappyheight + flappyV[0]) >= 0 && (flappyheight + flappyV[0]) <= gappipe[i]
                        // || (flappyheight + flappyV[0] + 25) >= gappipe[i] + 100
                                // && (flappyheight + flappyV[0] + 25) <= height) {
                    // gameOver = false;
                // }
            // }
            // if (pipe[i] + pipewidth <= 0) {
                // pipe[i] = width;
                // gappipe[i] = (int) (Math.random() * (height - 150));
            // }
        // }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // bird-action
        for (int i=0; i<b.length; ++i){
            flappyA[i] += flappyI[i];
            flappyV[i] += flappyA[i];
            if (flappyV[i] > HEIGHT-flappyheight-100){
                flappyV[i] = HEIGHT-flappyheight-100;
            }
        }
        // Pipe-action
        for (int i=0; i<p.length; ++i){
            if (p[i] != null){
                p[i].x-=5;
            }
            if (p[i].x < -1*p[i].getWidth()){
                p[i] = null;
            }
        }
        // background-action
        for (int i=0; i<bg.length; ++i){
            if (bg[i] != null){
                bg[i].x-=4;
            }
            if (bg[i].x < -1*bg[i].getWidth()-50){
                bg[i] = null;
            }
        }
        for (int i=0; i<p.length; ++i){
            if (p[i] != null && p[i].x-flappyheight < 1){
                score ++;
                System.out.println(score);
            }
        }
        repaint();
        Toolkit.getDefaultToolkit().sync(); //syschronizes the graphic state make it run smoothly
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            flappyA[0] = -7;
        }
        if (code == KeyEvent.VK_W) {
            flappyA[1] = -7;
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    

}
