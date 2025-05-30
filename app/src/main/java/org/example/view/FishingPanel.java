package org.example.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.example.controller.GameController;
import org.example.model.Farm;
import org.example.model.Items.Fish;
import org.example.model.Items.ItemDatabase;
import org.example.model.Player;
import org.example.model.enums.FishType;
import org.example.model.enums.LocationType;

public class FishingPanel extends JPanel {
    private final Farm farm;
    private final GameController controller;

    private JTextArea gameLog;
    private JTextField guessField;
    private JButton guessButton, startButton, backButton;
    private JLabel energyLabel, statusLabel;

    private Fish currentFish;
    private FishType currentFishType;
    private int targetNumber, maxAttempts, currentAttempt;
    private boolean gameActive;

    private Image background;
    private Font pixelFont;
    private final Color brownText = new Color(92, 64, 51);
    private JLabel fishImageLabel;

    public FishingPanel(Farm farm, GameController controller) {
        this.farm = farm;
        this.controller = controller;
        setLayout(null);
        setPreferredSize(new Dimension(640, 576));
        loadAssets();
        initComponents();
        updatePlayerInfo();
    }

    private void loadAssets() {
        try {
            background = ImageIO.read(getClass().getResource("/menu/FishingBG.png"));
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font/PressStart2P.ttf")).deriveFont(9f);
        } catch (Exception e) {
            System.err.println("Error loading assets: " + e.getMessage());
            pixelFont = new Font("Monospaced", Font.PLAIN, 9);
        }
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        try {
            ImageIcon rawIcon = new ImageIcon(ImageIO.read(getClass().getResource("/button/button.png")));
            Image scaled = rawIcon.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.CENTER);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Failed to load button image: " + e.getMessage());
        }
        button.setFont(pixelFont);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        return button;
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("Fishing Challenge");
        titleLabel.setFont(pixelFont.deriveFont(Font.BOLD, 13f));
        titleLabel.setForeground(brownText);
        titleLabel.setBounds(200, 100, 300, 20);
        add(titleLabel);

        energyLabel = new JLabel();
        energyLabel.setFont(pixelFont);
        energyLabel.setForeground(brownText);
        energyLabel.setBounds(90, 130, 300, 20);
        add(energyLabel);

        statusLabel = new JLabel("Ready to Fish!");
        statusLabel.setFont(pixelFont);
        statusLabel.setForeground(brownText);
        statusLabel.setBounds(90, 150, 400, 20);
        add(statusLabel);

