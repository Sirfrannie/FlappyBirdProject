import game.*;

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
    private int selectedBirdSinglePlayer = -1;
    private int selectedBirdDualPlayer1 = -1;
    private int selectedBirdDualPlayer2 = -1;
    private JLabel instructionLabel;
    private JButton startButtonSinglePlayer;
    private JButton startButtonDualPlayer;

    public FlappyBirdMenu() {
        setTitle("Flappy Bird");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);

        cards = new JPanel(cardLayout);
        setLayout(new BorderLayout());
        add(cards, BorderLayout.CENTER);

        loadCustomFont("fonts/2005_iannnnnAMD.ttf");
        loadAndResizeBackground("img/menu_background.png");
        JPanel mainMenuPanel = prepareGUI();
        JPanel gameModesPanel = createGameModesPanel();
        JPanel singlePlayerBirdSelectionPanel = createSinglePlayerBirdSelectionPanel();
        JPanel dualPlayerBirdSelectionPanel = createDualPlayerBirdSelectionPanel();

        cards.add(mainMenuPanel, "MainMenu");
        cards.add(gameModesPanel, "GameModes");
        cards.add(singlePlayerBirdSelectionPanel, "SinglePlayerBirdSelection");
        cards.add(dualPlayerBirdSelectionPanel, "DualPlayerBirdSelection");

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

        String[] buttonLabels = {"PLAY", "EXIT"};
        int yPos = 200;
        for (String label : buttonLabels) {
            JButton button = createMenuButton(label);
            button.setBounds(540, yPos, 200, 50);
            button.addActionListener(e -> {
                if ("PLAY".equals(label)) {
                    cardLayout.show(cards, "GameModes");
                } else if ("EXIT".equals(label)) {
                    System.exit(0);
                }
            });
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

        gameModesPanel.setPreferredSize(new Dimension(1280, 720));

        String[] modeButtons = {"SINGLEPLAYER", "DUAL PLAYER", "BACK"};
        int yPos = 200;
        for (String label : modeButtons) {
            JButton button = createMenuButton(label);
            button.setBounds(540, yPos, 200, 50);
            button.addActionListener(e -> {
                if ("SINGLEPLAYER".equals(label)) {
                    cardLayout.show(cards, "SinglePlayerBirdSelection");
                } else if ("DUAL PLAYER".equals(label)) {
                    cardLayout.show(cards, "DualPlayerBirdSelection");
                } else if ("BACK".equals(label)) {
                    cardLayout.show(cards, "MainMenu");
                }
            });
            yPos += 80;
            gameModesPanel.add(button);
        }

        return gameModesPanel;
    }

    private JPanel createSinglePlayerBirdSelectionPanel() {
        JPanel birdSelectionPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        birdSelectionPanel.setPreferredSize(new Dimension(1280, 720));

        JLabel titleLabel = new JLabel("Choose Your Bird", SwingConstants.CENTER);
        titleLabel.setFont(customFont.deriveFont(48f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 20, 1280, 60);
        birdSelectionPanel.add(titleLabel);

        String[] birdFiles = {"bird0.png", "bird1.png", "bird2.png", "bird3.png"};
        for (int i = 0; i < birdFiles.length; i++) {
            final int birdIndex = i;
            JButton birdButton = createBirdSelectionButton("game/img/bird/" + birdFiles[i]);
            birdButton.setBounds(380 + (i * 160), 200, 120, 90);
            birdButton.addActionListener(e -> {
                selectedBirdSinglePlayer = birdIndex;
                if (startButtonSinglePlayer == null) {
                    startButtonSinglePlayer = createStartButton(birdSelectionPanel, true);
                    birdSelectionPanel.add(startButtonSinglePlayer);
                    birdSelectionPanel.repaint();
                }
            });
            birdSelectionPanel.add(birdButton);
        }

        JButton backButton = createMenuButton("BACK");
        backButton.setBounds(60, 620, 200, 50);
        backButton.addActionListener(e -> {
            // Reset selection and remove START button if present
            selectedBirdSinglePlayer = -1;
            if (startButtonSinglePlayer != null) {
                birdSelectionPanel.remove(startButtonSinglePlayer);
                birdSelectionPanel.revalidate();
                birdSelectionPanel.repaint();
                startButtonSinglePlayer = null;
            }
            cardLayout.show(cards, "GameModes");
        });
        birdSelectionPanel.add(backButton);

        return birdSelectionPanel;
    }

    private JPanel createDualPlayerBirdSelectionPanel() {
        JPanel birdSelectionPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundScaled, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };
        birdSelectionPanel.setPreferredSize(new Dimension(1280, 720));

        instructionLabel = new JLabel("P1: Choose Your Bird", SwingConstants.CENTER);
        instructionLabel.setFont(customFont.deriveFont(48f));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setBounds(0, 20, 1280, 60);
        birdSelectionPanel.add(instructionLabel);

        String[] birdFiles = {"bird0.png", "bird1.png", "bird2.png", "bird3.png"};
        for (int i = 0; i < birdFiles.length; i++) {
            JButton birdButton = createBirdSelectionButton("game/img/bird/" + birdFiles[i]);
            birdButton.setBounds(380 + (i * 160), 200, 120,90);
            int finalI = i;
            birdButton.addActionListener(e -> {
                JLabel playerLabel = new JLabel();
                playerLabel.setFont(customFont.deriveFont(18f));
                playerLabel.setBounds(birdButton.getX(), birdButton.getY() + birdButton.getHeight(), birdButton.getWidth(), 30);
                playerLabel.setForeground(Color.WHITE);
                if (selectedBirdDualPlayer1 == -1) {
                    selectedBirdDualPlayer1 = finalI;
                    playerLabel.setText("P1");
                    birdSelectionPanel.add(playerLabel);
                    instructionLabel.setText("P2: Choose Your Bird");
                } else if (selectedBirdDualPlayer2 == -1 && finalI != selectedBirdDualPlayer1) {
                    selectedBirdDualPlayer2 = finalI;
                    playerLabel.setText("P2");
                    birdSelectionPanel.add(playerLabel);
                    if (startButtonDualPlayer == null) {
                        startButtonDualPlayer = createStartButton(birdSelectionPanel, false);
                        birdSelectionPanel.add(startButtonDualPlayer);
                    }
                }
                birdSelectionPanel.revalidate();
                birdSelectionPanel.repaint();
            });
            birdSelectionPanel.add(birdButton);
        }

        JButton backButton = createMenuButton("BACK");
        backButton.setBounds(60, 620, 200, 50);
        backButton.addActionListener(e -> {
            // Reset selections
            selectedBirdDualPlayer1 = -1;
            selectedBirdDualPlayer2 = -1;

            if (startButtonDualPlayer != null) {
                birdSelectionPanel.remove(startButtonDualPlayer);
                startButtonDualPlayer = null;
            }
            birdSelectionPanel.removeAll();
            JPanel newPanel = createDualPlayerBirdSelectionPanel();
            cards.add(newPanel, "DualPlayerBirdSelection");
            cardLayout.show(cards, "GameModes");
        });
        birdSelectionPanel.add(backButton);

        return birdSelectionPanel;
    }

    private JButton createBirdSelectionButton(String imagePath) {
        ImageIcon birdIcon = new ImageIcon(imagePath);
        JButton birdButton = new JButton(birdIcon);
        birdButton.setFocusPainted(false);
        birdButton.setBorderPainted(false);
        birdButton.setContentAreaFilled(false);
        birdButton.setOpaque(false);
        return birdButton;
    }

    private JButton createStartButton(JPanel panel, boolean isSinglePlayer) {
        JButton startButton = new JButton("START");
        startButton.setFont(customFont);
        startButton.setFocusPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(Color.ORANGE); 
        startButton.setOpaque(true);
        startButton.setBorderPainted(true);
        startButton.setBounds(panel.getWidth() - 250, panel.getHeight() - 65, 200, 50);
        startButton.addActionListener(e -> {
            if (isSinglePlayer) {
                startGame(0, selectedBirdSinglePlayer, selectedBirdSinglePlayer);
            } else {
                startGame(1, selectedBirdDualPlayer1, selectedBirdDualPlayer2);
            }
        });
        return startButton;
    }

    private void startGame(int mode, int bird1, int bird2) {
        this.setVisible(false);
        
        int[] birds = {bird1, bird2}; // Bird selections
        
        new Flappybird(mode, birds, this);
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
        label.setForeground(Color.WHITE);
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
                if (hoverSoundPlayer != null) {
                    hoverSoundPlayer.play();
                }
            }
        });

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
        private Clip clip;

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
