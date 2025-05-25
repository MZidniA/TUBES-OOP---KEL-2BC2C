package org.example.controller;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class UtilityTool {
     
    public BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        BufferedImage bufferedScaledImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = bufferedScaledImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return bufferedScaledImage;
    }
}
