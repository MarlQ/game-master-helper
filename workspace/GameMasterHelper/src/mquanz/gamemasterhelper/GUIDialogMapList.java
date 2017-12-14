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

public class GUIDialogMapList extends JDialog {


    JList list;
    GUIMain mainFrame;

    public GUIDialogMapList(GUIMain mainFrame) {
        super(mainFrame, "Drag Test");
        this.mainFrame = mainFrame;
        setSize(200, 150);

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);


        list = new JList<MapInformation>();
        list.setDragEnabled(true);
        list.setTransferHandler(new MyListDropHandler(list));
        list.setDropMode(DropMode.INSERT);
        new MyDragListener(list);
        DefaultListModel<MapInformation> model = new DefaultListModel<MapInformation>();
        for(MapInformation map : mainFrame.generalInformation.maps){
            model.addElement(map);
        }
        list.setModel(model);

        JButton buttonAddMap = new JButton("+");
        buttonAddMap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //TODO:
            }
        });

        JButton buttonDeleteMap = new JButton("-");
        buttonDeleteMap.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //TODO: Ask if it should delete, remove does not update
                int index = GUIDialogMapList.this.list.getSelectedIndex();
                DefaultListModel model = (DefaultListModel) GUIDialogMapList.this.list.getModel();
                model.remove(index);
                GUIDialogMapList.this.mainFrame.generalInformation.maps.remove(index);
            }
        });
        buttonDeleteMap.setEnabled(false);

        ListSelectionModel listSelectionModel = list.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if(lsm.isSelectionEmpty()){
                    buttonDeleteMap.setEnabled(false);
                }else{
                    buttonDeleteMap.setEnabled(true);
                }
            }
        });

        LH.place(0,0,1,6,1,1,"b", "w", null,this,c,list);
        LH.place(1,3,1,1,1,1,"n", "c", null,this,c,buttonAddMap);
        LH.place(1,4,1,1,1,1,"n", "c", null,this,c,buttonDeleteMap);

        setVisible(true);
        pack();
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

            if(index < dropTargetIndex){
                DefaultListModel<MapInformation>  model = (DefaultListModel) list.getModel();
                model.add(dropTargetIndex, model.getElementAt(index));
                model.remove(index);
            }else if(index > dropTargetIndex){
                DefaultListModel<MapInformation>  model = (DefaultListModel) list.getModel();
                model.add(dropTargetIndex, model.getElementAt(index));
                model.remove(index+1);

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
