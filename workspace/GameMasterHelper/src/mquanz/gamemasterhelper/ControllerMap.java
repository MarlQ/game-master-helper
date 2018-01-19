package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ControllerMap {

    GUIMain mainFrame;

    String standardMapName = "New Map";
    int standardMapSizeX = 500;
    int standardMapSizeY = 500;

    public ControllerMap(GUIMain mainFrame) {
        this.mainFrame = mainFrame;
    }


    void createMap(String mapName, Dimension mapSize) {
        standardMapSizeX = mapSize.width;
        standardMapSizeY = mapSize.height;

        MapInformation map = new MapInformation(mapName, mapSize);
        mainFrame.campaignInformation.maps.add(map);

        mainFrame.topPane.comboBoxMaps.addItem(map);

        ((DefaultListModel) mainFrame.topPane.dialogMapList.list.getModel()).addElement(map);
    }

    void deleteMap(MapInformation map) {
        mainFrame.campaignInformation.maps.remove(map);

        mainFrame.topPane.comboBoxMaps.removeItem(map);

        DefaultListModel model = (DefaultListModel) mainFrame.topPane.dialogMapList.list.getModel();
        model.removeElement(map);
    }

    void changeMapName(MapInformation map, String name) {
        map.name = name;
        mainFrame.topPane.comboBoxMaps.repaint();
        mainFrame.topPane.dialogMapList.repaint();
    }

    void changeMapSize(MapInformation map, int width, int height, boolean removeObjects) {
        map.mapSize.width = width;
        map.mapSize.height = height;

        //Remove Objects out of bounds
        ArrayList<MapObjectIcon> toRemove = new ArrayList<>();
        for (MapObjectIcon mapObjectIcon : map.itemIcons) {
            int iconWidth = mapObjectIcon.mapObject.icon.getIconWidth();
            int iconHeight = mapObjectIcon.mapObject.icon.getIconHeight();
            if (mapObjectIcon.posX + iconWidth > width || mapObjectIcon.posY + iconHeight > height) {
                if (mapObjectIcon.isSelected) {
                    mainFrame.mapObjectController.unselectObject(mapObjectIcon);
                }
                if (removeObjects) {
                    //Remove non-instanced objects
                    mainFrame.campaignInformation.objects.remove(mapObjectIcon.mapObject);
                }
                if(map == mainFrame.drawingSurface.mapInformation){
                    //Update drawn map if it is this one
                    mainFrame.drawingSurface.componentMover.deregisterComponent(mapObjectIcon);
                    mainFrame.drawingSurface.remove(mapObjectIcon);
                }
                toRemove.add(mapObjectIcon);
            }
        }
        map.itemIcons.removeAll(toRemove);
        if(map == mainFrame.drawingSurface.mapInformation){
            //Update drawn map if it is this one
            mainFrame.drawingSurface.setMaximumSize(map.mapSize);
            mainFrame.drawingSurface.setPreferredSize(map.mapSize);
            mainFrame.drawingSurface.revalidate();
            mainFrame.drawingSurface.repaint();
        }
    }

    void changeMap(MapInformation map) {
        mainFrame.drawingSurface.changeMap(map);
    }
}
