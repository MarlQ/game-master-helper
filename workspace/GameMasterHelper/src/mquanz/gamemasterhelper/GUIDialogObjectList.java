package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUIDialogObjectList extends JDialog {

    int rowCount = 0;

    GUIMain mainFrame;

    ArrayList<MapObjectIcon> mapObjectIconList = new ArrayList<MapObjectIcon>();
    ArrayList<ArrayList<JComponent>> rowList = new ArrayList<ArrayList<JComponent>>();

public GUIDialogObjectList(GUIMain mainFrame) {
    super(mainFrame, "Object List");
    this.mainFrame = mainFrame;
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    setLayout(gbl);

    CampaignInformation ginfo = mainFrame.campaignInformation;

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


    LH.place(0,0, 1,1,1,1,"b","c",null,this,c,textFieldName);
    LH.place(1,0, 1,1,1,1,"b","c",null,this,c,textFieldClass);
    LH.place(2,0, 1,1,1,1,"b","c",null,this,c,textFieldType);
    LH.place(3,0, 1,1,1,1,"b","c",null,this,c,textFieldIcon);
    LH.place(4,0, 1,1,1,1,"b","c",null,this,c,textFieldMap);
    LH.place(5,0, 1,1,1,1,"b","c",null,this,c,textFieldPosition);

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

    JTextField classField = new JTextField(mapObject.mapObject.className);
    classField.setEditable(false);

    JTextField typeField = new JTextField(mapObject.mapObject.type.typeName);
    typeField.setEditable(false);

    JButton icon = new JButton(mapObject.mapObject.icon);

    JTextField mapName = new JTextField(map.name);
    mapName.setEditable(false);


    JTextField textFieldPosition;
    if(mapObject.mapObject.getClass() == MapLink.class){
        textFieldPosition = new JTextField("x: " + mapObject.posX + " y: " + mapObject.posY + " (teleport: x: " + ((MapLink) mapObject.mapObject).linkPosX + " y: " + ((MapLink) mapObject.mapObject).linkPosY + ")");

    }

    else{
        textFieldPosition = new JTextField("x: " + mapObject.posX + " y: " + mapObject.posY);
    }


    JButton buttonGoto = new JButton("goto");
    buttonGoto.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
            //GUIDialogObjectList.this.mainFrame.drawingSurface.changeMap
        }
    });

    ArrayList<JComponent> rowComponents = new ArrayList<JComponent>();

    rowComponents.add(nameField);
    rowComponents.add(classField);
    rowComponents.add(typeField);
    rowComponents.add(icon);
    rowComponents.add(mapName);
    rowComponents.add(textFieldPosition);

    this.rowList.add(rowComponents);
    this.mapObjectIconList.add(mapObject);

    rowCount++;

    LH.place(0,rowCount, 1,1,1,1,"b","c",null,this,c,nameField);
    LH.place(1,rowCount, 1,1,1,1,"b","c",null,this,c,classField);
    LH.place(2,rowCount, 1,1,1,1,"b","c",null,this,c,typeField);
    LH.place(3,rowCount, 1,1,1,1,"b","c",null,this,c,icon);
    LH.place(4,rowCount, 1,1,1,1,"b","c",null,this,c,mapName);
    LH.place(5,rowCount, 1,1,1,1,"b","c",null,this,c,textFieldPosition);

}


}
