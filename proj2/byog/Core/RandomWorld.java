package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import byog.TileEngine.TERenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class RandomWorld {


    private static final long SEED = 2000;
    private static Random random = new Random(SEED);

    private static boolean isvalid(int x, int y, int length, int height) {
        if (x < 0 || y < 0 || x >= length || y >= height) {
            return false;
        }
        return true;
    }



    public TETile[][] createANewWorld(String input, int WIDTH, int HEIGHT){
        int seed = 0;
        for (int i = 0; i < input.length(); i += 1) {
            if (input.charAt(i) <= '9' && input.charAt(i) >= '0') {
                seed = seed * 10 + input.charAt(i);
            }
        }
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        random = new Random(seed);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < WIDTH; i += 1){
            for (int j = 0; j < HEIGHT; j += 1){
                world[i][j] = Tileset.NOTHING;
            }
        }
        ArrayList<Room> roomlist = new ArrayList();
        for (int i = 0; i <= 1000; i += 1) {
            Position bottomleft = new Position(RandomUtils.uniform(random, WIDTH), RandomUtils.uniform(random, HEIGHT));
            int roomlength = RandomUtils.uniform(random, 1, 8) * 2;
            int roomheight = RandomUtils.uniform(random, 1, 8) * 2;
            Position upright = new Position(bottomleft.getPositionx() + roomlength, bottomleft.getPositiony() + roomheight);
            Room room = new Room(bottomleft, upright);
            if (!Room.istoonear(room, roomlist) && !room.isfringe()){
                roomlist.add(room);
                room.drawroom(world);
            }
            if (roomlist.size() == 20) {
                break;
            }
        }
        for (Room room2 : roomlist) {
            for (Room room3 : roomlist){
                if (room2 != room3 ) {
                    room2.drawCorridor(room3, random, world);
                }
            }
        }

        for (int i = 0; i < WIDTH; i += 1) {
            for (int j = 0; j < HEIGHT; j += 1) {
                if (world[i][j] == Tileset.FLOOR) {
                    if (isvalid(i - 1, j, WIDTH, HEIGHT) && world[i - 1][j] == Tileset.NOTHING ) {
                        world[i - 1][j] = Tileset.WALL;
                    }
                    if (isvalid(i + 1, j, WIDTH, HEIGHT) && world[i + 1][j] == Tileset.NOTHING) {
                        world[i + 1][j] = Tileset.WALL;
                    }
                    if (isvalid(i, j + 1, WIDTH, HEIGHT) && world[i][j + 1] == Tileset.NOTHING) {
                        world[i][j + 1] = Tileset.WALL;
                    }
                    if (isvalid(i, j - 1, WIDTH, HEIGHT) && world[i][j - 1] == Tileset.NOTHING) {
                        world[i][j - 1] = Tileset.WALL;
                    }
                }
            }
        }
        Position doorposition;
        int doorx;
        int doory;
        while (true) {
            doorx = RandomUtils.uniform(random, WIDTH);
            doory = RandomUtils.uniform(random, HEIGHT);
            if (world[doorx][doory] == Tileset.FLOOR) {
                doorposition = new Position(doorx, doory);
                world[doorx][doory] = Tileset.LOCKED_DOOR;
                break;
            }
        }
        ter.renderFrame(world);
        return world;
    }
}
