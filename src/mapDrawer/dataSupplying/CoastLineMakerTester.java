package mapDrawer.dataSupplying;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;

import mapDrawer.AreaToDraw;

public class CoastLineMakerTester extends JFrame{
	
	private GeneralPath[] generalPathsToDraw;

	public CoastLineMakerTester()
	{
		generalPathsToDraw = CoastLineMaker.getCoastLineToDraw(200, 200, new AreaToDraw());
		this.setSize(new Dimension(200,200));
		//this.pack();
		this.setVisible(true);
	}
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		System.out.println(generalPathsToDraw.length);
		
		for(GeneralPath path : generalPathsToDraw)
			g2.draw(path);
		
		
	}
	
	
	
	
	public static void main( String[] args )
	{
		new CoastLineMakerTester();
	}

}
