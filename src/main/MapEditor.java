package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MapEditor {
    private BufferedImage world;
    private final int worldTileSize = 32; // size of each tile in the world.png
    private final int worldColumnsRows = 16; // number of tiles in a row or column in world.png
    private int selectedTileIndex;
    private int selectedTileExportIndex;
    private int xOffset; // Add this line

    public MapEditor() {
        try {
            world = ImageIO.read(new File("src/main/world.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectedTileIndex = -1;
        selectedTileExportIndex = -1;
    }

    public void handleMouseClick(int x, int y) {
        // Adjust for the upper-left corner of the small map.
        x -= 340;
        y -= 580;

        // Calculate tile indices.
        int tileX = x / 32;
        int tileY = y / 32;

        // Check if the click was within the bounds of the small map.
        if (tileX >= 0 && tileX < 10 && tileY >= 0 && tileY < 10) {
            selectedTileExportIndex = tileY * 10 + tileX;
            System.out.println("Clicked tile index: " + selectedTileExportIndex); // Print the index
        }
    }

    public void handleTilesetClick(int x, int y) {
        // Calculate tile indices.
        int tileX = (x - xOffset) / worldTileSize;
        int tileY = y / worldTileSize;

        // Check if the click was within the bounds of the tileset.
        if (tileX >= 0 && tileX < worldColumnsRows && tileY >= 0 && tileY < worldColumnsRows) {
            selectedTileIndex = tileY * worldColumnsRows + tileX;
            System.out.println("Selected tile index from PNG: " + selectedTileIndex);
        }
    }

    public int getSelectedTileExportIndex() {
        return selectedTileExportIndex;
    }

    public int getSelectedTileIndex() {
        return selectedTileIndex;
    }

    public void draw(Graphics2D g2, int windowDim) {
        g2.setBackground(Color.BLACK);
        g2.fillRect(0, 0, windowDim, windowDim);
        xOffset = (windowDim - (worldTileSize * worldColumnsRows)) / 2; // center the tile sheet
        g2.drawImage(world, xOffset, 0, null);

        // Draw grid lines
        g2.setColor(Color.RED);
        for (int i = 0; i <= worldColumnsRows; i++) {
            int x = xOffset + i * worldTileSize;
            int y = i * worldTileSize;
            g2.drawLine(xOffset, y, xOffset + worldTileSize * worldColumnsRows, y); // horizontal line
            g2.drawLine(x, 0, x, worldTileSize * worldColumnsRows); // vertical line
        }
    }
}

