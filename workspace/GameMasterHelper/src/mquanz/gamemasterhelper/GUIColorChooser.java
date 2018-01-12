package mquanz.gamemasterhelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIColorChooser extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JButton buttonsColorChooser[] = new JButton[GUIMain.COLOR_CHOOSER_COLOR_COUNT];
    private JButton buttonColorChooserPrim;
    private JButton buttonColorChooserSeco;

    private GUISidePane parentFrame;

    private boolean isPrimSelected = true;

    GUIColorChooser(GUISidePane parentFrame) {
        super();
        this.parentFrame = parentFrame;

        setOpaque(true);

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout gridbagLayout1 = new GridBagLayout();
        GridBagLayout gridbagLayout2 = new GridBagLayout();

        setLayout(gridbagLayout1);

        JPanel panelTop = new JPanel();
        panelTop.setLayout(gridbagLayout2);

        buttonColorChooserPrim = new JButton();
        buttonColorChooserPrim.addActionListener(this);
        buttonColorChooserPrim.setBackground(Color.BLACK);
        buttonColorChooserPrim.setPreferredSize(new Dimension(30, 30));

        buttonColorChooserSeco = new JButton();
        buttonColorChooserSeco.addActionListener(this);
        buttonColorChooserSeco.setBackground(Color.WHITE);
        buttonColorChooserSeco.setPreferredSize(new Dimension(30, 30));

        JButton buttonCustomColor = new JButton(CampaignInformation.createImageIcon("res/gui/color_custom.png"));
        buttonCustomColor.setPreferredSize(new Dimension(20, 20));
        buttonCustomColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCustomColorDialog();
            }
        });
        JButton buttonEditPalette = new JButton(CampaignInformation.createImageIcon("res/gui/color_palette"));
        buttonEditPalette.setPreferredSize(new Dimension(20, 20));
        buttonEditPalette.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditPaletteDialog();
            }
        });

        LH.place(0, 0, 1, 1, 1, 1, "n", "c", null, panelTop, c, buttonCustomColor);
        LH.place(1, 0, 1, 1, 1, 1, "n", "c", null, panelTop, c, buttonColorChooserPrim);
        LH.place(2, 0, 1, 1, 1, 1, "n", "c", null, panelTop, c, buttonColorChooserSeco);
        LH.place(3, 0, 1, 1, 1, 1, "n", "c", null, panelTop, c, buttonEditPalette);

        LH.place(0, 0, GUIMain.COLORS_PER_ROW + 2, 1, 1, 1, "n", "c", null, this, c, panelTop);

        c.gridwidth = 1;
        c.gridy = 1;

        for (int i = 0; i < GUIMain.COLOR_CHOOSER_COLOR_COUNT; i++) {
            buttonsColorChooser[i] = new JButton();
            buttonsColorChooser[i].addActionListener(this);

            if (i < parentFrame.parentFrame.campaignInformation.colorChooserPalette.size()) {
                buttonsColorChooser[i].setBackground(parentFrame.parentFrame.campaignInformation.colorChooserPalette.get(i));
            } else {
                buttonsColorChooser[i].setBackground(Color.WHITE);
                //TODO: Report Bug
            }
            buttonsColorChooser[i].setPreferredSize(new Dimension(20, 20));
            add(buttonsColorChooser[i], c);
            c.gridx++;
            if (Math.floorMod(i + 1, GUIMain.COLORS_PER_ROW) == 0) {
                c.gridx = 0;
                c.gridy++;
            }
        }
    }
    private JButton buttonPaletteSelected;

    private void openEditPaletteDialog(){
        JDialog dialog = new JDialog(this.parentFrame.parentFrame, "Edit Palette");
        dialog.setLocationRelativeTo(this.parentFrame.parentFrame);
        JPanel leftSide = new JPanel();
        JPanel rightSide = new JPanel();
        JPanel bottom = new JPanel();
        leftSide.setLayout(new GridBagLayout());
        rightSide.setLayout(new GridBagLayout());
        bottom.setLayout(new GridBagLayout());
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JButton buttonSave = new JButton("Apply Change");
        JButton[] buttonsColorPalette = new JButton[GUIMain.COLOR_CHOOSER_COLOR_COUNT];

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[0]);

        c.weightx = 1;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;

        for (int i = 0; i < GUIMain.COLOR_CHOOSER_COLOR_COUNT; i++) {
            buttonsColorPalette[i] = new JButton();
            buttonsColorPalette[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //TODO: Show it is selected
                    buttonPaletteSelected = (JButton) e.getSource();
                    colorChooser.setColor(buttonPaletteSelected.getBackground());
                }
            });

            if (i < parentFrame.parentFrame.campaignInformation.colorChooserPalette.size()) {
                buttonsColorPalette[i].setBackground(parentFrame.parentFrame.campaignInformation.colorChooserPalette.get(i));
            } else {
                buttonsColorPalette[i].setBackground(Color.WHITE);
                //TODO: Report Bug
            }
            buttonsColorPalette[i].setOpaque(true);
            buttonsColorPalette[i].setPreferredSize(new Dimension(20, 20));
            leftSide.add(buttonsColorPalette[i], c);
            c.gridx++;
            if (Math.floorMod(i + 1, GUIMain.COLORS_PER_ROW) == 0) {
                c.gridx = 0;
                c.gridy++;
            }
        }
        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(buttonPaletteSelected != null && !buttonPaletteSelected.getBackground().equals(colorChooser.getColor())){
                    buttonSave.setEnabled(true);
                }
                else{
                    buttonSave.setEnabled(false);
                }
            }
        });

        LH.place(0,0,1,1,1,1,"n","c",null,rightSide,c,colorChooser);

        buttonSave.setEnabled(false);
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               buttonPaletteSelected.setBackground(colorChooser.getColor());
               for(int i = 0; i < GUIMain.COLOR_CHOOSER_COLOR_COUNT; i++){
                   if(buttonsColorPalette[i] == buttonPaletteSelected){
                       parentFrame.parentFrame.campaignInformation.colorChooserPalette.set(i, colorChooser.getColor());
                       buttonsColorChooser[i].setBackground(colorChooser.getColor());
                   }
               }
            }
        });
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPaletteSelected = null;
                dialog.dispose();
            }
        });
        LH.place(0,0,1,1,1,1,"n","c",null,bottom,c, buttonSave);
        LH.place(1,0,1,1,1,1,"n","c",null,bottom,c, buttonCancel);

        LH.place(0,0,1,1,1,1,"n","c",null,dialog,c,leftSide);
        LH.place(1,0,1,1,1,1,"n","c",null,dialog,c,rightSide);
        LH.place(0,1,2,1,1,1,"n","c",null,dialog,c,bottom);
        dialog.setVisible(true);
        dialog.pack();
    }
    private Color customColor;
    private void openCustomColorDialog() {
        Color startingColor;
        if (isPrimSelected) {
            startingColor = buttonColorChooserPrim.getBackground();
        } else {
            startingColor = buttonColorChooserSeco.getBackground();
        }
        customColor = startingColor;
        JColorChooser colorChooser = new JColorChooser(startingColor);
        colorChooser.removeChooserPanel(colorChooser.getChooserPanels()[0]);

        JColorChooser.createDialog(parentFrame.parentFrame, "Color Picker", false, colorChooser, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Ok Button
                customColor = colorChooser.getColor();
                if (isPrimSelected) {
                    buttonColorChooserPrim.setBackground(customColor);
                    parentFrame.parentFrame.drawingSurface.setDrawingColorPrim(customColor);
                } else {
                    buttonColorChooserSeco.setBackground(customColor);
                    parentFrame.parentFrame.drawingSurface.setDrawingColorSeco(customColor);
                }

            }
        }, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Cancel Button

            }
        }).setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        JButton source = (JButton) arg0.getSource();

        if (source == buttonColorChooserPrim) {
            isPrimSelected = true;
        } else if (source == buttonColorChooserSeco) {
            isPrimSelected = false;
        } else {
            Color chosenColor = source.getBackground();
            if (isPrimSelected) {
                this.parentFrame.parentFrame.drawingSurface.setDrawingColorPrim(chosenColor);
                buttonColorChooserPrim.setBackground(chosenColor);
            } else {
                this.parentFrame.parentFrame.drawingSurface.setDrawingColorSeco(chosenColor);
                buttonColorChooserSeco.setBackground(chosenColor);
            }
        }
    }
}
