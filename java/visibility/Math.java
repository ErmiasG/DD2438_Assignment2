/*package visibility;

import java.awt.Point;

//http://stackoverflow.com/questions/563198/how-do-you-detect-where-two-line-segments-intersect
public class Math {
	
	 boolean collision(Line first, Line second){
	    	
	    	
		    float denom = (float) ((first.dX * second.dY) - (second.dX * first.dY));
		    
		    if (denom == 0)
		        return false; // Collinear
		    
		    boolean denomPositive = denom > 0;
		    
		    float numerS = (float) ((first.dX * -first.dY) - (first.dY * -first.dX));
		    
		    if ((numerS < 0) == denomPositive)
		        return false; // No collision
		    
		    
		    float numerT = (float) ((second.dX * -first.dY) - (second.dY * -first.dX));
		    
		    if ((numerT < 0) == denomPositive)
		        return false; // No collision

		    if (((numerS > denom) == denomPositive) || ((numerT > denom) == denomPositive))
		        return false; // No collision
	    		 	 	
		    float t = numerT/denom;
		    float colX= first.S.x +(t*first.dX);
		    float colY= first.S.y +(t*first.dY);
		    
		    Point p=new Point();
		    p.setLocation(colX, colY);
		    if(p.equals(first.S) || p.equals(first.E) || p.equals(second.S) || p.equals(second.E))
		    	//return false;
		   
		    
		System.out.println("collision at " +colX +","+colY);
	    return true;
	    }
}
*/