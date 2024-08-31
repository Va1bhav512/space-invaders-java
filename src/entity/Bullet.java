package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

public class Bullet extends Entity{
    GamePanel gamePanel;
    int xPosition;
    int yPosition;
    public String bDirection;
    public Rectangle hitbox;

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
        hitbox = new Rectangle(x,y,10,30);
    }
    public void update() {
        if (direction.equals("up")) {
            y -= speed;
        }
        else if (direction.equals("down")) {
            y += speed;
        }
        hitbox.x = x;
        hitbox.y = y;
    }
    public boolean collidesWithEnemy() {
        if (this.bDirection == "down") {
            return false; // So that enemy bullets dont kill enemies
        }
        for (int i = 0; i < gamePanel.enemyArray.length; i++) {
            for (int j = 0; j < gamePanel.enemyArray[0].length; j++) {
                Enemy enemy = gamePanel.enemyArray[i][j];
                if ((enemy != null) && hitbox.intersects(enemy.hitbox)) {
                    enemy.isHit = true;
                    return true;
                }
            }
        }
        return false;
    }
    public boolean collidesWithPlayer() {
        if (hitbox.intersects(gamePanel.player.hitbox)) {
            return true;
        }
        return false;
    }
    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.fillRect(x, y, 10, 30);
    }
}
