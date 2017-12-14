package mquanz.gamemasterhelper;

import javax.swing.*;
import java.util.ArrayList;

public class ObjectType {
	public String typeName;
	public ImageIcon icon;

	public ArrayList<Property> properties;

	
	
	public ObjectType(String typeName, ImageIcon icon) {
		this.typeName = typeName;
		this.icon = icon;
		properties = new ArrayList<>();
	}
}
