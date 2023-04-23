package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
    public MainMenu(ActionListener startGameAction, ActionListener exitAction) {
        // Set the preferred size of the MainMenu panel
        setPreferredSize(new Dimension(500, 500));

        // Set the background color of the main panel to black
        setBackground(Color.BLACK);

        setLayout(new BorderLayout());

        // Add a label with the game name at the top
        JLabel gameName = new JLabel("Your Game Name", SwingConstants.CENTER);
        gameName.setFont(new Font("Arial", Font.BOLD, 30)); // Increase the font size

        // Change the game name font color to white
        gameName.setForeground(Color.WHITE);

        // Add an empty border to the top of the game name label to move it down by 150 pixels
        gameName.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
        add(gameName, BorderLayout.NORTH);

        // Create a panel to hold the buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setBackground(Color.BLACK); // Set the background color of the buttons panel to black
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(-20, 10, 10, 10); // Move the buttons to the center

        // Set custom font and size for the buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 22); // Increase the font size

        // Add a button for starting the game
        JButton startButton = new JButton("Start Game");
        startButton.setFont(buttonFont);
        startButton.setForeground(Color.WHITE); // Set the text color of the button to white
        startButton.setOpaque(false); // Make the button background transparent
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false); // Remove the focus border around the button
        startButton.addActionListener(startGameAction);
        buttonsPanel.add(startButton, gbc);

        // Add some space between buttons
        gbc.gridy++;
        gbc.insets.top = 30;

        // Add a button for exiting the game
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(buttonFont);
        exitButton.setForeground(Color.WHITE); // Set the text color of the button to white
        exitButton.setOpaque(false); // Make the button background transparent
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false); // Remove the focus border around the button
        exitButton.addActionListener(exitAction);
        buttonsPanel.add(exitButton, gbc);

        // Add the buttons panel to the main panel
        add(buttonsPanel, BorderLayout.CENTER);
    }
}
