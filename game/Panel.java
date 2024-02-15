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
    private Player player[];
    private Player self;
    private Player pipeBuilder;

    public final int WIDTH = 1280, HEIGHT = 720;

    //  components detail
    private int bgWidth;
    // bird
    private int flappyheight;
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

        player = new Player[1];
        for (int i=0; i<player.length; ++i){
            player[i] = new Player(i);
        }

        self = player[player[0].id];
        pipeBuilder = player[0]; 
        pipeBuilder.buildPipe(player);

        flappyheight = (HEIGHT/2)+(self.bird.getHeight()/2);
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
                self.buildHitbox();
                self.score.update(g);
                logic(g);
            }
        }else{
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying){
            /// bird-action
            /// (de)increase bird position (to make bird fell down)
            self.flappyA += self.flappyI;
            self.flappyV += self.flappyA;
            /// lowest position possible
            if (self.flappyV > (HEIGHT-flappyheight)-100){
                self.flappyV = (HEIGHT-flappyheight)-100;
            }
            /// highest position possible 
            if (flappyheight + self.flappyV < 10){
                // System.out.println(p.flappyV);
                self.flappyV = -1*flappyheight+10;
            }
            /// Pipe-action
            for (int i=0; i<self.pipeList.length; ++i){ // for all pipe
                /// for the pipe which exist
                if (self.pipeList[i] != null){
                    // move to left
                    self.pipeList[i].x-=5;
                    /// get rid of pipe which out of frame
                    if (self.pipeList[i].x < -1*self.pipeList[i].getWidth()){  
                        self.pipeList[i] = null; // set pipe to null to make it reusable
                    }
                }
            }
            
        } // {if playing}
        // background-action
        for (int i=0; i<bg.length; ++i){
            /// moving
            if (bg[i] != null){
                bg[i].x-=4;
                /// get rid of it
                if (bg[i].x < -1*bg[i].getWidth()-50){
                    bg[i] = null;
                }
            }
        }
        /// update hit box position make hitbox follow the pipe
        if ( self.hitbox != null ){
            self.hitbox.update();
        }
        /// repaint components (call paintComponent()) 
        repaint();
        Toolkit.getDefaultToolkit().sync(); //synchronizes the graphic state make it run smoothly
    }
    private void drawbird(Graphics g) {
        g.drawImage(self.bird.getImage(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
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
        self.takeTmp();
        pipeBuilder.buildPipe(player);
        for (int i=0; i<self.pipeList.length; ++i){
            g.drawImage(self.pipeList[i].getTop(), self.pipeList[i].x, self.pipeList[i].yTop, null); 
            g.drawImage(self.pipeList[i].getBot(), self.pipeList[i].x, self.pipeList[i].yBot, null); 
        }
    }
    private int position; // current bird Y position
    private void logic(Graphics g) {
        if ( self.hitbox != null ){
            position = self.getBirdPosition(flappyheight);
            if (
                    /// check if top left side hits the pipe
                    self.hitbox.front <= self.bird.x && self.bird.x-10 <= self.hitbox.behind && position < self.hitbox.topLevel 
                    /// check if top right side hits the pipe
                    || self.hitbox.front <= self.bird.x+self.bird.getWidth()-10 && self.bird.x <= self.hitbox.behind && position < self.hitbox.topLevel 
                    /// check if bottom left side hits the pipe
                    || self.hitbox.front <= self.bird.x-10 && self.bird.x <= self.hitbox.behind && position+self.bird.getHeight() > self.hitbox.botLevel
                    /// check if bottom right side hits the pipe
                    || self.hitbox.front <= self.bird.x+self.bird.getWidth()-10 && self.bird.x <= self.hitbox.behind && position+self.bird.getHeight()> self.hitbox.botLevel
                    ){

                System.out.println("Hit");
                self.hitbox = null; // remove hitbox from that pipe 
                return;
            }
            if ( self.bird.x >= self.hitbox.scoreLine ){
                System.out.println("Scored");
                self.score.plus(123456);
                self.hitbox = null; // remove hitbox form that pipe
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
            self.flappyA = -7;
        }
        if (code == KeyEvent.VK_W) {
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
