package entity;

import main.KeyHandler;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Player extends Entity{
    GamePanel gamePanel;
    KeyHandler keyHandler;
    public Rectangle hitbox;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues() {
        x = 100;
        y = gamePanel.screenHeight - gamePanel.tileSize;
        speed = 4;
        direction = "left";
        hitbox = new Rectangle(x,y,gamePanel.tileSize,gamePanel.tileSize);
    }
    public void getPlayerImage() {
        try {
            File f1 = new File("./src/player/spaceShipDefault.png");
            File f2 = new File("./src/player/goLeftSpaceShip.png");
            File f3 = new File("./src/player/goRightSpaceShip.png");

            defaultImage = ImageIO.read(f1);
            left = ImageIO.read(f2);
            right = ImageIO.read(f3);

            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void update() {
        if (keyHandler.rightPressed) {
            if (!(x == gamePanel.screenWidth - gamePanel.tileSize)) {
                direction = "right";
                x += speed;
            }
        } else if (keyHandler.leftPressed) {
            if (!(x == 0)) {
                direction = "left";
                x -= speed;
            }
        }
        hitbox.x = x;
        hitbox.y = y;
    }
    public void draw(Graphics2D g2) {

        if (keyHandler.leftPressed || keyHandler.rightPressed) {
            
        
        BufferedImage image = null;
        
        switch (direction) {
            case "left":
                image = left;
                break;
            case "right":
                image = right;
                break;
            
        }
        g2.drawImage(image,x,y,gamePanel.tileSize,gamePanel.tileSize,null);
    } else {
        g2.drawImage(defaultImage,x,y,gamePanel.tileSize,gamePanel.tileSize,null);
    }
    }
}
