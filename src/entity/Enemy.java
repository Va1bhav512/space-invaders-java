package entity;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class Enemy extends Entity {
    GamePanel gamePanel;
    int xPosition;
    int yPosition;
    int enemyNumber;
    int count = 0;
    private long lastBulletTime;
    private final long bulletCooldownForEnemy = 2000000000;
    // long enemyUpdateInterval;
    public Rectangle hitbox;
    public boolean isHit;

    public Enemy(GamePanel gamePanel, int xPosition, int yPosition, int enemyNumber) {
        this.gamePanel = gamePanel;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.enemyNumber = enemyNumber;
        this.direction = "left";
        this.lastBulletTime = System.nanoTime();
        this.isHit = false;
        setDefaultValues();
        getEnemyImage();
    }
    public void  setDefaultValues() {
        x = xPosition;
        y = yPosition;
        speed = 50;
        hitbox = new Rectangle(x,y,gamePanel.tileSize,gamePanel.tileSize);
    }
    public void getEnemyImage() {
        try {
            File f1;
            switch (enemyNumber) {
                case 1:
                    f1 = new File("./src/enemy/typeOneEnemy.png");
                    break;
                case 2:
                    f1 = new File("./src/enemy/typeTwoEnemy.png");
                    break;
                default:
                    f1 = new File("./src/enemy/typeOneEnemy.png");
            }
            defaultImage = ImageIO.read(f1);
        } catch (IOException e) {
            e.printStackTrace();;
        }
    }
    public void update() {
        // long currentTime = System.nanoTime();
        if (direction.equals("left")) {
            if (count <= -1) {
                y += gamePanel.tileSize;
                direction = "right";
                count = 0;
                return;
            }
            x -= speed;
            count--;
        } else if (direction.equals("right")) {
            if (count >= 4) {
                y += gamePanel.tileSize;
                direction = "left";
                count = 3;
                return;
            }
            x += speed;
            count++;
        }
        hitbox.x = x;
        hitbox.y = y;
    }
    public void updateBulletsEnemy() {
        long currentTime = System.nanoTime();
        if (currentTime - lastBulletTime >= bulletCooldownForEnemy) {
            gamePanel.bullets.add(new Bullet(gamePanel, x, y + gamePanel.tileSize, "down"));
            lastBulletTime = currentTime;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = defaultImage;
        g2.drawImage(image,x,y,gamePanel.tileSize,gamePanel.tileSize,null);
    }
}
