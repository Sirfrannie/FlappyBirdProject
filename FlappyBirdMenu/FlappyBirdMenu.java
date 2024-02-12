import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class FlappyBirdMenu extends JFrame {

    private Font customFont;
    private BufferedImage backgroundScaled;
    private SoundPlayer backgroundMusicPlayer;
    private SoundPlayer hoverSoundPlayer;

    public FlappyBirdMenu() {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1280, 720);
        setLocationRelativeTo(null);

        loadCustomFont("fonts/2005_iannnnnAMD.ttf");
        loadAndResizeBackground("img/menu_background.png");
        prepareGUI();
        initializeSounds();

        setVisible(true);
    }

    private void prepareGUI() {
        setContentPane(new JLabel(new ImageIcon(backgroundScaled)));
        getLayeredPane().setLayout(null);

        JLabel titleLabel = createTitleLabel("FLAPPY BIRD");
        titleLabel.setBounds(340, 20, 600, 150);
        getLayeredPane().add(titleLabel, Integer.valueOf(2));

        String[] buttonLabels = {"PLAY", "LEADERBOARD", "OPTIONS", "CREDITS", "EXIT"};
        int yPos = 200;
        for (String label : buttonLabels) {
            JButton button = createMenuButton(label);
            button.setBounds(540, yPos, 200, 50);
            yPos += 80;
            getLayeredPane().add(button, Integer.valueOf(2));
        }
    }

    private void initializeSounds() {
        backgroundMusicPlayer = new SoundPlayer("audios/bgm_menu.wav", true); // BGM หน้าเมนู
        backgroundMusicPlayer.play();

        hoverSoundPlayer = new SoundPlayer("audios/button_hover.wav", false); // เสียงเมื่อ Mouse cursor แตะปุ่ม
    }

    private void loadCustomFont(String fontFileName) {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileName)).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 24); // ฟอนต์ Arial สำรองไว้
        }
    }

    private void loadAndResizeBackground(String imagePath) {
        try {
            BufferedImage originalBackground = ImageIO.read(new File(imagePath));
            backgroundScaled = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = backgroundScaled.createGraphics();
            g2d.drawImage(originalBackground, 0, 0, 1280, 720, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(customFont.deriveFont(100f));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(customFont);
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setBackground(Color.ORANGE); // สีกรอบของปุ่ม
        button.setForeground(Color.BLACK); // สีข้อความของปุ่ม

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                hoverSoundPlayer.play();
            }
        });

        // Actions ของปุ่ม "EXIT"
        if ("EXIT".equals(text)) {
            button.addActionListener(e -> System.exit(0));
        }

        return button;
    }

    private class SoundPlayer {
        private final String soundFilePath;
        private final boolean loop;

        public SoundPlayer(String soundFilePath, boolean loop) {
            this.soundFilePath = soundFilePath;
            this.loop = loop;
        }

        public void play() {
            try {
                File soundFile = new File(soundFilePath);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                if (loop) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlappyBirdMenu::new);
    }
}
