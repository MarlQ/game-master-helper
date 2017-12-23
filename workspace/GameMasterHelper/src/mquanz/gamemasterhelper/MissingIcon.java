package mquanz.gamemasterhelper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class MissingIcon {
	private static final long serialVersionUID = 1L;

    private static int width = 32;
    private static int height = 32;

    private static BasicStroke stroke = new BasicStroke(4);

    public static ImageIcon createIcon() {
        ImageIcon icon = new ImageIcon();


        BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) (image.getGraphics()).create();


        //g2d.setColor(Color.WHITE);
        g2d.fillRect(1 ,1,width -2 ,height -2);

        g2d.setColor(Color.BLACK);
        g2d.drawRect(1 , 1,width -2 ,height -2);

        g2d.setColor(Color.RED);

        g2d.setStroke(stroke);
        g2d.drawLine(10, 10, width -10, height -10);
        g2d.drawLine(10, height -10, width -10,  10);

        g2d.dispose();

        icon.setImage(image);
        return icon;
    }
}