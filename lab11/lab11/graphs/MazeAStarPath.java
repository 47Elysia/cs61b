package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int manhattandis = Math.abs(t / maze.N() - v / maze.N()) + Math.abs(t % maze.N() - v %maze.N());
        return manhattandis;
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        announce();
        if (s == t) {
            targetFound = true;
        }
        if (targetFound) {
            return;
        }
        int neibor = 0;
        int mindis = maze.N() * maze.N();
        for (int m : maze.adj(s)) {
            if (distTo[m] + h(m) < mindis && !marked[m]) {
                mindis = distTo[m] + h(m);
                neibor = m;
            }
        }
        astar(neibor);
    }

    @Override
    public void solve() {
        astar(s);
    }

}

