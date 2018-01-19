package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class ControllerMapObject {

    private GUIMain mainFrame;
    private ArrayList<GUIObjectEditScreen> objectEditScreenList = new ArrayList<GUIObjectEditScreen>();
    private ArrayList<MapObjectIcon> selectedIcons = new ArrayList<>();
    //MapObjectIcon selectedIcon;

    ControllerMapObject(GUIMain mainFrame) {
        this.mainFrame = mainFrame;
    }
    
    private void deleteObjectOnMap(MapObjectIcon objectIcon, MapInformation map){

    }
    private void deleteObjectsOnMap(ArrayList<MapObjectIcon> objectIcons,  MapInformation map){

    }
    private void deleteObjectCompletely(MapObjectIcon objectIcon){

    }
    private void deleteObjectsCompletely(ArrayList<MapObjectIcon> objectIcons){

    }
    void unselectAllObjects(){
        for(Iterator<MapObjectIcon> iterator = selectedIcons.iterator(); iterator.hasNext();){
            MapObjectIcon selectedIcon = iterator.next();
            selectedIcon.isSelected = false;
            selectedIcon.repaint();
            iterator.remove();
        }
    }
    void unselectObject(MapObjectIcon mapObjectIcon){
        mapObjectIcon.isSelected = false;
        mapObjectIcon.repaint();
        selectedIcons.remove(mapObjectIcon);
    }

    void deleteSelectedObjects(){
        for(Iterator<MapObjectIcon> iterator = selectedIcons.iterator(); iterator.hasNext();){
            MapObjectIcon selectedIcon = iterator.next();
            for(GUIObjectEditScreen editScreen : objectEditScreenList){
                if(editScreen.mapObjectIcon == selectedIcon){
                    editScreen.dispose();
                    objectEditScreenList.remove(editScreen);
                }
            }
            mainFrame.drawingSurface.componentMover.deregisterComponent(selectedIcon);
            mainFrame.drawingSurface.remove(selectedIcon);
            mainFrame.drawingSurface.mapInformation.itemIcons.remove(selectedIcon);
            iterator.remove();
            mainFrame.drawingSurface.repaint();
        }
    }
    void selectAdditionalObject(MapObjectIcon mapObjectIcon){
        mapObjectIcon.isSelected = true;
        selectedIcons.add(mapObjectIcon);
        mapObjectIcon.repaint();
    }

    void selectSingleObject(MapObjectIcon mapObjectIcon){
        unselectAllObjects();
        selectAdditionalObject(mapObjectIcon);
    }

    void doubleClicked(MapObjectIcon mapObjectIcon){
        if(mapObjectIcon.mapObject.getClass() == MapLink.class){
            followMapLink((MapLink) mapObjectIcon.mapObject);
        }
    }

    void editObject(MapObjectIcon mapObjectIcon){
        MapObject mapObject = mapObjectIcon.mapObject;

        for (GUIObjectEditScreen editScreen : objectEditScreenList) {
            if (editScreen.mapObjectIcon == mapObjectIcon) {
                editScreen.toFront();
                return;
            }
        }
        GUIObjectEditScreen editScreen = new GUIObjectEditScreen(
                mainFrame, mapObjectIcon, objectEditScreenList);
        editScreen.setLocationRelativeTo(mainFrame);
        objectEditScreenList.add(editScreen);
    }
    void followMapLink(MapLink mapLink){
        if(mapLink.map != null){
            mainFrame.mapController.changeMap(mapLink.map);
            mainFrame.topPane.comboBoxMaps.setSelectedItem(mapLink.map);
            mainFrame.scrollPane.getVerticalScrollBar().setValue(mapLink.linkPosX);
            mainFrame.scrollPane.getHorizontalScrollBar().setValue(mapLink.linkPosY);
        }
    }

    void rightClicked(MapObjectIcon mapObjectIcon,MouseEvent e){

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemEdit = new JMenuItem("Edit");
        menuItemEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editObject(mapObjectIcon);
            }
        });
        popupMenu.add(menuItemEdit);
        popupMenu.show(e.getComponent(),e.getX(),e.getY());
    }

}
