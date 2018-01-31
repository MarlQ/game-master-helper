package mquanz.gamemasterhelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIDialogTypeList extends JDialog {



    JPanel panelEditType;

    void createTypeRow(ObjectType objectType, GridBagConstraints c, int rowNumber){
        JTextField textFieldName = new JTextField(objectType.typeName);
        JButton buttonEdit = new JButton("Edit");
        JButton buttonDelete = new JButton("X");


        buttonEdit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                createEditPanel(objectType, rowNumber);
            }
        });

        LH.place(0,rowNumber,1,1,0,0,"h","c",null,this,c,textFieldName);
        LH.place(1,rowNumber,1,1,0,0,"n","c",null,this,c,buttonEdit);
        LH.place(2,rowNumber,1,1,1,0,"n","c",null,this,c,buttonDelete);
    }


    void createEditPanel(ObjectType objectType, int rowNumber){
        GUIDialogTypeList.this.panelEditType = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        GUIDialogTypeList.this.panelEditType.setLayout(gbl);

        int rowCounter = 0;

        JButton buttonAddProperty = new JButton("+");


        GUIDialogTypeList.this.panelEditType.setBackground(Color.ORANGE);



        for(Property property : objectType.properties){
            createPropertyRow(property, c, rowCounter, GUIDialogTypeList.this.panelEditType);
            rowCounter++;
        }

        LH.place(0, rowCounter,1,1,0,0,"h","w",null,GUIDialogTypeList.this.panelEditType,c,buttonAddProperty);

        LH.place(0,rowNumber+1,1,rowCounter+1,0,0,"h","c",null,GUIDialogTypeList.this,c,GUIDialogTypeList.this.panelEditType);


        GUIDialogTypeList.this.revalidate();
        GUIDialogTypeList.this.repaint();
    }

    void createPropertyRow(Property property, GridBagConstraints c, int rowCounter, JPanel panel){
/**
        JTextField textFieldPropertyName = new JTextField(property.name);
        JButton textFieldTypeName = new JButton(property.typeName);

        JFormattedTextField formattedTextFieldValue;
        if(property.format != null){
            formattedTextFieldValue = new JFormattedTextField(property.format);
            formattedTextFieldValue.setValue(property.value);
        }
        else {
            formattedTextFieldValue = new JFormattedTextField();
            formattedTextFieldValue.setEditable(false);
        }



        JButton buttonDeleteProperty = new JButton("X");


        LH.place(0,rowCounter,1,1,0,0,"h","c",null,panel,c,textFieldPropertyName);
        LH.place(1,rowCounter,1,1,0,0,"n","c",null,panel,c,textFieldTypeName);
        LH.place(2,rowCounter,1,1,0,0,"n","c",null,panel,c,formattedTextFieldValue);
        LH.place(3,rowCounter,1,1,0,0,"n","c",null,panel,c,buttonDeleteProperty);
 **/

    }




    public GUIDialogTypeList(GUIMain mainFrame){
        super(mainFrame);
        this.getContentPane().setBackground(Color.RED);

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout gbl = new GridBagLayout();
        setLayout(gbl);

        int rowNumber = 0;


        for(ItemType itemType : mainFrame.campaignInformation.itemTypes){
            createTypeRow(itemType, c, rowNumber);
            rowNumber += 2;
        }

        JButton buttonAddNewType = new JButton("+");
        LH.place(0,rowNumber+1,1,1,1,1,"n","w",null,this,c,buttonAddNewType);



        setVisible(true);
        pack();

    }




}
