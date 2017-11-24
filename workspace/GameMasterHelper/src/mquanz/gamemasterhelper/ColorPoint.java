package mquanz.gamemasterhelper;
import java.awt.Color;

public class ColorPoint {

	public int size;
	public Color color;
	public int type;
	public int x;
	public int y;
	/**
	 * 1 = circle
	 * 2 = rectangle
	 */
	public ColorPoint(int size, Color color, int type, int x, int y) {
		this.size = size;
		this.color = color;
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	
}
