package entity;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;

public class Bullet extends Entity{
    GamePanel gamePanel;
    int xPosition;
    int yPosition;
    String bDirection;

    public Bullet(GamePanel gamePanel, int xPosition, int yPosition, String bDirection) {
        this.gamePanel = gamePanel;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.bDirection = bDirection;
        setDefaultValues();
    }
    public void setDefaultValues() {
        x = xPosition;
        y = yPosition;
        speed = 10;
        direction = bDirection;
    }
    public void update() {
        if (direction.equals("up")) {
            y -= speed;
        }
        else if (direction.equals("down")) {
            y += speed;
        }
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(x, y, 10, 30);
    }
}
