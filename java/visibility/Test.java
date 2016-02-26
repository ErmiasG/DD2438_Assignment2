package visibility;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Astar.CsvReader;
import Astar.NeighborListMap;

public class Test{
	public static final int nrCars = 5;
	public static final int nrCustomers = 8;
	public static final int size =24;
	public static final int MARGIN =30;
	public static int limit;
	public static final int TIMER =10000;
	public static ArrayList<Point2D.Float> nodes;
	public static HashMap<String, ArrayList<ArrayList<Integer>> > solutions ;
	
	@SuppressWarnings("unchecked")
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
		
		nodes = new ArrayList<Point2D.Float>(); 
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
/*		for(int i=0;i<distances.length ; i++){
			System.out.println();
			for(int j=0; j<distances.length; j++){
				System.out.print(distances[i][j]+ " ");
			}
		}
*/
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
		
		double[] pathLength = new double[nrCars];
		double longest = 0;
		for(int i=0; i<pathLength.length;i++){
			pathLength[i]=getLength(cars.get(i));			
			if(pathLength[i]>longest){
				longest = pathLength[i];
			}
		}
		
		limit = MARGIN + (int) (longest);
		long start = System.currentTimeMillis();
		
		ArrayList<Integer> clone = new ArrayList<Integer>();
		
		
		solutions = new   HashMap<String, ArrayList<ArrayList<Integer>> >();
		addSolution(cars);
		
		
		while(System.currentTimeMillis()-start<TIMER){
			
			Object[] keys = solutions.keySet().toArray();
			int randIndex = (int) (Math.random()*keys.length);
			String key = (String) keys[randIndex];
			cars = solutions.get(key);

			
			longest = 0;
			for(int i=0; i<pathLength.length;i++){
				pathLength[i]=getLength(cars.get(i));			
				if(pathLength[i]>longest){
					longest = pathLength[i];
				}
			}
			
			int randomCar = 0;
			int customer = 0;
			do{
				randomCar= (int) (Math.random()*cars.size());
				clone = (ArrayList<Integer>) cars.get(randomCar).clone();
				
			} 
			while(clone.size()<2);

			customer = clone.remove((int)(Math.random()*clone.size()-1)+1);
			
			
			for(int i=0; i<cars.size(); i++){
				if(i==randomCar){
					continue;
				}
				ArrayList<Integer> current = (ArrayList<Integer>) cars.get(i).clone();
				for(int j=1; j<current.size(); j++){
					current.add(j, customer);
					double currentL= getLength(current);
					if(currentL<limit){
						ArrayList<Integer> temp1 =cars.remove(randomCar);
						cars.add(randomCar,clone);
						ArrayList<Integer> temp2 = cars.remove(i);
						cars.add(i, current);
						addSolution(cars);	
						
						cars.remove(randomCar);
						cars.add(randomCar,temp1);
						cars.remove(i);
						cars.add(i, temp2);
						//break outerloop;
					}

					current.remove(j);
					
				}				
			}
			
			
		}
		
		
		Iterator i = solutions.keySet().iterator();
	
		while(i.hasNext()){
			cars = solutions.get(i.next());
			
		
			System.out.println(cars +" "+getLongest(cars));
		}
			
			

	}
	
	public static double getLongest(ArrayList<ArrayList<Integer>> cars){
		double length =0;
		
		for(ArrayList<Integer> l : cars){
			double current =getLength(l);
			if(current > length)
				length = current;
		}
		return length;
	}

	
	private static void addSolution(ArrayList<ArrayList<Integer>> c) {
		StringBuilder sb = new StringBuilder();
		ArrayList<ArrayList<Integer>> copy = new ArrayList<ArrayList<Integer>>();
		for(ArrayList<Integer> car : c){
			ArrayList<Integer> current = new ArrayList<Integer>();
			copy.add(current);
			for(Integer i:car){
				current.add(i);
				sb.append(i+"-");
			}
		}
		
		String key = sb.toString();
		
		
		
		if(!solutions.containsKey(key)){
			
			solutions.put(key,copy);

		//	print(copy);
		}
		
	}


	public static double  getLength(ArrayList<Integer> list){

		int distance = 0;
		for(int i=1; i<list.size(); i++){
			distance += nodes.get(list.get(i-1)).distance(nodes.get(list.get(i)));
		}
		
		return distance;
		
	}

	
	public static void  print(ArrayList<ArrayList<Integer>> cars){
		for(int j=0; j<cars.size(); j++){
			ArrayList<Integer> c = cars.get(j);
			System.out.println();
	
			System.out.print(nodes.get(c.get(0)));
			double distance = 0;
			for(int i=1; i<c.size(); i++){
				distance += nodes.get(c.get(i-1)).distance(nodes.get(c.get(i)));
				System.out.print(nodes.get(c.get(i)));
			}
			System.out.print(" "+distance);
		}
		System.out.println();
		System.out.println("---------------------------------------------------------------------------------------------------------------------------------------");
	}
}