package byog.Core;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;
public class Room {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private Position roomposition;
    private Position bottomleft;
    private Position upright;
    private int roomlength;
    private int roomheight;
    public Room(Position bottomleft, Position upright) {
        this.upright = upright;
        this.bottomleft = bottomleft;
    }

    public int getbottomleftx() {
        return bottomleft.getPositionx();
    }
    public int getbottomlefty() {
        return bottomleft.getPositiony();
    }
    public int getuprightx() {
        return upright.getPositionx();
    }
    public int getuprighty() {
        return upright.getPositiony();
    }
    public int centrex() {
        return (getuprightx() + getbottomleftx()) / 2;
    }
    public int centrey() {
        return (getuprighty() + getbottomlefty()) / 2;
    }
    public boolean xIsValid(int x) {
        return x <= WIDTH - 1  && x >= 0;
    }
    public boolean yIsValid(int y) {
        return y <= HEIGHT - 1 && y >= 0;
    }

    public static boolean istoonear(Room room, ArrayList<Room> otherroom) {
        for (Room room1 : otherroom) {
            int centreXdiff = Math.abs(room.centrex() - room1.centrex());
            int centreYdiff = Math.abs(room.centrey() - room1.centrey());
            int maxroomlength = Math.max(room.roomlength, room1.roomlength);
            int maxroomheight = Math.max(room.roomheight, room1.roomheight);
            if (room != room1 && (centreXdiff < maxroomlength / 2 - 2)) {
                return true;
            }
            if (room != room1 && (centreYdiff < maxroomheight / 2 - 2)) {
                return true;
            }
        }
        return false;
    }
    public boolean isfringe() {
        if (this.getbottomleftx() == WIDTH - 1 || this.getuprightx() == 0) {
            return true;
        }
        if (this.getbottomlefty() == HEIGHT - 1 || this.getuprighty() == 0) {
            return true;
        }
        return false;
    }

    /*public boolean isconnected(Room room) {
        int centreXdiff = Math.abs(this.centrex() - room.centrex());
        int centreYdiff = Math.abs(this.centrey() - room.centrey());
        int length = (this.roomlength + room.roomlength) / 2;
        int height = (this.roomheight + room.roomheight) / 2;
        if (centreXdiff < length || centreYdiff < height) {
            return true;
        }
        return false;
    }*/



    public void drawroom(TETile[][] world) {
        int uprightx = getuprightx();
        int uprighty = getuprighty();
        int bottomleftx = getbottomleftx();
        int bottomlefty = getbottomlefty();
        for (int i = bottomleftx + 1; i < uprightx; i += 1) {
            for (int j = bottomlefty + 1; j < uprighty; j += 1) {
                if (xIsValid(i) && yIsValid(j)) {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
    }
    public void drawCorridor(Room room, Random random, TETile[][] world) {
        int thisx = this.centrex();
        int thisy = this.centrey();
        int otherx = room.centrex();
        int othery = room.centrey();
        int randomnum;
        if (thisx < otherx && thisy < othery) {
            int x = thisx;
            int y = thisy;
            for (int i = 0; i < otherx - thisx + othery - thisy; i += 1) {
                if (x == otherx) {
                    y += 1;
                } else if (y == othery) {
                    x += 1;
                } else {
                    randomnum = RandomUtils.uniform(random, 1);
                    switch (randomnum) {
                        case 0: x += 1;
                        break;
                        default: y += 1;
                    }
                }
                if (xIsValid(x) && yIsValid(y)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
        if (thisx < otherx && thisy > othery) {
            int x = thisx;
            int y = thisy;
            for (int i = 0; i < otherx - thisx + thisy - othery; i += 1) {
                if (x == otherx) {
                    y -= 1;
                } else if (y == othery) {
                    x += 1;
                } else {
                    randomnum = RandomUtils.uniform(random, 1);
                    switch (randomnum) {
                        case 0: x += 1;
                            break;
                        default: y -= 1;
                    }
                }
                if (xIsValid(x) && yIsValid(y)) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }
}
