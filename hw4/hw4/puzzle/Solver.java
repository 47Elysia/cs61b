package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {
    private class Searchnode implements Comparable<Searchnode> {
        private WorldState state;
        private int moves;
        private Searchnode pre;
        private Searchnode(WorldState state, int moves, Searchnode node) {
            this.state = state;
            this.moves = moves;
            this.pre = node;
        }
        private WorldState getstate() {
            return this.state;
        }
        private int getMoves() {
            return this.moves;
        }
        private Searchnode getPre() {
            return this.pre;
        }
        @Override
        public int compareTo(Searchnode o) {
            int movesdist = this.moves - o.moves;
            int distogoal = this.state.estimatedDistanceToGoal() - o.state.estimatedDistanceToGoal();
            return movesdist + distogoal;
        }
    }
    private MinPQ<Searchnode> searchnode = new MinPQ<>();
    private int tomoves;
    private ArrayList solution;

    private void returnanswer(Searchnode o) {
        tomoves = o.moves;
        solution = new ArrayList();
        Searchnode goal = o;
        while (goal != null) {
            solution.add(goal.state);
            goal = goal.pre;
        }
    }
    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     */
    public Solver(WorldState initial) {
        searchnode.insert(new Searchnode(initial, 0, null));
        while (true) {
            Searchnode min = searchnode.delMin();
            if (min.state.isGoal()) {
                returnanswer(min);
                return;
            } else {
                for (WorldState neighbor : min.state.neighbors()) {
                    if (min.pre == null || !min.pre.state.equals(neighbor)) {
                        searchnode.insert(new Searchnode(neighbor, min.moves + 1, min));
                    }
                }
            }
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     */
    public int moves() {
        return tomoves;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        ArrayList list = new ArrayList();
        for (int i = 0; i <= tomoves; i += 1) {
            list.add(solution.get(tomoves - i));
        }
        return list;
    }

}
