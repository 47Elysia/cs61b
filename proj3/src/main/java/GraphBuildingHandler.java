import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 *  You may find the CSCourseGraphDB and CSCourseGraphDBHandler examples useful.
 *
 *  The idea here is that some external library is going to walk through the XML
 *  file, and your override method tells Java what to do every time it gets to the next
 *  element in the file. This is a very common but strange-when-you-first-see it pattern.
 *  It is similar to the Visitor pattern we discussed for graphs.
 *
 *  @author Alan Yao, Maurice Lee
 */
public class GraphBuildingHandler extends DefaultHandler {
    /**
     * Only allow for non-service roads; this prevents going on pedestrian streets as much as
     * possible. Note that in Berkeley, many of the campus roads are tagged as motor vehicle
     * roads, but in practice we walk all over them with such impunity that we forget cars can
     * actually drive on them.
     */
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;
    private GraphDB.way curway;
    private ArrayList<Long> curwaylist;
    private long lastnode;
    private long curwayid;
    private String wayname;

    /**
     * Create a new GraphBuildingHandler.
     * @param g The graph to populate with the XML data.
     */
    public GraphBuildingHandler(GraphDB g) {
        this.g = g;
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */
        if (qName.equals("node")) {
            /* We encountered a new <node...> tag. */
            activeState = "node";
            System.out.println("Node id: " + attributes.getValue("id"));
            System.out.println("Node lon: " + attributes.getValue("lon"));
            System.out.println("Node lat: " + attributes.getValue("lat"));

            /* TODO Use the above information to save a "node" to somewhere. */
            lastnode = Long.parseLong(attributes.getValue("id"));
            g.addVertices(Long.parseLong(attributes.getValue("id")),
                    Double.parseDouble(attributes.getValue("lon")),
                    Double.parseDouble(attributes.getValue("lat")));
            /* Hint: A graph-like structure would be nice. */

        } else if (qName.equals("way")) {
            /* We encountered a new <way...> tag. */
            activeState = "way";
            curwayid = Long.parseLong(attributes.getValue("id"));
            curwaylist = new ArrayList<>();
            System.out.println("Beginning a way...");
        } else if (activeState.equals("way") && qName.equals("nd")) {
            /* While looking at a way, we found a <nd...> tag. */
            System.out.println("Id of a node in this way: " + attributes.getValue("ref"));
            curwaylist.add(Long.parseLong(attributes.getValue("ref")));
            /* TODO Use the above id to make "possible" connections between the nodes in this way */
            /* Hint1: It would be useful to remember what was the last node in this way. */
            /* Hint2: Not all ways are valid. So, directly connecting the nodes here would be
            cumbersome since you might have to remove the connections if you later see a tag that
            makes this way invalid. Instead, think of keeping a list of possible connections and
            remember whether this way is valid or not. */

        } else if (activeState.equals("way") && qName.equals("tag")) {
            /* While looking at a way, we found a <tag...> tag. */
            String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("maxspeed")) {
                System.out.println("Max Speed: " + v);
                int speed = 0;
                for (int i = 0; i < v.length(); i += 1) {
                    if (i - '0' >= 0 && i - '9' <= 0) {
                        speed = speed * 10 + (i - '0');
                    }
                }
                g.max_speed(curway, speed);
                /* TODO set the max speed of the "current way" here. */
            } else if (k.equals("highway")) {
                System.out.println("Highway type: " + v);
                if (ALLOWED_HIGHWAY_TYPES.contains(v)) {
                    g.addway(curwayid);
                    curway = g.getcurway(curwayid);
                    for (long ver : curwaylist) {
                        g.addtoway(curway, ver);
                    }
                    g.valid(curway);
                    curway.wayname = wayname;
                    int size = g.waysize(curway);
                    for (int i = 0, j = 1; j < size; i += 1, j += 1) {
                        long long1 = curwaylist.get(i);
                        long long2 = curwaylist.get(j);
                        g.addadj(long1, long2);
                        g.addadj(long2, long1);
                    }
                }
                /* TODO Figure out whether this way and its connections are valid. */
                /* Hint: Setting a "flag" is good enough! */
            } else if (k.equals("name")) {
                wayname = attributes.getValue("v");
                System.out.println("Way Name: " + v);
            }
//            System.out.println("Tag with k=" + k + ", v=" + v + ".");
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {
            /* While looking at a node, we found a <tag...> with k="name". */
            g.getName(lastnode, attributes.getValue("v"));
            /* TODO Create a location. */
            /* Hint: Since we found this <tag...> INSIDE a node, we should probably remember which
            node this tag belongs to. Remember XML is parsed top-to-bottom, so probably it's the
            last node that you looked at (check the first if-case). */
            System.out.println("Node's name: " + attributes.getValue("v"));
        }
    }

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way")) {
            /* We are done looking at a way. (We finished looking at the nodes, speeds, etc...)*/
            /* Hint1: If you have stored the possible connections for this way, here's your
            chance to actually connect the nodes together if the way is valid. */
//            System.out.println("Finishing a way...");
        }
    }

}
