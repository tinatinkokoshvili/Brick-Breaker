import java.awt.Color;

public class BoostScoreBrick extends Brick {

    public BoostScoreBrick(int bx, int by, int courtWidth, int courtHeight, Color bColor) {
        super(bx, by, courtWidth, courtHeight, bColor);
    }
    
    
    @Override
    public void breakBrick(int row, int col, GameCourt gc) {
        gc.modifyForBoostScoreBrick(row, col);        
    }

}
