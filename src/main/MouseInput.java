package main;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    private final MapEditor mapEditor;
    private int mouseX;
    private int mouseY;

    public MouseInput(MapEditor mapEditor) {
        this.mapEditor = mapEditor;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mapEditor.handleMouseClick(e.getX(), e.getY());
        mapEditor.handleTilesetClick(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}