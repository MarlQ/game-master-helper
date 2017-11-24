package mquanz.gamemasterhelper;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Scrollable;



public class GUIDrawingSurface extends JPanel implements Scrollable{
	private static final long serialVersionUID = 1L;

	MapInformation mapInformation;
	GeneralInformation generalInformation;
	ComponentMover componentMover;
	
	public Graphics2D g2;
	public Color drawingColorPrim = GUIMain.COLOR_PRIM_DEFAULT;
	public Color drawingColorSeco = GUIMain.COLOR_SEC_DEFAULT;
	private BasicStroke drawingStroke = new BasicStroke(GUIMain.STROKE_INIT);
	
	GUIDragScrollPane dragScrollPane;
	public Point lineStartPoint, lineEndPoint;
	
	boolean drawGrid = GUIMain.DRAW_GRID_DEFAULT;
	

	public GUIDrawingSurface(MapInformation mapInformation, GeneralInformation generalInformation) {
		setLayout(null);
		this.setPreferredSize(mapInformation.mapSize);
		this.generalInformation = generalInformation;
		setBackground(Color.WHITE);
		changeMap(mapInformation);
	}
	
	
	public void addNewIcon(int posX, int posY, ObjectType objectType){
		MapObjectIcon mapObjectIcon = null;
		//TODO: description and stuff properly
		
		if(objectType.getClass() == NpcType.class){
			mapObjectIcon = this.mapInformation.newNpc(posX, posY, "New Npc" , (NpcType)objectType);
		}
		else if(objectType.getClass() == ItemType.class){
			mapObjectIcon = this.mapInformation.newItem(posX, posY, "New Item", "", (ItemType)objectType);
		}
		else if(objectType.getClass() == MapLinkType.class){
			mapObjectIcon = this.mapInformation.newMapLink(posX, posY, "New Map Link", (MapLinkType)objectType, 0, 0, null );
		}
		if(mapObjectIcon != null){
			add(mapObjectIcon);
			Dimension size = mapObjectIcon.getPreferredSize();
			mapObjectIcon.setLocation(mapObjectIcon.posX, mapObjectIcon.posY);
			
			mapObjectIcon.setBounds(mapObjectIcon.posX, mapObjectIcon.posY, size.width, size.height);
			mapObjectIcon.repaint();

			componentMover.registerComponent(mapObjectIcon);
		}
	}
	
	public void setDrawingColorPrim(Color color){
		this.drawingColorPrim = color;
		g2.setPaint(drawingColorPrim);
	}
	public void setDrawingColorSeco(Color color){
		this.drawingColorSeco = color;
	}
	public void setDrawingStroke(int size){
		if(g2 == null) return;
		if(size < GUIMain.STROKE_MIN || size > GUIMain.STROKE_MAX) return;
		this.drawingStroke = new BasicStroke(size);
		g2.setStroke(drawingStroke);
	}
	
	public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        

        
        if(mapInformation.drawingImage == null){
        	mapInformation.drawingImage = new BufferedImage(getSize().width,getSize().height, ColorSpace.TYPE_RGB);
            g2 = (Graphics2D) mapInformation.drawingImage.getGraphics();
            if(GUIMain.ALLOW_ANTIALIASING){
            	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
            g2.setStroke(drawingStroke);
            clearDrawing();
        }
        g.drawImage(mapInformation.drawingImage, 0, 0, null);
        if(lineStartPoint != null && lineEndPoint != null){
        	((Graphics2D) g).setStroke(drawingStroke);
        	((Graphics2D) g).setPaint(drawingColorPrim);
        	g.drawLine(lineStartPoint.x, lineStartPoint.y, lineEndPoint.x,  lineEndPoint.y);
        }
        if(drawGrid){
        	((Graphics2D) g).setStroke(new BasicStroke(GUIMain.GRID_WIDTH_DEFAULT));
            ((Graphics2D) g).setPaint(GUIMain.COLOR_GRID_DEFAULT);
            
            int mapWidth = mapInformation.mapSize.width;
            int mapHeight = mapInformation.mapSize.height;
            
            for(int i = 0; i < mapWidth; i += GUIMain.METER){
            	g.drawLine(i, 0, i, mapHeight);
            }
            for(int i = 0; i < mapHeight; i += GUIMain.METER){
            	g.drawLine(0, i, mapWidth, i);
            }
            
            ((Graphics2D) g).setStroke(drawingStroke);
        	((Graphics2D) g).setPaint(drawingColorPrim);
        }
    }
	public void clearDrawing(){
		g2.setPaint(GUIMain.COLOR_BACKGROUND_DEFAULT);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(drawingColorPrim);
		repaint();
	}
	public void fillAreaWithColor(int x, int y){
		AreaFiller.floodFillImage(mapInformation.drawingImage, x, y, drawingColorPrim);
		repaint();	
	}

	public void changeMap(MapInformation mapInformation){
		
		if(this.mapInformation != mapInformation){
			if(dragScrollPane != null){
				if(dragScrollPane.selectedIcon != null){
					dragScrollPane.selectedIcon.isSelected = false;
					dragScrollPane.selectedIcon.repaint();
					dragScrollPane.selectedIcon = null;
				}
			}
		if(this.mapInformation != null){	
			for (MapObjectIcon itemIcon : this.mapInformation.itemIcons) {
				remove(itemIcon);
			}
			if(mapInformation.drawingImage != null){
				g2 = (Graphics2D) mapInformation.drawingImage.getGraphics();
				g2.setPaint(drawingColorPrim);
			}
			this.setMaximumSize(mapInformation.mapSize);
			this.setPreferredSize(mapInformation.mapSize);
			revalidate();
			repaint();	
		}
		this.mapInformation = mapInformation;
		componentMover = new ComponentMover(this);

		for (MapObjectIcon itemIcon : mapInformation.itemIcons) {

			add(itemIcon);
			itemIcon.setLocation(itemIcon.posX, itemIcon.posY);
			Dimension size = itemIcon.getPreferredSize();
			itemIcon.setBounds(itemIcon.posX, itemIcon.posY, size.width, size.height);
			itemIcon.repaint();

			componentMover.registerComponent(itemIcon);

		}
		}
	}
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean getScrollableTracksViewportHeight() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean getScrollableTracksViewportWidth() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		// TODO Auto-generated method stub
		return 0;
	}

}

	// setBounds(1, 1, 500, 500);

