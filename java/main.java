import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Astar.roadMap;
import RRT.RRT;
import motion.model.DynamicPoint;

public class main {

	public static final int size =24;
	public static final int MapSize =24;
	
	public static void main(String[] args) throws InterruptedException{
		
		
		//Creating list of customers
		ArrayList<Point2D.Float> customerCords = new ArrayList<Point2D.Float>(); 
		customerCords.add(new Point2D.Float(220,120));
		customerCords.add(new Point2D.Float(20,120));
		customerCords.add(new Point2D.Float(220,10));
		customerCords.add(new Point2D.Float(220,260));
		customerCords.add(new Point2D.Float(80,120));
		customerCords.add(new Point2D.Float(90,220));
		customerCords.add(new Point2D.Float(120,120));
		customerCords.add(new Point2D.Float(120,150));		

		ArrayList<Point2D.Float> carCords = new ArrayList<Point2D.Float>(); 
		carCords.add(new Point2D.Float(30,70));
		carCords.add(new Point2D.Float(130,40));
		carCords.add(new Point2D.Float(230,20));
		carCords.add(new Point2D.Float(130,270));
		carCords.add(new Point2D.Float(40,160));
		

		ArrayList<Point2D.Float> nodes = new ArrayList<Point2D.Float>(); 
		nodes.addAll(customerCords);
		nodes.addAll(carCords);
		
		roadMap RM = new roadMap(nodes);
		double[][] distances = RM.getMap();
		
		pathFinder PF = new pathFinder(distances, nodes, carCords.size(),customerCords.size());
		
		HashMap<String, ArrayList<ArrayList<Integer>>> solutions = PF.getSolutions();
		
		
		Iterator<String> i = solutions.keySet().iterator();
		ArrayList<ArrayList<Integer>> cars = new ArrayList<ArrayList<Integer>>();
		
		
		
		
	while(i.hasNext()){
			Thread.sleep(1500);
		RRT rrt = new RRT(MapSize,MapSize, RM.polygons );
		
		
		cars = solutions.get(i.next());
		System.out.println(cars);
		for(ArrayList<Integer> car : cars){

			float orientation =0F;
			float speed = 0F;
			Point2D.Float v =new Point2D.Float(0, 0);
			Point2D.Float start = nodes.get(car.get(0));
			DynamicPoint model =new DynamicPoint();
		//	DynamicCar model =new DynamicCar();
			for(int j=1; j<car.size(); j++){
				rrt.setValues(start, nodes.get(car.get(j)), model, orientation, speed, v);
				rrt.init();
				orientation = rrt.last.orientation;
				v= rrt.last.v;
				start = rrt.last.data;
				
			}
		}
			
	}
	}
		
			
		
	
}
