package main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable
{
    final int tileSizeNS = 16;
    final int scale = 3;
    final int tileSize =  tileSizeNS * scale;
    final int windowDim = 500; // 800px

    Thread gameThread;

    public GamePanel()
    {
        this.setPreferredSize(new Dimension(windowDim, windowDim));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
    }

    public void startGameThread()
    {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {

    }
}
