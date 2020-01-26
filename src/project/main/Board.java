package project.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
import project.factory.Factory;

public class Board extends JPanel implements ActionListener 
{

    private final int S_WIDTH = 600;
    private final int S_HEIGHT = 600;
    private final int SIZE_OF_DOT = 20;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;
    int sum=0;
    boolean value;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int food_x;
    private int food_y;

    private boolean LEFT_DIRECTION = false;
    private boolean RIGHT_DIRECTION = true;
    private boolean UP_DIRECTION = false;
    private boolean DOWN_DIRECTION = false;
    private boolean inGame = true;

    private Timer timer;

    public Board() 
    {    
        initBoard();                                               // CALLING FROM THIS CLASS
    }
    
    private void initBoard() 
    {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(S_WIDTH, S_HEIGHT));
     
        initGame();
    }

    private void initGame() 
    {
        dots = 3;

        for (int z = 0; z < dots; z++)
        {
            x[z] = 100 - z * 20;
            y[z] = 60;
        }
        
        locateFood();

        Factory.getPlayMusic().playMusic("src\\Music\\enter.wav");
        
        timer = new Timer(DELAY, this);
        timer.start();
        
        
    }

    @Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        try
        {
            doDrawing(g);
        }
        catch (InterruptedException ex) 
        {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void doDrawing(Graphics g) throws InterruptedException {
        
        if (inGame)
        {
            g.setColor(Color.orange);
            g.fillRect(food_x,food_y,20,20);
            
            for (int z = 0; z < dots; z++) 
            {
                if (z == 0) 
                {
                      g.setColor(Color.red);
                      g.fillRect(x[z],y[z],20, 20);
                    
                }
                else if(z%2 == 1)
                {
                    g.setColor(Color.green);
                    g.fillRect(x[z],y[z],20,20);
                }
                else
                {
                    g.setColor(Color.pink);
                    g.fillRect(x[z],y[z],20,20);
                }
            }
        //    System.out.println("HELLO  :  "+value);
            if(value)
             {
                 sum=sum+10;
             }
            Toolkit.getDefaultToolkit().sync();
            scoreUpdation(g);
            value=false;
        } 
        else 
        {
            Factory.getPlayMusic().playMusic("src\\Music\\gameover.wav");
            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) throws InterruptedException 
    {    
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (S_WIDTH - metr.stringWidth(msg)) / 2, S_HEIGHT / 2);
        
        Thread.sleep(2000);
    }

    private boolean checkFood() throws InterruptedException 
    {
        if ((x[0] == food_x) && (y[0] == food_y)) 
        {
            dots++;
        //    System.out.println(dots);
            Factory.getPlayMusic().playMusic("src\\Music\\eat.wav");
            Thread.sleep(100);
            locateFood();
            return true;
        }
        
        return false;
    }

    private void snakeMovement() 
    {
        for (int z = dots; z > 0; z--) 
        {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
            
        //   System.out.println(dots);
        //    System.out.println("x="+x[z]+"y="+y[z]);
        }

        if (LEFT_DIRECTION)
        {
            x[0] -= SIZE_OF_DOT;
        }

        if (RIGHT_DIRECTION) 
        {
            x[0] += SIZE_OF_DOT;
        }

        if (UP_DIRECTION) 
        {
            y[0] -= SIZE_OF_DOT;
        }

        if (DOWN_DIRECTION)
        {
            y[0] += SIZE_OF_DOT;
        }
    }

    private void checkCollision() 
    {
        for (int z = dots; z > 0; z--)
        {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) 
            {
                inGame = false;
            }
        }

        if (y[0] >= S_HEIGHT)
        {
          y[0]=0;  
        }

        if (y[0] < 0)
        {
           y[0]=S_HEIGHT;
        }

        if (x[0] >= S_WIDTH)
        {
           x[0]=0;
        }

        if (x[0] < 0) 
        {
           x[0]=S_WIDTH;
        }
        
        if (!inGame) 
        {
            timer.stop();
        }
    }

    private void locateFood()
    { 
        Random r=new Random();
	
	int low=0;
	int max=29;
	
	   food_x=((r.nextInt(max-low+1)+low)*(20));
           food_y=((r.nextInt(max-low+1)+low)*(20));
           
        //   System.out.println("Location of the Apple is : "+food_x+" "+food_y);	
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (inGame) 
        {
         //   System.out.println("Action Performed Method");
            try 
            {      
                value=checkFood();                 
            }
            catch (InterruptedException ex) 
            {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            }
            checkCollision();
            snakeMovement();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter 
    {
        @Override
        public void keyPressed(KeyEvent e) 
        {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!RIGHT_DIRECTION))
            {
                LEFT_DIRECTION = true;
                UP_DIRECTION = false;
                DOWN_DIRECTION = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!LEFT_DIRECTION)) 
            {
                RIGHT_DIRECTION = true;
                UP_DIRECTION = false;
                DOWN_DIRECTION = false;
            }

            if ((key == KeyEvent.VK_UP) && (!DOWN_DIRECTION)) 
            {
                UP_DIRECTION = true;
                RIGHT_DIRECTION = false;
                LEFT_DIRECTION = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!UP_DIRECTION)) 
            {
                DOWN_DIRECTION = true;
                RIGHT_DIRECTION = false;
                LEFT_DIRECTION = false;
            }
        }
    }
    
     private void scoreUpdation(Graphics g)  
     {   
        String msg = "SCORE : "+sum;
        Font small = new Font("Helvetica", Font.BOLD, 16);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (S_WIDTH - metr.stringWidth(msg)-30), 30);
     }
}
