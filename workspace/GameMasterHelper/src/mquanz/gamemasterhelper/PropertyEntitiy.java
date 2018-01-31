package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PropertyEntitiy extends MapEntity{

    ArrayList<Property> properties;
    String type;

    public PropertyEntitiy(int posX, int posY, ImageIcon icon, String name, boolean showNameOnMouseOver, Color nameColor, ArrayList<Property> properties, String type, MapInformation map) {
        super(posX, posY, icon, name, showNameOnMouseOver, nameColor, map);
        this.properties = properties;
        this.type = type;
    }

    public static PropertyEntitiy createPropertyEntityFromTemplate(PropertyEntityTemplate template, int posX, int posY, MapInformation map){
        return new PropertyEntitiy(posX, posY, template.icon, template.name, true, template.nameColor, template.properties, template.type, map);
    }
}
