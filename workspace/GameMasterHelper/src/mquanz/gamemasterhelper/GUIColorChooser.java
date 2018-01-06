package mquanz.gamemasterhelper;

import javax.swing.*;
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
        buttonColorChooserPrim.setOpaque(true);
        buttonColorChooserPrim.setPreferredSize(new Dimension(30, 30));

        buttonColorChooserSeco = new JButton();
        buttonColorChooserSeco.addActionListener(this);
        buttonColorChooserSeco.setBackground(Color.WHITE);
        buttonColorChooserSeco.setOpaque(true);
        buttonColorChooserSeco.setPreferredSize(new Dimension(30, 30));

        JButton buttonCustomColor = new JButton(GeneralInformation.createImageIcon("res/gui/color_custom.png"));
        buttonCustomColor.setPreferredSize(new Dimension(20, 20));
        buttonCustomColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCustomColorDialog();
            }
        });
        JButton buttonEditPalette = new JButton();
        buttonEditPalette.setPreferredSize(new Dimension(20, 20));

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

            if (i < parentFrame.parentFrame.generalInformation.colorChooserPalette.size()) {
                buttonsColorChooser[i].setBackground(parentFrame.parentFrame.generalInformation.colorChooserPalette.get(i));
            } else {
                buttonsColorChooser[i].setBackground(Color.WHITE);
                //TODO: Report Bug
            }
            buttonsColorChooser[i].setOpaque(true);
            buttonsColorChooser[i].setPreferredSize(new Dimension(20, 20));
            add(buttonsColorChooser[i], c);
            c.gridx++;
            if (Math.floorMod(i + 1, GUIMain.COLORS_PER_ROW) == 0) {
                c.gridx = 0;
                c.gridy++;
            }
        }
        c.gridx++;
        c.gridy++;

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


        // TODO Auto-generated method stub

    }
}
