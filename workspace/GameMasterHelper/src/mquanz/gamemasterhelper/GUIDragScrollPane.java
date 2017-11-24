package mquanz.gamemasterhelper;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolTip;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class GUIDragScrollPane extends JScrollPane {
	private static final long serialVersionUID = 1L;

	GUIPopupMenu popupMenu;
	MapObjectIcon selectedIcon;
	GUIDrawingSurface objectToMove;
	ArrayList<GUIObjectEditScreen> objectEditScreenList = new ArrayList<GUIObjectEditScreen>();

	
	public boolean shiftPressed = false;
	boolean snapToGrid = GUIMain.SNAP_TO_GRID_DEFAULT;
	public int mode = GUIMain.MODE_DEFAULT;
	boolean showDistance = true;
	/*
	 * 1 == Drag Mode 2 == Pencil 3 == Line 4 == Fill
	 * 
	 */
	public int shiftLineHorizontal = 0;

	public GUIDragScrollPane(GUIDrawingSurface objectToMove) {
		super(objectToMove);
		popupMenu = new GUIPopupMenu(objectToMove);
		this.objectToMove = objectToMove;
		setPreferredSize(new Dimension(500, 500));

		ViewportDragScrollListener l = new ViewportDragScrollListener(objectToMove, false);
		JViewport gridScrollPaneViewport = getViewport();
		gridScrollPaneViewport.addMouseMotionListener(l);
		gridScrollPaneViewport.addMouseListener(l);
		gridScrollPaneViewport.addHierarchyListener(l);

	}

	class ViewportDragScrollListener extends MouseAdapter implements HierarchyListener {
		private static final int SPEED = 4;
		private static final int DELAY = 10;
		private final Cursor dc;
		private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
		private final javax.swing.Timer scroller;
		private final JComponent label;
		private final Point startPt = new Point();
		private final Point move = new Point();
		private boolean autoScroll = false;

		private int currentX, currentY, oldX, oldY;

		public ViewportDragScrollListener(JComponent comp, boolean autoScroll) {
			this.label = comp;
			this.autoScroll = autoScroll;
			this.dc = comp.getCursor();
			this.scroller = new javax.swing.Timer(DELAY, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JViewport vport = (JViewport) label.getParent();
					Point vp = vport.getViewPosition();
					vp.translate(move.x, move.y);
					label.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
				}
			});
		}

		public void hierarchyChanged(HierarchyEvent e) {
			JComponent c = (JComponent) e.getSource();
			if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && !c.isDisplayable() && autoScroll) {
				scroller.stop();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {

			
			
			
			
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				switch (mode) {
				case 1:
					// Drag Mode
					JViewport vport = (JViewport) e.getSource();
					Point pt = e.getPoint();
					int dx = startPt.x - pt.x;
					int dy = startPt.y - pt.y;
					Point vp = vport.getViewPosition();
					vp.translate(dx, dy);
					label.scrollRectToVisible(new Rectangle(vp, vport.getSize()));
					move.setLocation(SPEED * dx, SPEED * dy);
					startPt.setLocation(pt);
					break;
				case 2:
					// Pencil Tool
					currentX = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					currentY = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = currentX%GUIMain.METER;
						int modY = currentY%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							currentX += GUIMain.METER-modX;
						}
						else{
							currentX -= modX;
						}
						if(modY > GUIMain.METER/2){
							currentY += GUIMain.METER-modY;
						}
						else{
							currentY -= modY;
						}
					}
					if(shiftPressed){
						int difX = currentX-oldX;
						int difY = currentY-oldY;

						if(difX == 0&& difY == 0){
							//Do nothing
						}
						else if(shiftLineHorizontal == 2){
							currentY = oldY;
						}
						else if(shiftLineHorizontal == 1){
							currentX = oldX;
						}
						else if(difX == 0){
							currentX = oldX;
							shiftLineHorizontal = 1;
							System.out.println(1);
						}
						else if(difY == 0){
							currentY = oldY;
							shiftLineHorizontal = 2;
							System.out.println(2);
						}
						else{
							currentX = oldX;
							shiftLineHorizontal = 1;
							System.out.println(3);
						}
					}
					
					if (objectToMove.g2 != null) {
						objectToMove.g2.setPaint(objectToMove.drawingColorPrim);
						objectToMove.g2.drawLine(oldX, oldY, currentX, currentY);
						objectToMove.repaint();
						oldX = currentX;
						oldY = currentY;
					}
					break;
				case 3:
					// Line Tool
					int lineX = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int lineY = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = lineX%GUIMain.METER;
						int modY = lineY%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							lineX += GUIMain.METER-modX;
						}
						else{
							lineX -= modX;
						}
						if(modY > GUIMain.METER/2){
							lineY += GUIMain.METER-modY;
						}
						else{
							lineY -= modY;
						}
					}		
					// TODO: REDO!!!
					objectToMove.lineEndPoint = new Point(lineX,lineY);
					if(showDistance && objectToMove.lineStartPoint != null){
						int xDist = objectToMove.lineEndPoint.x-objectToMove.lineStartPoint.x;
						int yDist = objectToMove.lineEndPoint.y-objectToMove.lineStartPoint.y;
						
						int distance = (int) Math.sqrt(Math.pow(xDist,2)+Math.pow(yDist, 2));
						int distanceInMeters = distance/GUIMain.METER;

						/**
						JToolTip tp = new JToolTip();
						tp.setLocation(objectToMove.lineEndPoint);
						String tpText = Integer.toString(distanceInMeters);
						tp.setTipText(tpText);
						tp.setVisible(true);
						tp.setToolTipText(tpText);
						tp.setComponent(objectToMove);
						tp.setOpaque(true);
						tp.setBackground(Color.RED);
						**/
						
						System.out.println(distanceInMeters);
						
					}
					objectToMove.repaint();
					break;
				default:
					break;

				}
			}
			if (SwingUtilities.isRightMouseButton(e)) {
				if (mode == 2) {
					// Pencil Tool Sec
					currentX = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
					currentY = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;

					if (objectToMove.g2 != null) {
						objectToMove.g2.setPaint(objectToMove.drawingColorSeco);
						objectToMove.g2.drawLine(oldX, oldY, currentX, currentY);
						objectToMove.g2.setPaint(objectToMove.drawingColorPrim);
						objectToMove.repaint();
						oldX = currentX;
						oldY = currentY;
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				switch (mode) {
				case 1:
					// Drag Mode
					((JComponent) e.getSource()).setCursor(hc);
					startPt.setLocation(e.getPoint());
					move.setLocation(0, 0);
					if (autoScroll) {
						scroller.stop();
					}
					if (selectedIcon != null) {
						selectedIcon.isSelected = false;
						selectedIcon.repaint();
						selectedIcon = null;
					}
					break;
				case 2:	
					// Pencil Tool
					oldX = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					oldY = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = oldX%GUIMain.METER;
						int modY = oldY%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							oldX += GUIMain.METER-modX;
						}
						else{
							oldX -= modX;
						}
						if(modY > GUIMain.METER/2){
							oldY += GUIMain.METER-modY;
						}
						else{
							oldY -= modY;
						}
					}
					break;
				case 3:
					// Line Tool
					int lineX = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int lineY = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = lineX%GUIMain.METER;
						int modY = lineY%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							lineX += GUIMain.METER-modX;
						}
						else{
							lineX -= modX;
						}
						if(modY > GUIMain.METER/2){
							lineY += GUIMain.METER-modY;
						}
						else{
							lineY -= modY;
						}
					}	
					objectToMove.lineStartPoint = new Point(lineX,lineY);
					break;
				case 4:
					// Fill Tool
					int x = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int y = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					objectToMove.fillAreaWithColor(x, y);
					break;
				default:
					// TODO: ERROR
					break;

				}

			}
			if (SwingUtilities.isRightMouseButton(e)) {
				if (mode == 3) {
					// Line Tool
					objectToMove.lineStartPoint = null;
					objectToMove.lineEndPoint = null;
					objectToMove.repaint();
				} else if (mode == 2) {
					// Pencil Tool Sec
					oldX = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
					oldY = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
				} else if (mode == 1) {
					popupMenu.posX = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
					popupMenu.posY = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				shiftLineHorizontal = 0;
				int mouseX = e.getX();
				int mouseY = e.getY();
				if (mode == 1) {
					((JComponent) e.getSource()).setCursor(dc);
					if (autoScroll) {
						scroller.start();
					}
				} else if (mode == 3) {
					// Line Tool
					int lineX = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int lineY = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = lineX%GUIMain.METER;
						int modY = lineY%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							lineX += GUIMain.METER-modX;
						}
						else{
							lineX -= modX;
						}
						if(modY > GUIMain.METER/2){
							lineY += GUIMain.METER-modY;
						}
						else{
							lineY -= modY;
						}
					}	
					if (objectToMove.lineStartPoint != null && objectToMove.lineEndPoint != null) {
						objectToMove.g2.drawLine(objectToMove.lineStartPoint.x, objectToMove.lineStartPoint.y,lineX,lineY);
					}

					objectToMove.lineStartPoint = null;
					objectToMove.lineEndPoint = null;
					objectToMove.repaint();
				}
			}
			if (SwingUtilities.isRightMouseButton(e)) {
				if (mode == 1) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (mode == 1) {
					((JComponent) e.getSource()).setCursor(dc);
					move.setLocation(0, 0);
					if (autoScroll) {
						scroller.stop();
					}
				}
			}
		}
	}

	public void itemClicked(MapObjectIcon itemIcon) {
		if (selectedIcon == null) {
			itemIcon.isSelected = true;
			selectedIcon = itemIcon;
			itemIcon.repaint();
		} else if (selectedIcon != itemIcon) {
			selectedIcon.isSelected = false;
			selectedIcon.repaint();
			itemIcon.isSelected = true;
			selectedIcon = itemIcon;
			itemIcon.repaint();
		}
	}

	public void itemDoubleClicked(MapObjectIcon mapObjectIcon) {
		MapObject mapObject = mapObjectIcon.mapObject;
		if (mapObject.getClass() == MapLink.class) {
			MapLink mapLink = (MapLink) mapObjectIcon.mapObject;

			if (mapLink.map != null) {
				// TODO: Edit Screen Dialog
				objectToMove.changeMap(mapLink.map);
				getVerticalScrollBar().setValue(mapLink.linkPosX);
				getHorizontalScrollBar().setValue(mapLink.linkPosY);
			}
			// TODO: Edit Screen
		} else if (mapObject.getClass() == Item.class) {

			for (GUIObjectEditScreen editScreen : objectEditScreenList) {
				if (editScreen.mapObjectIcon == selectedIcon) {
					editScreen.toFront();
					return;
				}
			}
			GUIObjectEditScreen editScreen = new GUIObjectEditScreen(
					(JFrame) this.getParent().getParent().getParent().getParent(), mapObjectIcon, objectEditScreenList);
			objectEditScreenList.add(editScreen);

		}
	}
}