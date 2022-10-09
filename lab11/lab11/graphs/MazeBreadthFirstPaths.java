package lab11.graphs;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int source;
    private int target;
    private boolean targetfound;
    private Queue queue = new ArrayDeque();

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        source = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
        targetfound = false;
        distTo[source] = 0;
        edgeTo[source] = source;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs(int v) {
        marked[v] = true;
        announce();

        if (v == target) {
            targetfound = true;
        }
        if (targetfound) {
            return;
        }
        for (int m : maze.adj(v)) {
            if (!marked[m]) {
                queue.add(m);
            }
        }
        bfs((int) queue.remove());
    }


    @Override
    public void solve() {
        bfs(source);
    }
}

