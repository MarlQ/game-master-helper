package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;

public class MapEntity extends JLabel{

    private int posX, posY;
    private ImageIcon icon;
    String name;
    boolean showNameOnMouseOver;
    Color nameColor;
    boolean isSelected;
    MapInformation map;

    //Creation
    public MapEntity(int posX, int posY, ImageIcon icon, String name, boolean showNameOnMouseOver, Color nameColor, MapInformation map) {
        super();
        if(icon == null){
            this.icon = MissingIcon.createIcon();
        }
        else this.icon = icon;
        this.setIcon(this.icon);
        this.posX = posX;
        this.posY = posY;
        this.name = name;
        this.showNameOnMouseOver = showNameOnMouseOver;
        this.nameColor = nameColor;
        this.isSelected = false;
        this.map = map;

        Dimension size = this.getPreferredSize();
        setBounds(this.posX, this.posY, size.width, size.height);
    }

    public static MapEntity createBackgroundEntityFromTemplate(BackgroundEntityTemplate template, int posX, int posY, MapInformation map){
        return new MapEntity(posX, posY, template.icon, template.name, false, Color.WHITE, map);
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
    public void setIcon(ImageIcon icon){
        if(icon == null){
            this.icon = MissingIcon.createIcon();
        }
        else this.icon = icon;
        Dimension size = this.getPreferredSize();
        setBounds(this.posX, this.posY, size.width, size.height);
    }
    public ImageIcon getIcon(){
        return this.icon;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

}
