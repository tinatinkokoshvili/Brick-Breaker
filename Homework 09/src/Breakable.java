import java.awt.Graphics;

public interface Breakable {

    public int getWidth();
    public int getHeight();
    
    public void breakBrick(int row, int col, GameCourt gc);
    public void draw(Graphics g);
}
