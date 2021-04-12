import java.awt.*;



public class Brick extends GameObj implements Breakable {

    private static final int WIDTH = 20;
    private static final int HEIGHT = 7;
    private Color color;
    
    /**
     * Constructor
     */
    public Brick(int bx, int by, int courtWidth, int courtHeight, Color bColor) {
        super(0, 0, bx, by, WIDTH, HEIGHT, courtWidth, courtHeight);

        color = bColor;
    }

    
    
    
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void breakBrick(int row, int col, GameCourt gc) {
        gc.modifyForBrick(row, col);        
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }
    
}
