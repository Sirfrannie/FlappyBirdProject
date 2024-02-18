import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Panel extends JPanel implements ActionListener{
    private Background bg[];
    private Boarder boarder;
    private Player player[];
    public Player self;
    private Player pipeBuilder;

    public final int WIDTH = 1280, HEIGHT = 720;

    //  components detail
    private int bgWidth;
    // bird
    private int flappyheight;
    private boolean gameOver = false;
   //
    private boolean isPlaying = true;
    private boolean firstStageBg = true;
    private boolean jumping = false;

    public Timer t;
    public JButton p = new JButton("p");
    public JButton s = new JButton("s");
    public Panel(int id, int jump, Player player[]) {
        setFocusable(true);
        setDoubleBuffered(true);
        bg = new Background[6];

        this.player = player;
        self = player[id];
        self.jump = jump;
        self.score.positionX = (WIDTH/player.length)-100;
        pipeBuilder = player[0]; 

        // stop and continue button
        p.setBounds(10, 5, 30, 30);
        s.setBounds(45, 5, 30, 30);
        this.add(p);
        this.add(s);

        this.setBounds((int)(0+((WIDTH/2)*id)), 0, (int)(WIDTH/player.length), HEIGHT);
        boarder = new Boarder(1280, 720);

        flappyheight = (HEIGHT/2)+(self.bird.getHeight()/2);
        t = new Timer(40, this);
        t.start();
        t.setInitialDelay(1000);
    }
    @Override 
    public void paintComponent(Graphics g) {
        if (!gameOver) { // while the game is poccessing
            drawbg(g);
            drawbird(g);
            if (isPlaying){ // while player is playing
                drawpipe(g);
                self.buildHitbox();
                logic(g);
            }
            drawboarder(g);
            self.score.update(g);
        }else{
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying){
            // bird-action
            // (de)increase bird position (to make bird fell down)
            self.flappyA += self.flappyI;
            self.flappyV += self.flappyA;
            // lowest position possible
            if (self.flappyV > (HEIGHT-flappyheight)-100){
                self.flappyV = (HEIGHT-flappyheight)-100;
            }
            // highest position possible 
            if (flappyheight + self.flappyV < 10){
                // System.out.println(p.flappyV);
                self.flappyV = -1*flappyheight+10;
            }
            // Pipe-action
            for (int i=0; i<self.pipeList.length; ++i){ // for all pipe
                // for the pipe which exist
                if (self.pipeList[i] != null){
                    // move to left
                    self.pipeList[i].x-=5;
                    // get rid of pipe which out of frame
                    if (self.pipeList[i].x < -1*self.pipeList[i].getWidth()){  
                        self.pipeList[i] = null; // set pipe to null to make it reusable
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
        if ( self.hitbox != null ){
            self.hitbox.update();
        }
        // find new pipe builder 
        findNewBuilder(); 
        // repaint components (call paintComponent()) 
        repaint();
        Toolkit.getDefaultToolkit().sync(); //synchronizes the graphic state make it run smoothly
    }
    private void drawboarder(Graphics g){
        g.drawImage(boarder.getImage(), -30, -20, null); // redraw bird
    }
    private void drawbird(Graphics g) {
        if ( jumping ){
            g.drawImage(self.bird.getFlapping(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
            jumping = !jumping;
        }else{
            g.drawImage(self.bird.getImage(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
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
        if (self == pipeBuilder){
            self.buildPipe(player);
        }else{
            self.takeTmp();
        }
        for (int i=0; i<self.pipeList.length; ++i){
            if (self.pipeList[i] != null){
                g.drawImage(self.pipeList[i].getTop(), self.pipeList[i].x, self.pipeList[i].yTop, null); 
                g.drawImage(self.pipeList[i].getBot(), self.pipeList[i].x, self.pipeList[i].yBot, null); 
            }
        }
    }
    private int position; // current bird Y position
    private void logic(Graphics g) {
        if ( self.hitbox != null ){
            position = self.getBirdPosition(flappyheight);
            if (
                    // check if top left side hits the pipe
                    self.hitbox.front <= self.bird.x && self.bird.x-15 <= self.hitbox.behind && position < self.hitbox.topLevel 
                    // check if top right side hits the pipe
                    || self.hitbox.front <= self.bird.x+self.bird.getWidth()-15 && self.bird.x <= self.hitbox.behind && position < self.hitbox.topLevel 
                    // check if bottom left side hits the pipe
                    || self.hitbox.front <= self.bird.x-15 && self.bird.x <= self.hitbox.behind && position+self.bird.getHeight()-10 > self.hitbox.botLevel
                    // check if bottom right side hits the pipe
                    || self.hitbox.front <= self.bird.x+self.bird.getWidth()-15 && self.bird.x <= self.hitbox.behind && position+self.bird.getHeight()-10> self.hitbox.botLevel
                    ){

                System.out.println("Hit");
                g.drawImage(self.bird.getStunning(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
                t.stop();
                t.start();
                self.hitbox = null; // remove hitbox from that pipe 
                return;
            }
            if ( self.bird.x >= self.hitbox.scoreLine ){
                System.out.println("Scored");
                self.score.plus(1);
                self.hitbox = null; // remove hitbox form that pipe
            }
        }
    }
    private void findNewBuilder(){
        for (int i=0; i<player.length; ++i){
            if (player[i].score.score > pipeBuilder.score.score){
                pipeBuilder = player[i];
                System.out.println("Current pipe builder is "+i);
                System.out.println("Current pipe builder score is "+pipeBuilder.score.score);
            }
        }
    }


    public void jump(){
        jumping = true;
        self.flappyA = -7;
    }
}
