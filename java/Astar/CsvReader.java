package Astar;

import java.io.BufferedReader;
import java.io.FileReader;

public class CsvReader {

	String path;
	int row, col;
	
	public CsvReader(String path, int row, int col){
		this.path = path;
		this.row = row;
		this.col = col;
	}
	
	public boolean[][] convert(){
		boolean[][] map = new boolean[col][row];
		
		String line = "";
		String cvsSplitBy = ",";
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(path));
			int counter=0;
			
			while ((line = br.readLine()) != null) {

			        // use comma as separator
				String[] vals = line.split(cvsSplitBy);

				for(int i=0; i<col; i++){
					if(Integer.valueOf(vals[i])==1)
						map[i][counter] = true;
				}
				counter++;
			}

		} catch(Exception e){}
		return map;
	}
	
	public float[] read(String path, int l){
		float[] val = new float[l];
		String line = "";
		String cvsSplitBy = ",";
	try {
			
			BufferedReader br = new BufferedReader(new FileReader(path));
			int i=0;
			while ((line = br.readLine()) != null) {
			       
					val[i] = Float.valueOf(line);
					i++;
				
			}

		} catch(Exception e){}
		return val;
	}
	
	public int[] readInt(String path, int l){
		int[] val = new int[l];
		String line = "";
		String cvsSplitBy = ",";
	try {
			
			BufferedReader br = new BufferedReader(new FileReader(path));
			int i=0;
			while ((line = br.readLine()) != null) {
			       
				val[i] = Integer.valueOf(line);
				i++;
			
		}

		} catch(Exception e){}
		return val;
	}
}
