import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;

import javax.swing.JLabel;

/** 
 *  You can use this file (and others) to test your
 *  implementation.
 */

public class GameTest {
    
    private GameCourt court;
    private Brick[][] grid;
    final JLabel status = new JLabel("Running...");
    final JLabel currScore = new JLabel("Score: " + 0);
    final JLabel livesLeft = new JLabel("Number of Lives: " + 3);
    
    @BeforeEach
    public void setUp() {       
        court = new GameCourt(status, currScore, livesLeft);
        court.resetForButton();
        grid = court.getBrickGrid();
    }

    @Test
    public void testForCollision() {
        Circle ball = new Circle(0, 0, 400, 400, Color.GREEN);
        Square paddle = new Square(400, 400, Color.GREEN, 100);
        ball.setPx(130);
        ball.setPy(350);
        ball.setVx(0);
        ball.setVy(0);
        assertNotEquals(ball.hitObj(paddle), null);
        
    }
    
    
    @Test
    public void testForNormalBrick() {
        grid[0][0] = new Brick(130, 350, 400, 400, Color.YELLOW);
        grid[0][0].breakBrick(0, 0, court);
        assertEquals(court.getBrickGrid()[0][0], null);
        assertEquals(court.getScore(), 10);

    }
    
    
    @Test
    public void testForAddBallBrick() {
        grid[0][0] = new AddBallBrick(130, 350, 400, 400, Color.YELLOW);
        grid[0][0].breakBrick(0, 0, court);
        assertEquals(court.getBrickGrid()[0][0], null);
        assertTrue(court.isBall2Active());
        assertEquals(court.getScore(), 12);
    }
    
    
    @Test
    public void testBoostScoreBrick() {
        grid[0][0] = new BoostScoreBrick(130, 350, 400, 400, Color.YELLOW);
        grid[0][0].breakBrick(0, 0, court);
        assertEquals(court.getBrickGrid()[0][0], null);
        assertFalse(court.isBall2Active());
        assertEquals(court.getScore(), 200);
    }
    
    @Test
    public void testBoostScoreBrickAfterAddBallBrick() {
        grid[0][0] = new BoostScoreBrick(130, 350, 400, 400, Color.YELLOW);
        grid[0][1] = new AddBallBrick(160, 350, 400, 400, Color.YELLOW);
        grid[0][1].breakBrick(0, 1, court);
        grid[0][0].breakBrick(0, 0, court);
        assertFalse(court.isBall2Active());       
        assertEquals(court.getBrickGrid()[0][0], null);
        assertEquals(court.getBrickGrid()[0][1], null);        
        assertEquals(court.getScore(), 212);
    }
    
    @Test
    public void testBoostLivesBrick() {
        int lives = court.getLivesLeft();
        grid[0][0] = new BoostLivesBrick(130, 350, 400, 400, Color.YELLOW);
        grid[0][0].breakBrick(0, 0, court);
        
        assertEquals(court.getBrickGrid()[0][0], null);
        assertEquals(court.getLivesLeft(), (lives + 1));
        assertEquals(court.getScore(), 16);
    }
    
    
    @Test
    public void testEnlargePaddleBrick() {
        int wid = court.getPaddleWidth();
        grid[0][0] = new EnlargePaddleBrick(130, 350, 400, 400, Color.YELLOW);
        grid[0][0].breakBrick(0, 0, court);
        
        assertEquals(court.getBrickGrid()[0][0], null);
        assertEquals(court.getPaddleWidth(), wid / 2);
        assertEquals(court.getScore(), 6);
    }
    
    @Test
    public void testBoostScoreBrickAfterPaddleBrick() {
        grid[0][0] = new BoostScoreBrick(130, 350, 400, 400, Color.YELLOW);
        grid[0][1] = new EnlargePaddleBrick(160, 350, 400, 400, Color.YELLOW);
        grid[0][1].breakBrick(0, 1, court);
        int wid = court.getPaddleWidth();
        grid[0][0].breakBrick(0, 0, court);
        assertEquals(court.getPaddleWidth(), wid * 2);       
        assertEquals(court.getBrickGrid()[0][0], null);
        assertEquals(court.getBrickGrid()[0][1], null);        
        assertEquals(court.getScore(), 206);
    }
    

    
//    @Test
//    public void testWritingScores() {
//        Writer wr = new Writer();
//        Reader rd = new Reader();
//        wr.writeToFile(20, "Tika", "files/HighScores.txt");
//        wr.writeToFile(10, "ILoveBrickBreaker", "files/HighScores.txt");
//        wr.writeToFile(100, "Top", "files/HighScores.txt");
//        assertEquals(rd.getTopThreeScore().get(0), 100);
//        assertEquals(rd.getTopThreeScore().get(1), 20);
//        assertEquals(rd.getTopThreeScore().get(2), 10);
//        
//    }
    
    @Test
    public void testReadingEmptyFile() {
        Reader rd = new Reader();
        assertTrue(rd.getTopThreeScore().isEmpty());
    }
    
    

    
    
    
    

}
