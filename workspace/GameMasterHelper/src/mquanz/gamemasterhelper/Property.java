package mquanz.gamemasterhelper;

import java.util.ArrayList;

public class Property {

    String name;

    Object value;

    private Property(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public static ArrayList<Property> createStandardProperties(){
        ArrayList<Property> properties = new ArrayList<>();

        //Map Link
        properties.add(new Property("Map Link", new MapLinkData()));




        return properties;
    }
}
