package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.*;

public class MapLink extends MapObject {

	int linkPosX, linkPosY;
	MapInformation map;
	MapLink twoWayLink;
	Boolean isTwoWayLink;

	MapInformation ownMap;
	int ownPosX, ownPosY;

	public MapLink(String name, Color textColor, int linkPosX, int linkPosY, MapInformation map,
			MapLinkType mapLinkType, ImageIcon icon) {
		super(name, textColor,icon,mapLinkType);
		this.linkPosX = linkPosX;
		this.linkPosY = linkPosY;
		this.map = map;
		this.type = mapLinkType;
		this.className = "Map Link";

	}

	MapLink(String name, int linkPosX, int linkPosY, MapInformation map, MapLinkType mapLinkType,  ImageIcon icon) {
		super(name, Color.CYAN,icon,mapLinkType);
		this.linkPosX = linkPosX;
		this.linkPosY = linkPosY;
		this.map = map;
		this.type = mapLinkType;
		this.className = "Map Link";
		this.twoWayLink = null;
		this.isTwoWayLink = false;
	}

}
