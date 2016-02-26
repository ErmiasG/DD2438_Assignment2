package RRT;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import visibility.Polygon;  
public class visualizer   extends JFrame implements Runnable {
	 panel panel ;
	 ArrayList<Polygon> polygons;
	 int x=300;
	 int y=300;
	 float r;

 	TimeUnit tu = TimeUnit.NANOSECONDS;
		   public visualizer(ArrayList<Polygon> polygons, int x, int y) {
			   r = 2.5F;
			   this.polygons = polygons;
		      JFrame f = new JFrame("Line");
		       panel = new panel();
		      f.add(panel);
		      f.setSize(new Dimension(800, 800));
		      f.setVisible(true);
		      
		   }
		   
		   public void add(Line2D d){
			 panel.add(d);
		   }
		   public void kek(){
			 panel.kek();;
		   }

		@Override
		public void run() {
			for(Polygon p: polygons){
				for(Line2D l : p.edges){
					add(new Line2D.Double((l.getP1().getX()*r),(800-(l.getP1().getY()*r)),(l.getP2().getX()*r),(800-(l.getP2().getY()*r))));
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}			
		}
		
		public void drawPath(Node last){
			kek();
			Node tmp = last;
	    	
	    	while(tmp.parent!=null){
	        	add(new Line2D.Float(new Point2D.Float(tmp.data.x*r, (800-(tmp.data.y*r)) ), new Point2D.Float(tmp.parent.data.x*r, (800-(tmp.parent.data.y*r)) )));
	        	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	tmp=tmp.parent;
	    	}
			kek();
		}

		public void drawLine(Node last) {
		add(new Line2D.Float(new Point2D.Float(last.data.x*r, (800-(last.data.y*r)) ), new Point2D.Float(last.parent.data.x*r, (800-(last.parent.data.y*r)) )));
    	try {
			tu.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			
		}
} 