public class Planet{
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public Planet(double xP, double yP, double xV, double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }    

    public Planet(Planet p){
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    /*calculate distance between two planet */
    public double calcDistance(Planet p){
        double distance;
        double distanceX;
        double distanceY;
        distanceX = this.xxPos - p.xxPos;
        distanceY = this.yyPos - p.yyPos;
        distance = Math.sqrt(distanceX * distanceX + distanceY *distanceY);
        return distance;
    }

    /* calculate force on a planet */
    public double calcForceExertedBy(Planet p){
        double G = 6.67e-11;
        double ForceExertedBy = G * this.mass * p.mass / (this.calcDistance(p) * this.calcDistance(p));
        return ForceExertedBy;
    }

    /*calculate force on x */
    public double calcForceExertedByX(Planet p){
        double distanceX = p.xxPos - this.xxPos;    
        double distance = this.calcDistance(p);
        double Force = this.calcForceExertedBy(p);
        if (distanceX < 0){
            return Force * distanceX / distance;
        }
        return Force * distanceX / distance;
    }
    /*calculate force on y */
    public double calcForceExertedByY(Planet p){
        double distanceY = p.yyPos - this.yyPos;    
        double distance = this.calcDistance(p);
        double Force = this.calcForceExertedBy(p);
        if (distanceY < 0){
            return Force * distanceY / distance;
        }
        return Force * distanceY / distance;
    }

    /*calculate force by all planet on x */
    public double calcNetForceExertedByX(Planet[] args){
        double NetForceByX = 0;
        for (int i = 0; i < args.length; i++){
            if (!this.equals(args[i])){
                NetForceByX += this.calcForceExertedByX(args[i]);
            }
        }
        return NetForceByX;
    }

    /*calculate force by all planet on y */
    public double calcNetForceExertedByY(Planet[] args){
        double NetForceByY = 0;
        for (int i = 0; i < args.length; i++){
            if (!this.equals(args[i])){
                NetForceByY += this.calcForceExertedByY(args[i]);
            }
        }
        return NetForceByY;
    }

    /*update the position and velocity */
    public void update(double dt, double fX, double fY){
        double ax = fX / mass;
        double ay = fY / mass;
        xxVel += dt * ax ;
        yyVel += dt * ay ;
        xxPos += xxVel * dt ;
        yyPos += yyVel * dt ;
    }

    public void draw(){
        StdDraw.picture(this.xxPos, this.yyPos, "/images/" + this.imgFileName);
    }
}
