package entity;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.GamePanel;

public class Enemy extends Entity {
    GamePanel gamePanel;
    int xPosition;
    int enemyNumber;
    int count = 0;

    public Enemy(GamePanel gamePanel, int xPosition, int enemyNumber) {
        this.gamePanel = gamePanel;
        this.xPosition = xPosition;
        this.enemyNumber = enemyNumber;
        this.direction = "left";
        setDefaultValues();
        getEnemyImage();
    }
    public void  setDefaultValues() {
        x = xPosition;
        y = 100;
        speed = 50;
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
    }public void update() {
    if (direction.equals("left")) {
        x -= speed;
        count--;
        if (count <= -1) {
            direction = "right";
            count = 0;
        }
    } else if (direction.equals("right")) {
        x += speed;
        count++;
        if (count >= 4) {
            direction = "left";
            count = 3;
        }
    }
}

    public void draw(Graphics2D g2) {
        BufferedImage image = defaultImage;
        g2.drawImage(image,x,y,gamePanel.tileSize,gamePanel.tileSize,null);
    }
}
