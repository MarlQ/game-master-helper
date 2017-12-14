package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;

public class Furniture extends JLabel {

    int posX;
    int posY;

    boolean isSelected;
    ImageIcon icon;

    public Furniture(int posX, int posY, ImageIcon icon) {
        super(icon);
        this.icon = icon;
        this.posX = posX;
        this.posY = posY;
        this.isSelected = false;
    }


    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        this.posX = x;
        this.posY = y;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (isSelected) {
            g.setColor(Color.RED);
            ((Graphics2D) g).setStroke(new BasicStroke(1));
            g.drawRect(0, 0, this.icon.getIconWidth() - 1, this.icon.getIconHeight() - 1);
        }
    }

/**
    public Furniture(String name, ImageIcon icon) {
        //TODO: type
        super(name, Color.LIGHT_GRAY, icon,null);
        this.name = name;
        this.className = "Furniture";
    }
 **/
}
