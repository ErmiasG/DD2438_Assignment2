package motion.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;


public class DynamicCar extends Motion {

    public Point2D.Float v = new Point2D.Float(0f, 0f);
	
	public float orientation =0;
	public float theta = 0;
	public float length =5F;

    public DynamicCar() {
    }
  

    
    
    public Point2D.Float calculate(Point2D.Float s, Point2D.Float e, float orientation, Point2D.Float v){
    	this.orientation = orientation;
    	this.v = v;
    	
    	Point2D.Float front = getFront(s);
    	
    	theta = (float) angle(getFront(front), front, e);
    	double Max = 1;
    	if(theta>Max)
    		theta =(float) Max;
    	if(theta<-Max)
    		theta =(float) -Max;

    	theta *= (0.3+(Math.random()*0.7));
    	
    	if(Math.random()<0.6)
    		theta = 0;
    	
    	float Vx = (e.x - s.x) /dt ;
        float Vy = (e.y - s.y) /dt;
        
        float ax = (Vx - v.x) /dt;
        float ay = (Vy - v.y) /dt;
        
        float at = (float) Math.sqrt(ax * ax + ay * ay);
        if (at > MAX_ACCELERATION) {
            ax = (ax / at) * MAX_ACCELERATION;
            ay = (ay / at) * MAX_ACCELERATION;
        }
         
        Vx = v.x + ax * dt;
        Vy = v.y + ay * dt ;
        
        float V = (float) Math.sqrt(((Vx * dt)*( Vx * dt))+ ((Vy*dt) * (Vy * dt)));
        if (V > MAX_VELOCITY) {
            Vx = (Vx / V) * MAX_VELOCITY;
            Vy = (Vy / V) * MAX_VELOCITY;
        }
        
        V = (float) (Math.sqrt(((Vx * dt)*( Vx * dt))+ ((Vy*dt) * (Vy * dt))));

        float newX = (float) (V*Math.cos(orientation));
        
        float newY = (float) (V*Math.sin(orientation));
        
        this.orientation += (float) ((V/length)* Math.tan(theta));
        v.x = Vx;
        v.y = Vy;
        
		return new Point2D.Float(s.x+newX, s.y+newY);    	
    }

    
    
    
    
}