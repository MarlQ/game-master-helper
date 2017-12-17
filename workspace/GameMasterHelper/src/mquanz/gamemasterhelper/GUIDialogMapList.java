package mquanz.gamemasterhelper;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GUIDialogMapList extends JDialog {


    JList list;
    GUIMain mainFrame;

    public GUIDialogMapList(GUIMain mainFrame) {
        super(mainFrame, "Maps");

        this.mainFrame = mainFrame;
        setSize(200, 150);
        Point Location = mainFrame.topPane.buttonOpenMapList.getLocationOnScreen();
        Location.x += mainFrame.topPane.buttonOpenMapList.getWidth();

        setLocation(Location);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);


        list = new JList<MapInformation>();
        list.setDragEnabled(true);
        list.setTransferHandler(new MyListDropHandler(list));
        list.setDropMode(DropMode.INSERT);
        new MyDragListener(list);
        DefaultListModel<MapInformation> model = new DefaultListModel<MapInformation>();
        for (MapInformation map : mainFrame.generalInformation.maps) {
            model.addElement(map);
        }
        list.setModel(model);

        JButton buttonAddMap = new JButton("+");
        buttonAddMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createMapDialog();
            }
        });

        JButton buttonDeleteMap = new JButton("-");
        buttonDeleteMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MapInformation map = (MapInformation)GUIDialogMapList.this.list.getSelectedValue();
                if(map.itemIcons.isEmpty()){
                    mainFrame.mapController.deleteMap(map);
                }
                else{
                    deleteMapDialog(map);
                }
            }
        });
        buttonDeleteMap.setEnabled(false);

        ListSelectionModel listSelectionModel = list.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                if (lsm.isSelectionEmpty()) {
                    buttonDeleteMap.setEnabled(false);
                } else {
                    buttonDeleteMap.setEnabled(true);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(list);
        LH.place(0, 0, 1, 6, 1, 1, "b", "w", null, this, c, scrollPane);
        LH.place(1, 3, 1, 1, 1, 1, "n", "c", null, this, c, buttonAddMap);
        LH.place(1, 4, 1, 1, 1, 1, "n", "c", null, this, c, buttonDeleteMap);

        setVisible(true);
        pack();
    }

    void deleteMapDialog(MapInformation map){
        String[] options = {"Yes","Cancel"};

        int n = JOptionPane.showOptionDialog(GUIDialogMapList.this,
                "This map contains items which may get deleted. Delete anyway?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]); //default button title

        if(n == JOptionPane.YES_OPTION){
            mainFrame.mapController.deleteMap(map);
        }

    }

    void createMapDialog() {
        JDialog dialogCreateMap = new JDialog(GUIDialogMapList.this, "new Map");
        dialogCreateMap.setLocationRelativeTo(this);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        dialogCreateMap.setLayout(gbl);

        JTextField textFieldNameLabel = new JTextField("Name:");
        JTextField textFieldSizeXLabel = new JTextField("X:");
        JTextField textFieldSizeYLabel = new JTextField("Y:");

        //TODO: Handling for problematic map name
        JFormattedTextField textFieldName = new JFormattedTextField(mainFrame.mapController.standardMapName);

        JFormattedTextField textFieldSizeX = new JFormattedTextField(mainFrame.mapController.standardMapSizeX);
        textFieldSizeX.addPropertyChangeListener("value",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                int value = (int) evt.getNewValue();
                if(value <= 0){
                    textFieldSizeX.setValue(evt.getOldValue());
                }
            }
        });
        JFormattedTextField textFieldSizeY = new JFormattedTextField(mainFrame.mapController.standardMapSizeY);
        textFieldSizeY.addPropertyChangeListener("value",new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                int value = (int) evt.getNewValue();
                if(value <= 0){
                    textFieldSizeY.setValue(evt.getOldValue());
                }
            }
        });

        JButton buttonCreateMap = new JButton("Create");
        buttonCreateMap.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainFrame.mapController.createMap((String) textFieldName.getValue(), new Dimension((int) textFieldSizeX.getValue(), (int) textFieldSizeY.getValue()));
                dialogCreateMap.dispose();
            }
        });
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialogCreateMap.dispose();
            }
        });
        LH.place(0,0,1,1,1,1,"n","w",null,dialogCreateMap,c,textFieldNameLabel);
        LH.place(1,0,1,1,1,1,"h","w",null,dialogCreateMap,c,textFieldName);
        LH.place(0,1,1,1,1,1,"n","w",null,dialogCreateMap,c,textFieldSizeXLabel);
        LH.place(1,1,1,1,1,1,"h","w",null,dialogCreateMap,c,textFieldSizeX);
        LH.place(0,2,1,1,1,1,"n","w",null,dialogCreateMap,c,textFieldSizeYLabel);
        LH.place(1,2,1,1,1,1,"h","w",null,dialogCreateMap,c,textFieldSizeY);

        LH.place(0,3,1,1,1,1,"n","w",null,dialogCreateMap,c,buttonCreateMap);
        LH.place(2,3,1,1,1,1,"n","w",null,dialogCreateMap,c,buttonCancel);
        dialogCreateMap.setVisible(true);
        dialogCreateMap.pack();
    }


    class MyDragListener implements DragSourceListener, DragGestureListener {
        JList list;

        DragSource ds = new DragSource();

        public MyDragListener(JList list) {
            this.list = list;
            DragGestureRecognizer dgr = ds.createDefaultDragGestureRecognizer(list,
                    DnDConstants.ACTION_MOVE, this);

        }

        public void dragGestureRecognized(DragGestureEvent dge) {
            StringSelection transferable = new StringSelection(Integer.toString(list.getSelectedIndex()));
            ds.startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
        }

        public void dragEnter(DragSourceDragEvent dsde) {
        }

        public void dragExit(DragSourceEvent dse) {
        }

        public void dragOver(DragSourceDragEvent dsde) {
        }

        public void dragDropEnd(DragSourceDropEvent dsde) {
            if (dsde.getDropSuccess()) {
                System.out.println("Succeeded");
            } else {
                System.out.println("Failed");
            }
        }

        public void dropActionChanged(DragSourceDragEvent dsde) {
        }
    }

    class MyListDropHandler extends TransferHandler {
        JList list;

        public MyListDropHandler(JList list) {
            this.list = list;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return false;
            }
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            if (dl.getIndex() == -1) {
                return false;
            } else {
                return true;
            }
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            if (!canImport(support)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            String indexString;
            try {
                indexString = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            int index = Integer.parseInt(indexString);
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int dropTargetIndex = dl.getIndex();

            System.out.println("Source: " + index);
            System.out.println("Target: " + dropTargetIndex);

            if (index < dropTargetIndex) {
                DefaultListModel<MapInformation> model = (DefaultListModel) list.getModel();
                model.add(dropTargetIndex, model.getElementAt(index));
                model.remove(index);
            } else if (index > dropTargetIndex) {
                DefaultListModel<MapInformation> model = (DefaultListModel) list.getModel();
                model.add(dropTargetIndex, model.getElementAt(index));
                model.remove(index + 1);

            }

            list.revalidate();
            list.repaint();

            return true;
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {

            //list.remove(source);
            System.out.println("ExportDone" + source);
            // Here you need to decide how to handle the completion of the transfer,
            // should you remove the item from the list or not...
        }
    }
}

