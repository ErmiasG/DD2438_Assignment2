package motion.model;

import java.awt.geom.Point2D;

public class Discrete extends Motion {

    public Discrete(String robot) {
        super(robot);
    }

    @Override
    public void move(Point2D.Float[] path) {
        float[] curent_pos;
        for (int i = 0; i < path.length; i++) {
            curent_pos = remote.getPosition(robotHandle);
            boolean dis_coverd = singleMove(path[i].x, path[i].y);
            while (!dis_coverd) {
                dis_coverd = singleMove(path[i].x, path[i].y);
            }
        }

    }

    public boolean singleMove(float x, float y) {
        float[] curent_pos = remote.getPosition(robotHandle);
        float[] position = new float[3];
        boolean dis_coverd = true;
        position[0] = x - curent_pos[0];
        position[1] = y - curent_pos[1];
        float dist = (float) Math.sqrt(position[0] * position[0] + position[1] * position[1]);
        if (dist > MAX_DIS) {
            position[0] = (position[0] / dist) * MAX_DIS;
            position[1] = (position[1] / dist) * MAX_DIS;
            dis_coverd = false;
        }
        remote.setPosition(robotHandle, position);
        return dis_coverd;
    }
    
    public Point2D.Float predict(Point2D.Float s, Point2D.Float e){    
	    
	    float x =  e.x - s.x;
	    float y = e.y - s.y;
	    
	    float dist = (float) Math.sqrt(x*x + y*y);
	    if (dist > MAX_DIS) {
	       x = (x / dist) * MAX_DIS;
	       y = (y / dist) * MAX_DIS;
	    }
	    
	    return new Point2D.Float(x, y);
    }
    
}
