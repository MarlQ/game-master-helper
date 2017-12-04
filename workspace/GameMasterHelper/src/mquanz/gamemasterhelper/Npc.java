package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.*;

public class Npc extends MapObject{


	public ImageIcon icon;
	public Npc(String name, Color textColor, NpcType npcType, ImageIcon icon){
		super(name,textColor,icon,npcType);
		this.type = npcType;
		this.className = "Npc";
	}
	
	
	public Npc(String name, NpcType npcType, ImageIcon icon){
		super(name,Color.BLACK,icon,npcType);
		this.type = npcType;
		this.className = "Npc";
	}
	
}
