package lab11.graphs;

import java.util.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Stack stack = new Stack();
    private Maze maze;
    private int[] nodeTo;
    private boolean found = false;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        nodeTo = new int[maze.V()];
    }

    @Override
    public void solve() {
        dfs(-1, 0);
    }

    // Helper methods go here
    private void dfs(int u, int v) {
        marked[v] = true;
        announce();
        for (int m : maze.adj(v)) {
            if (!marked[m]) {
                nodeTo[m] = v;
                dfs(v, m);
            } else if (u != m) {
                edgeTo[m] = v;
                found = true;
                for (int x = v; x != m; x = nodeTo[x]) {
                    edgeTo[x] = nodeTo[x];
                    announce();
                }
            }
            if (found) {
                return;
            }
        }
    }
}

