import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Flappybird {
    private JFrame frame;
    private Panel panel[];
    private Player player[];
    public Flappybird(int numberOfPlayer, int mode) {
        frame = new JFrame();
        player = new Player[numberOfPlayer];
        panel = new Panel[numberOfPlayer];
        for (int i=0; i<player.length; ++i){
            player[i] = new Player(i);
        }
        frame.setTitle("FlappyBird");
        frame.setSize(new Dimension(1280, 720));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        KeyListener k = new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
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
        if ( mode == 0 ){
            singlePlay(k);
        }
        if ( mode == 1 ){
            dualPanel(k);
        }
    }
    private void singlePlay(KeyListener k){
        panel[0] = new Panel(0, KeyEvent.VK_SPACE, player);
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
    private void dualPanel(KeyListener k){
        panel[0] = new Panel(0, KeyEvent.VK_SPACE, player);
        panel[1] = new Panel(1, KeyEvent.VK_UP, player);
        for ( int i=0; i<panel.length; ++i){
            frame.add(panel[i]);
        }
        panel[0].p.addActionListener((e) -> {
            panel[0].t.stop();
            frame.requestFocusInWindow();
        });
        panel[0].s.addActionListener((e) -> {
            panel[0].t.start();
            frame.requestFocusInWindow();
        });
        panel[1].p.addActionListener((e) -> {
            panel[1].t.stop();
            frame.requestFocusInWindow();
        });
        panel[1].s.addActionListener((e) -> {
            panel[1].t.start();
            frame.requestFocusInWindow();
        });
        frame.requestFocusInWindow();
        frame.addKeyListener(k);
    }

    public static void main(String[] args) {
        // 
        new Flappybird(2, 1);
    }
    
}
