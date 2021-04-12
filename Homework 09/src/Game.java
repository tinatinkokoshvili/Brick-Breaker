/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
//    private String pathToFile = "files/HighScores.txt";
    public String player;
    private String instructionss = "There is a grid of bricks in the top middle "
            + "part of the screen. A ball is moving all over the screen, bouncing off the "
            + "walls. When a ball hits a brick, brick breaks and disappears."
            + "Player uses keyboard arrows to move the paddle and catch the ball "
            + "using it, preventing the ball from falling on the bottom of the screen."
            + "There are different type of brick, randomly selected and arranged in the grid."
            + "Also, the ball starts to fall initially with a random angle"
            + "Normal brick just adds the score of 10. Special bricks: 1. One of the bricks"
            + "adds another ball to the screen and increases total score by 12. 2. Another "
            + "decreases the width of the paddle. It icreases score by 6. 3. one of them "
            + "boosts score by 200, makes the second ball disappear, if it is on the screen, and "
            + "resizes the paddle, returning its initial width. 4. Last one increments the score"
            + "by 16 and increses number of remaining lives by 1. "
            + "Player initially has 3 lives. If a ball touches the bottom wall"
            + "3 times, player loses. If player breaks all the bricks, player wins."
            + " User can use Reset button"
            + "located at the top of the screen, which resetes the game, instrucions button to "
            + "view instructions, and Top Scores button to view top 3 scores that anyone ever"
            + "gotten in the game! Enjoy playing THE BRICK BREAKER GAME!";
    
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);
        
        
        // Control Panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        

        final JLabel currScore = new JLabel("Score: " + 0);
        status_panel.add(currScore);
        
        final JLabel livesLeft = new JLabel("Number of Lives: " + 3);
        control_panel.add(livesLeft);
        
        // Main playing area
        final GameCourt court = new GameCourt(status, currScore, livesLeft);
        frame.add(court, BorderLayout.CENTER);

        

        
        //reset Button
        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.resetForButton();
            }
        });
        control_panel.add(reset);
        Reader rd = new Reader();
        System.out.println(rd.getUsersAndScores());
        
        // TopScore button - shows top 3 score when pressed
        final JButton topScores = new JButton("Top Scores");
        topScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Reader rd = new Reader();
                JOptionPane.showMessageDialog(frame, rd.getUsersAndScores());
            }
        });
        control_panel.add(topScores);
        
        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, instructionss);
            }
        });
        control_panel.add(instructions);
        


        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Ask for player's name and store it 
        player = JOptionPane.showInputDialog("Please enter your name");
        court.setPlayer(player);
        
        // Start game
        court.resetForButton();
    }
    
    
//    public String getPlayer() {
//        return player;
//    }
    
    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}