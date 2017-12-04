package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.*;


public class Item extends MapObject implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	//Position on the game map
	
	public String descr;


	public Item(String name, String descr, ItemType itemType, ImageIcon icon) {
		super(name, Color.WHITE, icon,itemType);
		this.name = name;
		this.descr = descr;
		this.type = itemType;
		this.textColor = Color.WHITE;
		this.className = "Item";
	}
	
	public Item(String name, String descr, Color textColor, ItemType itemType, ImageIcon icon) {
		super(name, textColor, icon,itemType);
		this.name = name;
		this.descr = descr;
		this.type = itemType;
		this.textColor = textColor;
		this.className = "Item";
	}
	
	


}
