package motion.model;

import java.awt.geom.Point2D;

public class KinematicPoint extends Motion {

    public KinematicPoint(String robot) {
        super(robot);
    }

    @Override
    public void move(Point2D.Float[] path) {
        float[] curent_pos;
        for (int i = 0; i < path.length; i++) {
            curent_pos = remote.getPosition(robotHandle);
            while (curent_pos[0] < path[i].x ||  curent_pos[1] < path[i].y) {//this will be a problem when moving back!
                singleMove(path[i].x, path[i].y);
                curent_pos = remote.getPosition(robotHandle);
            }           
        }
    }

    public void singleMove(float x, float y) {
        float[] curent_pos = remote.getPosition(robotHandle);
        float Vx = (x - curent_pos[0]) / dt;
        float Vy = (y - curent_pos[1]) / dt;
        float V = (float) Math.sqrt(Vx * Vx + Vy * Vy);
        if (V > MAX_VELOCITY) {
            Vx = (Vx / V) * MAX_VELOCITY;
            Vy = (Vy / V) * MAX_VELOCITY;
        }
        float[] position = new float[3];
        position[0] = Vx * dt;
        position[1] = Vy * dt;
        remote.setPosition(robotHandle, position);
    }
    
    public Point2D.Float calculate(Point2D.Float s, Point2D.Float e){
    	   float Vx = (e.x - s.x) / dt;
           float Vy = (e.y - s.y) / dt;
           float V = (float) Math.sqrt((Vx * dt)*( Vx * dt)+ (Vy*dt) * (Vy * dt));
           if (V > MAX_VELOCITY) {
               Vx = (Vx / V) * MAX_VELOCITY;
               Vy = (Vy / V) * MAX_VELOCITY;
           }
           Point2D.Float target = new Point2D.Float( s.x+ (Vx * dt),s.y+(Vy * dt));
	    return target;    
        
    }
}
