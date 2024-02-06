import javax.swing.*;

public class Flappybird {
    public static Flappybird flappyBird;

    public Flappybird() {
        JFrame jframe = new JFrame();
        Panel panel = new Panel();

        jframe.add(panel);
        jframe.setTitle("FlappyBird");
        jframe.setSize(1280, 720);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        flappyBird = new Flappybird();
    }
}
