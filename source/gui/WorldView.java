package gui;

/**
 * This is the class where SimCity will be represented.  It will contain JButtons with images as the click-able buildings.
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;

import city.Directory;
import city.Time;
import city.transportation.gui.BusAgentGui;
import city.transportation.gui.CommuterGui;
import city.transportation.gui.TruckAgentGui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

@SuppressWarnings("serial")
public class WorldView extends JPanel implements MouseListener, ActionListener 
{
	private static int WINDOWX = 1024 * 2 / 3;
	private static int WINDOWY = 720 / 2;

    private List<Gui> guis = new ArrayList<Gui>();
	
	ArrayList<WorldViewBuilding> buildings;
	
	public WorldView()
	{
		this.setPreferredSize(new Dimension(WINDOWX, WINDOWY));
		this.setBorder(BorderFactory.createTitledBorder("World View"));
		 buildings = new ArrayList<WorldViewBuilding>();
		 
         
         addMouseListener( this );

     	Timer timer = new Timer(10, this);
     	timer.start();
	}
	
    public void addGui(CommuterGui gui)
    {
       guis.add(gui);
    }
	
	public WorldViewBuilding addBuilding(int x, int y, int dim, BufferedImage image)
	{
		 WorldViewBuilding b = new WorldViewBuilding( x, y, dim, image );
		 buildings.add( b );
		 return b;
	}
	
	public void addGui(BusAgentGui gui){
		guis.add(gui);
	}
	
	public void addGui(TruckAgentGui gui){
		guis.add(gui);
	}
	

	public void paintComponent( Graphics g ) {
		
	/*	 if ((int)(100*Time.getTime()) % 25 == 0) {
				for ( int i=0; i<lanes.size(); i++ ) {
					lanes.get(i).redLight();
				}
			}
			if ((int)(100*Time.getTime()) % 50 == 0){
				for ( int i=0; i<lanes.size(); i++ ) {
					lanes.get(i).greenLight();
				}
		} */
			
		super.paintComponent(g); // this prevents the building animation panel from being copied in the worldview (for some reason).
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor( Color.black );
		
		g.drawString("Bus Stop", 60, 40);
		g.drawString("Bus Stop", 560, 40);
		g.drawString("Bus Stop", 560, 340);
		g.drawString("Bus Stop", 60, 340);
		
		for ( int i=0; i<buildings.size(); i++ ) {
			WorldViewBuilding b = buildings.get(i);
		    g2.drawImage(b.image, (int)b.x, (int)b.y, (int)b.width, (int)b.height, null, null);
		}
		
		for ( int i=0; i<Directory.lanes().size(); i++ ) {
			Lane l = Directory.lanes().get(i);
			l.draw( g2 );
		}
		
		for ( int i=0; i<Directory.sidewalks().size(); i++ ) {
			Lane l = Directory.sidewalks().get(i);
			l.draw( g2 );
		}
		
		for ( int i=0; i<Directory.busSidewalks().size(); i++ ) {
			Lane l = Directory.busSidewalks().get(i);
			l.draw( g2 );
		}
		
		try
		{
	        for(Gui gui : guis)
	        {
	            if (gui.isPresent())
	            {
	                gui.updatePosition();
	            }
	        }
	        //TODO make guis a synchronized list
	        for(Gui gui : guis)
	        {
	            if (gui.isPresent())
	            {
	                gui.draw(g2);
	            }
	        } 
		} catch(ConcurrentModificationException e) { } // do nothing, because this function will get called again
        
	}
	
	public ArrayList<WorldViewBuilding> getBuildings() {
		return buildings;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse Clicked in WorldView");

        for ( int i=0; i<buildings.size(); i++ ) {
                WorldViewBuilding b = buildings.get(i);
                if ( b.contains( e.getX(), e.getY() ) ) {
                	if(b.myBuildingPanel != null){
                        b.displayBuilding();
                	}
                }
        }	
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint(); // will call paintComponent
	}
}