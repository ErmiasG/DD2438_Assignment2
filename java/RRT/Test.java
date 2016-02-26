package RRT;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import Astar.CsvReader;
import Astar.Remote;
import motion.model.DifferentialDrive;
import motion.model.DynamicCar;
import motion.model.DynamicPoint;
import motion.model.KinematicCar;
import motion.model.KinematicPoint;
import visibility.Polygon;

public class Test {

	public static final int size =26;
	public static void main(String[] args) throws IOException{
		
		CsvReader reader = new CsvReader(null,0,0);
		float[] x = reader.read("finalX.csv", size);
		float[] y = reader.read("finalY.csv", size);
		int[] pol = reader.readInt("finalPol.csv", size);

    	System.out.println(Arrays.toString(x));
    	System.out.println(Arrays.toString(y));

		Point2D.Float start= new Point2D.Float(120,220);
		Point2D.Float end= new Point2D.Float(150,75);

	    ArrayList<Polygon> obstacles= new ArrayList<Polygon>();


	    Point[] V = new Point[size];
	    
	    	Polygon current = new Polygon();
			obstacles.add(current);
	    	int first =0;
	    	Point tmp = new Point();
			tmp.setLocation(x[0], y[0]);
			V[0]=tmp;

	    	for(int i=1; i<size ; i++){
	    		tmp = new Point();
	    		tmp.setLocation(x[i], y[i]);
	    		V[i]=tmp;
	    		if(current==null){
	    			current = new Polygon();
	    			obstacles.add(current);
	    			first =i;
	    		}
	    		else if(pol[i]==1){
	    			current.addLine(new Line2D.Double(V[i],V[i-1]),i);
	    		}
	    		else if(pol[i]==3){
	    		    
	    			
	    			current.addLine(new Line2D.Double(V[i],V[i-1]),i);
	    			current.addLine(new Line2D.Double(V[i],V[first]),i);
	    			current = null;
	    		}
	    	}
	    
	    	CollisionCheck cc= new CollisionCheck(x,y,pol,obstacles);
	    	
		DynamicPoint dp = new DynamicPoint("node");
		KinematicPoint kp = new KinematicPoint("node");
		KinematicCar kc = new KinematicCar("node");
		DynamicCar dc = new DynamicCar("node");
		DifferentialDrive dd = new DifferentialDrive("node");
		
		FileHandler fh = new FileHandler(); 
		
		
		while(true){			
			RRT rrt = new RRT(300,300, start, end, obstacles,kc,cc);
			if(rrt.last == null){
				System.out.println("skipping");
				continue;
				
			}
			fh.store(rrt.last);;
			
		}
	
		/*
		
		visualizer v;
		new Thread(v = new visualizer(obstacles,300,300)).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		v.drawPath(fh.read("results/final/14a/best.txt"));
	*/
	}

}
