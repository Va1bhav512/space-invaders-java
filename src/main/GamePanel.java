package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.JPanel;

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
    KeyHandler keyHandler = new KeyHandler();

    int playerX = 100;
    int playerY = screenHeight - tileSize;
    int playerSpeed = 4;

    int FPS = 60;

    Thread gameThread;
    Player player = new Player(this, keyHandler);
    

    Enemy[][] enemyArray;
    // Enemy[] enemyArrayTwo = initializeEnemyArray(2);
    // Enemy[] enemyArrayThree = initializeEnemyArray(1);
    // Enemy[] enemyArrayFour = initializeEnemyArray(2);
    // Enemy[] enemyArrayFive = initializeEnemyArray(1);
    public GamePanel () {
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.gray);
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
            update();
            repaint();
            delta--;
            }
            
            if (currentTime - lastEnemyUpdationTime >= enemyUpdateInterval) {
                updateEnemies();
                lastEnemyUpdationTime = currentTime;
            }
            
        }
    }
    public void update() {
        player.update();
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
        // enemyOne.draw(g2);
        // enemyTwo.draw(g2);

        for (int i = 0; i < enemyArray.length; i++) {
            for (int j = 0; j < enemyArray[0].length; j++) {
                enemyArray[i][j].draw(g2);
            }
        }
        g2.dispose();
    }

    public Enemy[][] initializeEnemyArray(int enemyNumber) {
        Enemy[][] newEnemyArray = new Enemy[3][10];
        for (int i = 0; i < newEnemyArray.length; i++) {
            // enemyArray[i] = new Enemy(this, (i * this.tileSize) + 100, enemyNumber);
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
