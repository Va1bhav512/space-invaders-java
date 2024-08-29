package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.JPanel;

import entity.Bullet;
import entity.Enemy;
import entity.Player;

public class GamePanel extends JPanel implements Runnable{

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    private long lastBulletTime = 0;
    private final long bulletCooldown = 500000000;
    KeyHandler keyHandler = new KeyHandler();

    int playerX = 100;
    int playerY = screenHeight - tileSize;
    int playerSpeed = 4;

    int FPS = 60;

    Thread gameThread;
    List<Bullet> bullets = new ArrayList<>();
    Player player = new Player(this, keyHandler);

    Enemy[][] enemyArray;
    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(new Color(46,7,43));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        enemyArray = initializeEnemyArray(1);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long lastEnemyUpdationTime = System.nanoTime();
        long enemyUpdateInterval = (long) 1.5 * 1000000000;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                player.update();
                updateBullets();
                repaint();
                delta--;
            }
            
            if (currentTime - lastEnemyUpdationTime >= enemyUpdateInterval) {
                updateEnemies();
                lastEnemyUpdationTime = currentTime;
            }
            
        }
    }
    public void updateBullets() {
        long currentBulletTime = System.nanoTime();

        if ((keyHandler.spacePressed || keyHandler.enterPressed) && (currentBulletTime - lastBulletTime >= bulletCooldown)) {
            bullets.add(new Bullet(this,(player.x + tileSize/2)-5,player.y,"up"));
            keyHandler.enterPressed = false;
            keyHandler.spacePressed = false;
            lastBulletTime = currentBulletTime;
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.y < 0) {
                bullets.remove(i);
                i--;
            }
        }
    }
    public void updateEnemies() {
        for (int i = 0; i < enemyArray.length; i++) {
            for (int j = 0; j < enemyArray[0].length; j++) {
                enemyArray[i][j].update();
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        player.draw(g2);

        for (int i = 0; i < enemyArray.length; i++) {
            for (int j = 0; j < enemyArray[0].length; j++) {
                enemyArray[i][j].draw(g2);
            }
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }
        g2.dispose();
    }

    public Enemy[][] initializeEnemyArray(int enemyNumber) {
        Enemy[][] newEnemyArray = new Enemy[3][10];
        for (int i = 0; i < newEnemyArray.length; i++) {
            for (int j = 0; j < newEnemyArray[0].length; j++) {
                newEnemyArray[i][j] = new Enemy(this, (j * this.tileSize) + 100, (i * this.tileSize), enemyNumber);
            }
            switch (enemyNumber) {
                case 1:
                    enemyNumber = 2;
                    break;
                case 2:
                    enemyNumber = 1;
                    break;
                default:
                    break;
            }
        }
        return newEnemyArray;
    }
}
