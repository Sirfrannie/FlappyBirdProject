import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class FlappyBirdMenu extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards; // Container for card layout
    private Font customFont;
    private BufferedImage backgroundScaled;
    private SoundPlayer backgroundMusicPlayer;
    private SoundPlayer hoverSoundPlayer;

    public FlappyBirdMenu() {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        cards = new JPanel(cardLayout);
        setLayout(new BorderLayout());
        add(cards);

        loadCustomFont("fonts/2005_iannnnnAMD.ttf");
        loadAndResizeBackground("img/menu_background.png");
        JPanel mainMenuPanel = prepareGUI();
        JPanel gameModesPanel = createGameModesPanel();

        cards.add(mainMenuPanel, "MainMenu");
        cards.add(gameModesPanel, "GameModes");

        initializeSounds();
        setVisible(true);
    }

    private JPanel prepareGUI() {
        JPanel menuPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        menuPanel.setPreferredSize(new Dimension(1280, 720));
        
        JLabel titleLabel = createTitleLabel("FLAPPY BIRD");
        titleLabel.setBounds(340, 20, 600, 150);
        menuPanel.add(titleLabel);

        String[] buttonLabels = {"PLAY", "LEADERBOARD", "OPTIONS", "CREDITS", "EXIT"};
        int yPos = 200;
        for (String label : buttonLabels) {
            JButton button = createMenuButton(label);
            button.setBounds(540, yPos, 200, 50);
            if ("EXIT".equals(label)) {
                button.addActionListener(e -> System.exit(0));
            }
            yPos += 80;
            menuPanel.add(button);
        }
        return menuPanel;
    }

    private JPanel createGameModesPanel() {
        // Create a panel with null layout for absolute positioning
        JPanel gameModesPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the same background image
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
    
        // Array of game mode button labels
        String[] modeButtons = {"SINGLEPLAYER", "DUAL", "MULTIPLAYER", "BACK"};
        int yPos = 200; // Initial Y position for the first button
    
        // Create and add buttons to the panel
        for (String label : modeButtons) {
            JButton button = createMenuButton(label);
            button.setBounds(540, yPos, 200, 50); // Match the main menu button size and positioning
            yPos += 80; // Increment Y position for the next button
    
            if ("BACK".equals(label)) {
                button.addActionListener(e -> cardLayout.show(cards, "MainMenu"));
            }
            // Optionally add actions for other buttons here
    
            gameModesPanel.add(button);
        }
    
        gameModesPanel.setPreferredSize(new Dimension(1280, 720)); // Ensure panel size matches the frame
        return gameModesPanel;
    }

    private void loadCustomFont(String fontFileName) {
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFileName)).deriveFont(24f);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 24);
        }
    }

    private void loadAndResizeBackground(String imagePath) {
        try {
            backgroundScaled = ImageIO.read(new File(imagePath));
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
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setBackground(Color.ORANGE);
        button.setForeground(Color.BLACK);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                hoverSoundPlayer.play();
            }
        });

        if ("PLAY".equals(text)) {
            button.addActionListener(e -> cardLayout.show(cards, "GameModes"));
        }

        return button;
    }

    private void initializeSounds() {
        backgroundMusicPlayer = new SoundPlayer("audios/bgm_menu.wav", true);
        hoverSoundPlayer = new SoundPlayer("audios/button_hover.wav", false);
        backgroundMusicPlayer.play();
        // Optionally start playing background music here if desired
        // backgroundMusicPlayer.play();
    }

    private class SoundPlayer {
        private String soundFilePath;
        private boolean loop;

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
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlappyBirdMenu::new);
    }
}