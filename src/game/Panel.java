package game;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Insets;
import java.awt.Color;

public class Panel extends JPanel implements ActionListener{
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
    private int baseWidth;
    private int flappyheight; // the initial height of bird from ground 

    private Background bg[]; // background array
    private Base base[];
    private BufferedImage gameOverIcon;  
    private BufferedImage startImage;
    private BufferedImage dimpBackground;
    private BufferedImage flappyIcon;

    
    private boolean gameOver = false; // if game Over
    private boolean isPlaying = false; // if player playing
    private boolean firstStageBg = true; // no comments
    private boolean firstStageBase = true; // like Bg 
    private boolean jumping = false; // if the bird is jumping
    private boolean started = false; // if the game has started
    private boolean onPausing = false; // if game pausing

    private Timer t;
    // pause and continue button
    public JButton pauseButton = new JButton("⏸");
    public JButton exitButton = new JButton("exit");
    public JButton resumeButton = new JButton("resume");

    public Panel(int id, int jump, Player player[], int frameWidth, int frameHeight) {
        this.WIDTH = frameWidth;
        this.HEIGHT = frameHeight;
        // set Panel Size and Location
        this.setBounds((int)(0+((WIDTH/2)*id)), 0, WIDTH/player.length, HEIGHT);
        setFocusable(true);
        // enable Double buffered feature make animation run more smoothly
        setDoubleBuffered(true);
        this.setLayout(null);

        // precreate 6 backgrounds and base ahead 
        bg = new Background[6];
        base = new Base[6];

        // loading Image
        try {
            startImage = ImageIO.read(new File("game/img/message.png")); 
            gameOverIcon = ImageIO.read(new File("game/img/gameover.png")); 
            dimpBackground = ImageIO.read(new File("game/img/dimp.png")); 
            flappyIcon = ImageIO.read(new File("game/img/flappybirdlogo.png"));
        }catch (IOException e){
            e.printStackTrace();
        }

        // reference player array
        this.player = player;
        // declare primary player
        this.self = player[id];
        self.jump = jump;
        // set Scoreboard Position
        self.score.positionX = (WIDTH/player.length)-100;
        self.heartBar.positionX = (WIDTH/player.length)-280;
        // first Pipe builder
        pipeBuilder = player[0]; 

        // setup JButton 
        // pauseButton.setPreferredSize(new Dimension(30, 30));
        pauseButton.setBounds(10, 10, 30, 30);
        pauseButton.setMargin(new Insets(0, 0, 0, 0));
        pauseButton.setForeground(Color.cyan);
        pauseButton.setBackground(Color.blue);

        // resumeButton.setPreferredSize(new Dimension(120, 50));
        resumeButton.setBounds((this.getWidth()/2)-60, (this.getHeight()/2)-25, 120, 50);
        resumeButton.setMargin(new Insets(0, 0, 0, 0));
        resumeButton.setForeground(Color.cyan);
        resumeButton.setBackground(Color.blue);
        
        // exitButton.setPreferredSize(new Dimension(120, 50));
        exitButton.setBounds((this.getWidth()/2)-60, (this.getHeight()/2)+40, 120, 50);
        exitButton.setMargin(new Insets(0, 0, 0, 0));
        exitButton.setForeground(Color.cyan);
        exitButton.setBackground(Color.blue);
        this.add(exitButton);
        this.add(resumeButton);


        this.add(pauseButton);
        exitButton.setVisible(false);
        resumeButton.setVisible(false);

        // Border frame
        boarder = new Boarder(1280, 720);
        // set scale of boarder
        boarder.img = boarder.setScale(boarder.getImage(), WIDTH/player.length, HEIGHT);

        flappyheight = (HEIGHT/2)+(self.bird.getHeight()/2);
        t = new Timer(40, this);
        t.start();
        // to delay time before Timer start
        t.setInitialDelay(1000);
    }
    @Override 
    public void paintComponent(Graphics g) {
        drawbg(g);
        drawpipe(g);
        drawbase(g);
        drawbird(g);
        drawboarder(g);
        if (!started){
            drawStart(g);
            return;
        }
        if (onPausing){
            drawPausing(g);
            return;
        }
        if (!gameOver) { // while the game is poccessing
            if (isPlaying){ // while player is playing
                self.buildHitbox();
                logic(g);
            }
            self.heartBar.drawHeart(g);
            self.score.update(g);
        }else{
            drawGameOver(g);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying && !gameOver && !onPausing){
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
            // background-action
            for (int i=0; i<bg.length; ++i){
                // moving
                if (bg[i] != null){
                    bg[i].x-=4;
                    // get rid of it
                    if (bg[i].x < -1*bg[i].getWidth()-50){
                        bg[i].outFrame = true; // mark that background is already out of frame
                    }
                }
            }
            // base-action
            for (int i=0; i<base.length; ++i){
                // moving
                if (base[i] != null){
                    base[i].x-=4;
                    // get rid of it
                    if (base[i].x < -1*base[i].getWidth()-50){
                        base[i].outFrame = true; // mark that background is already out of frame
                    }
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
    private void drawPausing(Graphics g){
        // dimp background
        g.drawImage(dimpBackground, 0, 0, null);
        g.drawImage(flappyIcon, (this.getWidth()/2)-(flappyIcon.getWidth()/2), 200, null);
    }   
    private void drawGameOver(Graphics g){
        // dimp background
        g.drawImage(dimpBackground, 0, 0, null);
        // draw StartImage
        g.drawImage(gameOverIcon, (this.getWidth()/2)-(startImage.getWidth()/2), (this.getHeight()/2)-(startImage.getHeight()/2), null);
        t.stop();
        exitButton.setVisible(true);
    }   
    private void drawStart(Graphics g){
        // dimp background
        g.drawImage(dimpBackground, 0, 0, null);
        // draw StartImage
        g.drawImage(startImage, (this.getWidth()/2)-(startImage.getWidth()/2), (this.getHeight()/2)-(startImage.getHeight()/2), null);
    }   
    private void drawboarder(Graphics g){
        // paint Boarder
        g.drawImage(boarder.getImage(), 0, 0, null); 
    }
    private void drawbird(Graphics g) {
        // while not playing draw bird at same height level
        if ( !isPlaying ){
            g.drawImage(self.bird.getImage(), self.bird.x, flappyheight, null); // redraw bird
        }
        // to draw jumping image
        if ( jumping ){
            g.drawImage(self.bird.getFlapping(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
            jumping = !jumping;
        }else{
            // normal action
            g.drawImage(self.bird.getImage(), self.bird.x, flappyheight + self.flappyV, null); // redraw bird
        }
    }
    private void drawbase(Graphics g){
        if (firstStageBase){
            for (int i=0; i<base.length; ++i){
                if (base[i] == null){
                    base[i] = new Base();
                    // make each bg continue 
                    base[i].x += base[i].getWidth()*i;
                }
                g.drawImage(base[i].getImage(), base[i].x, (HEIGHT-60), null); // draw bg
            }    
            baseWidth = base[0].getWidth();
            firstStageBase = false; // end initialize stage 
        }else{
            for (int i=0; i<base.length; ++i){
                // if base is out of frame 
                if (base[i].outFrame == true){
                    // check if the first one in array
                    if ( i == 0){
                        // make it continue next to the last one 
                        base[i].x = base[bg.length-1].x+baseWidth;
                        base[i].outFrame = false;
                    }else{
                        // make it continue next to one before it 
                        base[i].x = base[i-1].x+baseWidth;
                        base[i].outFrame = false;
                    }
                }
                g.drawImage(base[i].getImage(), base[i].x, (HEIGHT-60), null); // redraw base
            }    
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
                // if bg is out of frame 
                if (bg[i].outFrame == true){
                    // check if the first one in array
                    if ( i == 0){
                        // make it continue next to the last one 
                        bg[i].x = bg[bg.length-1].x+bgWidth;
                        bg[i].outFrame = false;
                    }else{
                        // make it continue next to one before it 
                        bg[i].x = bg[i-1].x+bgWidth;
                        bg[i].outFrame = false;
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
                    self.hitbox.front <= self.bird.x && self.bird.x+20 <= self.hitbox.behind && position < self.hitbox.topLevel 
                    // check if top right side hits the pipe
                    || self.hitbox.front <= self.bird.x+self.bird.getWidth()-15 && self.bird.x <= self.hitbox.behind && position < self.hitbox.topLevel 
                    // check if bottom left side hits the pipe
                    || self.hitbox.front <= self.bird.x-15 && self.bird.x <= self.hitbox.behind && position+self.bird.getHeight()-10 > self.hitbox.botLevel
                    // check if bottom right side hits the pipe
                    || self.hitbox.front <= self.bird.x+self.bird.getWidth()-15 && self.bird.x <= self.hitbox.behind && position+self.bird.getHeight()-10> self.hitbox.botLevel
                    ){

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
                    self.alive = false;
                    self.score.setZero();
                }
                return;
            }
            if ( self.bird.x >= self.hitbox.scoreLine ){ // if the bird passed scoreLine
                // add score by one 
                self.score.plus(1);
                self.hitbox = null; // remove hitbox form that pipe
            }
        }
    }
    // find new Pipe builder by finding highest score player
    private void findNewBuilder(){
        for (int i=0; i<player.length; ++i){
            if (player[i].score.score > pipeBuilder.score.score && player[i].alive){
                pipeBuilder = player[i];
                // System.out.println("Current pipe builder is "+i);
                // System.out.println("Current pipe builder score is "+pipeBuilder.score.score);
            }
        }
    }

    public void doPause(){
        if ( onPausing ){
            // hide button while playing
            resumeButton.setVisible(false);
            exitButton.setVisible(false);
            onPausing = !onPausing;
        }else{
            // reveal button when on pause screen
            resumeButton.setVisible(true);
            exitButton.setVisible(true);
            onPausing = !onPausing;
        }
    }
    public void jump(){
        if (!isPlaying){
            isPlaying = true;
        }
        if (!started){
            started = true;
        }
        jumping = true;
        self.flappyA = -7;
    }
}
