package motion.model;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class DifferentialDrive extends Motion {

	public float orientation =0;
	public float theta = 0;
	public float length =1F;
	public float v = 0;

    public DifferentialDrive(String robot) {
        super(robot);
    }

    @Override
    public void move(Point2D.Float[] path) {

    }

    
    
    public Point2D.Float calculate(Point2D.Float s, Point2D.Float e, float orientation){
this.orientation = orientation;
    	
    	Point2D.Float front = getFront(s);
    	
    	theta = (float) angle(getFront(front), front, e);
    	double Max = 0.3;
    	if(theta>Max)
    		theta =(float) Max;
    	if(theta<-Max)
    		theta =(float) -Max;

    
    	
    	float Vx = (e.x - s.x) /dt ;
        float Vy = (e.y - s.y) /dt;
                
        float V = (float) Math.sqrt(((Vx * dt)*( Vx * dt))+ ((Vy*dt) * (Vy * dt)));

        if (V > MAX_VELOCITY) {
            V = MAX_VELOCITY;
        }else{

            V = -MAX_VELOCITY;
        }
        
        Vx = (float) Math.cos(theta) * V;
        Vy = (float) Math.sin(theta) * V;
        
        
        this.v = V;
        
		return new Point2D.Float(s.x+Vx, s.y+Vy);      	
    }

}
