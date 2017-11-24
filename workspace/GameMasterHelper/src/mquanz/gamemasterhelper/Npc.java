package mquanz.gamemasterhelper;
import java.awt.Color;

import javax.swing.ImageIcon;

public class Npc extends MapObject{

	NpcType npcType;
	public ImageIcon icon;
	public Npc(String name, Color textColor, NpcType npcType, ImageIcon icon){
		super(name,textColor,icon,npcType);
		this.npcType = npcType;
	}
	
	
	public Npc(String name, NpcType npcType, ImageIcon icon){
		super(name,Color.BLACK,icon,npcType);
		this.npcType = npcType;
	}
	
}
