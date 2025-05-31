package org.example.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.awt.FontFormatException;
import java.util.List;

import org.example.controller.action.ChattingAction;
import org.example.model.Farm;

public class ChattingDialogPanel extends JPanel {
    private JFrame parentFrame;
    private List<String> dialogLines;
    private int currentLine = 0;
    private JTextPane dialogPane;
    private JButton nextButton;
    private Image npcBackground;
    private Image playerBackground;
    private boolean isNpcTurn = true;
    private String playerName;
    private String npcName;
    private Font customFont;

    public ChattingDialogPanel(JFrame parentFrame, List<String> dialogLines, String npcName, String playerName, ChattingAction action, Farm farm) {
        this.parentFrame = parentFrame;
        this.dialogLines = dialogLines;
        this.playerName = playerName;
        this.npcName = npcName;

        setLayout(null);
        setPreferredSize(new Dimension(480, 240));
        setOpaque(false);

        try {
            npcBackground = ImageIO.read(getClass().getResourceAsStream("/box/" + npcName + ".png"));
            playerBackground = ImageIO.read(getClass().getResourceAsStream("/box/box.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            customFont = new Font("Arial", Font.PLAIN, 12);
        }

        // ⬇️ Ganti jadi JTextPane
        dialogPane = new JTextPane();
        dialogPane.setEditable(false);
        dialogPane.setOpaque(false);
        dialogPane.setForeground(Color.WHITE);
        dialogPane.setFont(customFont.deriveFont(10f));
        dialogPane.setFocusable(false);
        dialogPane.setHighlighter(null);
        dialogPane.setBounds(30, 70, 240, 100); // default NPC area
        add(dialogPane);

        // Tombol Next
        nextButton = new JButton("Next");
        nextButton.setBounds(160, 180, 100, 30);
        nextButton.setFont(customFont.deriveFont(10f));
        nextButton.setFocusPainted(false);
        nextButton.setBackground(new Color(139, 69, 19));
        nextButton.setForeground(Color.WHITE);
        add(nextButton);

        nextButton.addActionListener((ActionEvent e) -> {
            if (currentLine < dialogLines.size()) {
                String speaker = isNpcTurn ? npcName : playerName;
                String text = speaker + ": " + dialogLines.get(currentLine);

                if (isNpcTurn) {
                    dialogPane.setFont(customFont.deriveFont(10f));
                    dialogPane.setBounds(30, 70, 240, 100);
                } else {
                    dialogPane.setFont(customFont.deriveFont(11f));
                    dialogPane.setBounds(50, 70, 380, 100);
                }

                dialogPane.setText(text);
                isNpcTurn = !isNpcTurn;
                currentLine++;
                repaint();
            } else {
                SwingUtilities.getWindowAncestor(this).dispose();
            }
        });

        action.execute(farm);
        nextButton.doClick();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image background = isNpcTurn ? playerBackground : npcBackground;
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
