package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.TileEngine.TERenderer;

import java.awt.*;
import java.util.Random;
public class Position {
    private int positionx;
    private int positiony;
    public Position(int x, int y) {
        positionx = x;
        positiony = y;
    }

    public int getPositionx() {
        return positionx;
    }

    public int getPositiony() {
        return positiony;
    }
}
