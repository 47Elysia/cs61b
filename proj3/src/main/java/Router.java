import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long stNode = g.closest(stlon, stlat);
        long deNode = g.closest(destlon, destlat);
        Map<Long, Long> edgeTo = new HashMap<>();
        Set<Long> visited = new HashSet<>();
        PriorityQueue<Long> fringe = new PriorityQueue<>(g.getNodeComparator());
        for (long node : g.vertices()) {
            g.changedisTo(node, Double.POSITIVE_INFINITY);
        }
        g.changedisTo(stNode, 0);
        fringe.add(stNode);
        while (!fringe.isEmpty()) {
            long ver = fringe.poll();
            if (visited.contains(ver)) {
                continue;
            }
            if (ver == deNode) {
                break;
            }
            visited.add(ver);
            for (long neibor : g.adjacent(ver)) {
                if (g.getdistTo(ver) + g.distance(neibor, ver) < g.getdistTo(neibor)) {
                    g.changedisTo(neibor, g.getdistTo(ver) + g.distance(neibor, ver));
                    g.changePriority(neibor, g.getdistTo(neibor)
                            + g.distance(deNode, neibor));
                    edgeTo.put(neibor, ver);
                    fringe.add(neibor);
                }
            }
        }
        List<Long> res = new LinkedList<>();
        res.add(deNode);
        while (deNode != stNode) {
            // cannot reach destNode
            if (edgeTo.get(deNode) == null) {
                return new LinkedList<>();
            }
            res.add(0, edgeTo.get(deNode));
            deNode = edgeTo.get(deNode);
        }

        for (long ver : g.vertices()) {
            g.changePriority(ver, 0);
        }
        return res; // FIXME
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> res = new ArrayList<>();
        NavigationDirection current = new NavigationDirection();
        current.direction = NavigationDirection.START;
        current.way = getwayname(g, route.get(0), route.get(1));
        current.distance = g.distance(route.get(0), route.get(1));
        for (int i = 1, j = 2; j < route.size(); i += 1, j += 2) {
            if (!getwayname(g, route.get(i), route.get(j)).equals(current.way)) {
                res.add(current);
                current = new NavigationDirection();
                current.way = getwayname(g, route.get(i), route.get(j));
                double prebearing = g.bearing(route.get(i - 1), route.get(i));
                double curbearing = g.bearing(route.get(i), route.get(j));
                current.direction = convertBearingToDirection(prebearing, curbearing);
                current.distance += g.distance(route.get(i), route.get(j));
                continue;
            }
            current.distance += g.distance(route.get(i), route.get(j));
        }
        res.add(current);
        return res;
    }

    private static String getwayname(GraphDB g, long v, long w) {
        String name = "";
        HashMap<Long, GraphDB.node> way1 = g.getwayMap(v);
        HashMap<Long, GraphDB.node> way2 = g.getwayMap(w);
        LinkedList<Long> intersection = new LinkedList<>();
        for (Long node : way1.keySet()) {
            if (way2.containsKey(node)) {
                intersection.add(node);
            }
        }
        if (!intersection.isEmpty()) {
            if (!g.wayexist(intersection.get(0))) {
                return name;
            } else {
                return g.getwayname(intersection.get(0));
            }
        }
        return name;
    }
    private static int convertBearingToDirection(double prevBearing, double curBearing) {
        double relativeBearing = curBearing - prevBearing;
        if (relativeBearing > 180) {
            relativeBearing -= 360;
        } else if (relativeBearing < -180) {
            relativeBearing += 360;
        }

        if (relativeBearing < -100) {
            return NavigationDirection.SHARP_LEFT;
        } else if (relativeBearing < -30) {
            return NavigationDirection.LEFT;
        } else if (relativeBearing < -15) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (relativeBearing < 15) {
            return NavigationDirection.STRAIGHT;
        } else if (relativeBearing < 30) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (relativeBearing < 100) {
            return NavigationDirection.RIGHT;
        } else {
            return NavigationDirection.SHARP_RIGHT;
        }
    }
    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */

    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
