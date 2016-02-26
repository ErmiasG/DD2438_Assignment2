package RRT;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class Tree {
    Node root;
    ArrayList<Node> allNodes = new ArrayList<Node>();
    int count;

    public Tree(Point2D.Float start) {
    	
        root = new Node(null, start, this);
    }

    
    //Can be optimized by a lot
	public Node getClosest(Point2D.Float random) {
		
		double current = Float.MAX_VALUE;
		Node best = null;
		
		for(Node n:allNodes){
			double distance =random.distance(n.data);
			if(distance< current){
				current =distance;
				best = n;}
		}
		return best;
	}

}