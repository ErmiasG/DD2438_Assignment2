package Astar;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NeighborListMap {

    private int start;
    private int end;
    private int V;
    private float[] x;
    private float[] y;

    public boolean[][] map;
    
    private double[] Hmap;
    private double[] Gmap;
    private double[] Fmap;
    
    private int[] parents;
    Point2D.Float S;
    Point2D.Float E;
    int nodes;
    
    ArrayList<Integer> openList = new ArrayList<Integer>();
    ArrayList<Integer> closedList = new ArrayList<Integer>();
    ArrayList<Integer> path = new ArrayList<Integer>();

    public NeighborListMap(boolean[][] map, float[] x2, float[] y2, Point2D.Float S, Point2D.Float E , int si, int ei) {
    	this.start= x2.length+si;
    	this.end = x2.length+ei;
    	this.S = S;
    	this.E = E;
        this.x = x2;
        this.y = y2;
        V = map.length;
        nodes = x2.length;
        
        this.map = map;
        Hmap = new double[V];
        Fmap = new double[V];
        Gmap = new double[V];
        parents = new int[V];

        init();
    }

    public void init() {

        for (int i = 0; i < Gmap.length; i++) {
            Gmap[i] = Double.MAX_VALUE;
            Fmap[i] = Double.MAX_VALUE;
        }

        //calculate all distances to end
        for (int i = 0; i < nodes; i++) {

            double dist = calcDist(i, E);
        	Hmap[i]=dist;
        }

        //set start as parent to neighboring nodes
        for (int i = 0; i <=nodes; i++) {
            if (map[start][i]) {
                parents[i] = start;
            }
        }

        //add start to open list, set its G-value to 0
        openList.add(start);
        Gmap[start] = 0;
        Fmap[start] = 0;
        parents[start] = start;
    }

    //calculates oclidian distance
    private double calcDist(int s, int e) {
    	if(s==start)
    		return calcDist(e,S);
        return Math.sqrt(Math.pow(x[s] - x[e], 2) + Math.pow(y[s] - y[e], 2));
    }
    
    private double calcDist(int s, Point2D.Float e) {
        return Math.sqrt(Math.pow(x[s] - e.x, 2) + Math.pow(y[s] - e.y, 2));
    }
    
    public ArrayList<Integer> getShortestPath() {
        int current;

        while (!openList.isEmpty()) {
        	Collections.sort(openList, new Comparator<Integer>() {
                @Override
                public int compare(Integer f, Integer s)
                {

                    return  (int) (Fmap[f]-Fmap[s]);
                }
            });
            current = openList.remove(0);
            closedList.add(current);
           // System.out.println(current);
            if (getNeighbors(current)) {
                backtrack(current);
                break;
            }
        }
        
        
        return path;
    }

    
    private void backtrack(int current) {
     //   path.add(end);
        path.add(current);
        while (current != start) {
            current = parents[current];
            path.add(current);
        }

        Collections.reverse(path);
    }

    private boolean getNeighbors(int current) {
        double newVal;
        int parent;

        //updates the Gvalues of neighbors
        for (int i = 0; i < V; i++) {

            if (!map[current][i] || i == start || contains(closedList,i) || (i>=nodes && !(i==start ||i==end)) ) {
                continue;
            }

            //return success if end point found
            if (i == end) {
                return true;
            }

            //if new point, add to openlist
            if (!contains(openList, i) && !contains(closedList, i)) {
                openList.add(i);
                parents[i] = current; 
            }

            //calculate new value to be parents value + cost
            parent = parents[i];
            newVal = Gmap[parent] + calcDist(current,i);

            //Change values if new one is smaller
            if (newVal < Gmap[i] && i != start) {
                Gmap[i] = newVal;
                parents[i] = current;
                Fmap[i] = Gmap[i]+Hmap[i];
            }

        }

        //Sort the openList
        Collections.sort(openList, new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return (int) (Gmap[a] - Gmap[b]);
            }
        });
        return false;

    }

    public boolean contains(ArrayList<Integer> a, int v) {
        for (int i : a) {
            if (i == v) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getPath() {
        return path;
    }

}
