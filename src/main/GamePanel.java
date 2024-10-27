package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import javax.swing.JPanel;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

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
    private long lastBulletTimeByPlayer = 0;
    private final long bulletCooldown = 500000000;
    private Random random = new Random();
    KeyHandler keyHandler = new KeyHandler();
    public boolean isGameEnd = false;
    public boolean enemiesRemain = true;
    public boolean lost = false;
    public int score = 0;

    int playerX = 100;
    int playerY = screenHeight - tileSize;
    int playerSpeed = 4;

    int FPS = 60;

    Thread gameThread;
    public List<Bullet> bullets = new ArrayList<>();
    public List<Bullet> enemyBullets = new ArrayList<>();
    Set<Integer> xEnemyLastRow = new HashSet<>();
    public Player player = new Player(this, keyHandler);

    public Enemy[][] enemyArray;
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
    private void resetGame() {
      score = 0;
      enemiesRemain = true;
      isGameEnd = false;
      bullets.clear();
      enemyBullets.clear();
      xEnemyLastRow.clear();
      player = new Player(this, keyHandler);
      enemyArray = initializeEnemyArray(2);
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
                if (!isGameEnd) {
                    player.update();
                    updateBullets();
                    updateEnemiesDeath();
                    if (currentTime - lastEnemyUpdationTime >= enemyUpdateInterval) {
                        updateEnemies();
                        lastEnemyUpdationTime = currentTime;
                    }
                }
                else {
                    // drawEndScreen();
                    if (keyHandler.spacePressed || keyHandler.enterPressed) {
                      resetGame();
                      keyHandler.spacePressed = false;
                      keyHandler.enterPressed = false;
                      lastEnemyUpdationTime = System.nanoTime();
                      startGameThread();
                      return;
                    }
                }
                    repaint();
                    delta--;
            }
        }
    }   
    public void updateBullets() {
        long currentBulletTime = System.nanoTime();
        if ((keyHandler.spacePressed || keyHandler.enterPressed) && (currentBulletTime - lastBulletTimeByPlayer >= bulletCooldown)) {
            bullets.add(new Bullet(this,(player.x + tileSize/2)-5,player.y,"up",10));
            keyHandler.enterPressed = false;
            keyHandler.spacePressed = false;
            lastBulletTimeByPlayer = currentBulletTime;
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update();
            if (bullet.y < 0 || bullet.y > screenHeight || bullet.collidesWithEnemy()) {
                bullets.remove(i);
                i--;
            }
            if (bullet.bDirection == "down" && bullet.collidesWithPlayer()) {
                bullets.remove(i);
                isGameEnd = true;
            }
        }
    }
    public void updateEnemiesDeath() {
        enemiesRemain = false;
        for (int i = 0; i < enemyArray.length; i++) {
            for (int j = 0; j < enemyArray[0].length; j++) {
                Enemy enemy = enemyArray[i][j];
                if (enemy != null) {
                    enemiesRemain = true;
                    if (enemy.isHit) {
                        enemyArray[i][j] = null;
                        score++;
                    }
                    if (random.nextDouble() < 0.0005) {
                        enemy.shoot();
                    }
                }
            }
        }
        if (!enemiesRemain) {
          isGameEnd = true;
        }
    }
    public void updateEnemies() {
        for (int i = 0; i < enemyArray.length; i++) {
            for (int j = 0; j < enemyArray[0].length; j++) {
                Enemy enemy = enemyArray[i][j];
                if (enemy != null) {
                    enemy.update();
                    if (enemy.isHit) {
                        enemyArray[i][j] = null;
                    }
                    // enemy.updateBulletsEnemy();
                }
                // enemyArray[i][j].update();
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        player.draw(g2);

        for (int i = 0; i < enemyArray.length; i++) {
            for (int j = 0; j < enemyArray[0].length; j++) {
                if (enemyArray[i][j] != null) {
                enemyArray[i][j].draw(g2);
                }
            }
        }
        for (Bullet bullet : bullets) {
            bullet.draw(g2);
        }
        String scoreString = "Score: " + Integer.toString(score);
        int length1 = (int)g2.getFontMetrics().getStringBounds(scoreString, g2).getWidth();
        int x1 = this.tileSize/2 - length1/2;
        int y1 = this.tileSize/2 - length1/2 + this.tileSize/2;
        g2.drawString(scoreString,x1,y1);
        if (isGameEnd) {
            String text = "GAME ENDED! YOUR SCORE WAS " + score;
            if (!enemiesRemain) {
              text += " AND YOU WON!";
            } else if (lost) {
              text += " AND YOU LOST!";
            }
            int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
            int x = this.screenWidth/2 - length/2;
            int y = this.screenHeight/2;
            g2.drawString(text, x, y);
        }
        g2.dispose();
    }

    public Enemy[][] initializeEnemyArray(int enemyNumber) {
        Enemy[][] newEnemyArray = new Enemy[3][10];
        for (int i = 0; i < newEnemyArray.length; i++) {
            for (int j = 0; j < newEnemyArray[0].length; j++) {
                newEnemyArray[i][j] = new Enemy(this, (j * this.tileSize) + 100, (i * this.tileSize), enemyNumber);
                xEnemyLastRow.add(j);
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
