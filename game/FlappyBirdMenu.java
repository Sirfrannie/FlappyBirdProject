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
    private JPanel cards;
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
        JPanel singlePlayerPanel = createSinglePlayerPanel(); // Singleplayer Panel
        JPanel dualPlayerPanel = createDualPlayerPanel(); // Dual Panel

        cards.add(mainMenuPanel, "MainMenu");
        cards.add(gameModesPanel, "GameModes");
        cards.add(singlePlayerPanel, "SinglePlayer");
        cards.add(dualPlayerPanel, "DualPlayer");

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
        JPanel gameModesPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        String[] modeButtons = {"SINGLEPLAYER", "DUAL", "BACK"};
        int yPos = 200;

        for (String label : modeButtons) {
            JButton button = createMenuButton(label);
            button.setBounds(540, yPos, 200, 50);
            yPos += 80;

            if ("SINGLEPLAYER".equals(label)) {
                button.addActionListener(e -> cardLayout.show(cards, "SinglePlayer"));
            } else if ("DUAL".equals(label)) {
                button.addActionListener(e -> cardLayout.show(cards, "DualPlayer"));
            } else if ("BACK".equals(label)) {
                button.addActionListener(e -> cardLayout.show(cards, "MainMenu"));
            }
             
            gameModesPanel.add(button);
        }

        gameModesPanel.setPreferredSize(new Dimension(1280, 720));
        return gameModesPanel;
    }
    private JPanel createSinglePlayerPanel() {
        
        JPanel singlePlayerPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
    
        int middleX = (1280 - 200) / 2; 
        int middleY = (720 - 200) / 2;
    
        JButton onePlayerButton = createMenuButtonWithIcon("1P", "img/bluebird-midflap.png");
        onePlayerButton.setBounds(middleX, middleY, 200, 100);
        singlePlayerPanel.add(onePlayerButton);
    
        int backButtonX = 60; 
        int startButtonX = 1280 - 260;
        int buttonsY = 720 - 120; 
    
        JButton backButton = createMenuButton("BACK");
        backButton.setBounds(backButtonX, buttonsY, 200, 50);
        backButton.addActionListener(e -> cardLayout.show(cards, "GameModes"));
        singlePlayerPanel.add(backButton);
    
        JButton startButton = new JButton("START");
        startButton.setFont(customFont);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);
        startButton.setBorderPainted(true);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(startButtonX, buttonsY, 200, 50);
        startButton.addActionListener(e -> {
            if (backgroundMusicPlayer != null) {
                backgroundMusicPlayer.stop();
            }

        FlappyBirdMenu.this.dispose();
        // Close the menu
        // Start the Flappy Bird game for singleplayer
        String[] args = {};
        // Flappybird.main(args);
        new Flappybird(1, 0); // 1 player mode number 0 (single player)
        });

        singlePlayerPanel.add(startButton);

    
        // MouseListenerfor the button sound effect
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                hoverSoundPlayer.play();
            }
        });
    
        singlePlayerPanel.add(startButton);
    
        return singlePlayerPanel;
    }
    
    private JPanel createDualPlayerPanel() {
        JPanel dualPlayerPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        // "1P" and "2P" buttons
        int middleX = (1280 - 400) / 2; 
        int middleY = (720 - 200) / 2;

        JButton onePlayerButton = createMenuButtonWithIcon("1P", "img/bluebird-midflap.png");
        onePlayerButton.setBounds(middleX, middleY, 200, 100);
        dualPlayerPanel.add(onePlayerButton);

        JButton twoPlayerButton = createMenuButtonWithIcon("2P", "img/bluebird-midflap.png");
        twoPlayerButton.setBounds(middleX + 200, middleY, 200, 100);
        dualPlayerPanel.add(twoPlayerButton);

        int buttonsY = 720 - 120;
        JButton backButton = createMenuButton("BACK");
        backButton.setBounds(60, buttonsY, 200, 50);
        backButton.addActionListener(e -> cardLayout.show(cards, "GameModes"));
        dualPlayerPanel.add(backButton);

        JButton startButton = new JButton("START");
        startButton.setFont(customFont);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setOpaque(true);
        startButton.setBorderPainted(true);
        startButton.setBackground(Color.ORANGE);
        startButton.setForeground(Color.BLACK);
        startButton.setBounds(1280 - 260, buttonsY, 200, 50);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                hoverSoundPlayer.play();
            }
        });
        dualPlayerPanel.add(startButton);

        return dualPlayerPanel;
    }

    private JButton createMenuButtonWithIcon(String text, String iconFilePath) {
        JButton button = new JButton(text, new ImageIcon(iconFilePath));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFont(customFont);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setBackground(Color.ORANGE);
        button.setForeground(Color.BLACK);
        return button;
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
    }

    public class SoundPlayer {
        private String soundFilePath;
        private boolean loop;
        private Clip clip; // Make clip an instance variable to control it outside of play method
    
        public SoundPlayer(String soundFilePath, boolean loop) {
            this.soundFilePath = soundFilePath;
            this.loop = loop;
        }
    
        public void play() {
            try {
                File soundFile = new File(soundFilePath);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                clip = AudioSystem.getClip();
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
    
        // Method to stop the music
        public void stop() {
            if (clip != null) {
                clip.stop();
                clip.close();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlappyBirdMenu::new);
    }
}
