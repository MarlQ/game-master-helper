package mquanz.gamemasterhelper;
import java.awt.Color;

import javax.swing.ImageIcon;

public class MapLink extends MapObject {

	public int linkPosX, linkPosY;
	MapInformation map;
	MapLinkType mapLinkType;
	public ImageIcon icon;

	public MapLink(String name, Color textColor, int linkPosX, int linkPosY, MapInformation map,
			MapLinkType mapLinkType, ImageIcon icon) {
		super(name, textColor,icon,mapLinkType);
		this.linkPosX = linkPosX;
		this.linkPosY = linkPosY;
		this.map = map;
		this.mapLinkType = mapLinkType;

	}

	public MapLink(String name, int linkPosX, int linkPosY, MapInformation map, MapLinkType mapLinkType,  ImageIcon icon) {
		super(name, Color.CYAN,icon,mapLinkType);
		this.linkPosX = linkPosX;
		this.linkPosY = linkPosY;
		this.map = map;
		this.mapLinkType = mapLinkType;
	}

}
