package main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;

public class TileMap {
    private int width;
    private int height;
    private int tileSizeNS;
    private int[][][] mapData;
    private BufferedImage tileset;
    private BufferedImage[][] tileImages;
    public int columns;
    public int rows;
    private int selectedTile;
    private JsonNode mapDataE;
    private String mapFile;
    private ObjectMapper mapper = new ObjectMapper();

    public TileMap(String mapFile, String tilesetFile, int tileSizeNS) {
        this.tileSizeNS = tileSizeNS;
        this.mapFile = mapFile;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode mapJson = mapper.readTree(new File(mapFile));

            this.mapDataE = mapJson;  // Assigning the map JSON to mapDataE

            width = mapJson.get("width").asInt();
            height = mapJson.get("height").asInt();

            ArrayNode layersJson = (ArrayNode) mapJson.get("layers");
            mapData = new int[layersJson.size()][height][width];
            for (int layer = 0; layer < layersJson.size(); layer++) {
                JsonNode layerJson = layersJson.get(layer);
                ArrayNode dataJson = (ArrayNode) layerJson.get("data");
                for (int i = 0; i < height; i++) {
                    ArrayNode rowJson = (ArrayNode) dataJson.get(i);
                    for (int j = 0; j < width; j++) {
                        mapData[layer][i][j] = rowJson.get(j).asInt();
                    }
                }
            }
            columns = width;
            rows = height;
            tileset = ImageIO.read(new File(tilesetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getMapHeight() {
        return height;
    }

    public int getMapWidth() {
        return width;
    }

    public int getTile(int row, int col) {
        return mapData[0][row][col];
    }

    public void setTile(int row, int col, int value) {
        mapData[0][row][col] = value;
    }

    public int getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(int value) {
        selectedTile = value;
    }

    public void initTilesetImages() {
        int tilesetColumns = tileset.getWidth() / tileSizeNS;
        int tilesetRows = tileset.getHeight() / tileSizeNS;

        tileImages = new BufferedImage[tilesetRows][tilesetColumns];

        for (int i = 0; i < tilesetRows; i++) {
            for (int j = 0; j < tilesetColumns; j++) {
                tileImages[i][j] = tileset.getSubimage(j * tileSizeNS, i * tileSizeNS, tileSizeNS, tileSizeNS);
            }
        }
    }

    public void changeTile(int newTileType, int tileIndex) {
        int rowIdx = tileIndex / 10;
        int colIdx = tileIndex % 10;

        // Ensure the indices are within bounds
        if (rowIdx >= 0 && rowIdx < height && colIdx >= 0 && colIdx < width) {
            // Change the tile in the second layer of the map data
            mapData[1][rowIdx][colIdx] = newTileType;

            // Also change it in the JsonNode
            ArrayNode row = (ArrayNode) ((ArrayNode) mapDataE.get("layers").get(1).get("data")).get(rowIdx);
            row.set(colIdx, mapper.valueToTree(newTileType));
        }
    }

    public void writeChangesToFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(mapDataE);
            try (FileWriter file = new FileWriter(mapFile)) {
                file.write(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, int tileSize, int scale, int cameraX, int cameraY) {
        int srcWidth = tileSize;
        int srcHeight = tileSize;
        int destWidth = tileSize * scale;
        int destHeight = tileSize * scale;
        for (int layer = 0; layer < mapData.length; layer++) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    int tile = mapData[layer][i][j];
                    if (tile != -1) {
                        int tilesetX = (tile % (tileset.getWidth() / srcWidth));
                        int tilesetY = (tile / (tileset.getWidth() / srcHeight));
                        int destX = j * destWidth - cameraX;
                        int destY = i * destHeight - cameraY;
                        BufferedImage subImage = tileImages[tilesetY][tilesetX];
                        g2.drawImage(subImage, destX, destY, destWidth, destHeight, null);
                    }
                }
            }
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }
}

