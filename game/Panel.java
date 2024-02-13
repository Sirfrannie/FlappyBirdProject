import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel implements KeyListener, ActionListener{
    private Background bg[];
    private Pipe p[] = new Pipe[8];
    private Bird b[] = new Bird[1];
    private Hitbox h = null;
    private Scoreboard sb = new Scoreboard();

    public final int WIDTH = 1280, HEIGHT = 720;

    private int score = 0;
    //  components detail
    private int pipeWidth;
    private int pipeHeight;
    private int bgWidth;
    private int birdWidth;
    private int birdHeight;
    // bird
    private int flappyheight;
    private int flappyV[] = {0, 0};
    private int flappyA[] = {7, 7};
    private int flappyI[] = {1, 1};
    private boolean gameOver = false;
   //
    private boolean isPlaying = false;
    private boolean firstStage = true;
    private boolean firstStageBg = true;

    private Timer t;
    public Panel() {
        setFocusable(true);
        addKeyListener(this);
        setDoubleBuffered(true);
        bg = new Background[6];

        for (int i=0; i<b.length; ++i){
            b[i] = new Bird();
        }
        birdHeight = b[0].getHeight();
        birdWidth = b[0].getWidth(); 
        flappyheight = (HEIGHT/2)+(b[0].getHeight()/2);
        t = new Timer(40, this);
        t.start();
    }
    @Override 
    public void paintComponent(Graphics g) {
        if (!gameOver) { // while the game is poccessing
            drawbg(g);
            drawbird(g);
            if (isPlaying){ // while player is playing
                drawpipe(g);
                buildHitbox();
                sb.update(g);
                logic(g);
            }
        }else{
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying){
            // bird-action
            for (int i=0; i<b.length; ++i){ // for all bird
                // (de)increase bird position (to make bird fell down)
                flappyA[i] += flappyI[i];
                flappyV[i] += flappyA[i];
                // lowest position possible
                if (flappyV[i] > (HEIGHT-flappyheight)-100){
                    flappyV[i] = (HEIGHT-flappyheight)-100;
                }
                // highest position possible 
                if (flappyheight+flappyV[i] < 10){
                    System.out.println(flappyV[i]);
                    flappyV[i] = -1*flappyheight+10;
                }
            }
            // Pipe-action
            for (int i=0; i<p.length; ++i){ // for all pipe
                // for the pipe which exist
                if (p[i] != null){
                    // move to left
                    p[i].x-=5;
                    // get rid of pipe which out of frame
                    if (p[i].x < -1*p[i].getWidth()){  
                        p[i] = null; // set p[i] to null to make it reusable
                    }
                }
            }
            
        } // {if playing}
        // background-action
        for (int i=0; i<bg.length; ++i){
            // moving
            if (bg[i] != null){
                bg[i].x-=4;
                // get rid of it
                if (bg[i].x < -1*bg[i].getWidth()-50){
                    bg[i] = null;
                }
            }
        }
        // update hit box position make hitbox follow the pipe
        if ( h != null){
            h.update();
        }
        // repaint components (call paintComponent()) 
        repaint();
        Toolkit.getDefaultToolkit().sync(); //synchronizes the graphic state make it run smoothly
    }
    private void drawbird(Graphics g) {
        // for all bird in array
        for (int i=0; i<b.length; ++i){
            g.drawImage(b[i].getImage(), b[i].x, flappyheight + flappyV[i], null); // redraw bird
        }
    }
    private void drawbg(Graphics g){
        // initalization ( all element in array of pipe are null )
        if (firstStageBg){
            for (int i=0; i<bg.length; ++i){
                if (bg[i] == null){
                    bg[i] = new Background();
                    // make each bg continue 
                    bg[i].x += bg[i].getWidth()*i;
                }
                g.drawImage(bg[i].getImage(), bg[i].x, 0, null); // draw bg
            }    
            bgWidth = bg[0].getWidth();
            firstStageBg = false; // end initialize stage 
        }else{
            for (int i=0; i<bg.length; ++i){
                // if bg is non-exist
                if (bg[i] == null){
                    bg[i] = new Background();
                    // check if the first one in array
                    if ( i == 0){
                        // make it continue next to the last one 
                        bg[i].x = bg[bg.length-1].x+bgWidth;
                    }else{
                        // make it continue next to one before it 
                        bg[i].x = bg[i-1].x+bgWidth;
                    }
                }
                g.drawImage(bg[i].getImage(), bg[i].x, 0, null); // redraw bg
            }    

        }
    }

    private void drawpipe(Graphics g) {
        // initalization ( all element in array of pipe are null )
        if (firstStage){
            for (int i=0; i<p.length; ++i){
                if (p[i] == null){
                    p[i] = new Pipe();
                    // create space between each pipe
                    p[i].x += ((p[i].getWidth()*2)+200)*i;
                }
                g.drawImage(p[i].getTop(), p[i].x, p[i].yTop, null); // draw pipe
                g.drawImage(p[i].getBot(), p[i].x, p[i].yBot, null); // draw pipe
            }
            pipeWidth = p[0].getWidth();
            firstStage = false; // end the initialize stage 
        }else{
            for (int i=0; i<p.length; ++i){
                // creat non-exist pipe 
                if (p[i] == null){ 
                    p[i] = new Pipe();
                    // check if non-exist pipe is the first pipe in array
                    if ( i == 0 ){ 
                        // create with position next to last one
                        p[i].x = (p[p.length-1].x+pipeWidth)+((p[i].getWidth()*2)); 
                    }else{
                        // create with position next to before
                        p[i].x = (p[i-1].x+pipeWidth)+((p[i].getWidth()*2)); 
                    }
                }
                g.drawImage(p[i].getTop(), p[i].x, p[i].yTop, null); // redraw topside
                g.drawImage(p[i].getBot(), p[i].x, p[i].yBot, null); // redraw botside
            }    

        }
    }
    int pipenumber=0; // to tell which pipe the hit box should be with
    private void buildHitbox(){
        for (int i=0; i<p.length; ++i){
            if ( p[i] != null){
                if ( h == null && i == pipenumber){
                    h = new Hitbox(p[i]);
                    pipenumber ++;
                    if (pipenumber == p.length){ // if reached to the last pipe
                        pipenumber = 0;
                    }
                }
            }
        }
    }
    private int position; // current bird Y position
    private void logic(Graphics g) {
        if ( h != null ){
            for (int i=0; i<b.length; ++i){
                position = flappyheight+flappyV[i];
                if (
                        /// check if top left side hits the pipe
                        h.front <= b[i].x && b[i].x-10 <= h.behind && position < h.topLevel 
                        /// check if top right side hits the pipe
                        || h.front <= b[i].x+birdWidth-10 && b[i].x <= h.behind && position < h.topLevel 
                        /// check if bottom left side hits the pipe
                        || h.front <= b[i].x-10 && b[i].x <= h.behind && position+birdHeight > h.botLevel
                        /// check if bottom right side hits the pipe
                        || h.front <= b[i].x+birdWidth-10 && b[i].x <= h.behind && position+birdHeight > h.botLevel
                        ){

                    System.out.println("Hit");
                    h = null; // remove hitbox from that pipe 
                    return;
                }
                if ( b[i].x >= h.scoreLine ){
                    System.out.println("Scored");
                    sb.plus(1);
                    h = null; // remove hitbox form that pipe
                }
            }
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // spacebar and arrow up
        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_UP) { 
            if (!isPlaying){
                isPlaying = true;
            }
            flappyA[0] = -7;
        }
        if (code == KeyEvent.VK_W) {
            flappyA[1] = -7;
        }
        if (code == KeyEvent.VK_ESCAPE){
            isPlaying = !isPlaying;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
