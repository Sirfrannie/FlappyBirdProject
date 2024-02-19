import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

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
