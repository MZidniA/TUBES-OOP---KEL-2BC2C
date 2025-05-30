package org.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.example.controller.action.ChattingAction;
import org.example.model.Farm;

public class ChattingDialogPanel extends JPanel {
    private JFrame parentFrame;
    private List<String> dialogLines;
    private int currentLine = 0;
    private JLabel dialogLabel;
    private JButton nextButton;
    private Image npcBackground;
    private Image playerBackground;
    private boolean isNpcTurn = true;
    private String playerName;

    public ChattingDialogPanel(JFrame parentFrame, List<String> dialogLines, String npcName, String playerName, ChattingAction action, Farm farm) {
        this.parentFrame = parentFrame;
        this.dialogLines = dialogLines;
        this.playerName = playerName;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 200));
        setBackground(Color.BLACK);

        try {
            npcBackground = ImageIO.read(getClass().getResourceAsStream("/box/" + npcName + ".png"));
            playerBackground = ImageIO.read(getClass().getResourceAsStream("/box/box.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialogLabel = new JLabel();
        dialogLabel.setForeground(Color.WHITE);
        dialogLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dialogLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogLabel.setVerticalAlignment(SwingConstants.CENTER);

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentLine < dialogLines.size()) {
                    dialogLabel.setText((isNpcTurn ? npcName : playerName) + ": " + dialogLines.get(currentLine));
                    isNpcTurn = !isNpcTurn;
                    currentLine++;
                } else {
                    // Tutup panel setelah selesai
                    SwingUtilities.getWindowAncestor(ChattingDialogPanel.this).dispose();
                }
            }
        });

        add(dialogLabel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        // Jalankan efek action chatting
        action.execute(farm);
        nextButton.doClick(); // langsung tampilkan dialog pertama
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
