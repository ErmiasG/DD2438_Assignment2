package RRT;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

import motion.model.DifferentialDrive;
import motion.model.DynamicCar;
import motion.model.DynamicPoint;
import motion.model.KinematicCar;
import motion.model.KinematicPoint;
import motion.model.Motion;
import visibility.Polygon;

public class RRT {
	
	int margin = 300;
    int maxX = 300;
    int maxY = 300;
    Point2D.Float start;
    Point2D.Float goal;
    ArrayList<Polygon> polygons;
    CollisionCheck cc ;
    
  //KinematicPoint model ;
    DynamicPoint model ;
  //KinematicCar model;
  //DynamicCar model;
  //DifferentialDrive model;
    Node closest;
    Point2D.Float locked = null;

	visualizer v = null;
	public Node last = null;

    Point2D.Float f = new Point2D.Float(maxY, maxX);
	long timer = System.currentTimeMillis();
	boolean draw = true; 
	Tree tree;
	
    public  RRT(int maxX, int maxY, ArrayList<Polygon> polygons){
    	this.maxX = maxX;
    	this.maxY = maxY;
        this.cc =  new CollisionCheck(polygons);//cc;
    	this.polygons = polygons;     	
    	
      	if(draw){
    			new Thread(v =new visualizer(polygons, maxX, maxY)).start();
    			try {
    				Thread.sleep(500);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    				}
    		}
        	
    	
    }
    
    public void setValues(Point2D.Float start, Point2D.Float goal, Motion model, float orientation, float speed, Point2D.Float v){   	
    	this.start = start;
    	this.goal = goal;
    //  this.model = (DynamicCar) model;
    //  this.model = (KinematicCar) model;
        this.model = (DynamicPoint) model;
    //  this.model = (DifferentialDrive) model;
       
        tree = new Tree(start);
    	//tree.root.speed = speed;
    	tree.root.orientation = orientation;
    	tree.root.v = v;
        
    }
    
    public void init(){    
    

  
    	Point2D.Float target;
    	Point2D.Float random;
    	int counter;
    	do{
    		if(System.currentTimeMillis()-timer >180000){
    			last = null;
    			break;
    		}
    	
    		random = randomPos();    	
    		closest = tree.getClosest(random);
    		
	    	counter =0;	    	
    		do{		    
		  
		    	target = move(closest, random);
		    		
		    if(cc.checkForCollison(new Line2D.Float(closest.data,target))||cc.contains(target)/* ||target.x>maxX+margin||target.y>maxY+margin || target.x <0-margin || target.y <0-margin */)
		    		break;
		    
		    	last= new Node(closest, target, tree);    	
		    //	last.speed = this.model.v;
		    	last.v = new Point2D.Float(this.model.v.x, this.model.v.y);
		    	last.orientation = this.model.orientation;
		
		    		if(draw) v.drawLine(last);
		    	
		    	
		    	closest = last;
		    	counter++;
    		}
    		while(counter <5 && target.distance(goal)>5);
    		
	    }
    	while(target.distance(goal)>5);

    		Point2D.Float temp = new Point2D.Float(this.model.v.x, this.model.v.y);
    		if(draw) v.drawPath(last);
    		last.v = temp;
    }
    

    


	private Point2D.Float move(Node closest, Point2D.Float random) {
				
	//	return model.calculate(closest.data, random, closest.orientation, closest.v);
	//	return model.calculate(closest.data, random, closest.orientation);
		return model.calculate(closest.data, random, closest.v);
		//return model.calculate(closest.data, random);
	}
	


	
	public Point2D.Float randomPos(){
		if( Math.random()<0.9 )
			return goal;
		Point2D.Float p = new Point2D.Float();
		float x;
		float y;
	/*	do{*/
			x =(float)Math.random()* maxX;
			y =(float)Math.random()* maxY;
			p.setLocation(x, y);
		/*}
		while(cc.contains(p));*/
		return p;
	}
		
	
    
    
}
