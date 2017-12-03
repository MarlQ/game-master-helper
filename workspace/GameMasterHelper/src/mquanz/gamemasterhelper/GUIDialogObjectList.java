package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUIDialogObjectList extends JDialog {

    int rowCount = 0;


    ArrayList<MapObjectIcon> mapObjectIconList = new ArrayList<MapObjectIcon>();
    ArrayList<ArrayList<JComponent>> rowList = new ArrayList<ArrayList<JComponent>>();

public GUIDialogObjectList(GUIMain mainFrame) {
    super();
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setLayout(gbl);

    GeneralInformation ginfo = mainFrame.generalInformation;

    JTextField textFieldName = new JTextField("Name");
    JTextField textFieldClass = new JTextField("Class");
    JTextField textFieldType = new JTextField("Type");
    JTextField textFieldIcon = new JTextField("Icon");
    JTextField textFieldMap = new JTextField("Map");
    JTextField textFieldPosition = new JTextField("Position");
    textFieldName.setEditable(false);
    textFieldIcon.setEditable(false);
    textFieldClass.setEditable(false);
    textFieldType.setEditable(false);
    textFieldMap.setEditable(false);
    textFieldPosition.setEditable(false);


    LH.place(0,0, 1,1,1,1,"b","c",null,this,gbl,c,textFieldName);
    LH.place(1,0, 1,1,1,1,"b","c",null,this,gbl,c,textFieldClass);
    LH.place(2,0, 1,1,1,1,"b","c",null,this,gbl,c,textFieldType);
    LH.place(3,0, 1,1,1,1,"b","c",null,this,gbl,c,textFieldIcon);
    LH.place(4,0, 1,1,1,1,"b","c",null,this,gbl,c,textFieldMap);
    LH.place(5,0, 1,1,1,1,"b","c",null,this,gbl,c,textFieldPosition);

    for (MapInformation map : ginfo.maps) {
        for (MapObjectIcon mapObject : map.itemIcons) {
            addRow(mapObject,map,gbl,c);

        }
    }


    setVisible(true);
    pack();

}

private void deleteRow(int rowIndex){


    for(JComponent jComponent : rowList.get(rowIndex)){
        this.remove(jComponent);
        rowList.remove(rowIndex);
    }
    rowCount--;

    for(int i = rowIndex; i < rowCount; i++){
        //TODO:
    }



}

private void addRow(MapObjectIcon mapObject,MapInformation map, GridBagLayout gbl, GridBagConstraints c){
    JTextField nameField = new JTextField(mapObject.mapObject.name);

    JTextField classField = new JTextField(mapObject.mapObject.getClass().getName());
    classField.setEditable(false);

    JTextField typeField;

    if(mapObject.mapObject.type != null){
        typeField = new JTextField(mapObject.mapObject.type.typeName);
    }
    else{typeField = new JTextField("ERROR");}

    typeField.setEditable(false);

    JButton icon = new JButton(mapObject.mapObject.icon);

    JTextField mapName = new JTextField(map.name);
    mapName.setEditable(false);

    JTextField position = new JTextField("x: " + mapObject.posX + " y: " + mapObject.posY);

    ArrayList<JComponent> rowComponents = new ArrayList<JComponent>();

    rowComponents.add(nameField);
    rowComponents.add(classField);
    rowComponents.add(typeField);
    rowComponents.add(icon);
    rowComponents.add(mapName);
    rowComponents.add(position);

    this.rowList.add(rowComponents);
    this.mapObjectIconList.add(mapObject);

    rowCount++;

    LH.place(0,rowCount, 1,1,1,1,"b","c",null,this,gbl,c,nameField);
    LH.place(1,rowCount, 1,1,1,1,"b","c",null,this,gbl,c,classField);
    LH.place(2,rowCount, 1,1,1,1,"b","c",null,this,gbl,c,typeField);
    LH.place(3,rowCount, 1,1,1,1,"b","c",null,this,gbl,c,icon);
    LH.place(4,rowCount, 1,1,1,1,"b","c",null,this,gbl,c,mapName);
    LH.place(5,rowCount, 1,1,1,1,"b","c",null,this,gbl,c,position);

}


}
