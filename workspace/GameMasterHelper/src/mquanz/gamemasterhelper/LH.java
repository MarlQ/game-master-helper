package mquanz.gamemasterhelper;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


public abstract class LH {

	public static void place(int x, int y, int w, int h, double wx, double wy, String fill, String anchor, Insets insets, Container p, GridBagLayout gbl, GridBagConstraints c,Component component){
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = w;
		c.gridheight = h;
		c.weightx = wx;
		c.weighty = wy;
		
		switch(fill){
		case "n":
			c.fill = GridBagConstraints.NONE;
			break;
		case "h":
			c.fill = GridBagConstraints.HORIZONTAL;
			break;
		case "v":
			c.fill = GridBagConstraints.VERTICAL;
			break;
		case "b":
			c.fill = GridBagConstraints.BOTH;
			break;
		}
		switch(anchor){
		case "nw": 
			c.anchor = GridBagConstraints.NORTHWEST;
			break;
		case "n": 
			c.anchor = GridBagConstraints.NORTH;
			break;
		case "ne": 
			c.anchor = GridBagConstraints.NORTHEAST;
			break;
		case "w": 
			c.anchor = GridBagConstraints.WEST;
			break;
		case "c": 
			c.anchor = GridBagConstraints.CENTER;
			break;
		case "e": 
			c.anchor = GridBagConstraints.EAST;
			break;
		case "sw": 
			c.anchor = GridBagConstraints.SOUTHWEST;
			break;
		case "s": 
			c.anchor = GridBagConstraints.SOUTH;
			break;
		case "se": 
			c.anchor = GridBagConstraints.SOUTHEAST;
			break;
		}
		if(insets != null) c.insets = insets;
		p.add(component, c);
	}
	
	
	
}
