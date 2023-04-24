package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener
{

    public boolean up = false, down = false, left = false, right = false;

    @Override
    public void keyTyped(KeyEvent keyEvent)
    {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent)
    {
        int keyCode = keyEvent.getKeyCode();

        if(keyCode == KeyEvent.VK_W)
        {
            up = true;
        }
        else if(keyCode == KeyEvent.VK_A)
        {
            left = true;
        }
        else if(keyCode == KeyEvent.VK_S)
        {
            down = true;
        }
        else if(keyCode == KeyEvent.VK_D)
        {
            right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent)
    {
        int keyCode = keyEvent.getKeyCode();

        if(keyCode == KeyEvent.VK_W)
        {
            up = false;
        }
        else if(keyCode == KeyEvent.VK_A)
        {
            left = false;
        }
        else if(keyCode == KeyEvent.VK_S)
        {
            down = false;
        }
        else if(keyCode == KeyEvent.VK_D)
        {
            right = false;
        }
    }
}
