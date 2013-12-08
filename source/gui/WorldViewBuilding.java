package gui;
import java.awt.geom.*;


@SuppressWarnings("serial")
public class WorldViewBuilding extends Rectangle2D.Double {
	BuildingInteriorAnimationPanel _myInteriorAnimationPanel;

	// PROPERTIES
	public int positionX() { return (int)x; }
	public int positionY() { return (int)y; }

	public WorldViewBuilding( int x, int y, int width, int height ) {
		super( x, y, width, height );
	}

	public WorldViewBuilding( int x, int y, int dim) {
		super( x*10 + 41, y*10 + 30, dim, dim );
	}

	public void displayBuilding() {
		_myInteriorAnimationPanel.displayBuildingPanel();
	}

	public void setBuildingPanel( BuildingInteriorAnimationPanel bp ) {
		_myInteriorAnimationPanel = bp;
	}
}
