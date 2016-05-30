/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author nishad
 */
public class Notepad extends JFrame {

    static JTextArea mainArea;
    JMenuBar mBar;
    JMenu mnuFile, mnuEdit, mnuFormat, mnuHelp;
    JMenuItem itmNew, itmOpen, itmSave, itmSaveAs, itmExit,
            itmCut, itmCopy, itmPaste, itmFind, itmReplace,
            itmColor, itmFont;
    JButton btnNew, btnOpen, btnSave, btnSaveAs;
    JCheckBoxMenuItem itmWordWrap;
    JFileChooser fc;
    String fileName;
    String fileContent;
    UndoManager undo;
    UndoAction undoAction;
    RedoAction redoAction;
    //public static Notepad frmMain = new Notepad();
    FontHelper font;
    JToolBar toolBar;

    public Notepad() {
        initComponent();

        itmSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        itmSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        
        btnSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });

        itmOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        
        btnOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });

        itmNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNew();
            }
        });
        
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNew();
            }
        });

        itmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        itmCut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.cut();
            }
        });

        itmCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.copy();
            }
        });

        itmPaste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.paste();
            }
        });

        mainArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undo.addEdit(e.getEdit());
                undoAction.update();
                redoAction.update();
            }
        });

        itmWordWrap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (itmWordWrap.isSelected()) {
                    mainArea.setLineWrap(true);
                    mainArea.setWrapStyleWord(true);
                } else {
                    mainArea.setLineWrap(true);
                    mainArea.setWrapStyleWord(false);
                }
            }
        });

        itmColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color c = JColorChooser.showDialog(rootPane, "Choose Font Color", Color.BLACK);
                mainArea.setForeground(c);
            }
        });

        itmFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(null, false);
            }
        });

        itmReplace.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FindAndReplace(null, true);
            }
        });

        itmFont.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(true);
            }
        });
        font.getOk().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainArea.setFont(font.font());
                font.setVisible(false);
            }
        });
        font.getCancel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                font.setVisible(false);
            }
        });
    }

    private void initComponent() {
        fc = new JFileChooser(".");
        mainArea = new JTextArea();
        getContentPane().add(mainArea);
        getContentPane().add(new JScrollPane(mainArea), BorderLayout.CENTER);
        setTitle("Untitled - Notepad");
        setSize(800, 500);

        // menu bar
        mBar = new JMenuBar();

        // menu
        mnuFile = new JMenu("File");
        mnuEdit = new JMenu("Edit");
        mnuFormat = new JMenu("Format");
        mnuHelp = new JMenu("Help");
        
        // tool bar
        toolBar = new JToolBar();
        getContentPane().add(toolBar, BorderLayout.NORTH);

        // add icon to menu item
        ImageIcon iconNew = new ImageIcon(getClass().getResource("/img/new-icon.png"));
        ImageIcon iconOpen = new ImageIcon(getClass().getResource("/img/open-icon.png"));
        ImageIcon iconSave = new ImageIcon(getClass().getResource("/img/save-icon.png"));
        ImageIcon iconSaveAs = new ImageIcon(getClass().getResource("/img/save-as-icon.png"));
        ImageIcon iconExit = new ImageIcon(getClass().getResource("/img/exit-icon.png"));
        ImageIcon iconCut = new ImageIcon(getClass().getResource("/img/cut-icon.png"));
        ImageIcon iconCopy = new ImageIcon(getClass().getResource("/img/copy-icon.png"));
        ImageIcon iconPaste = new ImageIcon(getClass().getResource("/img/paste-icon.png"));
        ImageIcon iconUndo = new ImageIcon(getClass().getResource("/img/undo-icon.png"));
        ImageIcon iconRedo = new ImageIcon(getClass().getResource("/img/redo-icon.png"));
        ImageIcon iconFind = new ImageIcon(getClass().getResource("/img/find-icon.png"));
        ImageIcon iconReplace = new ImageIcon(getClass().getResource("/img/replace-icon.png"));

        // undo manager
        undo = new UndoManager();

        // font helper
        font = new FontHelper();

        // menu item
        itmNew = new JMenuItem("New", iconNew);
        itmOpen = new JMenuItem("Open", iconOpen);
        itmSave = new JMenuItem("Save", iconSave);
        itmSaveAs = new JMenuItem("Save As", iconSaveAs);
        itmExit = new JMenuItem("Exit", iconExit);

        undoAction = new UndoAction(iconUndo);
        redoAction = new RedoAction(iconRedo);
        itmCut = new JMenuItem("Cut", iconCut);
        itmCopy = new JMenuItem("Copy", iconCopy);
        itmPaste = new JMenuItem("Paste", iconPaste);
        itmFind = new JMenuItem("Find", iconFind);
        itmReplace = new JMenuItem("Replace", iconReplace);

        itmWordWrap = new JCheckBoxMenuItem("Word Wrap");
        itmColor = new JMenuItem("Font Color");
        itmFont = new JMenuItem("Font");
        
        // button
        btnNew = new JButton(iconNew);
        btnOpen = new JButton(iconOpen);
        btnSave = new JButton(iconSave);
        btnSaveAs = new JButton(iconSaveAs);

        // add shortcut to menu item
        itmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        itmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK));
        itmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK));

        // add menu item
        mnuFile.add(itmNew);
        mnuFile.add(itmOpen);
        mnuFile.add(itmSave);
        mnuFile.add(itmSaveAs);
        mnuFile.addSeparator();
        mnuFile.add(itmExit);

        mnuEdit.add(undoAction);
        mnuEdit.add(redoAction);
        mnuEdit.add(itmCut);
        mnuEdit.add(itmCopy);
        mnuEdit.add(itmPaste);
        mnuEdit.add(itmFind);
        mnuEdit.add(itmReplace);

        mnuFormat.add(itmWordWrap);
        mnuFormat.add(itmColor);
        mnuFormat.add(itmFont);

        // add menu to menu bar
        mBar.add(mnuFile);
        mBar.add(mnuEdit);
        mBar.add(mnuFormat);
        mBar.add(mnuHelp);

        // add menu bar to frame
        setJMenuBar(mBar);
        
        // add button to toolbar
        toolBar.add(btnNew);
        toolBar.add(btnOpen);
        toolBar.add(btnSave);
        toolBar.add(btnSaveAs);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void save() {
        PrintWriter pw = null;
        try {
            if (fileName == null) {
                saveAs();
            } else {
                pw = new PrintWriter(new FileWriter(fileName));
                String s = mainArea.getText();
                StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                while (st.hasMoreElements()) {
                    pw.println(st.nextToken());
                }
                JOptionPane.showMessageDialog(rootPane, "File saved");
                fileContent = mainArea.getText();
            }

        } catch (IOException e) {
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private void saveAs() {
        PrintWriter pw = null;
        int option = -1;
        try {
            option = fc.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                if (fc.getSelectedFile().exists()) {
                    int op = JOptionPane.showConfirmDialog(rootPane, "Do you want to replace this file?", "Confirmation", JOptionPane.OK_CANCEL_OPTION);
                    if (op == 0) {
                        pw = new PrintWriter(new FileWriter(fc.getSelectedFile()));
                        String s = mainArea.getText();
                        StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                        while (st.hasMoreElements()) {
                            pw.println(st.nextToken());
                        }
                        JOptionPane.showMessageDialog(rootPane, "File saved");
                        fileContent = mainArea.getText();
                        fileName = fc.getSelectedFile().getName();
                        setTitle(fileName);
                    } else {
                        saveAs();
                    }
                } else {
                    pw = new PrintWriter(new FileWriter(fc.getSelectedFile()));
                    String s = mainArea.getText();
                    StringTokenizer st = new StringTokenizer(s, System.getProperty("line.separator"));
                    while (st.hasMoreElements()) {
                        pw.println(st.nextToken());
                    }
                    JOptionPane.showMessageDialog(rootPane, "File saved");
                    fileContent = mainArea.getText();
                    fileName = fc.getSelectedFile().getName();
                    setTitle(fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    private void open() {
        Reader in = null;
        try {
            int option = fc.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                mainArea.setText(null);
                in = new FileReader(fc.getSelectedFile());
                char[] buff = new char[100000];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    mainArea.setText(new String(buff, 0, nch));
                }
                fileName = fc.getSelectedFile().getName();
                setTitle(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openNew() {
        if (!mainArea.getText().equals("") && !mainArea.getText().equals(fileContent)) {
            if (fileName == null) {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    saveAs();
                    clear();
                } else if (option == 2) {
                } else {
                    clear();
                }
            } else {
                int option = JOptionPane.showConfirmDialog(rootPane, "Do you want to save the changes?");
                if (option == 0) {
                    save();
                    clear();
                } else if (option == 2) {
                } else {
                    clear();
                }
            }
        } else {
            clear();
        }
    }

    private void clear() {
        mainArea.setText(null);
        setTitle("Untitled - Notepad");
        fileName = null;
        fileContent = null;
    }

    class UndoAction extends AbstractAction {

        public UndoAction(ImageIcon undoIcon) {
            super("Undo", undoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ex.printStackTrace();
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, "Undo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");

            }
        }

    }

    class RedoAction extends AbstractAction {

        public RedoAction(ImageIcon redoIcon) {
            super("Redo", redoIcon);
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ex.printStackTrace();
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, "Redo");
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");

            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Notepad np = new Notepad();

    }

    public static JTextArea getMainArea() {
        return mainArea;
    }

}
