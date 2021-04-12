/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Random;


/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact with one another. Take
 * time to understand how the timer interacts with the different methods and how it repaints the GUI
 * on every tick().
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {
    
    
    private Random rand = new Random(); // RandomGenerator for bricks

    // the state of the game logic
    private Square square; // the Black Square, keyboard control
    private Circle ball1; // the Golden Snitch, bounces
    private Circle ball2; // second ball, gets added when addBallBrick is hit
    private boolean ball2Active = false;
    private int gridWidth = 11;
    private int gridHeight = 6;
    private int score = 0;
    private int offset = 10;
    private int paddleWidth = 60;
    private int ballInitVelX = 4;
    private int ballInitVelY = 7;
    private int numTicks = 0; // Number of ticks happened
    private int numBricks = gridWidth * gridHeight;
    private String playerName;
    private int livesLeft = 3;
    private boolean outcome = false;  // true in case player wins
    private String pathToFile = "files/HighScores.txt";
    
    private Brick[][] bricks; // grid of bricks
    
 
    private boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private JLabel currScore;
    private JLabel numLives;

   
    private Writer wr = new Writer();
    
    // Game constants
    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;
    public static final int SQUARE_VELOCITY = 7;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    


    public GameCourt(JLabel status, JLabel score, JLabel numLives) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.

        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        

        timer.start(); // MAKE SURE TO START THE TIMER!   
        
        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key is pressed, by
        // changing the square's velocity accordingly. (The tick method below actually moves the
        // square.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    square.setVx(-SQUARE_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    square.setVx(SQUARE_VELOCITY);
                } 
            }

            public void keyReleased(KeyEvent e) {
                square.setVx(0);
            }
        });

        this.status = status;
        this.currScore = score;
        this.numLives = numLives;
    }
    
    
    public void resetForButton() {
        brickSetUp();
        reset();
        score = 0;
        livesLeft = 3;
        currScore.setText("Score: " + score);
        numLives.setText("Number of Lives: " + livesLeft);
        
    }
    
    

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        ball2Active = false;
        numTicks = 0;
       
        
        ballInitVelX = rand.nextInt(6);
        int xVelDir = ballInitVelX;
        if (rand.nextBoolean()) {
            xVelDir = -ballInitVelX;
        }
        
        ball1 = new Circle(xVelDir, ballInitVelY, COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);
        square = new Square(COURT_WIDTH, COURT_HEIGHT, Color.BLACK, paddleWidth);
        
        
        playing = true;
        currScore.setText("Score: " + score);
        numLives.setText("Number of Lives: " + livesLeft);
        status.setText("Running...");
        
        

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
        
  
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
            if (numTicks == 20) {
                ball1.move();
                if (ball2Active) {
                    ball2.move();
                }
                numTicks = 19;
            }
            
            // advance the paddle in its current direction.
            square.move();

