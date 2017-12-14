package mquanz.gamemasterhelper;

import java.text.Format;

public class Property {

    String name;
    String typeName;

    Object value;
    Format format;

    public Property(String name, String typeName, Object value, Format format) {
        this.name = name;
        this.typeName = typeName;
        this.value = value;
        this.format = format;
    }
}
