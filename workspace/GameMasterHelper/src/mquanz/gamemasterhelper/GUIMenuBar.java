package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIMenuBar extends JMenuBar {

    private GUIMain mainFrame;

    GUIMenuBar(GUIMain mainFrame) {
        super();
        this.mainFrame = mainFrame;

        JMenu file = new JMenu("File");
        JMenu map = new JMenu("Map");

        JMenuItem clearMap = new JMenuItem("Clear Map");
        clearMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIMenuBar.this.mainFrame.drawingSurface.clearMap();
            }
        });

        JMenuItem load = new JMenuItem("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIMenuBar.this.mainFrame.loadGame();
            }
        });

        JMenuItem save = new JMenuItem("Save");
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIMenuBar.this.mainFrame.saveGame();
            }
        });

        JMenuItem saveToImage = new JMenuItem("Save to Image");
        saveToImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GUIMenuBar.this.mainFrame.saveToImage();
            }
        });
        JMenuItem itemList = new JMenuItem("Item List");
        itemList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GUIDialogObjectList(GUIMenuBar.this.mainFrame);
            }
        });

        JMenuItem itemTypeList = new JMenuItem("Item Types");
        itemTypeList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GUIDialogTypeList(GUIMenuBar.this.mainFrame);
            }
        });
        file.add(load);
        file.add(save);

        map.add(clearMap);
        map.add(saveToImage);
        map.add(itemList);
        map.add(itemTypeList);

        add(file);
        add(map);
    }
}
