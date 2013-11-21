package city.restaurant.yixin.gui;

import gui.Gui;

import java.awt.*;

import javax.swing.*;

import city.restaurant.yixin.YixinHostRole;

public class YixinHostGui extends JPanel implements Gui {

    private YixinHostRole agent = null;

    private int xPos = 50, yPos = 20;//default waiter position
    public static int xGap = 40;
    public static int yGap = 40;
    private String WA = "Waiting Area";
    
    //private ImageIcon i = new ImageIcon("image/host.png");
    //private Image image = i.getImage();
    public YixinHostGui(YixinHostRole agent) {
        this.agent = agent;
    }

    public void updatePosition() {
    }

    public void draw(Graphics2D g) {
    	g.setColor(Color.BLUE);
    	g.drawString(WA, 30, 10);
    }

    public boolean isPresent() {
        return true;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

}