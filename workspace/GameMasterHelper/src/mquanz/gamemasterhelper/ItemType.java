package mquanz.gamemasterhelper;
import javax.swing.ImageIcon;

public class ItemType extends ObjectType implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	public String typeName;
	public ImageIcon icon;

	
	
	public ItemType(String typeName, ImageIcon icon) {
		super(typeName,icon);
	}
	

}
