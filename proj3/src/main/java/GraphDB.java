import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private HashMap<Long, node> nodes = new HashMap<>();
    private HashMap<Long, way> allway = new HashMap<>();
    public class way {
        HashMap<Long, node> nodeinway;
        int max_speed;
        boolean isvalid;
        long wayid;
        String wayname;
        public way(long wayid) {
            nodeinway = new HashMap<>();
            max_speed = 0;
            isvalid = false;
            this.wayid = wayid;
        }
    }
    public String getwayname(long wayid) {
        return getcurway(wayid).wayname;
    }
    public HashMap<Long, node> getwayMap(long wayid) {
        return getcurway(wayid).nodeinway;
    }
    public boolean wayexist(long wayid) {
        return allway.containsKey(wayid);
    }
    public int waysize(way curway) {
        return curway.nodeinway.size();
    }
    public void valid(way curway) {
        curway.isvalid = true;
    }
    public void max_speed(way curway, int maxspeed) {
        curway.max_speed = maxspeed;
    }
    public way getcurway(Long wayid) {
        return allway.get(wayid);
    }
    public void addway(long wayid) {
        allway.put(wayid, new way(wayid));
    }
    public void addtoway(way curway, long v) {
        double lon = nodes.get(v).lon;
        double lat = nodes.get(v).lat;
        curway.nodeinway.put(v, new node(lon, lat));
    }

    public class node {
        double lon;
        double lat;
        String name;
        ArrayList<Long> adj;
        double distTo;
        double priority;
        public node(double lon, double lat) {
            this.lon = lon;
            this.lat = lat;
            this.adj = new ArrayList<>();
        }
    }
    class NodeComparator implements Comparator<Long> {
        @Override
        public int compare(Long v, Long w) {
            return Double.compare(nodes.get(v).priority, nodes.get(w).priority);
        }
    }

    public Comparator<Long> getNodeComparator() {
        return new NodeComparator();
    }
    public void changePriority(long ver, double priority) {
        nodes.get(ver).priority = priority;
    }
    public void changedisTo(long ver, double dist) {
        nodes.get(ver).distTo = dist;
    }
    public double getdistTo(long ver) {
        return nodes.get(ver).distTo;
    }
    public void addadj(long long1, long long2) {
        nodes.get(long1).adj.add(long2);
    }
    public void getName(long ver, String name) {
        nodes.get(ver).name = name;
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        HashMap<Long, node> nodes1 = new HashMap<>();
        for (long ver : nodes.keySet()) {
            node node1 = nodes.get(ver);
            if (!node1.adj.isEmpty()) {
                nodes1.put(ver, node1);
            }
        }
        nodes = nodes1;
        /*Iterator<Long> it = nodes.keySet().iterator();
        while (it.hasNext()) {
            Long node = it.next();
            if (nodes.get(node).adj.isEmpty()) {
                it.remove();
            }
        }*/
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return nodes.keySet();
    }
    void addVertices(long id, double lon, double lat) {
        node node1 = new node(lon, lat);
        this.nodes.put(id, node1);
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return nodes.get(v).adj;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double min = 0;
        long ver = 0;
        for (long vertice : this.nodes.keySet()) {
            double dis = distance(lon, lat,
                    lon(vertice), lat(vertice));
            if (min == 0) {
                min = dis;
                ver = vertice;
            } else {
                if (min > dis) {
                    min = dis;
                    ver = vertice;
                }
            }
        }
        return ver;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return this.nodes.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return this.nodes.get(v).lat;
    }
}
