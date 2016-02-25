package RRT;

import java.awt.geom.Point2D;


public  class Node {
    public Point2D.Float data;
    public Node parent;
    public float speed;
    public Point2D.Float v;

    public float orientation;
    
    float cost;
    
    public Node(Node parent, Point2D.Float data, Tree t){
    	if(parent!=null)
    		this.cost = parent.cost+(float) data.distance(parent.data);
    	this.parent = parent;
    	this.data = data;

    	t.allNodes.add(this);
    	t.count++;
    }
    
    
}