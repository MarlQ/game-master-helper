package mquanz.gamemasterhelper;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AreaFiller {

	
	public static void floodFillImage(BufferedImage image,int x, int y, Color color) 
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void fillPaintArea(Color newColor, BufferedImage bufferedImage, int x, int y) {

		int newColorINT = newColor.getRGB();
		int[][] rgbArray = convertTo2DUsingGetRGB(bufferedImage);
		int oldColorINT = rgbArray[x][y];

		fillPaint(rgbArray, x, y, oldColorINT, newColorINT);

		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				bufferedImage.setRGB(x, y, rgbArray[x][y]);
			}
		}
	}

	private static void fillPaint(int[][] array, int x, int y, int oldColor, int newColor) {
		if (x < 0 || x >= array.length || y < 0 || y >= array[x].length)
			return;
		if (array[x][y] != oldColor)
			return;

		array[x][y] = newColor;
		
		//TODO: Optimize?
		
		fillPaint(array, x-1, y, oldColor, newColor);
		fillPaint(array, x+1, y, oldColor, newColor);
		fillPaint(array, x, y-1, oldColor, newColor);
		fillPaint(array, x, y+1, oldColor, newColor);
		
	}

	private static int[][] convertTo2DUsingGetRGB(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		int[][] result = new int[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				result[row][col] = bufferedImage.getRGB(col, row);
			}
		}
		return result;
	}

}