        gameLog = new JTextArea();
        gameLog.setFont(pixelFont);
        gameLog.setOpaque(false);
        gameLog.setEditable(false);
        gameLog.setForeground(brownText);
        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setBounds(90, 180, 460, 150);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(brownText));
        add(scrollPane);

        JLabel guessLabel = new JLabel("Guess:");
        guessLabel.setFont(pixelFont);
        guessLabel.setForeground(brownText);
        guessLabel.setBounds(90, 340, 70, 25);
        add(guessLabel);

        guessField = new JTextField();
        guessField.setFont(pixelFont);
        guessField.setBounds(160, 340, 100, 25);
        add(guessField);

        guessButton = createCustomButton("Guess!");
        guessButton.setBounds(270, 340, 100, 25);
        guessButton.addActionListener(e -> makeGuess());
        add(guessButton);

        startButton = createCustomButton("Start!");
        startButton.setBounds(380, 340, 100, 25);
        startButton.addActionListener(e -> startFishing());
        add(startButton);

        backButton = createCustomButton("Return");
        backButton.setBounds(20, 20, 120, 30);
        backButton.addActionListener(e -> {
            controller.returnToGamePanel();
        });
        add(backButton);

        guessButton.setEnabled(false);
        guessField.setEnabled(false);

        fishImageLabel = new JLabel();
        fishImageLabel.setBounds(200, 400, 64, 64);
        add(fishImageLabel);
    }

    private void updatePlayerInfo() {
        Player player = farm.getPlayerModel();
        energyLabel.setText("Energy: " + player.getEnergy());
    }

    private void startFishing() {
        if (!canFish()) {
            appendToLog("Cannot fish here or you are missing a Fishing Rod.");
            return;
        }

        Player player = farm.getPlayerModel();
        player.decreaseEnergy(5);
        farm.getGameClock().advanceTimeMinutes(15);
        updatePlayerInfo();

        List<Fish> validFish = getValidFish();
        if (validFish.isEmpty()) {
            appendToLog("No fish available at this time or condition.");
            return;
        }

        currentFish = validFish.get(new Random().nextInt(validFish.size()));
        currentFishType = currentFish.getFishType().iterator().next();
        targetNumber = generateTargetNumber(currentFishType);
        maxAttempts = getMaxAttempts(currentFishType);
        currentAttempt = 0;
        gameActive = true;

        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        startButton.setEnabled(false);

        appendToLog("Fishing started. Try to guess the number!");
        appendToLog("A " + currentFish.getName() + " is on the hook!");
        statusLabel.setText("Guess a number in range " + getRangeText(currentFishType) +
                " (" + currentAttempt + "/" + maxAttempts + ")");
    }

    private void makeGuess() {
        if (!gameActive) return;

        int guess;
        try {
            guess = Integer.parseInt(guessField.getText().trim());
        } catch (NumberFormatException e) {
            appendToLog("Please enter a valid number.");
            return;
        }

        guessField.setText("");
        currentAttempt++;

        if (guess == targetNumber) {
            appendToLog("Success! You caught: " + currentFish.getName());
            farm.getPlayerModel().getInventory().addInventory(currentFish, 1);

            try {
                Image fishImage = ImageIO.read(getClass().getResource("/fish/" + currentFish.getName() + ".png"));
                fishImageLabel.setIcon(new ImageIcon(fishImage.getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                System.err.println("Gagal muat gambar ikan: " + currentFish.getName());
            }

            endFishing(true);
        } else {
            if (guess < targetNumber && guess >= 1) {
                appendToLog("Too low.");
            } else if (guess > targetNumber && guess <= Integer.parseInt(getRangeText(currentFishType).split("–")[1])) {
                appendToLog("Too high.");
            } else {
                appendToLog("Invalid guess. Must be within range " + getRangeText(currentFishType));
            }
        }

        if (currentAttempt >= maxAttempts && gameActive) {
            appendToLog("You failed. The fish escaped. The number was: " + targetNumber);
            endFishing(false);
        } else {
            statusLabel.setText("Guess a number in range " + getRangeText(currentFishType) +
                    " (" + currentAttempt + "/" + maxAttempts + ")");
        }
    }

    private void endFishing(boolean success) {
        gameActive = false;
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        startButton.setEnabled(true);
        statusLabel.setText(success ? "Fishing succeeded! Ready for next." : "Fishing failed. Ready for next.");
    }

    private boolean canFish() {
        Player player = farm.getPlayerModel();
        LocationType loc = player.getCurrentLocationType();
        return player.getEnergy() >= 5 &&
                player.getInventory().hasItem(ItemDatabase.getItem("Fishing Rod"), 1) &&
                (loc == LocationType.POND || loc == LocationType.OCEAN ||
                 loc == LocationType.MOUNTAIN_LAKE || loc == LocationType.FOREST_RIVER);
    }

    private List<Fish> getValidFish() {
        LocalTime now = farm.getGameClock().getCurrentTime();
        return ItemDatabase.getItemsByCategory("Fish").stream()
                .filter(i -> i instanceof Fish)
                .map(i -> (Fish) i)
                .filter(f -> {
                    boolean seasonMatch = f.getSeason().contains(farm.getCurrentSeason());
                    boolean weatherMatch = f.getWeather().contains(farm.getCurrentWeather());
                    boolean locationMatch = f.getLocationType().contains(farm.getPlayerModel().getCurrentLocationType());
                    boolean timeMatch = f.getTimeRanges().stream().anyMatch(t -> t.isWithin(now));
                    return seasonMatch && weatherMatch && locationMatch && timeMatch;
                })
                .collect(Collectors.toList());
    }

    private int generateTargetNumber(FishType type) {
        Random rand = new Random();
        return switch (type) {
            case COMMON -> rand.nextInt(10) + 1;
            case REGULAR -> rand.nextInt(100) + 1;
            case LEGENDARY -> rand.nextInt(500) + 1;
        };
    }

    private int getMaxAttempts(FishType type) {
        return switch (type) {
            case COMMON, REGULAR -> 10;
            case LEGENDARY -> 7;
        };
    }

    private String getRangeText(FishType type) {
        return switch (type) {
            case COMMON -> "1–10";
            case REGULAR -> "1–100";
            case LEGENDARY -> "1–500";
        };
    }

    private void appendToLog(String msg) {
        gameLog.append(msg + "\n");
        gameLog.setCaretPosition(gameLog.getDocument().getLength());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
