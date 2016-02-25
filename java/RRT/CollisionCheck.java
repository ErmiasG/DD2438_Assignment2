package RRT;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import visibility.Polygon;

public class CollisionCheck {
	
	    ArrayList<Polygon> polygons;
	    ArrayList<Float[]> xList = new ArrayList<Float[]>();
	    ArrayList<Float[]> yList= new ArrayList<Float[]>();
	    
	    public CollisionCheck(ArrayList<Polygon> polygons){
	    	this.polygons = polygons;
	    }
	    
	    public CollisionCheck(float[] x, float[] y, int[] pol,ArrayList<Polygon> polygons) {

	    		this.polygons = polygons;
	    		int start=0;
	    		int end=0;
	    		while(start!=pol.length){
	    			
			    	for(int i=start; i<pol.length ; i++){	
			    		if(pol[i]==3){
			    			end = i;
			    			break;
			    		}
			    	}

			    	Float[] x1 = new Float[end-start+1];
			    	Float[] y1 = new Float[end-start+1];
			    	for(int i=0; i<end-start+1; i++){
			    		x1[i]=x[start+i];
			    		y1[i]=y[start+i];
			    	}
			    	//System.out.println(Arrays.deepToString(x1));
			    	
			    	xList.add(x1);
			    	yList.add(y1);
			    	start =end+1;
		    		
	    		}
	    		System.out.println(xList.size() +" "+yList.size()+" ");
		}

		public boolean contains(Point2D.Float p){
			
	    	for( int i=0; i<xList.size(); i++){
	    		
	    		if(contains(xList.get(i).length, xList.get(i), yList.get(i), p.x,p.y))
	    			return true;
	    	}
	    	return false;
	    }
	    	

		public boolean checkForCollison(Line2D tmp1) {
			Point2D t1,t2;
			
			t1=tmp1.getP1(); 
			t2=tmp1.getP2();
			
			for(Polygon p:polygons){						
				for(Line2D l: p.edges){					
					Point2D l1,l2;
					l1=l.getP1(); l2=l.getP2();				
					if(l1.equals(t1) || l1.equals(t2) || l2.equals(t1) || l2.equals(t2))
						 continue;					
					if(l.intersectsLine(tmp1))	{
						return true;		}
				}
			}
			return false;	
		}
		
		
	    public boolean contains(int nvert, Float vertx[], Float[] verty, float testx, float testy) {
	    	 int i, j = 0;
	    	 boolean c = false;
	    	  for (i = 0, j = nvert-1; i < nvert; j = i++) {
	    	    if ( ((verty[i]>testy) != (verty[j]>testy)) &&
	    	     (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
	    	       c = !c;
	    	  }
	    	  return c;
	      }
	
}
