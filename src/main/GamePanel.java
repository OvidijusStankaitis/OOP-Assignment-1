package main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    final int tileSizeNS = 32;
    final int scale = 4;
    final int tileSize = tileSizeNS * scale;
    final int windowDim = 1000;
    final int walkingFrameStart = 2;
    final int walkingFrameEnd = 7;
    final int animationSpeed = 3;
    int currentFrame = walkingFrameStart;
    int animationCounter = 0;
    private TileMap tileMap;
    private TileMap tileMap1;
    private TileMap tileMap2;
    KeyInput keyInput = new KeyInput();
    Timer gameTimer;
    int playerX = windowDim / 2 - tileSize / 2;
    int playerY = windowDim / 2 - tileSize / 2;
    boolean left = false;
    BufferedImage character;
    BufferedImage[][] characterFrames;
    int cameraX = 0;
    int cameraY = 0;
    private boolean initialized = false;
    private ArrayList<Enemy> enemies;
    private int currentLevel = 1;
    private float timeLeft;
    private boolean levelComplete;
    private boolean gameComplete;
    private BufferedImage[] enemyFrames;
    private Timer levelCompleteTimer;
    private boolean playerCaught;
    private MapEditor mapEditor;
    private MouseInput mouseInput;
    private int mapX = (windowDim - tileSizeNS * 10) / 2;
    private int mapY = (windowDim - tileSizeNS * 10);

    public GamePanel() {
        try {
            character = ImageIO.read(new File("src/main/characters.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mapEditor = new MapEditor();
        mouseInput = new MouseInput(mapEditor);
        this.addMouseListener(mouseInput);
        this.setPreferredSize(new Dimension(windowDim, windowDim));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyInput);
        this.setFocusable(true);

        gameTimer = new Timer(1000 / 60, e -> {
            update();
            repaint();
        });
        gameTimer.start();
        initCharacterFrames();
        initTileMaps();
        initialized = true;

        initEnemyImages();
        enemies = new ArrayList<>();
        timeLeft = 10;
        playerCaught = false;
        levelComplete = false;
        gameComplete = false;

        spawnEnemies();

        initLevelCompleteTimer();

    }

    private void initLevelCompleteTimer() {
        levelCompleteTimer = new Timer(3000, e -> {
            if (playerCaught) {
                System.exit(0);
            } else if (!gameComplete) {
                currentLevel++;
                enemies.clear();
                spawnEnemies();
                timeLeft = 10;
                levelComplete = false;
                gameTimer.start();
            } else {
                currentLevel++;
                System.exit(0);
            }
        });
        levelCompleteTimer.setRepeats(false);
    }


    private void initTileMaps() {
        tileMap = new TileMap("src/main/map.json", "src/main/world.png", tileSizeNS);
        tileMap1 = new TileMap("src/main/map1.json", "src/main/world.png", tileSizeNS);
        tileMap2 = new TileMap("src/main/map2.json", "src/main/world.png", tileSizeNS);

        // Initialize tileImages for each TileMap object
        tileMap.initTilesetImages();
        tileMap1.initTilesetImages();
        tileMap2.initTilesetImages();
    }


    private void initEnemyImages() {
        enemyFrames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            enemyFrames[i] = character.getSubimage(i * tileSizeNS, 3 * tileSizeNS, tileSizeNS, tileSizeNS);
        }
    }

    private void spawnEnemies() {
        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy(2 * i, 2 * i, enemyFrames, 1, tileSize));
        }
    }

    private boolean isPlayerCaught() {
        Rectangle player = new Rectangle(playerX, playerY, tileSize, tileSize);
        for (Enemy enemy : enemies) {
            int enemyX = enemy.getX() - cameraX; // Adjust for the camera position
            int enemyY = enemy.getY() - cameraY; // Adjust for the camera position
            Rectangle enemyRec = new Rectangle(enemyX, enemyY, tileSize, tileSize);
            if (player.intersects(enemyRec)) {
                return true;
            }
        }
        return false;
    }

    private void initCharacterFrames() {
        int characterColumns = character.getWidth() / tileSizeNS;
        int characterRows = character.getHeight() / tileSizeNS;

        characterFrames = new BufferedImage[characterRows][characterColumns];

        for (int i = 0; i < characterRows; i++) {
            for (int j = 0; j < characterColumns; j++) {
                characterFrames[i][j] = character.getSubimage(j * tileSizeNS, i * tileSizeNS, tileSizeNS, tileSizeNS);
            }
        }
    }

    public void update() {
        if (!initialized) {
            return;
        }

        if (keyInput.esc) {
            tileMap.changeTile(mapEditor.getSelectedTileIndex(), mapEditor.getSelectedTileExportIndex());
            tileMap.writeChangesToFile();
            return;
        }

        int speed = 7;
        boolean moving = false;

        int maxX = (tileMap.getColumns() * tileSize) - windowDim;
        int maxY = (tileMap.getRows() * tileSize) - windowDim;

        int centerX = windowDim / 2 - tileSize / 2;
        int centerY = windowDim / 2 - tileSize / 2;

        if (keyInput.left) {
            if (cameraX > 0) {
                cameraX -= speed;
            } else if (playerX - speed >= 0) {
                playerX -= speed;
            } else if (playerX > centerX) {
                playerX -= speed;
            }
            left = true;
            moving = true;
        }
        if (keyInput.right) {
            if (cameraX < maxX) {
                cameraX += speed;
            } else if (playerX + tileSize + speed <= windowDim) {
                playerX += speed;
            } else if (playerX < centerX) {
                playerX += speed;
            }
            left = false;
            moving = true;
        }
        if (keyInput.up) {
            if (cameraY > 0) {
                cameraY -= speed;
            } else if (playerY - speed >= 0) {
                playerY -= speed;
            } else if (playerY > centerY) {
                playerY -= speed;
            }
            moving = true;
        }
        if (keyInput.down) {
            if (cameraY < maxY) {
                cameraY += speed;
            } else if (playerY + tileSize + speed <= windowDim) {
                playerY += speed;
            } else if (playerY < centerY) {
                playerY += speed;
            }
            moving = true;
        }

        if (keyInput.left && keyInput.right || keyInput.up && keyInput.down) {
            moving = false;
        }

        if (moving) {
            updateAnimationFrame();
        } else {
            currentFrame = 1;
        }

        if (!levelComplete && !gameComplete) {
            for (Enemy enemy : enemies) {
                enemy.update(playerX, playerY);
            }

            timeLeft -= 1 / 60.0f;
            if (timeLeft <= 0) {
                timeLeft = 0;
                levelComplete = true;

                if (currentLevel == 3) {
                    gameComplete = true;
                }
            }
        } else {
            gameTimer.stop();
            if (!levelCompleteTimer.isRunning()) {
                levelCompleteTimer.start();
            }
        }

        if (!playerCaught && isPlayerCaught()) {
            playerCaught = true;
            levelCompleteTimer.setInitialDelay(3000);
            levelCompleteTimer.start();
        }

        switch (currentLevel) {
            case 1:
                tileMap = tileMap;
                break;
            case 2:
                tileMap = tileMap1;
                break;
            case 3:
                tileMap = tileMap2;
                break;
            default:
                break;
        }
    }

    public void updateAnimationFrame() {
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            currentFrame++;
            if (currentFrame > walkingFrameEnd) {
                currentFrame = walkingFrameStart;
            }
            animationCounter = 0;
        }
    }

    public void paintComponent(Graphics g) {
        if (!initialized) {
            return;
        }

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int scaleDown = 1;

        if (keyInput.esc) {
            mapEditor.draw(g2, windowDim);
            tileMap.draw(g2, tileSizeNS, scaleDown, -mapX, -mapY + 100);

            g2.setColor(Color.RED);
            for (int i = 0; i <= 10; i++) {
                int x = mapX + i * tileSizeNS * scaleDown;
                int y = mapY + i * tileSizeNS * scaleDown - 100;
                g2.drawLine(mapX, y, mapX + tileSizeNS * 10, y);
                g2.drawLine(x, mapY - 100, x, mapY + tileSizeNS * 10 - 100);
            }

        } else {
            tileMap.draw(g2, tileSizeNS, scale, cameraX, cameraY);

            int srcWidth = 32;
            int srcHeight = 32;
            int srcX = (currentFrame - 1) * srcWidth;
            int srcY = 32;
            int destX = playerX;
            int destY = playerY;

            BufferedImage subImage = characterFrames[srcY / tileSizeNS][srcX / tileSizeNS];

            AffineTransform transform = AffineTransform.getScaleInstance(scale, scale);
            if (left) {
                transform.concatenate(AffineTransform.getTranslateInstance((destX + tileSize) / scale, destY / scale));
                transform.concatenate(AffineTransform.getScaleInstance(-1, 1));
                g2.drawImage(subImage, transform, null);
            } else {
                transform.concatenate(AffineTransform.getTranslateInstance(destX / scale, destY / scale));
                g2.drawImage(subImage, transform, null);
            }

            g2.setColor(Color.RED);
            g2.drawRect(playerX, playerY, tileSize, tileSize);

            for (Enemy enemy : enemies) {
                enemy.draw(g2, tileSize, scale, cameraX, cameraY);
            }

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.drawString(String.format("%.0f", timeLeft), getWidth() - 50, 30);

            if (levelComplete || gameComplete) {
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 32));
                if (gameComplete) {
                    g2.drawString("Game Complete", getWidth() / 2 - 100, getHeight() / 2);
                } else {
                    g2.drawString("Level Complete", getWidth() / 2 - 100, getHeight() / 2);
                }
            } else if (playerCaught) {
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.PLAIN, 32));
                g2.drawString("Game Over", getWidth() / 2 - 100, getHeight() / 2);
            }
        }

        g2.dispose();
    }
}