package org.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.example.controller.GameController;
import org.example.controller.StoreController;
import org.example.model.Farm;
import org.example.model.Items.Crops;
import org.example.model.Items.ItemDatabase;
import org.example.model.Items.Items;
import org.example.model.Items.Seeds;
import org.example.model.Map.Store;
import org.example.model.Player;
import org.example.model.Recipe;
import org.example.model.RecipeDatabase;

public class StorePanel extends JPanel {
    private final JFrame parentFrame;
    private final Font pixelFont;

    public StorePanel(JFrame parentFrame, Store store, Player player, GameController controller, Farm farm, String npcName) {
        this.parentFrame = parentFrame;
        StoreController storeController = new StoreController(store, player);
        this.pixelFont = loadFont();

        setPreferredSize(new Dimension(640, 576));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Store - Crops, Seeds, Recipes", SwingConstants.CENTER);
        title.setFont(pixelFont.deriveFont(Font.BOLD, 16f));
        title.setForeground(Color.BLACK);
        add(title, BorderLayout.NORTH);

        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.Y_AXIS));
        scrollPanel.setBackground(new Color(255, 248, 220));

        addSectionLabel(scrollPanel, "Crops");
        for (Crops crop : store.getAvailableCrops()) {
            int price = crop.getBuyprice();
            JButton btn = createButton(crop.getName() + " - " + (price > 0 ? price + "g" : "Tidak Dijual"));
            btn.setEnabled(price > 0);
            btn.addActionListener(e -> {
                if (storeController.buyCrop(crop.getName())) {
                    JOptionPane.showMessageDialog(this, "Berhasil membeli crop: " + crop.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Uang tidak cukup!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            scrollPanel.add(btn);
        }

        addSectionLabel(scrollPanel, "Seeds");
        for (Items item : ItemDatabase.getItemsByCategory("Seeds")) {
            if (item instanceof Seeds seed && seed.isPlantableInSeason(store.getSeason())) {
                JButton btn = createButton(seed.getName() + " - " + seed.getBuyprice() + "g");
                btn.addActionListener(e -> {
                    if (storeController.buyItem(seed)) {
                        JOptionPane.showMessageDialog(this, "Berhasil membeli seed: " + seed.getName());
                    } else {
                        JOptionPane.showMessageDialog(this, "Uang tidak cukup!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                scrollPanel.add(btn);
            }
        }

        addSectionLabel(scrollPanel, "Recipes");
        for (Recipe recipe : RecipeDatabase.getAllRecipes()) {
            if ("Beli di store".equalsIgnoreCase(recipe.getUnlockMechanism())) {
                JButton btn = createButton(recipe.getDisplayName() + " - Resep");
                btn.addActionListener(e -> {
                    if (storeController.buyRecipe(recipe)) {
                        JOptionPane.showMessageDialog(this, "Berhasil membeli resep: " + recipe.getDisplayName());
                    } else {
                        JOptionPane.showMessageDialog(this, "Uang tidak cukup atau sudah dimiliki.");
                    }
                });
                scrollPanel.add(btn);
            }
        }

        JButton backButton = createButton("BACK");
        backButton.addActionListener(e -> {
            NPCInteractionPanel npcPanel = new NPCInteractionPanel(parentFrame, controller, farm, npcName, player.getName());
            parentFrame.setContentPane(npcPanel);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        scrollPanel.add(Box.createVerticalStrut(16));
        scrollPanel.add(backButton);

        JScrollPane scroll = new JScrollPane(scrollPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private Font loadFont() {
        try {
            InputStream is = getClass().getResourceAsStream("/font/PressStart2P.ttf");
            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);
            return base;
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 12);
        }
    }

    private void addSectionLabel(JPanel panel, String title) {
        JLabel label = new JLabel(title);
        label.setFont(pixelFont.deriveFont(Font.BOLD, 12f));
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(pixelFont.deriveFont(10f));
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(222, 203, 155));
        btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        btn.setMaximumSize(new Dimension(300, 28));
        return btn;
    }
}