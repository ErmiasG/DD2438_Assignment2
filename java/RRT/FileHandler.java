package RRT;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileHandler {

	ArrayList<Point2D.Float> road = new ArrayList<Point2D.Float>();
	
	int best = Integer.MAX_VALUE ;
	public void store(Node last) throws IOException{
		File file = new File("test.txt");
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
			
		
		
		float distance = 0;
		int counter =0;
		while(last.parent != null){
			counter++;
			distance += last.data.distance(last.parent.data);
			bw.write(last.data.x+" "+ last.data.y+"\n");
			last = last.parent;			
		}
		bw.flush();
	
		bw.close();
		if(counter <best){
		File file2 = new File("results/final/14b/"+counter+"--"+distance+".txt");
		boolean success = file.renameTo(file2);
		
		best = counter;
		System.out.println("current best: "+best);
		}		
		

	}
	
	public Node read(String path) throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		int counter=0;
		String line = "";
		String cvsSplitBy = " ";
		Node current = null;
		Node first = null;
		Node previous = null;
		
		Tree t = new Tree(null);
		
		while ((line = br.readLine()) != null) {

			String[] vals = line.split(cvsSplitBy);
			
			
			for(int i=0; i<vals.length; i++){
				Point2D.Float data = new Point2D.Float(Float.valueOf(vals[0]),Float.valueOf(vals[1]));
				current = new Node(null,data,t);
				road.add(data);
				/*current.speed =;
				current.v=; 
				current.orientation=;*/ 
			}
		if(first==null)
			first = current;
		else
			previous.parent = current;
		
		previous = current;
		}
		
		Collections.reverse(road);
		return first;
	}
}
