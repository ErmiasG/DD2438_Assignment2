
package motion.model;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public abstract class Motion {
    public float MAX_VELOCITY = 100f;
    public float MAX_DIS = 0.05f;
    public float MAX_ACCELERATION = 1f;
    public  float dt = 1f;
    public float length =3;
    public float orientation = 0;
    
    
    public Motion() {
    }
    
    public double angle(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3){
    	Line2D line1 = new Line2D.Double(p1,p2);
    	Line2D line2 = new Line2D.Double(p2,p3);
    	
    	 double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
                 line1.getX1() - line1.getX2());
    	 
    	 double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
                 line2.getX1() - line2.getX2());
    	 
    	 return -((angle1-angle2)>Math.PI? -((2*Math.PI)-(angle1-angle2)) : (angle1-angle2));
    }
    
    
    public Point2D.Float getFront(Point2D.Float current){
    	float x = current.x +  (length *(float) Math.sin(orientation));
    	float y = current.y +  (length *(float) Math.cos(orientation));
    	return new Point2D.Float(x, y);
    	
    }
    
    
}
