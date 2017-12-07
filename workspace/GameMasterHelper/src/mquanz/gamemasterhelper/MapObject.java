package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.*;

public class MapObject {


	public String name;
	public Color textColor;
	public ImageIcon icon;
	public ObjectType type;
	public String className;
	
	public MapObject(String name, Color textColor, ImageIcon icon,ObjectType type) {
		this.name = name;
		this.textColor = textColor;
		if(icon == null){
			if(type.icon == null){
				this.icon = new MissingIcon();
			}
			else this.icon = type.icon;
		}
		else this.icon = icon;
	}
	
	
}
