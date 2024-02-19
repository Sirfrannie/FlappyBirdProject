import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.awt.Graphics;
import java.awt.GridLayout;

public class Flappybird {
    private JFrame frame;
    private Panel panel[];
    private Player player[];
    private JButton b = new JButton("test");
    public Flappybird(int mode, int bird[]) {
        frame = new JFrame();
        // setup Frame 
        frame.pack();
        frame.setTitle("FlappyBird");
        frame.setSize(new Dimension(1280, 720));
        frame.setLayout(new GridLayout(1, mode+1, 0, 0));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Graphics g = frame.getGraphics();
        // implement KeyListener
        KeyListener k = new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ESCAPE){
                    for (int i=0; i<panel.length; ++i){
                        panel[i].doPause();
                    }
                    frame.requestFocusInWindow();
                    return;
                }
                // if code is a jump button then make a bird who uses that code jump
                for (int i=0; i<panel.length; ++i){
                    if (code == panel[i].self.jump){
                        panel[i].jump();
                        frame.requestFocusInWindow();
                    } 
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        };
        // Single player mode 
        if ( mode == 0 ){
            player = new Player[1];
            panel = new Panel[1];
            for (int i=0; i<player.length; ++i){
                player[i] = new Player(i);
                player[i].bird = new Bird(bird[i]);
            }
            singlePlay(k);
        }
        // Dual player mode
        if ( mode == 1 ){
            player = new Player[2];
            panel = new Panel[2];
            for (int i=0; i<player.length; ++i){
                player[i] = new Player(i);
                player[i].bird = new Bird(bird[i]);
            }
            dualPanel(k);
        }
    }
    // implement single player mode
    private void singlePlay(KeyListener k){
        panel[0] = new Panel(0, KeyEvent.VK_SPACE, player, frame.getWidth(), frame.getHeight()-frame.getInsets().top);
        panel[0].pauseButton.addActionListener((e) -> {
            panel[0].doPause();
            frame.requestFocusInWindow();
        });
        panel[0].resumeButton.addActionListener((e) -> {
            panel[0].doPause();
            frame.requestFocusInWindow();
        });
        panel[0].exitButton.addActionListener((e) -> {
            frame.dispose();
            frame.requestFocusInWindow();
        });
        frame.add(panel[0]);
        frame.requestFocusInWindow();
        frame.addKeyListener(k);
    }
    // implement dual player mode
    private void dualPanel(KeyListener k){
        panel[0] = new Panel(0, KeyEvent.VK_SPACE, player, frame.getWidth(), frame.getHeight()-frame.getInsets().top);
        panel[1] = new Panel(1, KeyEvent.VK_UP, player, frame.getWidth(), frame.getHeight()-frame.getInsets().top);
        for ( int i=0; i<panel.length; ++i){
            frame.add(panel[i]);
        }
        panel[0].pauseButton.addActionListener((e) -> {
            panel[0].doPause();
            frame.requestFocusInWindow();
        });
        panel[1].pauseButton.addActionListener((e) -> {
            panel[1].doPause();
            frame.requestFocusInWindow();
        });
        panel[0].resumeButton.addActionListener((e) -> {
            panel[0].doPause();
            frame.requestFocusInWindow();
        });
        panel[1].resumeButton.addActionListener((e) -> {
            panel[1].doPause();
            frame.requestFocusInWindow();
        });
        panel[0].exitButton.addActionListener((e) -> {
            frame.dispose();
            frame.requestFocusInWindow();
        });
        panel[1].exitButton.addActionListener((e) -> {
            frame.dispose();
            frame.requestFocusInWindow();
        });
        frame.addKeyListener(k);
        frame.requestFocusInWindow();
    }
    
}

