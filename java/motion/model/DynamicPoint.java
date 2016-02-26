package motion.model;

import java.awt.geom.Point2D;

public class DynamicPoint extends Motion {

    public Point2D.Float velocity = new Point2D.Float(0f, 0f);

    public Point2D.Float v = new Point2D.Float(0f, 0f);
    
    public DynamicPoint() {

    }

    

    
    public Point2D.Float calculate(Point2D.Float s, Point2D.Float e, Point2D.Float vel){
    	v=vel;
    	
        float Vx = (e.x - s.x) / dt;
        float Vy = (e.y - s.y) / dt;
        
        float ax = (Vx - v.x) /dt;
        float ay = (Vy - v.y) /dt;

        float at = (float) Math.sqrt(ax * ax + ay * ay);
        if (at > MAX_ACCELERATION) {
            ax = (ax / at) * MAX_ACCELERATION;
            ay = (ay / at) * MAX_ACCELERATION;
        }
        
        Vx = v.x + ax * dt ;
        Vy = v.y + ay * dt  ;
        float V = (float) Math.sqrt(Vx * Vx + Vy * Vy);
        if (V > MAX_VELOCITY) {

            Vx = (Vx / V) * MAX_VELOCITY;
            Vy = (Vy / V) * MAX_VELOCITY;
        }
        Point2D.Float target = new Point2D.Float(s.x+ (Vx * dt), s.y+(Vy * dt));

        //System.out.println("going from start " +s.x+","+s.y+" to "+target.x+","+target.y);
        v.x = Vx * dt;
        v.y = Vy * dt;
	    return target;    
        
    }


}
