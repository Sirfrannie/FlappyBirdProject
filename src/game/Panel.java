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
    private Background bg[]; // background array
    private Boarder boarder; 
    private Player player[]; // array of all player
    public Player self; // primary player ( who use this Panel )
    // pipe builder's duty is to make Pipe for itself and another player 
    // for all pipe that all player face must be same 
    private Player pipeBuilder; 
    // frame width and height
    private int WIDTH, HEIGHT;

    // components detail
    private int bgWidth;
    private int flappyheight; // the initial height of bird from ground 

    private boolean gameOver = false; // if game Over
    private boolean isPlaying = true; // if player playing
    private boolean firstStageBg = true; // no comments
    private boolean jumping = false; // if the bird is jumping

    public Timer t;
    // pause and continue button
    public JButton p = new JButton("pause");
    public JButton s = new JButton("continue");

    public Panel(int id, int jump, Player player[], int frameWidth, int frameHeight) {
        this.WIDTH = frameWidth;
        this.HEIGHT = frameHeight;
        setFocusable(true);
        // enable Double buffered feature make animation run more smoothly
        setDoubleBuffered(true);
        // precreate 6 backgrounds ahead
        bg = new Background[6];

        // reference player array
        this.player = player;
        // declare primary player
        this.self = player[id];
        self.jump = jump;
        // set Scoreboard Position
        self.score.positionX = (WIDTH/player.length)-100;
        self.heartBar.positionX = ((WIDTH/player.length)/2)-100;
        // first Pipe builder
        pipeBuilder = player[0]; 

        // stop and continue button
        p.setBounds(10, 5, 30, 30);
        s.setBounds(45, 5, 30, 30);
        // add Button only for Fist Panel
        if (id == 0){
            this.add(p);
            this.add(s);
        }

        // set Panel Size and Location
        this.setBounds((int)(0+((WIDTH/2)*id)), 0, (int)(WIDTH/player.length), HEIGHT);
        // Border frame
        boarder = new Boarder(1280, 720);
        // set scale of boarder
        boarder.img = boarder.setScale(boarder.getImage(), WIDTH, HEIGHT);

        flappyheight = (HEIGHT/2)+(self.bird.getHeight()/2);
        t = new Timer(40, this);
        t.start();
        // to delay time before Timer start
        t.setInitialDelay(1000);
    }
    @Override 
    public void paintComponent(Graphics g) {
        if (!gameOver) { // while the game is poccessing
            drawbg(g);
            drawbird(g);
            drawpipe(g);
            if (isPlaying){ // while player is playing
                self.buildHitbox();
                logic(g);
            }
            // drawboarder(g);
            self.heartBar.drawHeart(g);
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
            // limits lowest position 
            if (self.flappyV > (HEIGHT-flappyheight)-100){
                self.flappyV = (HEIGHT-flappyheight)-100;
            }
            // limits highest position 
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
        // paint Boarder
        g.drawImage(boarder.getImage(), 0, 0, null); 
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
                // decrease player's heart 
                self.heartBar.decrease();
                // draw dizzing img
                g.drawImage(self.bird.getStunning(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
                // to freeze the Screen
                t.stop();
                t.start();
                self.hitbox = null; // remove hitbox from that pipe 
                // if player has no remain heart then game OVER
                if ( self.heartBar.numOfHeart == 0 ){
                    gameOver = true;
                }
                return;
            }
            if ( self.bird.x >= self.hitbox.scoreLine ){ // if the bird passed scoreLine
                System.out.println("Scored");
                // add score by one 
                self.score.plus(1);
                self.hitbox = null; // remove hitbox form that pipe
            }
        }
    }
    // find new Pipe builder by finding highest score player
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
