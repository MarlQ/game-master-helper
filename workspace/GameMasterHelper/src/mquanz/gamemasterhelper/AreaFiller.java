package mquanz.gamemasterhelper;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

abstract class AreaFiller {

	static void floodFillImage(BufferedImage image,int x, int y, Color color)
	{
	    int srcColor = image.getRGB(x, y);
	    if(color.getRGB() == srcColor) return;
	    boolean[][] hits = new boolean[image.getHeight()][image.getWidth()];

	    Queue<Point> queue = new LinkedList<Point>();
	    queue.add(new Point(x, y));

	    while (!queue.isEmpty()) 
	    {
	        Point p = queue.remove();

	        if(floodFillImageDo(image,hits,p.x,p.y, srcColor, color.getRGB()))
	        {     
	            queue.add(new Point(p.x,p.y - 1)); 
	            queue.add(new Point(p.x,p.y + 1)); 
	            queue.add(new Point(p.x - 1,p.y)); 
	            queue.add(new Point(p.x + 1,p.y)); 
	        }
	    }
	}

	private static boolean floodFillImageDo(BufferedImage image, boolean[][] hits,int x, int y, int srcColor, int tgtColor) 
	{
	    if (y < 0) return false;
	    if (x < 0) return false;
	    if (y > image.getHeight()-1) return false;
	    if (x > image.getWidth()-1) return false;

	    if (hits[y][x]) return false;

	    if (image.getRGB(x, y)!=srcColor)
	        return false;

	    // valid, paint it

	    image.setRGB(x, y, tgtColor);
	    hits[y][x] = true;
	    return true;
	}
}
