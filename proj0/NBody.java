public class NBody{
    public static double readRadius(String file){
        In in = new In(file);
        in.readDouble();
        double firstdouble = in.readDouble();
        return firstdouble;
    }

    /**return an array of planet */
    public static Planet[] readPlanets(String file){
        In in = new In(file);
        int len = in.readInt();
        Planet[] str = new Planet[len];
        in.readDouble();
        for (int i = 0; i < len; i++){
            str[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return str;
    }

    /**the functionality to draw the universe in its starting position */
    public static void main(String[] args){
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        double t = 0;
        String filename = args[2];
        Planet[] planetlist = NBody.readPlanets(filename);
        double[] xForces = new double [planetlist.length];
        double[] yForces = new double [planetlist.length];
        double radius = NBody.readRadius(filename);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        while (t <= T){
        StdDraw.clear();
        StdDraw.picture(0, 0, "/images/starfield.jpg");
            for (int i = 0; i < planetlist.length; i++ ){
                planetlist[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            t += dt;
            for (int j = 0; j < planetlist.length; j++){
                xForces[j] = planetlist[j].calcNetForceExertedByX(planetlist);
                yForces[j] = planetlist[j].calcNetForceExertedByY(planetlist);
            }
            for (int k = 0; k < planetlist.length; k++){
                planetlist[k].update(dt, xForces[k], yForces[k]);
            }
        }
        StdDraw.clear();
        StdDraw.picture(0, 0, "/images/starfield.jpg");
        for (int i = 0; i < planetlist.length; i++ ){
            planetlist[i].draw();
        }
        StdDraw.show();
        StdDraw.pause(10);

    }
}