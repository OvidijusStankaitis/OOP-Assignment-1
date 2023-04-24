package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable
{
    final int tileSizeNS = 16;
    final int scale = 3;
    final int tileSize =  tileSizeNS * scale;
    final int windowDim = 500; // 800px

    KeyInput keyInput = new KeyInput();
    Thread gameThread;

    int playerX = windowDim / 2 - tileSize / 2;
    int playerY = windowDim / 2 - tileSize / 2;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(windowDim, windowDim));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyInput);
        this.setFocusable(true);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run()
    {
        while(gameThread != null)
        {
            update();

            repaint();
        }
    }

    public void update()
    {

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }
}
