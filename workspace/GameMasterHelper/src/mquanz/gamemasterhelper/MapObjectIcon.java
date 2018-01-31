package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;

public class MapObjectIcon extends JLabel implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public MapObject mapObject;

	public int posX;
	public int posY;
	
	public boolean isSelected;

	public MapObjectIcon(int posX, int posY, MapObject mapObject) {
		super(mapObject.icon);
		this.posX = posX;
		this.posY = posY;
		this.mapObject = mapObject;
		this.isSelected = false;
	}
	public void updateIcon(){
		if(mapObject.icon != null){
			this.setIcon(mapObject.icon);
		}
		else if(mapObject.type.icon != null){
			this.setIcon(mapObject.type.icon);
		}
		else{
			this.setIcon(MissingIcon.createIcon());
		}
		
		Dimension size = this.getPreferredSize();
		setBounds(this.posX, this.posY, size.width, size.height);

	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		this.posX = x;
		this.posY = y;
		if(mapObject.getClass() == MapLink.class){
			((MapLink) mapObject).ownPosX = posX;
			((MapLink) mapObject).ownPosY = posY;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (isSelected) {
			g.setColor(Color.RED);
			((Graphics2D) g).setStroke(new BasicStroke(1));
			g.drawRect(0, 0, this.mapObject.icon.getIconWidth() - 1, this.mapObject.icon.getIconHeight() - 1);
		}
	}

}
