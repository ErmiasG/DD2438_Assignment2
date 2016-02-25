import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class pathFinder {
	private final int MARGIN =30;
	private int limit;
	private final int TIMER =1000;
	private HashMap<String, ArrayList<ArrayList<Integer>>> solutions = new   HashMap<String, ArrayList<ArrayList<Integer>> >(); ;
	
	ArrayList<ArrayList<Integer>> cars;
	ArrayList<Integer> customers;
	double[][] distances;
	ArrayList<Float> nodes;
	
	public pathFinder(double[][] distances, ArrayList<Float> nodes, int nrCars, int nrCustomers){
		
		this.distances = distances;
		this.nodes = nodes;
		
		//create list of customers by ID
		customers = new ArrayList<Integer>();
		for(int i=0; i<nrCustomers; i++)
			customers.add(i);
		
		//create list of car-list each car-list contains ID of customers to visit
		cars = new ArrayList<ArrayList<Integer>>();
		for(int i=nrCustomers; i<nrCustomers+nrCars; i++){
			ArrayList<Integer> car = new ArrayList<Integer>();
			car.add(i);
			cars.add(car);
		}
		
		
		greedy();
		limit = (int) (getLongest(cars)) + MARGIN;	
		findVariants();
		
	/*	
		Iterator<String> i = solutions.keySet().iterator();
		while(i.hasNext()){
			cars = solutions.get(i.next());
			
		
			System.out.println(cars +" "+getLongest(cars));
		}*/
				
	}
	
	public HashMap<String, ArrayList<ArrayList<Integer>>> getSolutions(){
		return solutions;
	}
	
	

	@SuppressWarnings("unchecked")
	private void findVariants() {
		long start = System.currentTimeMillis();
		ArrayList<Integer> clone = new ArrayList<Integer>();
		while(System.currentTimeMillis()-start<TIMER){
			
			Object[] keys = solutions.keySet().toArray();
			int randIndex = (int) (Math.random()*keys.length);
			String key = (String) keys[randIndex];
			cars = solutions.get(key);

			
			
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
		
	}



	private void greedy() {
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
		
		addSolution(cars);
	}
	
	
	/**
	 * Returns the length of the longest path
	 * @param list
	 * @return
	 */
	public double getLongest(ArrayList<ArrayList<Integer>> cars){
		double length =0;
		
		for(ArrayList<Integer> l : cars){
			double current =getLength(l);
			if(current > length)
				length = current;
		}
		return length;
	}
	
	
	/**
	 * Returns the length of a path
	 * @param list
	 * @return
	 */
	public double  getLength(ArrayList<Integer> list){

		int distance = 0;
		for(int i=1; i<list.size(); i++){
			distance += nodes.get(list.get(i-1)).distance(nodes.get(list.get(i)));
		}
		
		return distance;
		
	}

	/**
	 * Adds a set of solutions to hashmap, using the order of the nodes as key
	 * @param c
	 */
	private void addSolution(ArrayList<ArrayList<Integer>> c) {
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
	
	public void  print(ArrayList<ArrayList<Integer>> cars){
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
