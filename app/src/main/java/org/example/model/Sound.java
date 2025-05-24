package org.example.model;

import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private Clip clip;
    private final String soundPath = "/sound/SDW.wav";

    public Sound() {
        InputStream is = getClass().getResourceAsStream(soundPath);
        if (is == null) {
            System.err.println("Sound file not found: " + soundPath);
        }
    }

    public void setFile() {
        try (InputStream is = getClass().getResourceAsStream(soundPath)) {
            if (is == null) {
                System.err.println("Cannot load sound file: " + soundPath);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

