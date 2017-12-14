package mquanz.gamemasterhelper;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class MapInformation implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	
	ArrayList<MapObjectIcon> itemIcons;
	String name;
	Dimension mapSize;
	public BufferedImage drawingImage;
	
	public MapInformation(ArrayList<MapObjectIcon> itemIcons, String name, Dimension mapSize) {
		this.itemIcons = itemIcons;
		this.name = name;
		this.mapSize = mapSize;
	}

	public MapInformation(String name, Dimension mapSize){	
		this.itemIcons = new ArrayList<MapObjectIcon>();
		this.name = name;
		this.mapSize = mapSize;
	}
	
    public MapObjectIcon newItem(int posX, int posY, String name, String descr, ItemType itemType){
		Item newItem = new Item(name,descr, itemType, null);
		MapObjectIcon newMapObjectIcon = new MapObjectIcon(posX, posY, newItem);
		this.itemIcons.add(newMapObjectIcon);	
	    return newMapObjectIcon;
	}
	
    public MapObjectIcon newNpc(int posX, int posY, String name, NpcType npcType){
  		Npc newNpc = new Npc(name, npcType,null);
  		MapObjectIcon newMapObjectIcon = new MapObjectIcon(posX, posY, newNpc);
  		this.itemIcons.add(newMapObjectIcon);	
  	    return newMapObjectIcon;
  	}
	
    public MapObjectIcon newMapLink(int posX, int posY, String name, MapLinkType mapLinkType, int linkPosX, int linkPosY, MapInformation map){
    	MapLink newMapLink = new MapLink(name, linkPosX, linkPosY, map, mapLinkType, null);
  		MapObjectIcon newMapObjectIcon = new MapObjectIcon(posX, posY, newMapLink);
  		this.itemIcons.add(newMapObjectIcon);	
  	    return newMapObjectIcon;
  	}
	@Override
	public String toString(){
    	return this.name;
	}



}
