package com.Jongyeol.ChangeSeat;

import java.awt.*;

import static com.Jongyeol.ChangeSeat.Main.*;

public class Seat {
    int x;
    int y;
    int id;
    String name = "";
    boolean isVisible = true;
    boolean selected = false;
    boolean locked = false;
    public Seat(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setVisibility(boolean visible) {
        isVisible = visible;
    }

    public String getName() {
        return name;
    }

    public void draw(Graphics2D g) {
        Image image = main.createImage(main.seatWidth, main.seatHeight);
        Graphics graphics = image.getGraphics();
        draws((Graphics2D) graphics);
        g.drawImage(image, (main.seatWidth + 100) * x + 100, (main.seatHeight + 50) * y + 50, main.seatWidth, main.seatHeight, null);
    }
    private void draws(Graphics2D g) {
        g.setFont(new Font("Korean", Font.BOLD, 120));
        g.setColor(new Color(246, 126, 2, 255));
        g.fillRect(0, 0, main.seatWidth - 1, main.seatHeight - 1);
        if(isVisible) {
            g.setColor(new Color(0, 0, 0, 255));
            g.drawString(name, 30, 150);
            g.setColor(new Color(236, 225, 9, 255));
            if(selected) {
                g.fillRect(0, 0, main.seatWidth - 1, main.round - 1);
                g.fillRect(0, 0, main.round - 1, main.seatHeight - 1);
                g.fillRect(0, main.seatHeight - main.round, main.seatWidth - 1, main.round - 1);
                g.fillRect(main.seatWidth - main.round, 0, main.round - 1, main.seatHeight - 1);
            }
            if(locked) g.drawImage(lock_true, main.seatWidth - 143, 2, 141, 102, null);
            else g.drawImage(lock_false, main.seatWidth - 143, 2, 141, 102, null);
        } else {
            g.drawImage(hide, 100, 0, main.seatWidth - 201, main.seatHeight - 1, null);
        }
    }
    public boolean location(int x, int y) {
        return x > (main.seatWidth + 100) * this.x + 100 && x < (main.seatWidth + 100) * this.x + 100 + main.seatWidth &&
                y > (main.seatHeight + 50) * this.y + 50 && y < (main.seatHeight + 50) * this.y + 50 + main.seatHeight;
    }
    public boolean lockLocation(int x, int y) {
        return x > (main.seatWidth + 100) * this.x + 100 + main.seatWidth - 143 && x < (main.seatWidth + 100) * this.x + 100 + main.seatWidth - 2 &&
                y > (main.seatHeight + 50) * this.y + 50 + 2 && y < (main.seatHeight + 50) * this.y + 50 + 104;
    }
    public void onLockClick() {
        if(!isVisible) isVisible = true;
        else {
            locked = !locked;
            main.setting.lockingSeat(id, locked);
        }
    }
    public void onClick() {
        if(!isVisible) isVisible = true;
        else main.seatSelectListener.select(this);
    }
}
