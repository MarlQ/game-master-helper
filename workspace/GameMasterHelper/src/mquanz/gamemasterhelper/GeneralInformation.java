package mquanz.gamemasterhelper;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GeneralInformation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	

	String name;
	public ArrayList<ItemType> itemTypes = new ArrayList<ItemType>();
	public ArrayList<NpcType> npcTypes = new ArrayList<NpcType>();
	public ArrayList<MapLinkType> mapLinkTypes = new ArrayList<MapLinkType>();
	public ArrayList<MapInformation> maps;

	//Standard item types
	public static ItemType itemTypeText;	
	public static ItemType itemTypeFirearm;
	public static NpcType  npcTypeGeneric;
	public static NpcType  npcTypeImportant;
	public static NpcType  npcTypeVampire;
	public static MapLinkType mapLinkType_small;
	
	public static MapInformation testMAPLINK;
	
	
	public GeneralInformation(String name, ArrayList<ItemType> itemTypes, ArrayList<MapInformation> maps) {
		this.name = name;
		this.itemTypes = itemTypes;
		this.maps = maps;
	}

	public GeneralInformation() {
		name = "newGame";
		itemTypes = new ArrayList<ItemType>();
		maps = new ArrayList<MapInformation>();
		
		itemTypeText = new ItemType("Text",createImageIcon("res/icons/Papier.png"));
		itemTypeFirearm = new ItemType("Firearm",createImageIcon("res/icons/gun.png"));
		
		itemTypes.add(itemTypeText);
		itemTypes.add(itemTypeFirearm);
		
		npcTypeGeneric = new NpcType("Generic",createImageIcon("res/icons/NpcGeneric.png"));
		npcTypeImportant = new NpcType("Important",createImageIcon("res/icons/NpcImportant.png"));
		npcTypeVampire = new NpcType("Vampire",createImageIcon("res/icons/NpcVampire.png"));
		
		npcTypes.add(npcTypeGeneric);
		npcTypes.add(npcTypeImportant);
		npcTypes.add(npcTypeVampire);
		
		mapLinkType_small = new MapLinkType("Small", createImageIcon("res/icons/MapLink.png"));
		
		mapLinkTypes.add(mapLinkType_small);
		
		newMap("Map 01", new Dimension(3000,3000));
		newMap("Map 02", new Dimension(400,300));
		testMAPLINK = newMap("Map 03", new Dimension(4000,3000));
		

		/**
		
		
		testMap.newItem(20,20,"Test","I am testy", itemTypeText);
		testMap.newItem(40,60,"Test","I am testy", itemTypeText);
		testMap.newItem(50,80,"Test","I am testy", itemTypeFirearm);
		**/
	

	}

	public void newItemType(String typeName, String path) {
		ItemType newItemType = new ItemType(typeName, createImageIcon(path));
		itemTypes.add(newItemType);
	}

	public MapInformation newMap(String mapName, Dimension mapSize){
		MapInformation map = new MapInformation(mapName, mapSize);
		maps.add(map);
		return map;
	}

	public static ImageIcon createImageIcon(String path) {

		java.net.URL imgURL = GeneralInformation.class.getResource(path);
		System.out.println(path);
		System.out.println(imgURL);
		if (imgURL != null) {
			
			BufferedImage image;
			try {
				image = ImageIO.read(imgURL);
				ImageIcon icon = new ImageIcon(image);
				return icon;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			// TODO: error
			return null;
		}

	}
	public static ImageIcon createImageIcon(java.net.URL imgURL) {
		if (imgURL != null) {
			
			BufferedImage image;
			try {
				image = ImageIO.read(imgURL);
				ImageIcon icon = new ImageIcon(image);
				return icon;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else {
			// TODO: error
			return null;
		}

	}
	
}
