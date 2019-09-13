import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Tetris extends JPanel {
   private final Point[][][] Tetraminos = {
   		// I-Piece
   		{
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
   			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
   			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
   		},
   		
   		// J-Piece
   		{
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
   			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
   			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
   		},
   		
   		// L-Piece
   		{
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
   			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
   			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
   		},
   		
   		// O-Piece
   		{
   			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
   			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
   			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
   			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
   		},
   		
   		// S-Piece
   		{
   			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
   			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
   			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
   			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
   		},
   		
   		// T-Piece
   		{
   			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
   			{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
   			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
   			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
   		},
   		
   		// Z-Piece
   		{
   			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
   			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
   			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
   			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
   		}
   };
	
   private final Color[] tetraminoColors = {
   	Color.magenta, Color.blue, Color.orange, Color.yellow, Color.green, Color.cyan, Color.red
   };
	
   private Point pieceOrigin;
   private Point ghostOrigin;
   private Point nextOrigin;
   private Point holdOrigin;
   private int ghostPiece;
   private int currentPiece;
   private int nextPiece;
   private int holdPiece;
   private int rotation;
   private int start;
   private int hold;
   private int speed;
   private boolean pressed=false;
   private ArrayList<Integer> nextPieces = new ArrayList<Integer>();

   private long score;
   private Color[][] well;
   
   public long getScore()//returns score
   {
      return score;
   }   
   public int getStart()//returns start status (0-game didnt start, 1-game start, 2-gameover)
   {
      return start;
   }   
   public int getSpeed()//returns speed of the pieces based on number input from player
   {
      if(speed==1)
         return 1000;//1 second
      else if(speed==2)
         return 900;
      else if(speed==3)
         return 700;
      else if(speed==4)
         return 600;
      else
         return 500;
   }
   public void setSpeed(int s)//sets speed
   {
      speed=s;
   }                     
	// Creates a border around the well and initializes the dropping piece
   private void init() {
      start=0;
      hold=0;
      score=0;
      well = new Color[20][24];
      for (int i = 0; i < 20; i++) {
         for (int j = 0; j < 23; j++) {
            if (i == 0 || i >= 11 || j == 22)
               well[i][j] = Color.GRAY;               
            else
               well[i][j] = Color.BLACK;
            if(i >= 12 && i <= 16 && j >= 15 && j <= 19)
               well[i][j] = Color.BLACK;               
         }
      }
      newPiece();
   }
	
	// Put a new, random piece into the dropping position
   public void newPiece()
   {
      pressed=false;
      pieceOrigin = new Point(5, 1);
      ghostOrigin = new Point(5,10);
      nextOrigin= new Point(14,2);//location of next piece
      rotation = 0;
      if (nextPieces.isEmpty())
      {
         Collections.addAll(nextPieces, 0, 1, 2, 3, 4, 5, 6);
         Collections.shuffle(nextPieces); 
         if(start==0)
         {
            nextPiece = nextPieces.get(0);
            nextPieces.remove(0);
            start=1;
         }   
      }
      currentPiece = nextPiece;
      ghostPiece = currentPiece;
      nextPiece=nextPieces.get(0);
      nextPieces.remove(0);
      move(0,0);
      if(collidesAt(pieceOrigin.x, pieceOrigin.y, rotation, currentPiece))
         gameOver();   
   }
   public void hold()//holding the piece
   {
      if(!pressed)//only can be pressed once per piece
      {
         if(hold==0)//if no held piece
         {
            holdPiece=currentPiece;
            holdOrigin = new Point(13,16);
            hold=1;
            newPiece();
            pressed=true;
         }
         else
         {
            int temp=holdPiece;
            holdPiece=currentPiece;
            currentPiece=temp;
            pieceOrigin = new Point(5, 1);
            ghostOrigin = new Point(5, 1);
            ghostPiece = currentPiece;
            while(!collidesAt(ghostOrigin.x,ghostOrigin.y+1,rotation,ghostPiece))//drops ghost piece
            {
               ghostOrigin.y += 1;
            } 
            pressed=true;
         }      
      }
   }   
	
	// Collision test for the dropping piece
   private boolean collidesAt(int x, int y, int rotation, int piece) {
      for (Point p : Tetraminos[piece][rotation]) {
         if (well[p.x + x][p.y + y] != Color.BLACK&&well[p.x + x][p.y + y] != Color.lightGray) {
            return true;
         }
      }
      return false;
   }
	
	// Rotate the piece clockwise or counterclockwise
   public void rotate(int i) {
      ghostOrigin.y=pieceOrigin.y;
      int newRotation = (rotation + i) % 4;
      if (newRotation < 0) {
         newRotation = 3;
      }
      if (!collidesAt(pieceOrigin.x, pieceOrigin.y, newRotation, currentPiece)) {
         rotation = newRotation;
      }
      while(!collidesAt(ghostOrigin.x,ghostOrigin.y+1,rotation,ghostPiece))//drops ghost piece
      {
         ghostOrigin.y += 1;
      } 
      repaint();
   }
	
	// Move the piece left or right
   public void move(int i,int j)
   {
      ghostOrigin.y=pieceOrigin.y;
      if (j!=0&&!collidesAt(pieceOrigin.x, pieceOrigin.y + j, rotation, currentPiece)){
         pieceOrigin.y += j;
      }
      if (!collidesAt(pieceOrigin.x + i, pieceOrigin.y, rotation, currentPiece)) {
         pieceOrigin.x += i;
         ghostOrigin.x += i;
      }
      while(!collidesAt(ghostOrigin.x,ghostOrigin.y+1,rotation,ghostPiece))//drops ghost piece
      {
         ghostOrigin.y += 1;
      } 
      repaint();
   }
	
	// Drops the piece one line or fixes it to the well if it can't drop
   public boolean dropDown()
   {
      if (!collidesAt(pieceOrigin.x, pieceOrigin.y + 1, rotation, currentPiece))
      {
         pieceOrigin.y += 1;
         repaint();
      }
      else
      {
         fixToWell(currentPiece);
         repaint();
         return false;
      }	
      return true;
   }
	
	// Make the dropping piece part of the well, so it is available for
	// collision detection.
   public void fixToWell(int piece) {
      for (Point p : Tetraminos[piece][rotation]) {
         well[pieceOrigin.x + p.x][pieceOrigin.y + p.y] = tetraminoColors[piece];
      }
      clearRows();
      newPiece();
   }
	
   public void deleteRow(int row) {
      for (int j = row-1; j > 0; j--) {
         for (int i = 1; i < 11; i++) {
            well[i][j+1] = well[i][j];
         }
      }
   }
	
	// Clear completed rows from the field and award score according to
	// the number of simultaneously cleared rows.
   public void clearRows() {
      boolean gap;
      int numClears = 0;
   	
      for (int j = 21; j > 0; j--) {
         gap = false;
         for (int i = 1; i < 11; i++) {
            if (well[i][j] == Color.BLACK||well[i][j] == Color.lightGray) {
               gap = true;
               break;
            }
         }
         if (!gap) {
            deleteRow(j);
            j += 1;
            numClears += 1;
         }
      }
   	
      switch (numClears) {
         case 1:
            score += 10;
            break;
         case 2:
            score += 30;
            break;
         case 3:
            score += 50;
            break;
         case 4:
            score += 80;
            break;
      }
   }
	
	// Draw the falling piece
   private void drawPiece(Graphics g, int piece, int rot, Point origin, Color c)
   {		
      g.setColor(c);
      for (Point p : Tetraminos[piece][rot])
      {
         g.fillRect((p.x + origin.x) * 26,(p.y + origin.y) * 26, 25, 25);
      }
   }	
	
   public void paintComponent(Graphics g)
   {
   	// Paint the well
      g.fillRect(0, 0, 26*20, 26*23);
      for (int i = 0; i < 20; i++)
      {
         for (int j = 0; j < 23; j++)
         {
            g.setColor(well[i][j]);
            g.fillRect(26*i, 26*j, 25, 25);
         }
      }
   	
   	// Display the score
      g.setColor(Color.WHITE);
      g.drawString("" + score*10, 19*20, 25);
   	
   	// Draw the currently falling piece
      drawPiece(g, ghostPiece, rotation, ghostOrigin, Color.lightGray);
      drawPiece(g, currentPiece, rotation, pieceOrigin, tetraminoColors[currentPiece]);
      drawPiece(g, nextPiece, 0, nextOrigin, tetraminoColors[nextPiece]);
      if(hold==1)
      {
         drawPiece(g, holdPiece, 0, holdOrigin, tetraminoColors[holdPiece]);
      }	
   }
   private void gameOver()//game over screen
   {
      start = 2;
      Object selection = JOptionPane.showOptionDialog(f, "Game Is Over!!\nYour score is: "+game.getScore()*10, "Game Over", 
         	JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[] {"Play Again", "Exit TETRIS"}, "Play Again");
      if(selection.equals(JOptionPane.OK_OPTION)) {
         game.setVisible(false);
         panel.setVisible(true);
         
      }
      else {
         System.exit(0);
      }
   }
   private static JFrame f;
   private static Tetris game;
   private static JPanel panel;
   public static void main(String[] args)
   {
      f = new JFrame("Tetris");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setSize(20*26+10, 26*23+25);
        
      panel = new JPanel();
      panel.setLayout(null);
         
      final JLabel label = new JLabel("Enter Initial Speed(1-5):");
      label.setBounds(150, 150, 200, 30);
         
      final JTextField speed = new JTextField();
      speed.setBounds(150, 180, 200, 30);
         
      final JButton button = new JButton("Start Game!");
      button.setBounds(200, 320, 150, 30);
      button.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               String spdTxt = speed.getText();
               try {
                  int n = Integer.parseInt(spdTxt);
                  if(n>=1 && n<=5) {
                     panel.setVisible(false);
                     startGame(f);
                     game.setSpeed(n);
                  }
               }
               catch(Exception e) {
                 
               }
            }
         });
         
      panel.add(label);
      panel.add(speed);
      panel.add(button);
      f.add(panel);
         
      f.setVisible(true);
   }
	
   private static void startGame(JFrame f) {
      if(game!=null) {
         game.init();
         game.setVisible(true);
         f.requestFocus();
         return;
      }
      game = new Tetris();
      game.init();
      f.requestFocus();
      f.add(game); 
   	
   	
   	// Keyboard controls
      f.addKeyListener(
         new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }
            public void keyPressed(KeyEvent e) {
               if(game.getStart()!=2)
               {
                  switch (e.getKeyCode())
                  {
                     case KeyEvent.VK_UP:
                        game.rotate(+1);
                        break;
                     case KeyEvent.VK_DOWN:
                        game.dropDown();
                        game.score+=1;
                        break;
                     case KeyEvent.VK_LEFT:
                        game.move(-1,0);
                        break;
                     case KeyEvent.VK_RIGHT:
                        game.move(+1,0);
                        break;
                     case KeyEvent.VK_SPACE:
                        for(int i=0;i<19;i++)
                        {
                           if(game.dropDown()==false)
                              break;
                           game.score+=1;   
                        }      
                        break;
                     case KeyEvent.VK_C:
                        game.hold();
                        break;   
                  }
               }
            } 
            
         
            public void keyReleased(KeyEvent e) {
            }
         });
   	
   	// Make the falling piece drop depending on speed
         new Thread() {
            @Override public void run() {
               while (game.getStart()!=2) {
                  try {
                     Thread.sleep(game.getSpeed()/(game.getScore()/400+1));
                     game.dropDown();
                  } 
                  catch ( InterruptedException e ) {}
               }
            }
         }.start();
   }
}
