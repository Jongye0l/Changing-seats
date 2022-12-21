package com.Jongyeol.ChangeSeat;

public class ClickLocation {
    int x, y, width, height;
    MouseListener mouseListener;
    public ClickLocation(int x, int y, int width, int height, MouseListener mouseListener) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mouseListener = mouseListener;
    }
    public boolean location(int x, int y) {
        return x > this.x && x < this.x + this.width && y > this.y && y < this.y + this.height;
    }
    public interface MouseListener {
        void onClick();
        void onReleased();
        void onEnter();
        void onExit();
    }
}
