package mquanz.gamemasterhelper;
import javax.swing.ImageIcon;

public class NpcType extends ObjectType implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	public NpcType(String typeName, ImageIcon icon) {
		super(typeName,icon);
	}
}
