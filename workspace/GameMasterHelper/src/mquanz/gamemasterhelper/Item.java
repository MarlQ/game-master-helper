package mquanz.gamemasterhelper;
import java.awt.Color;
import javax.swing.ImageIcon;


public class Item extends MapObject implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	//Position on the game map
	
	public String descr;
	public ItemType itemType;
	public ImageIcon icon;

	public Item(String name, String descr, ItemType itemType, ImageIcon icon) {
		super(name, Color.WHITE, icon,itemType);
		this.name = name;
		this.descr = descr;
		this.itemType = itemType;
		this.textColor = Color.WHITE;
	}
	
	public Item(String name, String descr, Color textColor, ItemType itemType, ImageIcon icon) {
		super(name, textColor, icon,itemType);
		this.name = name;
		this.descr = descr;
		this.itemType = itemType;
		this.textColor = textColor;
	}
	
	


}
