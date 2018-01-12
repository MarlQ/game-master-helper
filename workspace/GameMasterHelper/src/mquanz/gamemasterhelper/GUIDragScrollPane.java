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
     * 1 == Drag Mode 2 == Pencil 3 == Line 4 == Fill, 5 == Rectangle, 6 == Stairs
     *
     */
    int shiftLineHorizontal = 0;
    private boolean stairsClicked = false;

    GUIDragScrollPane(GUIDrawingSurface objectToMove, GUIBottomPane bottomPane) {
        super(objectToMove);
        popupMenu = new GUIPopupMenu(objectToMove);
        this.objectToMove = objectToMove;
        this.bottomPane = bottomPane;
        setPreferredSize(new Dimension(500, 500));

        ViewportDragScrollListener l = new ViewportDragScrollListener(objectToMove);
        JViewport gridScrollPaneViewport = getViewport();
        gridScrollPaneViewport.addMouseMotionListener(l);
        gridScrollPaneViewport.addMouseListener(l);
    }

    class ViewportDragScrollListener extends MouseAdapter{
        private static final int SPEED = 4;
        private final Cursor dc;
        private final Cursor hc = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        private final JComponent label;
        private final Point startPt = new Point();
        private final Point move = new Point();

        CPoint startP  = new CPoint(0,0);
        CPoint endP = new CPoint(0,0);

        private ViewportDragScrollListener(JComponent comp) {
            this.label = comp;
            this.dc = comp.getCursor();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            int x = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
            int y = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
            bottomPane.updateMouseCoordinates(x, y);

            if(mode == 6){
                //Stairs Tool
                CPoint p = new CPoint(e.getX(),e.getY());
                p.transform(e);
                if(snapToGrid){
                    p.alignToGrid();
                }
                if(stairsClicked){
                    objectToMove.stairsPoint3 = p;
                    if(shiftPressed){
                        //Makes the stairs go stairs go straight (orthogonal)
                        int dx = objectToMove.stairsPoint2.x-objectToMove.stairsPoint1.x;
                        int dy = objectToMove.stairsPoint2.y-objectToMove.stairsPoint1.y;

                        //Middle point
                        int mx = objectToMove.stairsPoint1.x+dx/2;
                        int my = objectToMove.stairsPoint1.y+dy/2;
                        Point m = new Point(mx,my);

                        double a = dy*(m.y-objectToMove.stairsPoint3.y)+dx*(m.x-objectToMove.stairsPoint3.x);
                        double b = (dx*dx+dy*dy);
                        double c = a/b;

                        double projX = objectToMove.stairsPoint3.x+c*dx;
                        double projY = objectToMove.stairsPoint3.y+c*dy;
                        objectToMove.stairsPoint3.x = (int) projX;
                        objectToMove.stairsPoint3.y = (int) projY;
                    }
                    objectToMove.repaint();
                }else{
                    objectToMove.stairsPoint2 = p;
                    objectToMove.repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            int xTrans = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
            int yTrans = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
            bottomPane.updateMouseCoordinates(xTrans, yTrans);

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
                        endP.setLocation(e.getX(), e.getY());
                        endP.transform(e);

                        if (snapToGrid) {
                            endP.alignToGrid();
                        }
                        if (shiftPressed) {
                            int difX = endP.x - startP.x;
                            int difY = endP.y - startP.y;
                            if (!(difX == 0 && difY == 0)) {
                                if (shiftLineHorizontal == 2) {
                                    endP.y = startP.y;
                                } else if (shiftLineHorizontal == 1) {
                                    endP.x = startP.x;
                                } else if (difX == 0) {
                                    endP.x = startP.x;
                                    shiftLineHorizontal = 1;
                                } else if (difY == 0) {
                                    endP.y = startP.y;
                                    shiftLineHorizontal = 2;
                                } else {
                                    endP.x = startP.x;
                                    shiftLineHorizontal = 1;
                                }
                            }
                        }

                        if (objectToMove.g2 != null) {
                            objectToMove.g2.setPaint(objectToMove.drawingColorPrim);
                            objectToMove.g2.drawLine(startP.x, startP.y, endP.x, endP.y);
                            objectToMove.repaint();
                            startP.setLocation(endP);
                        }
                        break;
                    case 3:
                    case 5:
                        // Line Tool, Rectangle Tool
                        endP.setLocation(e.getX(), e.getY());
                        endP.transform(e);
                        if (snapToGrid) {
                            endP.alignToGrid();
                        }
                        if (mode == 3) {
                            //Line Tool
                            objectToMove.lineEndPoint = endP;
                            if (objectToMove.lineStartPoint != null) {
                                int xDist = objectToMove.lineEndPoint.x - objectToMove.lineStartPoint.x;
                                int yDist = objectToMove.lineEndPoint.y - objectToMove.lineStartPoint.y;

                                int distance = (int) Math.sqrt(xDist * xDist + yDist * yDist);

                                bottomPane.setContextualInfoLineTool(distance);
                            }
                        } else if (mode == 5) {
                            //Rectangle Tool
                            objectToMove.rectangleEndPoint = endP;
                            if (objectToMove.rectangleStartPoint != null) {
                                int xDist = objectToMove.rectangleEndPoint.x - objectToMove.rectangleStartPoint.x;
                                int yDist = objectToMove.rectangleEndPoint.y - objectToMove.rectangleStartPoint.y;
                                bottomPane.setContextualInfoRectTool(xDist, yDist);
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
                    endP.setLocation(e.getX(),e.getY());
                    endP.transform(e);

                    if (snapToGrid) {
                        endP.alignToGrid();
                    }
                    if (shiftPressed) {
                        int difX = endP.x - startP.x;
                        int difY = endP.y - startP.y;
                        if (!(difX == 0 && difY == 0)) {
                            if (shiftLineHorizontal == 2) {
                                endP.y = startP.y;
                            } else if (shiftLineHorizontal == 1) {
                                endP.x = startP.x;
                            } else if (difX == 0) {
                                endP.x = startP.x;
                                shiftLineHorizontal = 1;
                            } else if (difY == 0) {
                                endP.y = startP.y;
                                shiftLineHorizontal = 2;
                            } else {
                                endP.x = startP.x;
                                shiftLineHorizontal = 1;
                            }
                        }
                    }
                    if (objectToMove.g2 != null) {
                        objectToMove.g2.setPaint(objectToMove.drawingColorSeco);
                        objectToMove.g2.drawLine(startP.x, startP.y, endP.x, endP.y);
                        objectToMove.g2.setPaint(objectToMove.drawingColorPrim);
                        objectToMove.repaint();
                        startP.setLocation(endP);
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
                        if (selectedIcon != null) {
                            selectedIcon.isSelected = false;
                            selectedIcon.repaint();
                            selectedIcon = null;
                        }
                        break;
                    case 2:
                        // Pencil Tool
                        startP.setLocation(e.getX(),e.getY());
                        startP.transform(e);

                        if (snapToGrid) {
                            startP.alignToGrid();
                        }
                        break;
                    case 3:
                    case 5:
                        // Line Tool, Rectangle Tool
                        startP.setLocation(e.getX(),e.getY());
                        startP.transform(e);

                        if (snapToGrid) {
                            startP.alignToGrid();
                        }
                        if (mode == 3) {
                            //Line Tool
                            objectToMove.lineStartPoint = startP;
                            bottomPane.setContextualInfoLineTool(0);
                        } else if (mode == 5) {
                            //Rectangle Tool
                            objectToMove.rectangleStartPoint = startP;
                        }
                        objectToMove.repaint();
                        break;
                    case 4:
                        // Fill Tool
                        int x = mouseX + ((JViewport) e.getComponent()).getViewPosition().x;
                        int y = mouseY + ((JViewport) e.getComponent()).getViewPosition().y;
                        objectToMove.fillAreaWithColor(x, y);
                        break;
                    case 6:
                        //Stairs Tool
                        CPoint p = new CPoint(e.getX(),e.getY());
                        p.transform(e);
                        if (snapToGrid) {
                            p.alignToGrid();
                        }
                        if(objectToMove.stairsPoint1 == null){
                            objectToMove.stairsPoint1 = p;
                        }
                        else if(!stairsClicked){
                            objectToMove.stairsPoint2 = p;
                            stairsClicked = true;
                        } else{
                            //Draw Outline
                            objectToMove.g2.setStroke(new BasicStroke(1));
                            //objectToMove.g2.setPaint(Color.BLUE);

                            int widthX = objectToMove.stairsPoint2.x-objectToMove.stairsPoint1.x;
                            int widthY = objectToMove.stairsPoint2.y-objectToMove.stairsPoint1.y;
                            Point p3 = new Point(objectToMove.stairsPoint3.x+widthX/2, objectToMove.stairsPoint3.y+widthY/2);
                            Point p4 = new Point(objectToMove.stairsPoint3.x-widthX/2, objectToMove.stairsPoint3.y-widthY/2);

                            objectToMove.g2.drawLine(objectToMove.stairsPoint1.x, objectToMove.stairsPoint1.y, objectToMove.stairsPoint2.x,  objectToMove.stairsPoint2.y);
                            objectToMove.g2.drawLine(objectToMove.stairsPoint1.x,objectToMove.stairsPoint1.y,p4.x,p4.y);
                            objectToMove.g2.drawLine(objectToMove.stairsPoint2.x,objectToMove.stairsPoint2.y,p3.x,p3.y);
                            objectToMove.g2.drawLine(p3.x,p3.y,p4.x,p4.y);

                            //Draw Steps
                            double length = (objectToMove.stairsPoint2.x-p3.x)*(objectToMove.stairsPoint2.x-p3.x)+(objectToMove.stairsPoint2.y-p3.y)*(objectToMove.stairsPoint2.y-p3.y);
                            length = Math.sqrt(length);

                            double staircount = length/GUIMain.STAIR_TOOL_STEP_WIDTH;
                            double stairlengthX = (p3.x-objectToMove.stairsPoint2.x)/staircount;
                            double stairlengthY = (p3.y-objectToMove.stairsPoint2.y)/staircount;

                            for(int i = 0; i < staircount; i++){
                                objectToMove.g2.drawLine(objectToMove.stairsPoint2.x+ (int) (i*stairlengthX), objectToMove.stairsPoint2.y+ (int) (i*stairlengthY), objectToMove.stairsPoint1.x+ (int) (i*stairlengthX), objectToMove.stairsPoint1.y+(int) (i*stairlengthY));
                            }

                            objectToMove.g2.setStroke(objectToMove.drawingStroke);
                            objectToMove.stairsPoint1 = p3;
                            objectToMove.stairsPoint2 = p4;
                            objectToMove.stairsPoint3 = null;
                        }
                        objectToMove.repaint();
                }

            }
            if (SwingUtilities.isRightMouseButton(e)) {
                switch(mode){
                    case 1:
                        //Drag Tool
                        popupMenu.posX = e.getX() + ((JViewport) e.getComponent()).getViewPosition().x;
                        popupMenu.posY = e.getY() + ((JViewport) e.getComponent()).getViewPosition().y;
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        break;
                    case 2:
                        //Pencil Tool
                        startP.setLocation(e.getX(),e.getY());
                        startP.transform(e);

                        if (snapToGrid) {
                            startP.alignToGrid();
                        }
                        break;
                    case 3:
                        //Line Tool
                        objectToMove.lineStartPoint = null;
                        objectToMove.lineEndPoint = null;
                        bottomPane.resetContextualInfoLineTool();
                        objectToMove.repaint();
                        break;
                    case 5:
                        //Rectangle Tool
                        objectToMove.rectangleStartPoint = null;
                        objectToMove.rectangleEndPoint = null;
                        objectToMove.repaint();
                        break;
                    case 6:
                        //Stairs Tool
                        objectToMove.stairsPoint1 = null;
                        objectToMove.stairsPoint2 = null;
                        objectToMove.stairsPoint3 = null;
                        stairsClicked = false;
                        objectToMove.repaint();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            shiftLineHorizontal = 0;
            if (SwingUtilities.isLeftMouseButton(e)) {
                switch (mode) {
                    case 1:
                        //Drag Mode
                        ((JComponent) e.getSource()).setCursor(dc);
                        break;
                    case 3:
                        //Line Tool
                        endP.setLocation(e.getX(), e.getY());
                        endP.transform(e);
                        if (snapToGrid) {
                            endP.alignToGrid();
                        }
                        if (objectToMove.lineStartPoint != null && objectToMove.lineEndPoint != null) {
                            objectToMove.g2.drawLine(objectToMove.lineStartPoint.x, objectToMove.lineStartPoint.y, endP.x, endP.y);
                        }
                        bottomPane.resetContextualInfoLineTool();

                        objectToMove.lineStartPoint = null;
                        objectToMove.lineEndPoint = null;
                        objectToMove.repaint();
                        break;
                    case 5:
                        //Rectangle Tool
                        if (objectToMove.rectangleStartPoint != null && objectToMove.rectangleEndPoint != null) {
                            int rectWidth = objectToMove.rectangleEndPoint.x - objectToMove.rectangleStartPoint.x;
                            int rectHeight = objectToMove.rectangleEndPoint.y - objectToMove.rectangleStartPoint.y;

                            if (rectHeight < 0 && rectWidth < 0) {
                                objectToMove.g2.drawRect(objectToMove.rectangleEndPoint.x, objectToMove.rectangleEndPoint.y, Math.abs(rectWidth), Math.abs(rectHeight));
                            } else if (rectWidth < 0) {
                                objectToMove.g2.drawRect(objectToMove.rectangleStartPoint.x + rectWidth, objectToMove.rectangleStartPoint.y, Math.abs(rectWidth), rectHeight);
                            } else if (rectHeight < 0) {
                                objectToMove.g2.drawRect(objectToMove.rectangleStartPoint.x, objectToMove.rectangleStartPoint.y + rectHeight, rectWidth, Math.abs(rectHeight));
                            } else {
                                objectToMove.g2.drawRect(objectToMove.rectangleStartPoint.x, objectToMove.rectangleStartPoint.y, rectWidth, rectHeight);
                            }
                        }
                        objectToMove.rectangleStartPoint = null;
                        objectToMove.rectangleEndPoint = null;
                        objectToMove.repaint();
                        break;
                    case 6:
                        //Stairs Tool
                        //TODO:

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
                }
            }
        }
    }
    class CPoint extends Point{
        CPoint(int x, int y){
            super(x,y);
        }

        void transform(MouseEvent e){
            this.x += ((JViewport) e.getComponent()).getViewPosition().x;
            this.y += ((JViewport) e.getComponent()).getViewPosition().y;
        }
        void alignToGrid(){
            int modX = this.x % GUIMain.METER;
            int modY = this.y % GUIMain.METER;
            if (modX > GUIMain.METER / 2) {
                this.x += GUIMain.METER - modX;
            } else {
                this.x -= modX;
            }
            if (modY > GUIMain.METER / 2) {
                this.y += GUIMain.METER - modY;
            } else {
                this.y -= modY;
            }
        }

    }
/**
    private Point transform(MouseEvent e) {
        Point p = e.getPoint();
        int xTrans = p.x + ((JViewport) e.getComponent()).getViewPosition().x;
        int yTrans = p.y + ((JViewport) e.getComponent()).getViewPosition().y;
        return new Point(xTrans, yTrans);
    }

    private Point transformCoordinatesGrid(Point point) {
        int newX = point.x;
        int newY = point.y;
        int modX = point.x % GUIMain.METER;
        int modY = point.y % GUIMain.METER;
        if (modX > GUIMain.METER / 2) {
            newX += GUIMain.METER - modX;
        } else {
            newX -= modX;
        }
        if (modY > GUIMain.METER / 2) {
            newY += GUIMain.METER - modY;
        } else {
            newY -= modY;
        }
        return new Point(newX, newY);
    }
 **/

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

        for (GUIObjectEditScreen editScreen : objectEditScreenList) {
            if (editScreen.mapObjectIcon == selectedIcon) {
                editScreen.toFront();
                return;
            }
        }
        GUIObjectEditScreen editScreen = new GUIObjectEditScreen(
                (GUIMain) this.getParent().getParent().getParent().getParent(), mapObjectIcon, objectEditScreenList);
        editScreen.setLocationRelativeTo(this.getParent().getParent().getParent().getParent());
        objectEditScreenList.add(editScreen);
        /**
        if (mapObject.getClass() == MapLink.class) {
            MapLink mapLink = (MapLink) mapObjectIcon.mapObject;
            if (mapLink.map != null) {
                // TODO: Edit Screen Dialog
                objectToMove.changeMap(mapLink.map);
                getVerticalScrollBar().setValue(mapLink.linkPosX);
                getHorizontalScrollBar().setValue(mapLink.linkPosY);
            }
            // TODO: Edit Screen
        }**/
    }
}