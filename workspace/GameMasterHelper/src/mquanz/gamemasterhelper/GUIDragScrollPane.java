package mquanz.gamemasterhelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class GUIDragScrollPane extends JScrollPane {
	private static final long serialVersionUID = 1L;

	private GUIPopupMenu popupMenu;
	MapObjectIcon selectedIcon;
	GUIDrawingSurface objectToMove;
	ArrayList<GUIObjectEditScreen> objectEditScreenList = new ArrayList<GUIObjectEditScreen>();

	private GUIBottomPane bottomPane;
	
	boolean shiftPressed = false;
	boolean snapToGrid = GUIMain.SNAP_TO_GRID_DEFAULT;
	int mode = GUIMain.MODE_DEFAULT;
	/*
	 * 1 == Drag Mode 2 == Pencil 3 == Line 4 == Fill
	 * 
	 */
	int shiftLineHorizontal = 0;

	GUIDragScrollPane(GUIDrawingSurface objectToMove, GUIBottomPane bottomPane) {
		super(objectToMove);
		popupMenu = new GUIPopupMenu(objectToMove);
		this.objectToMove = objectToMove;
		this.bottomPane = bottomPane;
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

		private ViewportDragScrollListener(JComponent comp, boolean autoScroll) {
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
			int x = e.getX()+ ((JViewport) e.getComponent()).getViewPosition().x;
			int y = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
			bottomPane.updateMouseCoordinates(x,y);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int xTrans = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
			int yTrans = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
			bottomPane.updateMouseCoordinates(xTrans,yTrans);

			if (SwingUtilities.isLeftMouseButton(e)) {
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
					currentX = xTrans;
					currentY = yTrans;
					
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



						if(!(difX == 0 && difY == 0)){
							if(shiftLineHorizontal == 2){
                                currentY = oldY;
                            }
                            else if(shiftLineHorizontal == 1){
                                currentX = oldX;
                            }
                            else if(difX == 0){
                                currentX = oldX;
                                shiftLineHorizontal = 1;
                            }
                            else if(difY == 0){
                                currentY = oldY;
                                shiftLineHorizontal = 2;
                            }
                            else{
                                currentX = oldX;
                                shiftLineHorizontal = 1;
                            }
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
				case 3: case 5:
					// Line Tool, Rectangle Tool
					if(snapToGrid){
						int modX = xTrans%GUIMain.METER;
						int modY = yTrans%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							xTrans += GUIMain.METER-modX;
						}
						else{
							xTrans -= modX;
						}
						if(modY > GUIMain.METER/2){
							yTrans += GUIMain.METER-modY;
						}
						else{
							yTrans -= modY;
						}
					}
					if(mode == 3){
						//Line Tool
						if(objectToMove.lineEndPoint == null){
							objectToMove.lineEndPoint = new Point(xTrans,yTrans);
						}
						else{
							objectToMove.lineEndPoint.setLocation(xTrans,yTrans);
						}
						if(objectToMove.lineStartPoint != null){
							int xDist = objectToMove.lineEndPoint.x-objectToMove.lineStartPoint.x;
							int yDist = objectToMove.lineEndPoint.y-objectToMove.lineStartPoint.y;

							int distance = (int) Math.sqrt(xDist*xDist+yDist*yDist);

							bottomPane.setLineLength(distance);
						}
					}
					else if(mode == 5){
						//Rectangle Tool
						if(objectToMove.rectangleEndPoint == null){
							objectToMove.rectangleEndPoint = new Point(xTrans,yTrans);
						}
						else{
							objectToMove.rectangleEndPoint.setLocation(xTrans,yTrans);
						}

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
					currentX = xTrans;
					currentY = yTrans;

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
				case 3: case 5:
					// Line Tool, Rectangle Tool
					int startX = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int startY = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = startX%GUIMain.METER;
						int modY = startY%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							startX += GUIMain.METER-modX;
						}
						else{
							startX -= modX;
						}
						if(modY > GUIMain.METER/2){
							startY += GUIMain.METER-modY;
						}
						else{
							startY -= modY;
						}
					}
					if(mode == 3){
						//Line Tool
						objectToMove.lineStartPoint = new Point(startX,startY);
						bottomPane.setLineLength(0);
					}
					else if(mode == 5){
						//Rectangle Tool
						objectToMove.rectangleStartPoint = new Point(startX,startY);
					}
					objectToMove.repaint();

					break;
				case 4:
					// Fill Tool
					int x = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int y = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					objectToMove.fillAreaWithColor(x, y);
					break;
				}

			}
			if (SwingUtilities.isRightMouseButton(e)) {
				if(mode == 5){
					//Rectangle Tool
					objectToMove.rectangleStartPoint = null;
					objectToMove.rectangleEndPoint = null;
					objectToMove.repaint();
				}
				else if (mode == 3) {
					// Line Tool
					objectToMove.lineStartPoint = null;
					objectToMove.lineEndPoint = null;
					bottomPane.nullLineLength();
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
				} else if (mode == 3 || mode == 5) {
					// Line Tool, Rectangle Tool
					int xTrans = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
					int yTrans = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
					
					if(snapToGrid){
						int modX = xTrans%GUIMain.METER;
						int modY = yTrans%GUIMain.METER;
						
						if(modX > GUIMain.METER/2){
							xTrans += GUIMain.METER-modX;
						}
						else{
							xTrans -= modX;
						}
						if(modY > GUIMain.METER/2){
							yTrans += GUIMain.METER-modY;
						}
						else{
							yTrans -= modY;
						}
					}
					if(mode == 3){
						//Line Tool
						if (objectToMove.lineStartPoint != null && objectToMove.lineEndPoint != null) {
							objectToMove.g2.drawLine(objectToMove.lineStartPoint.x, objectToMove.lineStartPoint.y,xTrans,yTrans);
						}
						bottomPane.nullLineLength();

						objectToMove.lineStartPoint = null;
						objectToMove.lineEndPoint = null;
					}
					else if(mode == 5){
						//Rectangle Tool
						if (objectToMove.rectangleStartPoint != null && objectToMove.rectangleEndPoint != null) {
							int rectWidth = objectToMove.rectangleEndPoint.x-objectToMove.rectangleStartPoint.x;
							int rectHeight = objectToMove.rectangleEndPoint.y-objectToMove.rectangleStartPoint.y;

							if(rectHeight < 0 && rectWidth < 0){
								objectToMove.g2.drawRect(objectToMove.rectangleEndPoint.x, objectToMove.rectangleEndPoint.y,Math.abs(rectWidth),Math.abs(rectHeight));
							}
							else if(rectWidth < 0){
								objectToMove.g2.drawRect(objectToMove.rectangleStartPoint.x+rectWidth, objectToMove.rectangleStartPoint.y,Math.abs(rectWidth),rectHeight);
							}
							else if(rectHeight < 0 ){
								objectToMove.g2.drawRect(objectToMove.rectangleStartPoint.x, objectToMove.rectangleStartPoint.y+rectHeight,rectWidth,Math.abs(rectHeight));
							}
							else {
								objectToMove.g2.drawRect(objectToMove.rectangleStartPoint.x, objectToMove.rectangleStartPoint.y,rectWidth,rectHeight);
							}
						}
						objectToMove.rectangleStartPoint = null;
						objectToMove.rectangleEndPoint = null;
					}

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

	void itemClicked(MapObjectIcon itemIcon) {
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

	void itemDoubleClicked(MapObjectIcon mapObjectIcon) {
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
		} else {

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