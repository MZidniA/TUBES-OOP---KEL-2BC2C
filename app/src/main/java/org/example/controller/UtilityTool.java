package org.example.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class UtilityTool {

    public BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        if (originalImage == null) {
            System.err.println("UtilityTool: originalImage is null in scaleImage. Cannot scale.");
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        
        return scaledImage;
    }
}