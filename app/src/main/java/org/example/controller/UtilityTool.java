package org.example.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;

public class UtilityTool {

    public BufferedImage scaleImage(BufferedImage originalImage, int width, int height) {
        if (originalImage == null) {
            System.err.println("UtilityTool: originalImage is null in scaleImage. Cannot scale.");
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        
        // UBAH METODE INTERPOLASI UNTUK PIXEL ART
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // Anda bisa menghapus hint rendering dan antialiasing lainnya jika hanya fokus pada pixel art
        // g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); // Bisa dihapus
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Bisa dihapus

        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        
        return scaledImage;
    }
}