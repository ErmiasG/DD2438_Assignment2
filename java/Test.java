package visibility;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import Astar.CsvReader;
import Astar.NeighborListMap;

public class Test{
	public static final int nrCars = 5;
	public static final int nrCustomers = 8;
	public static final int size =24;
	public static void main(String[] args){
		

		CsvReader reader = new CsvReader(null,0,0);
		float[] x = reader.read("2x.csv", size);
		float[] y = reader.read("2y.csv", size);
		int[] pol = reader.readInt("2pol.csv", size);
		
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
		
		//Creating visibility graf
		VGraph v = new VGraph(x,y,pol,nodes);
		
		//Running A* between all nodes		
		double[][] distances = new double[nodes.size()][nodes.size()];
		for(int l=0; l<nodes.size(); l++){
			for(int j=l+1; j<nodes.size(); j++){
				NeighborListMap m = new NeighborListMap(v.adjG, x,y,nodes.get(l), nodes.get(j),l,j );
				Point2D.Float last = null;
				double dist =0;
				
				for(int i : m.getShortestPath()){

					if(last ==null){
						last = nodes.get(l);
						continue;
					}
					Point2D.Float n = new Point2D.Float(x[i], y[i]);
					
					dist +=  last.distance(n);
					last = n;
				}
				dist += nodes.get(j).distance(last);
				
				distances[l][j] = dist;
				distances[j][l] = dist;
			}
		}
		
		//Printing weighted Adj matrix 
		for(int i=0;i<distances.length ; i++){
			System.out.println();
			for(int j=0; j<distances.length; j++){
				System.out.print(distances[i][j]+ " ");
			}
		}

		//create list of customers by ID
		ArrayList<Integer> customers = new ArrayList<Integer>();
		for(int i=0; i<nrCustomers; i++)
			customers.add(i);
		
		//create list of car-list each car-list contains ID of customers to visit
		ArrayList<ArrayList<Integer>> cars = new ArrayList<ArrayList<Integer>>();
		for(int i=nrCustomers; i<nrCustomers+nrCars; i++){
			ArrayList<Integer> car = new ArrayList<Integer>();
			car.add(i);
			cars.add(car);
		}
		
		
		//creating initial GREEDY car lists		
		int temp = customers.size();
		for(int i=0; i<temp ;i++){
			double current= Double.MAX_VALUE;
			int car = 0;
			for(int j=0; j<cars.size(); j++){
				double d = distances[cars.get(j).get(cars.get(j).size()-1)][i];
				if(d <current){
					current =d;
					car = j;
				}
			}
			cars.get(car).add(customers.remove(0));
		}
		int[] pathLength = new int[nrCars];
		for(int j=0; j<cars.size(); j++){
			ArrayList<Integer> c = cars.get(j);
			System.out.println();

			System.out.print(nodes.get(c.get(0)));
			int distance = 0;
			for(int i=1; i<c.size(); i++){
				distance += nodes.get(c.get(i-1)).distance(nodes.get(c.get(i)));
				System.out.print(nodes.get(c.get(i)));
			}
			pathLength[j] = distance;
			System.out.print(" "+distance);
		}
		
		long start = System.currentTimeMillis();
		while(start-System.currentTimeMillis()<10000){
			
			int car =0;
			int longest = 0;
			for(int i=0; i<pathLength.length;i++)
				if(pathLength[i]>longest){
					longest = 0;
					car = i;
				}
			
			ArrayList<Integer> clone = (ArrayList<Integer>) cars.get(car).clone();
			int customer = clone.remove((int)Math.random()*clone.size());
			
			for(int i=0; i<cars.size(); i++){
				for(int j=0; j<cars.get(i).size(); j++){
					
					
				}				
			}
		}
		
	}
	
}
