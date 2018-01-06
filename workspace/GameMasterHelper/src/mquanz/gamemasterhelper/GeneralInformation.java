package mquanz.gamemasterhelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

public class GeneralInformation implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	

	String name;
	ArrayList<ItemType> itemTypes = new ArrayList<ItemType>();
	ArrayList<NpcType> npcTypes = new ArrayList<NpcType>();
	ArrayList<MapLinkType> mapLinkTypes = new ArrayList<MapLinkType>();
	ArrayList<MapInformation> maps;
	ArrayList<MapObject> objects;
	ArrayList<Color> colorChooserPalette;

	//Standard item types TODO: For Testing
	static ItemType itemTypeText;
	static ItemType itemTypeFirearm;
	static NpcType  npcTypeGeneric;
	static NpcType  npcTypeImportant;
	static NpcType  npcTypeVampire;
	static MapLinkType mapLinkType_small;
	
	
	public GeneralInformation(String name, ArrayList<ItemType> itemTypes, ArrayList<MapInformation> maps) {
		//TODO: Load-constructor for serialization
		this.name = name;
		this.itemTypes = itemTypes;
		this.maps = maps;
	}

	public GeneralInformation() {
		//First initialization
		name = "newGame";
		itemTypes = new ArrayList<ItemType>();
		maps = new ArrayList<MapInformation>();
		objects = new ArrayList<>();
		colorChooserPalette = new ArrayList<>();
		for(int i = 0; i < GUIMain.COLOR_CHOOSER_COLOR_COUNT; i++){
			if(i >= GUIMain.COLOR_CHOOSER_STANDARD_PALETTE.length){
				colorChooserPalette.add(Color.WHITE);
			}
			else{
				colorChooserPalette.add(GUIMain.COLOR_CHOOSER_STANDARD_PALETTE[i]);
			}
		}

		itemTypeText = new ItemType("Text",createImageIcon("res/icons/item_paper.png"));
		itemTypeFirearm = new ItemType("Firearm",createImageIcon("res/icons/item_gun.png"));
		
		itemTypes.add(itemTypeText);
		itemTypes.add(itemTypeFirearm);
		
		npcTypeGeneric = new NpcType("Generic",createImageIcon("res/icons/npc_generic.png"));
		npcTypeImportant = new NpcType("Important",createImageIcon("res/icons/npc_important.png"));
		npcTypeVampire = new NpcType("Vampire",createImageIcon("res/icons/npc_vampire.png"));
		
		npcTypes.add(npcTypeGeneric);
		npcTypes.add(npcTypeImportant);
		npcTypes.add(npcTypeVampire);
		
		mapLinkType_small = new MapLinkType("Small", createImageIcon("res/icons/maplink_small.png"));
		
		mapLinkTypes.add(mapLinkType_small);


		//TODO: TESTING
		newMap("Map 01", new Dimension(3000,3000));
		newMap("Map 02", new Dimension(400,300));
		newMap("Map 03", new Dimension(400,300));
		newMap("Map 04", new Dimension(400,300));

		Property damage = new Property("Damage", "Number", 5, NumberFormat.getIntegerInstance());

		itemTypeFirearm.properties.add(damage);

	}

	public void newItemType(String typeName, String path) {
		ItemType newItemType = new ItemType(typeName, createImageIcon(path));
		itemTypes.add(newItemType);
	}

	//TODO: TESTING
	public MapInformation newMap(String mapName, Dimension mapSize){
		MapInformation map = new MapInformation(mapName, mapSize);
		maps.add(map);
		return map;
	}

	public static ImageIcon createImageIcon(String path) {

		java.net.URL imgURL = GeneralInformation.class.getResource(path);
		if (imgURL != null) {
			
			BufferedImage image;
			try {
				image = ImageIO.read(imgURL);
				return new ImageIcon(image);
			} catch (IOException e) {
				// Returns a "Missing" Icon (a red cross)
				return MissingIcon.createIcon();
			}
		} else {
			// Returns a "Missing" Icon (a red cross)
			return MissingIcon.createIcon();
		}

	}
	public static ImageIcon createImageIcon(java.net.URL imgURL) {
		if (imgURL != null) {
			
			BufferedImage image;
			try {
				image = ImageIO.read(imgURL);
				return new ImageIcon(image);
			} catch (IOException e) {
				// Returns a "Missing" Icon (a red cross)
				return MissingIcon.createIcon();
			}
		} else {
			// Returns a "Missing" Icon (a red cross)
			return MissingIcon.createIcon();
		}

	}
	
}
