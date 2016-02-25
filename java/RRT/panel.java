package RRT;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;

public class panel extends JPanel{
	 Graphics2D g2 ;
	 ArrayList<Line2D> list= new ArrayList<Line2D>();
	 Line2D line = new Line2D.Double(0, 0, 0, 0);
	 boolean main = false;
	 
	   public void paintComponent(Graphics g) {
	       g2 = (Graphics2D) g;
	       if(main){
	    	   g2.setPaint(Color.red);
	    	   
          g2.setStroke(new BasicStroke(2));
	       }
	      g2.draw(new Line2D.Double(line.getX1(),line.getY1(),line.getX2(),line.getY2()));

	   }

	   public void add(Line2D l){
		   line = l;
		   this.repaint();
		   
	   }

	public void kek() {
		main = !main;
	}
}
