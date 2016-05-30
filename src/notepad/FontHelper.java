/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author nishad
 */
public class FontHelper extends JDialog implements ListSelectionListener {

    JPanel pan1, pan2, pan3;
    JLabel lblFont, lblSize, lblType, lblPreview;
    JTextField tfLabel, tfFont, tfSize, tfType;
    JScrollPane scrlFont, scrlSize, scrlType;
    JList lstFont, lstSize, lstType;
    JButton btnOk, btnCancel;
    GridBagLayout gbl;
    GridBagConstraints gbc;
    
    public FontHelper() {
        setTitle("Choose Font");
        setSize(300, 400);
        //setResizable(false);
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        setLayout(gbl);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        lblFont = new JLabel("Fonts:");
        getContentPane().add(lblFont, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        lblSize = new JLabel("Sizes:");
        getContentPane().add(lblSize, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        lblType = new JLabel("Types:");
        getContentPane().add(lblType, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        tfFont = new JTextField("Arial", 12);
        getContentPane().add(tfFont, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        tfSize = new JTextField("8", 4);
        getContentPane().add(tfSize, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        tfType = new JTextField("Regular", 6);
        getContentPane().add(tfType, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        lstFont = new JList(fonts);
        lstFont.setFixedCellWidth(110);
        lstFont.addListSelectionListener(this);
        lstFont.setSelectedIndex(0);
        scrlFont = new JScrollPane(lstFont);
        getContentPane().add(scrlFont, gbc);
        
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] sizes = {"8", "10", "12", "16", "20", "28", "36", "48", "72"};
        lstSize = new JList(sizes);
        lstSize.setFixedCellWidth(20);
        lstSize.addListSelectionListener(this);
        lstSize.setSelectedIndex(0);
        scrlSize = new JScrollPane(lstSize);
        getContentPane().add(scrlSize, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        String[] types = {"Regular", "Bold", "Italic", "Bold Italic"};
        lstType = new JList(types);
        lstType.setFixedCellWidth(60);
        lstType.addListSelectionListener(this);
        lstType.setSelectedIndex(0);
        scrlType = new JScrollPane(lstType);
        getContentPane().add(scrlType, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan1 = new JPanel();
        pan1.setLayout(new FlowLayout());
        lblPreview = new JLabel("Preview: ");
        pan1.add(lblPreview);
        getContentPane().add(pan1, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan2 = new JPanel();
        pan2.setLayout(new FlowLayout());
        tfLabel = new JTextField("AaBbCcDdEeFfGgHhIiJj");
        tfLabel.setEditable(false);
        tfLabel.setBorder(BorderFactory.createEtchedBorder());
        tfLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        pan2.add(tfLabel);
        getContentPane().add(pan2, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        pan3 = new JPanel();
        pan3.setLayout(new FlowLayout());
        btnOk = new JButton("Ok");
        btnCancel = new JButton("Cancel");
        pan3.add(btnOk);
        pan3.add(btnCancel);
        getContentPane().add(pan3, gbc);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            if(e.getSource() == lstFont) {
                Font f1 = font();
                tfFont.setText(String.valueOf(lstFont.getSelectedValue()));
                tfLabel.setFont(f1);
            } else if(e.getSource() == lstSize) {
                Font f2 = font();
                tfSize.setText(String.valueOf(lstSize.getSelectedValue()));
                tfLabel.setFont(f2);
            } else {
                Font f3 = font();
                tfType.setText(String.valueOf(lstType.getSelectedValue()));
                tfLabel.setFont(f3);
            }
        } catch (Exception ex) {
        }
    }
    
    public Font font() {
        Font font = new Font(String.valueOf(lstFont.getSelectedValue()), lstType.getSelectedIndex(), Integer.parseInt(String.valueOf(lstSize.getSelectedValue())));
        return font;
    }
    
    public JButton getOk() {
        return btnOk;
    }
    
    public JButton getCancel() {
        return btnCancel;
    }
    
}
