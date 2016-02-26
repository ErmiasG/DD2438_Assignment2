package visibility;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Polygon {

	public ArrayList<Line2D> edges = new ArrayList<Line2D>();
	public ArrayList<Integer> vIndex = new ArrayList<Integer>();
	public ArrayList<Point2D> points = new ArrayList<Point2D>();
	
	public Polygon(){
			
	}
	
	public void addLine(Line2D l,int index){
		if(!contains(l.getP1()))
			points.add(l.getP1());
		if(!contains(l.getP2()))
			points.add(l.getP2());		
		edges.add(l);
		vIndex.add(index);
	}
	
	
	public boolean contains(Integer i){
		for(Integer I:vIndex){
			if(I.equals(i))
				return true;
		}
		return false;
	}

	public boolean contains(Line2D tmp1) {
		for(Line2D l : edges){
				 if((l.getP1().equals(tmp1.getP1()) && l.getP2().equals(tmp1.getP2()) )||
					(l.getP1().equals(tmp1.getP2()) && l.getP2().equals(tmp1.getP1()) ))
				return true;
		}
		return false;
	}
	
	public boolean contains(Point2D n) {
		for(Point2D p : points){
				 if(n.getX() == p.getX() && n.getY()==p.getY())
				return true;
		}
		return false;
	}
}
