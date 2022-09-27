package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
/*import byog.TileEngine.TERenderer;*/

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

    private static void fillAround(TETile[][] world, int x, int y) {
        int length = world.length;
        int height = world[0].length;
        for (int i = x - 1; i <= x + 1; i += 1) {
            for (int j = y - 1; j <= y + 1; j += 1) {
                if (isvalid(i, j, length, height) && world[i][j] == Tileset.NOTHING) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }



    public TETile[][] createANewWorld(long seed, int width, int height) {
        random = new Random(seed);
        TETile[][] world = new TETile[width][height];
        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                world[i][j] = Tileset.NOTHING;
            }
        }
        ArrayList<Room> roomlist = new ArrayList();
        for (int i = 0; i <= 1000; i += 1) {
            int bottomleftx = RandomUtils.uniform(random, width);
            int bottomlefty = RandomUtils.uniform(random, height);
            Position bottomleft = new Position(bottomleftx, bottomlefty);
            int roomlength = RandomUtils.uniform(random, 1, 8) * 2;
            int roomheight = RandomUtils.uniform(random, 1, 8) * 2;
            int uprightx = bottomleft.getPositionx() + roomlength;
            int uprighty = bottomleft.getPositiony() + roomheight;
            Position upright = new Position(uprightx, uprighty);
            Room room = new Room(bottomleft, upright);
            if (!Room.istoonear(room, roomlist) && !room.isfringe()) {
                roomlist.add(room);
                room.drawroom(world);
            }
            if (roomlist.size() == 20) {
                break;
            }
        }
        for (Room room2 : roomlist) {
            for (Room room3 : roomlist) {
                if (room2 != room3) {
                    room2.drawCorridor(room3, random, world);
                }
            }
        }

        for (int i = 0; i < width; i += 1) {
            for (int j = 0; j < height; j += 1) {
                if (world[i][j] == Tileset.FLOOR) {
                    fillAround(world, i, j);
                }
            }
        }
        Position doorposition;
        int doorx;
        int doory;
        while (true) {
            doorx = RandomUtils.uniform(random, width);
            doory = RandomUtils.uniform(random, height);
            if (world[doorx][doory] == Tileset.FLOOR) {
                doorposition = new Position(doorx, doory);
                world[doorx][doory] = Tileset.LOCKED_DOOR;
                break;
            }
        }
        return world;
    }
}
