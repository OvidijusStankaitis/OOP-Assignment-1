package main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Enemy {
    private int x, y;
    private BufferedImage[] frames;
    private int currentFrame;
    private boolean left;
    private int tileSize;
    private int speed;

    public Enemy(int x, int y, BufferedImage[] frames, int speed, int tileSize) {
        this.x = x * tileSize;
        this.y = y * tileSize;
        this.frames = frames;
        this.currentFrame = 0;
        this.left = false;
        this.speed = speed;
        this.tileSize = tileSize;
    }
    public void update(int playerX, int playerY) {
        if (playerX < x) {
            x -= speed;
            left = true;
        } else if (playerX > x) {
            x += speed;
            left = false;
        }
        if (playerY < y) {
            y -= speed;
        } else if (playerY > y) {
            y += speed;
        }
        currentFrame = (currentFrame + 1) % 4;
    }

    public void draw(Graphics2D g2, int tileSize, int scale, int cameraX, int cameraY) {
        int destX = x - cameraX;
        int destY = y - cameraY;
        BufferedImage subImage = frames[currentFrame];

        AffineTransform transform = AffineTransform.getScaleInstance(scale, scale); 

        if (left) {
            transform.concatenate(AffineTransform.getTranslateInstance((destX + tileSize) / scale, destY / scale));
            transform.concatenate(AffineTransform.getScaleInstance(-1, 1));
            g2.drawImage(subImage, transform, null);
        } else {
            transform.concatenate(AffineTransform.getTranslateInstance(destX / scale, destY / scale));
            g2.drawImage(subImage, transform, null);
        }

//        g2.setColor(Color.RED);
//        g2.drawRect(destX, destY, tileSize, tileSize);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
