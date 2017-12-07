package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Npc extends MapObject{

	public ArrayList<Item> inventory;

	public Npc(String name, Color textColor, NpcType npcType, ImageIcon icon){
		super(name,textColor,icon,npcType);
		this.type = npcType;
		this.className = "Npc";
	}
	
	/** Initialization-Construction **/
	Npc(String name, NpcType npcType, ImageIcon icon){
		super(name,Color.BLACK,icon,npcType);
		this.type = npcType;
		this.className = "Npc";
		this.inventory = new ArrayList<Item>();


		//TODO: TESTING
		inventory.add(new Item("Hello", "Bla", GeneralInformation.itemTypeText, null));
		inventory.add(new Item("Hello2", "Bla", GeneralInformation.itemTypeFirearm, null));
		inventory.add(new Item("Hello3", "Bla", GeneralInformation.itemTypeText, null));

		inventory.add(new Item("Hello4", "Bla", GeneralInformation.itemTypeText, null));
		inventory.add(new Item("Hello5", "Bla", GeneralInformation.itemTypeText, null));
		inventory.add(new Item("Hello6", "Bla", GeneralInformation.itemTypeText, null));
		inventory.add(new Item("Hello7", "Bla", GeneralInformation.itemTypeText, null));

	}
	
}
