import javax.swing.*;
import java.awt.Dimension;

public class Flappybird {
    public Flappybird() {
        JFrame jframe = new JFrame();
        Panel panel = new Panel();

        jframe.add(panel);
        jframe.setTitle("FlappyBird");
        jframe.setSize(new Dimension(1280, 720));
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Flappybird();
    }
}
