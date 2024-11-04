import java.awt.*;
import java.awt.event.*;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{




    private class Tile{
        int y;
        int x;

        Tile(int y, int x){
            this.y = y;
            this.x = x;
        }
    }
    int boardWidht;
    int boardHeigth;
    int tileSize = 25;

    Tile sneakHead;

    Tile food;
    Random random;

    //body
    ArrayList<Tile> sneakBody;

    // Game Logic
    Timer gamelLoop;
    int velocityY;
    int velocityX;
    boolean gameOver = false;



    SnakeGame(int boardWidht,int boardHeigth){
        this.boardWidht = boardWidht;
        this.boardHeigth = boardHeigth;
        setPreferredSize(new Dimension(this.boardWidht, this.boardHeigth));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);


        sneakHead = new Tile(5,5);
        sneakBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();

        velocityY = 0;
        velocityX = 0;

        gamelLoop = new Timer(100, this);
        gamelLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g){
        //Grid
//        for (int i = 0; i< boardWidht/tileSize; i++){
//            g.drawLine(i*tileSize, 0 , i*tileSize, boardHeigth);
//            g.drawLine(0, i*tileSize, boardWidht, i*tileSize);
//        }


        //Food
        g.setColor(Color.red);
//        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);


        //SnakeHead
        g.setColor(Color.green);
        g.fill3DRect(sneakHead.x*tileSize, sneakHead.y*tileSize, tileSize, tileSize, true);

        //SneakBody

        for (int i =0; i<sneakBody.size(); i++){
            Tile snakePart = sneakBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y *tileSize, tileSize, tileSize, true);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("GameOver: " + String.valueOf(sneakBody.size()), tileSize-16, tileSize);

        }else {
            g.drawString("Score: " + String.valueOf(sneakBody.size()), tileSize -16, tileSize);
        }
    }

    public void placeFood(){
        food.x = random.nextInt(boardWidht/tileSize);
        food.y = random.nextInt(boardHeigth/tileSize);


    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }
    public void move(){
        // eat food
        if (collision(sneakHead, food)){
            sneakBody.add(new Tile(food.x, food.y));
            placeFood();
        }
        //Snake body
        for (int i = sneakBody.size()-1; i>=0; i--){
            Tile snakePart = sneakBody.get(i);
            if (i==0){
                snakePart.y = sneakHead.y;
                snakePart.x = sneakHead.x;
            }else {
                Tile prevSnakePart = sneakBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Score


        //snake head
        sneakHead.x += velocityX;
        sneakHead.y += velocityY;

        //game over conditions
        for (int i = 0; i<sneakBody.size(); i++){
            Tile snakePart = sneakBody.get(i);
            //collide with snake head
            if (collision(sneakHead, snakePart)){
                gameOver = true;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gamelLoop.stop();
        }

        if (sneakHead.x * tileSize < 0 || sneakHead.x * tileSize > boardWidht ||
        sneakHead.y * tileSize < 0 || sneakHead.y * tileSize > boardHeigth){
            gameOver = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;

        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
