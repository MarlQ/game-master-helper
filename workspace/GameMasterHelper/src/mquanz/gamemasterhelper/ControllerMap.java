package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;

public class ControllerMap{

    GUIMain mainFrame;

    String standardMapName = "New Map";
    int standardMapSizeX = 500;
    int standardMapSizeY = 500;

    public ControllerMap(GUIMain mainFrame){
        this.mainFrame = mainFrame;
    }


    void createMap(String mapName, Dimension mapSize){
        standardMapSizeX = mapSize.width;
        standardMapSizeY = mapSize.height;

        MapInformation map = new MapInformation(mapName, mapSize);
        mainFrame.generalInformation.maps.add(map);

        mainFrame.topPane.comboBoxMaps.addItem(map);

        ((DefaultListModel) mainFrame.topPane.dialogMapList.list.getModel()).addElement(map);
    }

    void deleteMap(MapInformation map){
        mainFrame.generalInformation.maps.remove(map);

        mainFrame.topPane.comboBoxMaps.removeItem(map);

        DefaultListModel model = (DefaultListModel) mainFrame.topPane.dialogMapList.list.getModel();
        model.removeElement(map);
    }

    void changeMap(MapInformation map){
        mainFrame.drawingSurface.changeMap(map);
    }
}
