package mquanz.gamemasterhelper;
import javax.swing.ImageIcon;

public class MapLinkType extends ObjectType implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	public String typeName;
	public ImageIcon icon;

	
	
	public MapLinkType(String typeName, ImageIcon icon) {
		super(typeName,icon);
	}

}
