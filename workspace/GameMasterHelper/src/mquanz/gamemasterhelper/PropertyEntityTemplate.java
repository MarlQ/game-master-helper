package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PropertyEntityTemplate {

    ImageIcon icon;
    String name;
    ArrayList<Property> properties;
    Color nameColor;
    String type;

    public PropertyEntityTemplate(ImageIcon icon, String name, ArrayList<Property> properties, Color nameColor, String type) {
        this.icon = icon;
        this.name = name;
        this.properties = properties;
        this.nameColor = nameColor;
        this.type = type;
    }
}
