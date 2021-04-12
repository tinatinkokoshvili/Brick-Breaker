=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. I am using a 2-D array of bricks for storing the current game state. 
    Each grid stores a specific brick that appears at the relevant coordinates. 
    I am randomly generating this grid using Random class 
    (given a specific size restrictions). I am using the grid for instantiating the 
    bricks by translating the indices of the brick in the grid to its actual 
    coordinates on the board. When a brick is broken, it is set to null
    in the grid. 2-D array is appropriate for my game state because generally the 
    bricks create a grid on the screen, and also rows and columns help for 
    determining the coordinates of the bricks. Also, it helps to set a color
    of the entire row of bricks to one specific color. It is an appropriate use
    of 2-D array since I am using array to store my game state and 
    I am not doing brute-force searching, since I am splitting the data first and
    deciding where I need to look for the brick I need.

  2. My Brick Breaker implementation uses I/O to store previous scores 
    (write it in the file), so that the user can view the highest scores using a particular 
    Top Score button: my game reads the text file and parses the data so it can be displayed.
    I am storing 2 game states: UserName and score, separating the by semicolon. 
    The file format is txt, and it stores strings and integer line by line, without 
    skipping any lines. This feature is appropriate use of the concept because I am 
    writing names and scores to the file and then reading them. Also, I am storing
    2 game state: names and scores. Also, I am not using csv, and I do not need separate
    FileLineIterator since I can directly use BufferedReader because I am sure that I do 
    not need to handle empty lines and inconsistencies within my file.

  3. I am using Inheritance for Dynamic Dispatch. I have implemented an interface Breakable. 
   There is a super class Brick that implements the breakable interface with its 
   method brickBreak(). This method just makes the brick disappear. Brick will have subclasses of special 
   bricks that do different things after they are hit, for example one brick creates an additional ball,
   one decreases the paddle in length, one boosts the score and takes the additional ball out of the game,
   if it has been created before, one increases the number of lives by one. (All the bricks increase score
   by certain amount.)
   These subclasses extend Brick and override break(). In the game 2-D array is storing static type
   of brick but I am calling methods on dynamic type depending on which type of the brick was hit.
   Specifically, I am calling brickBreak() method on the brick that was hit by the ball. brickBreak() 
   of the dynamic type gets executed.

  4. The main state of my game is the grid (2D array). I also have other fields within the 
  GameCourt, such as score, number of lives, etc. I have public methods within my GameCourt that 
  are used in testing. I have tests for checking whether each type of brick updates the game
  is it is expected to, for collisions, ending game, checking for score, number of bricks, etc. I am also 
  testing how the reader and writer are working. Also, I am testing whether user won or lost
  in certain cases - if no bricks are left, user won, otherwise lost. It is appropriate use
  of concept because I am mainly testing game logic and not view - what is displayed.
  


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Game - I have class Game, which mainly takes care of what is displayed on the panel and where.
  It creates the appropriate buttons and labels and instantiates the GameCourt.
  
  GameObj - this class is a superclass of all the objects in my game. It stores the position, size,
  velocity of the object, modifies them when necessary, and checks for collisions.
  
  GameCourt - this class contains the main logic of the game. It has a timer, which
  calls the tick method after specific interval over and over again - tick method check for
  collisions, checks for any events that mught happen, and properly updates the game state.
  It also takes care of the  keyboard events.
  This class also contains 2 reset methods - one for when the reset button is used and another
  for when number of lives decrease but game still continues exavtly where it stopped(moving ball
  to its initial starting position). Also, contains methods for testing purposes.
  It also selects the bricks randomly and throws the ball with an initial random starting angle,
  using Random class.
  
  Brick - extends GameObj. There are 4 classes that extend it: BoostLivesBrick, BoostScoreBrick,
  addBallBrick, EnlargePaddleBrick. Brick class implements Breakable interface. it has method
  brickBreak(). Each subclass overrides it differently to account for the different power-ups
  and modify the game state accordingly.
  
  Writer - it has one method that takes in the score, name and path to file, and
  writes into the file. Until writing, it checks whether the same score already exists
  in the file. If it does, it is not adding the score.
  
  
 Reader - it reads the file. Reads all the names, scores line by line, then splits 
 names and scores, and stores them to a TreeMap. I also use ArrayLists for 
 all the scores and player names. Then I have different methods for getting top 3
 scores out of all the scores, and then another method which creates an
 appropriate String with three usernames and scores in a descending order. This
 String is ready to be displayed. It is used in Game.java.
  
  Direction - it includes the for possible directions that GameObj can have
  
  Circle - extends GameObj, is used for a ball
  
  Square - extends GameObj, is used for a paddle
  
 

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  I spent a relatively larger amount of time for figuring out how to 
  implement my Bricks classes, so that I will be able to let them do 
  different modifications of the game state.



- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
	I believe I do have a good separation of functionality. All my classes
	serve the different purpose, also within classes I have helper functions that
	have different purposes. My private state is encapsulated since I am not
	passing anything directly out of my GameCourt, or other classes.
	I would possibly refactor the GameCourt since it seems more involved
	compared to other classes.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  I have used java Swing library.
  
