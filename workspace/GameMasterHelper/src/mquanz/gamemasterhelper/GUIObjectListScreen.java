package mquanz.gamemasterhelper;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUIObjectListScreen extends JDialog {

    GUIObjectListScreen(GUIMain mainFrame){
        super(mainFrame, "Object List");



        ArrayList<String> names = new ArrayList<String>();
        ArrayList<ImageIcon> icons = new ArrayList<ImageIcon>();
        ArrayList<String> mapNames = new ArrayList<String>();
        ArrayList<String> positions = new ArrayList<String>();

        for(MapInformation map : mainFrame.generalInformation.maps){
            for(MapObjectIcon objectIcon :  map.itemIcons) {
                names.add(objectIcon.mapObject.name);
                icons.add(objectIcon.mapObject.icon);
                mapNames.add( map.name);
                positions.add("x: " + objectIcon.posX + "y: " + objectIcon.posY);
            }
        }


       JTable table = new JTable(new ModelData(names,icons,mapNames,positions));
       table.setPreferredScrollableViewportSize(new Dimension (500, 80));
       table.setFillsViewportHeight(true);
       add(new JScrollPane(table));
       setVisible(true);
        pack();


    }


    class ModelData extends AbstractTableModel {
        ArrayList<Data> data = new ArrayList<Data>();
        String colNames[] = { "Name", "Icon", "Map","Position" };
        Class<?> colClasses[] = { String.class, ImageIcon.class, String.class, String.class  };

        ModelData(ArrayList<String> names, ArrayList<ImageIcon> icons, ArrayList<String> maps,ArrayList<String> positions) {
            for(int i = 0; i < names.size(); i++){
                data.add(new Data(names.get(i),icons.get(i),maps.get(i),positions.get(i)));
            }
        }

        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return colNames.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return data.get(rowIndex).name;
            }
            if (columnIndex == 1) {
                return data.get(rowIndex).icon;
            }
            if (columnIndex == 2) {
                return data.get(rowIndex).mapName;
            }
            if (columnIndex == 3) {
                return data.get(rowIndex).position;
            }
            return null;
        }

        public String getColumnName(int columnIndex) {
            return colNames[columnIndex];
        }

        public Class<?> getColumnClass(int columnIndex) {
            return colClasses[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                data.get(rowIndex).name = (String) aValue;
            }
            if (columnIndex == 1) {
                data.get(rowIndex).icon = (ImageIcon) aValue;
            }
            if (columnIndex == 2) {
                data.get(rowIndex).mapName = (String) aValue;
            }
            if (columnIndex == 2) {
                data.get(rowIndex).position = (String) aValue;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
    class Data {
        String name;
        ImageIcon icon;
        String mapName;
        String position;

        Data(String name, ImageIcon icon, String mapName, String position) {
            this.name = name;
            this.icon = icon;
            this.mapName = mapName;
            this.position = position;
        }
    }


}
