package visibility;

import java.awt.Point;

public class Line {

	Point S;
	Point E;
	float dX, dY ; 
	
	public Line(Point S, Point E){
		if(S==null || E == null)
			System.err.println("bad");
		this.S=S;
		this.E=E;
		
		dX = (float) (E.x-S.x); 
		dY = (float) (E.y-S.y); 
	}
	
	
}
