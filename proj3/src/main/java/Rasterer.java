import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    public static final int TILE_SIZE = 256;
    private double ROOT_LonDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
    private double[] depth = new double[8];

    public Rasterer() {
        for (int i = 0; i < 8; i += 1) {
            depth[i] = ROOT_LonDPP / Math.pow(2, i);
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double requiredULLON = params.get("ullon");
        double requiredULLAT = params.get("ullat");
        double requiredLRLON = params.get("lrlon");
        double requiredLRLAT = params.get("lrlat");
        if (requiredLRLAT < ROOT_LRLAT || requiredLRLON > ROOT_LRLON
                || requiredULLAT > ROOT_ULLAT || requiredULLON < ROOT_ULLON
                || requiredLRLON <= requiredULLON || requiredLRLAT >= requiredULLAT) {
            results.put("render_grid", null);
            results.put("depth", null);
            results.put("raster_ul_lon", requiredULLON);
            results.put("raster_ul_lat", requiredULLAT);
            results.put("raster_lr_lon", requiredLRLON);
            results.put("raster_lr_lat", requiredLRLAT);
            results.put("query_success", false);
            return results;
        }
        double requireLonDPP = (requiredLRLON - requiredULLON) / params.get("w");
        int dep = getdepth(requireLonDPP);
        int size = (int) Math.pow(2, dep);
        double widthperpicture = (ROOT_LRLON - ROOT_ULLON) / size;
        double heightperpicture = (ROOT_ULLAT - ROOT_LRLAT) / size;
        int rNUM = 0, lNUM = 0, tOPNUM = 0, bOTTOMNUM = 0;
        for (int i = 0; i < size; i += 1) {
            if (requiredULLON <= ROOT_ULLON + (i + 1) * widthperpicture
                    && requiredULLON >= ROOT_ULLON + i * widthperpicture) {
                lNUM = i;
                break;
            }
        }
        for (int i = 0; i < size; i += 1) {
            if (requiredLRLON <= ROOT_ULLON + (i + 1) * widthperpicture
                    && requiredLRLON >= ROOT_ULLON + i * widthperpicture) {
                rNUM = i;
                break;
            }
        }
        for (int i = 0; i < size; i += 1) {
            if (requiredULLAT <= ROOT_ULLAT - i * heightperpicture
                    && requiredULLAT >= ROOT_ULLAT - (i + 1) * heightperpicture) {
                tOPNUM = i;
                break;
            }
        }
        for (int i = 0; i < size; i += 1) {
            if (requiredLRLAT <= ROOT_ULLAT - i * heightperpicture
                    && requiredLRLAT >= ROOT_ULLAT - (i + 1) * heightperpicture) {
                bOTTOMNUM = i;
                break;
            }
        }
        int col = rNUM - lNUM + 1;
        int row = bOTTOMNUM - tOPNUM + 1;
        String[][] map = new String[row][col];
        for (int i = 0; i < row; i += 1) {
            for (int j = 0; j < col; j += 1) {
                map[i][j] = "d" + dep + "_x"
                        + (lNUM + j) + "_y" + (tOPNUM + i) + ".png";
            }
        }
        results.put("render_grid", map);
        results.put("raster_ul_lon", ROOT_ULLON + lNUM * widthperpicture);
        results.put("raster_ul_lat", ROOT_ULLAT - tOPNUM * heightperpicture);
        results.put("raster_lr_lon", ROOT_ULLON + (rNUM + 1) * widthperpicture);
        results.put("raster_lr_lat", ROOT_ULLAT - (bOTTOMNUM + 1) * heightperpicture);
        results.put("depth", dep);
        results.put("query_success", true);
        return results;
    }
    private int getdepth(double lonDPP) {
        int dep = 0;
        while (lonDPP <= depth[dep]) {
            dep += 1;
            if (dep == 7) {
                break;
            }
        }
        return dep;
    }

}
