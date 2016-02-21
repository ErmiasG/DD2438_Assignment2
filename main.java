import java.awt.geom.Point2D;
import java.util.Arrays;

public class main {
	
		int d = 50;
		
		//alla startpunkter
		Point2D[] start ={
				new Point2D.Float(30,70),
				new Point2D.Float(130,40),
				new Point2D.Float(230,20),
				new Point2D.Float(130,270),
				new Point2D.Float(40,716),
				new Point2D.Float(180,210),
				new Point2D.Float(50,230),
				new Point2D.Float(20,230),
				new Point2D.Float(80,130),
				new Point2D.Float(70,50)};
		
		//alla slutpunkter
		Point2D[] end ={
				new Point2D.Float(0*d,0*d),
				new Point2D.Float(1*d,0*d),
				new Point2D.Float(2*d,0*d),
				new Point2D.Float(3*d,0*d),
				new Point2D.Float(0*d,1*d),
				new Point2D.Float(1*d,1*d),
				new Point2D.Float(2*d,1*d),
				new Point2D.Float(3*d,1*d),
				new Point2D.Float(1*d,2*d),
				new Point2D.Float(2*d,2*d)};
				
	public static void main(String args[]){
		
		main m = new main();
		
		//lista som representerar ordning av vilken punkt som ska till vilken destination
		
		int[] order = new int[m.start.length];
		for(int i=0; i<order.length; i++)
			order[i] = i;
		
		int distance= m.calcDist(order);
		int[] current;
		boolean converged = false;
		int temp =0;
		int d;
		while(!converged){
			converged = true;
			outerloop:
			for(int i=0 ; i<order.length ; i++) {
				for(int j=0; j<order.length; j++){
					current = order.clone();
					temp = current[i];
					current[i]= current[j];
					current[j]= temp;
					d = m.calcDist(current);
					if(d<distance){
						converged = false;
						distance = d;

						order = current;

						System.out.println("switching to "+ Arrays.toString(order)+ "("+distance+")");
						break outerloop;
					}
				}
			}
		}
			

			System.out.println("final order: "+ Arrays.toString(order)+ "("+distance+")");
	
	}
	
	
	  private int calcDist(int[] l) {
		int d = 0;
		for(int i=0; i<l.length; i++){
			d += start[l[i]].distance(end[i]);			
		}
		return d;
	}

	  public void move(float x, float y){
		  for(Point2D p: end){
			  p.setLocation(p.getX()+x, p.getY()+y);
		  }
	  }
}