//            System.out.println(square.getPx());
            if (ball1.hitWall() != null && ball1.hitWall().equals(Direction.DOWN)) {               
                decreaseLives();
            }
            
            if (numBricks == 0) {
                status.setText("You win! Final");
                wr.writeToFile(score, playerName, pathToFile);
                playing = false;   
                outcome = true;
            }
            // make the ball bounce off walls...
            ball1.bounce(ball1.hitWall());

            // ...and the paddle
            ball1.bounce(ball1.hitObj(square));
            
            if (ball2Active) {
                if (ball2.hitWall() != null && ball2.hitWall().equals(Direction.DOWN)) {
                    decreaseLives();
                }
                ball2.bounce(ball2.hitWall());
                ball2.bounce(ball2.hitObj(square));
            }
            
            
            // ...and the bricks
            // first ball

            Brick br = new Brick(0, 0, 0, 0, Color.BLUE);
            int height = br.getHeight();
            if (ball1.getPy() < (gridHeight / 2) * (height + 2) + offset) {           
                for (int r = 0; r <= (gridHeight / 2); r++) {
                    for (int c = 0; c < gridWidth; c++) {
                        if (bricks[r][c] != null) {
                            Direction dir = ball1.hitObj(bricks[r][c]);
                            if (dir != null) {
                                ball1.bounce(dir);
                                bricks[r][c].breakBrick(r, c, this);
                                numBricks--;
                            }
                        }
                    }
                }
            } else {
                for (int r = (gridHeight / 2) - 1; r < gridHeight; r++) {
                    for (int c = 0; c < gridWidth; c++) {
                        if (bricks[r][c] != null) {
                            Direction dir = ball1.hitObj(bricks[r][c]);
                            if (dir != null) {
                                ball1.bounce(dir);
                                bricks[r][c].breakBrick(r, c, this);
                                numBricks--;
                            }
                        }
                    }
                }
            }         
            
            // For second ball           
            if (ball2Active) {
                if (ball2.getPy() < (gridHeight / 2) * (height + 2) + offset) {
                    for (int r = 0; r <= (gridHeight / 2); r++) {
                        for (int c = 0; c < gridWidth; c++) {
                            if (bricks[r][c] != null) {
                                Direction dir = ball2.hitObj(bricks[r][c]);
                                if (dir != null) {
                                    ball2.bounce(dir);
                                    bricks[r][c].breakBrick(r, c, this);
                                    numBricks--;
                                }
                            }
                        }
                    }
                
                } else {
                    for (int r = (gridHeight / 2) - 1; r < gridHeight; r++) {
                        for (int c = 0; c < gridWidth; c++) {
                            if (bricks[r][c] != null) {
                                Direction dir = ball2.hitObj(bricks[r][c]);
                                if (dir != null) {
                                    ball2.bounce(dir);
                                    bricks[r][c].breakBrick(r, c, this);
                                    numBricks--;
                                }
                            }
                        }
                    }
                }
            }
            
         

            // update the display
            repaint();
            numTicks++;

        }
    }

    
    private void brickSetUp() {
        bricks = new Brick[gridHeight][gridWidth];

        Brick br = new Brick(0, 0, 0, 0, Color.BLUE);
        int width = br.getWidth();
        int height = br.getHeight();
        int dx = (COURT_WIDTH - ((gridWidth + 1) * gridWidth + 2 * gridWidth)) / 2;
       
        
        for (int r = 0; r < gridHeight; r++) {
            for (int c = 0; c < gridWidth; c++) { 
                int ran = rand.nextInt(13);
                switch (ran) {
                    case 0: bricks[r][c] = new EnlargePaddleBrick(c * (width + 2) + dx - 50, 
                            r * (height + 2) + offset,                          
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN); 
                            break;
                    case 1: bricks[r][c] = new AddBallBrick(c * (width + 2) + dx - 50, 
                            r * (height + 2) + offset,                          
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN); 
                            break;
                    case 2: bricks[r][c] = new BoostScoreBrick(c * (width + 2) + dx - 50, 
                            r * (height + 2) + offset,                          
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN); 
                            break;
                    case 3: bricks[r][c] = new BoostLivesBrick(c * (width + 2) + dx - 50, 
                            r * (height + 2) + offset,                          
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN); 
                            break;
                    default: bricks[r][c] = new Brick(c * (width + 2) + dx - 50, 
                            r * (height + 2) + offset,                          
                            COURT_WIDTH, COURT_HEIGHT, Color.GREEN); 
                            break;
                }
                

                switch (r) {
                    case 0: bricks[r][c].setColor(Color.BLUE); 
                    break;
                    case 1: bricks[r][c].setColor(Color.BLUE); 
                    break;
                    case 2: bricks[r][c].setColor(Color.BLACK); 
                    break;
                    case 3: bricks[r][c].setColor(Color.YELLOW); 
                    break;
                    case 4: bricks[r][c].setColor(Color.YELLOW); 
                    break;
                    case 5: bricks[r][c].setColor(Color.BLACK); 
                    break;
                    case 6: bricks[r][c].setColor(Color.GREEN); 
                    break;
                    case 7: bricks[r][c].setColor(Color.GREEN); 
                    break;
                    default: bricks[r][c].setColor(Color.CYAN); 
                    break;
                }
            }             
        }
    }
    
    private void decreaseLives() {
        livesLeft--;
        if (livesLeft == 0) {
            status.setText("You lose! Final");
            wr.writeToFile(score, playerName, pathToFile);
            numLives.setText("Number of Lives: " + 0);
            playing = false; 
            livesLeft = 3;
        } else {
            reset();            
        }  
    }
    
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (square != null) {
            square.draw(g);
            ball1.draw(g);
            if (ball2Active) {
                ball2.draw(g);
            }
            for (int r = 0; r < gridHeight; r++) {
                for (int c = 0; c < gridWidth; c++) {
                    Brick newBrick = bricks[r][c];
                    if (newBrick != null) {
                        newBrick.draw(g);
                    }
                }
            }
        }
        
    }

    
    public void modifyForBrick(int row, int col) {
        bricks[row][col] = null;
        incrementScore(10);
        repaint();
    }
    
    public void modifyForEnlargePaddleBrick(int row, int col) {
        bricks[row][col] = null;
        incrementScore(6);
        if (square.getWidth() == paddleWidth) {
            square.setWidth(square.getWidth() / 2);
        }
        repaint();
    }
    
    public void modifyForaddBallBrick(int row, int col) {
        bricks[row][col] = null;
        if (!ball2Active) {
            incrementScore(12);
            ballInitVelX = rand.nextInt(6);
            int xVelDir = ballInitVelX;
            if (rand.nextBoolean()) {
                xVelDir = -ballInitVelX;
            }
            ball2 = new Circle(xVelDir, ballInitVelY - 1, COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
            ball2Active = true;
            repaint();
        }
    }
    
    
    public void modifyForBoostScoreBrick(int row, int col) {
        bricks[row][col] = null;
        incrementScore(200);
        ball2Active = false;
        if (square.getWidth() == paddleWidth / 2) {
            square.setWidth(paddleWidth);
        }
        repaint();
    }
    
    public void modifyForBoostLivesBrick(int row, int col) {
        bricks[row][col] = null;
        incrementScore(16);
        livesLeft++;
        numLives.setText("Number of Lives: " + livesLeft);
        repaint();
    }
    
    
    
    
    
    // Methods for testing purposes   
    
    public int getScore() {
        return score;
    }
    
    public int[] getBall1Coords() {
        int[] coords = new int[2];
        coords[0] = ball1.getPx();
        coords[1] = ball1.getPy();
        return coords;
    }
    
    public int getBall1VelX() {
        return ballInitVelX;
    }
    
    public int getBall1VelY() {
        return ballInitVelX;
    }
    
    public int[] getBall2Coords() {
        int[] coords = new int[2];
        coords[0] = ball2.getPx();
        coords[1] = ball2.getPy();
        return coords;
    }
    
    public int[] getPaddleCoords() {
        int[] coords = new int[2];
        coords[0] = square.getPx();
        coords[1] = square.getPy();
        return coords;
    }
    
    public int getLivesLeft() {
        return livesLeft;
    }
    
    public int getGridHeight() {
        return gridHeight;
    }
    
    public int getGridWidtht() {
        return gridWidth;
    }
    
    public boolean isBall2Active() {
        return ball2Active;
    }
    
    public int getPaddleWidth() {
        return square.getWidth();
    }
    
    public int getBallSize() {
        return ball1.getWidth();
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public boolean isPlaying() {
        return playing;
    }
    
    public Brick[][] getBrickGrid() {
        return bricks.clone();
    }
    
    public boolean getOutcome() {
        return outcome;
    }
    
    private void incrementScore(int x) {
        score += x;
        currScore.setText("Score: " + score);
    }
    
    public void setPlayer(String name) {
        playerName = name;
    }
    
    
    public void setBall1CoordX(int x) {
        ball1.setPx(x);
    }
    
    public void setBall1CoordY(int y) {
        ball1.setPx(y);
    }
    
    public void setBall2CoordX(int x) {
        ball2.setPx(x);
    }
    
    public void setBall2CoordY(int y) {
        ball2.setPx(y);
    }
    
    
    public void moveBall1() {
        ball1.move();
    }
    
    public void moveBall2() {
        ball2.move();
    }
    
        
    
    public void setPaddleCoords(int x, int y) {
        square.setPx(x);
        square.setPx(y);
    }
    
    public void setBall1VelX(int vx) {
        ball1.setVx(vx);
    }
    
    public void setBall1VelY(int vy) {
        ball1.setVy(vy);
    }
    
    
//    public void setBallBrickWillIntersectTrue(int row, int col) {
//        ball1.willIntersect(bricks[row][col]) = true;
//    }
    
    public void checkForCollisions() {
        if (ball1.hitWall() != null && ball1.hitWall().equals(Direction.DOWN)) {
            decreaseLives();
        }
    }
    
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}