import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;

public class Flappybird {
    private JFrame frame;
    private Panel panel[];
    private Player player[];
    public Flappybird(int mode) {
        frame = new JFrame();
        // setup Frame 
        // frame.setUndecorated(true);
        frame.pack();
        frame.setTitle("FlappyBird");
        frame.setSize(new Dimension(1280, 720));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(frame.getHeight()+" " + frame.getWidth());
        // implement KeyListener
        KeyListener k = new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
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
            }
            singlePlay(k);
        }
        // Dual player mode
        if ( mode == 1 ){
            player = new Player[2];
            panel = new Panel[2];
            for (int i=0; i<player.length; ++i){
                player[i] = new Player(i);
            }
            dualPanel(k);
        }
    }
    // implement single player mode
    private void singlePlay(KeyListener k){
        panel[0] = new Panel(0, KeyEvent.VK_SPACE, player, frame.getWidth(), frame.getHeight()-frame.getInsets().top);
        panel[0].p.addActionListener((e) -> {
            panel[0].t.stop();
            panel[0].requestFocusInWindow();
        });
        panel[0].s.addActionListener((e) -> {
            panel[0].t.start();
            panel[0].requestFocusInWindow();
        });
        frame.add(panel[0]);
        frame.requestFocusInWindow();
        frame.addKeyListener(k);
    }
    // implement dual player mode
    private void dualPanel(KeyListener k){
        panel[0] = new Panel(0, KeyEvent.VK_SPACE, player, frame.getWidth(), frame.getHeight());
        panel[1] = new Panel(1, KeyEvent.VK_UP, player, frame.getWidth(), frame.getHeight());
        for ( int i=0; i<panel.length; ++i){
            frame.add(panel[i]);
        }
        panel[0].p.addActionListener((e) -> {
            panel[0].t.stop();
            panel[1].t.stop();
            frame.requestFocusInWindow();
        });
        panel[0].s.addActionListener((e) -> {
            panel[0].t.start();
            panel[1].t.start();
            frame.requestFocusInWindow();
        });
        frame.requestFocusInWindow();
        frame.addKeyListener(k);
    }
    
}

