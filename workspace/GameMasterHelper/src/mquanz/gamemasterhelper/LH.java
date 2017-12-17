package mquanz.gamemasterhelper;
import java.awt.*;


abstract class LH {


	/**
	 * Places a component using an existing {@link GridBagLayout} in a container.
	 * @param x gridx
	 * @param y gridy
	 * @param w gridwidth
	 * @param h gridheight
	 * @param wx weightx
	 * @param wy weigthty
	 * @param fill specifies how the component stretches out if it has space. Can be n (NONE), h (HORIZONTAL), v (VERTICAL) or b (BOTH).
	 * @param anchor specifies how the component is laid out. Can be n (NORTH), w (WEST), e (EAST), s (South), nw, ne, sw or se.
	 * @param insets inserts additional empty space between components. Does not add space if null.
	 * @param p the Container in which the component should be laid out.
	 * @param c the GridBagConstraints of the existing GridBagLayout.
	 * @param component the component that is to be laid out.
	 */

	static void place(int x, int y, int w, int h, double wx, double wy, String fill, String anchor, Insets insets, Container p, GridBagConstraints c,Component component){
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
