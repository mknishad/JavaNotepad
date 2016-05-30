/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author nishad
 */
public class FindAndReplace extends JDialog implements ActionListener {

    boolean foundOne, isReplace;
    JTextField findText, replaceText;
    JCheckBox cbCase, cbWhole;
    JRadioButton up, down;
    JLabel statusInfo;
    JFrame owner;
    JPanel north, center, south;

    public FindAndReplace(JFrame owner, boolean isReplace) {
        super(owner, true);
        this.isReplace = isReplace;
        north = new JPanel();
        center = new JPanel();
        south = new JPanel();
        if (isReplace) {
            setTitle(" Find And Replace");
            setReplacePanel(north);
        } else {
            setTitle(" Find");
            setFindPanel(north);
        }
        setComponent(center);
        statusInfo = new JLabel("Status Info:");
        south.add(statusInfo);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        getContentPane().add(north, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(south, BorderLayout.SOUTH);
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        //int x = (owner.getWidth() * 3 / 5) - (getWidth() / 2);
        //int y = (owner.getHeight() * 3 / 5) - (getHeight() / 2);
        //setLocation(x, y);
        setVisible(true);
    }

    private void setComponent(JPanel center) {
        JPanel east = new JPanel();
        JPanel west = new JPanel();
        center.setLayout(new GridLayout(1, 2));
        east.setLayout(new GridLayout(2, 1));
        west.setLayout(new GridLayout(2, 1));
        cbCase = new JCheckBox("Match Case", true);
        cbWhole = new JCheckBox("Match Whole", true);
        ButtonGroup group = new ButtonGroup();
        up = new JRadioButton("Up", false);
        down = new JRadioButton("Down", false);
        group.add(up);
        group.add(down);
        east.add(cbCase);
        east.add(cbWhole);
        east.setBorder(BorderFactory.createTitledBorder("Search Options: "));
        west.add(up);
        west.add(down);
        west.setBorder(BorderFactory.createTitledBorder("Search Direction: "));
        center.add(east);
        center.add(west);
    }

    private void setFindPanel(JPanel north) {
        final JButton NEXT = new JButton("Find Next");
        NEXT.addActionListener(this);
        NEXT.setEnabled(false);
        findText = new JTextField(20);
        findText.addActionListener(this);
        findText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean state = findText.getDocument().getLength() > 0;
                NEXT.setEnabled(state);
                foundOne = false;
            }
        });
        if (findText.getText().length() > 0) {
            NEXT.setEnabled(true);
        }
        north.add(new JLabel("Find Word:"));
        north.add(findText);
        north.add(NEXT);
    }

    private void setReplacePanel(JPanel north) {
        GridBagLayout grid = new GridBagLayout();
        north.setLayout(grid);
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblFWord = new JLabel(" Find Word:");
        JLabel lblRWord = new JLabel(" Replace Word:");
        final JButton NEXT = new JButton("Replace Text");
        NEXT.addActionListener(this);
        NEXT.setEnabled(false);
        final JButton REPLACE = new JButton("Replace All");
        REPLACE.addActionListener(this);
        REPLACE.setEnabled(false);
        findText = new JTextField(20);
        replaceText = new JTextField(20);
        replaceText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean state = ((replaceText.getDocument().getLength() > 0) && (findText.getDocument().getLength() > 0));
                NEXT.setEnabled(state);
                REPLACE.setEnabled(state);
                foundOne = false;
            }
        });
        con.gridx = 0;
        con.gridy = 0;
        grid.setConstraints(lblFWord, con);
        north.add(lblFWord);
        con.gridx = 1;
        con.gridy = 0;
        grid.setConstraints(findText, con);
        north.add(findText);
        con.gridx = 2;
        con.gridy = 0;
        grid.setConstraints(NEXT, con);
        north.add(NEXT);
        con.gridx = 0;
        con.gridy = 1;
        grid.setConstraints(lblRWord, con);
        north.add(lblRWord);
        con.gridx = 1;
        con.gridy = 1;
        grid.setConstraints(replaceText, con);
        north.add(replaceText);
        con.gridx = 2;
        con.gridy = 1;
        grid.setConstraints(REPLACE, con);
        north.add(REPLACE);
    }

    private void setFindAndReplacePanel() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(findText) || e.getSource().equals(replaceText)) {
            validate();
        }
        process();
        if(e.getActionCommand().equals("Replace All")) {
            replaceAll();
        }
    }

    private void process() {
        if (isReplace) {
            statusInfo.setText("Replacing " + findText.getText());
        } else {
            statusInfo.setText("Searching For " + findText.getText());
        }
        int caret = Notepad.getMainArea().getCaretPosition();
        String word = getWord();
        String text = getAllText();
        caret = search(text, word, caret);
        if (caret < 0) {
            endResult(false, 0);
        }
    }

    private void endResult(boolean isReplaceAll, int tally) {
        String message = "";
        if (isReplaceAll) {
            if(tally == 0) {
                message = findText.getText() + " not found";
            } else if(tally == 1) {
                message = "One change was made to " + findText.getText();
            } else {
                message = tally + "changes were made to " + findText.getText();
            }
        } else {
            String str = "";
            if (isSearchDown()) {
                str = "Search Down";
            } else {
                str = "Search Up";
            }
            if (foundOne && !isReplace) {
                message = "End of " + str + " for " + findText.getText();
            } else if (foundOne && isReplace) {
                message = "End of Replace " + findText.getText() + " with " + replaceText.getText();
            }
        }
        statusInfo.setText(message);
    }

    private int search(String text, String word, int caret) {
        boolean found = false;
        int all = text.length();
        int check = word.length();
        if (isSearchDown()) {
            int add = 0;
            for (int i = caret + 1; i < (all - check); i++) {
                String temp = text.substring(i, i + check);
                if (temp.equals(word)) {
                    if (isWhoeWord()) {
                        if (checkForWholeWord(check, text, add, caret)) {
                            caret = i;
                            found = true;
                            break;
                        }
                    } else {
                        caret = i;
                        found = true;
                        break;
                    }
                }
            }
        } else {
            int add = caret;
            for (int i = caret; i >= check; i--) {
                add--;
                String temp = text.substring(i - check, i);
                if (temp.equals(word)) {
                    if (isWhoeWord()) {
                        if (checkForWholeWord(check, text, add, caret)) {
                            caret = i;
                            found = true;
                            break;
                        }
                    } else {
                        caret = i;
                        found = true;
                        break;
                    }
                }
            }
        }
        Notepad.getMainArea().setCaretPosition(0);
        if (found) {
            Notepad.getMainArea().requestFocus();
            if (isSearchDown()) {
                Notepad.getMainArea().select(caret, caret + check);
            } else {
                Notepad.getMainArea().select(caret - check, caret);
            }
            // for replace
            if (isReplace) {
                String replace = replaceText.getText();
                Notepad.getMainArea().replaceSelection(replace);
                if (isSearchDown()) {
                    Notepad.getMainArea().select(caret, caret + replace.length());
                } else {
                    Notepad.getMainArea().select(caret - replace.length(), caret);
                }
            }
            foundOne = true;
            return caret;
        }
        return -1;
    }

    private String getAllText() {
        if (caseNotSelected()) {
            return Notepad.getMainArea().getText().toLowerCase();
        }
        return Notepad.getMainArea().getText();
    }

    private String getWord() {
        if (caseNotSelected()) {
            return findText.getText().toLowerCase();
        }
        return findText.getText();
    }

    private boolean caseNotSelected() {
        return !cbCase.isSelected();
    }

    private boolean isSearchDown() {
        return down.isSelected();
    }

    private boolean isWhoeWord() {
        return cbWhole.isSelected();
    }

    private boolean checkForWholeWord(int check, String text, int add, int caret) {
        int offsetLeft = (caret + add) - 1;
        int offsetRight = (caret + add) + check;
        if (offsetLeft < 0 || offsetRight >= text.length()) {
            return true;
        }
        return (Character.isLetterOrDigit(text.charAt(offsetLeft)) && Character.isLetterOrDigit(text.charAt(offsetRight)));
    }
    
    private void replaceAll() {
        // debug
//        System.out.println("On replaceAll method");
        String text = Notepad.getMainArea().getText();
        // debug
//        System.out.println("Text:\n" + text);
        String word = findText.getText();
        // debug
//        System.out.println("Word:\n" + word);
        String insert = replaceText.getText();
        StringBuffer sb = new StringBuffer(text);
        int diff = insert.length() - word.length();
        int offset = 0;
        int tally = 0;
        for (int i = 0; i < (text.length()-word.length()); i++) {
            String temp = text.substring(i, i+word.length());
            // debug
//            System.out.println("Temp:\n" + temp);
            if(temp.equals(word)) {
                tally++;
                // debug
                System.out.println(tally);
                sb.replace(i+offset, i+offset+word.length(), insert);
                offset += diff;
            }
        }
        Notepad.getMainArea().setText(sb.toString());
        
        endResult(true, tally);
        Notepad.getMainArea().setCaretPosition(0);
    }

}
