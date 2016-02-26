package Astar;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import RRT.CollisionCheck;
import visibility.Polygon;
import visibility.VGraph;


public class roadMap {

	private ArrayList<Point2D.Float> nodes;
	private final int size =24;
	private double[][] distances;

    public ArrayList<Polygon> polygons;

	public roadMap(ArrayList<Point2D.Float> nodes){		
		this.nodes = nodes;		
		generateGraph();	
	}
	
	public double[][] getMap(){
		return distances;
	}
	
	
	
	public void generateGraph(){
		CsvReader reader = new CsvReader(null,0,0);
		float[] x = reader.read("2x.csv", size);
		float[] y = reader.read("2y.csv", size);
		int[] pol = reader.readInt("2pol.csv", size);
		

		VGraph v = new VGraph(x,y,pol,nodes);
		polygons = v.obstacles;
		calculateDist(v,x,y);
	}
	
	
	
	public void calculateDist(VGraph v, float[] x, float[] y){
		distances = new double[nodes.size()][nodes.size()];
		
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
	}

}
