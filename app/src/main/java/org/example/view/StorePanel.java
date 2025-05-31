package org.example.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
    private Image backgroundImage;

    public StorePanel(JFrame parentFrame, Store store, Player player, GameController controller, Farm farm, String npcName) {
        this.parentFrame = parentFrame;
        StoreController storeController = new StoreController(store, player);
        this.pixelFont = loadFont();

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/menu/StoreBG.png"));
        } catch (Exception e) {
            e.printStackTrace();
            backgroundImage = null;
        }

        setPreferredSize(new Dimension(640, 576));
        setLayout(new BorderLayout());
        setOpaque(true);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); 

        contentPanel.setBackground(new Color(255, 248, 220, 200)); 

        addSectionLabel(contentPanel, "Crops");
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
            contentPanel.add(btn);
        }

        addSectionLabel(contentPanel, "Seeds");
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
                contentPanel.add(btn);
            }
        }

        addSectionLabel(contentPanel, "Recipes");
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
                contentPanel.add(btn);
            }
        }

        JButton backButton = createButton("BACK");
        backButton.addActionListener(e -> {
            JPanel wrapper = new JPanel(new java.awt.GridBagLayout());
            wrapper.setOpaque(false);

            NPCInteractionPanel npcPanel = new NPCInteractionPanel(parentFrame, controller, farm, npcName, player.getName());
            npcPanel.setOpaque(false);

            wrapper.add(npcPanel);

            parentFrame.setContentPane(wrapper);
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        contentPanel.add(Box.createVerticalStrut(16));
        contentPanel.add(backButton);

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
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