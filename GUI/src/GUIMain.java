import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/*
GUIMain functions:

    It creates the main GUI for the application along with the buttons and menu bar
    It adds panels whenever entries are added, removes panels when deleted, and clears frame when reset
    It allows panel to be customized based on selected setting and user input
    It allows an old save to be reloaded using the save files

*/
public class GUIMain {

    private JFrame frame;
    private JPanel center;
    private JScrollPane mainArea;
    private File file;
    private File score;
    private File date;
    private ArrayList<MyTextArea> textAreas = new ArrayList<MyTextArea>();
    private ArrayList<MyTextArea> dates = new ArrayList<>();
    private Color c = new Color(230, 230, 230);
    private boolean showScore;

    public GUIMain(File file, File score, File date) {

        this.file = file;
        this.score = score;
        this.date = date;

    }

    //Startup method; creates GUI with menu bar, text fields, and buttons
    public void createAndRunGUI(JFrame prevFrame) {

        createBasicGUI();
        frame.setLocationRelativeTo(prevFrame);
        if (fileModifier.isEmpty(file, 0)) {
            fileModifier.fileAppend(file, "default");
            fileModifier.fileAppend(file, "showScore");
        }
        scoreVisibility(fileModifier.readLine(file,2));
        createMenuBar();
        createNewCenterPanel();
        createTextBarWithButtons();

        loadPreviousGUI();

        frame.setVisible(true);

    }

    //Creates a basic GUI with traditional close operation
    private void createBasicGUI() {

        frame = new JFrame("Event log");
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,400);
        frame.setMinimumSize(new Dimension(600,300));
        frame.setMaximumSize(new Dimension(600, 5000));


    }

    //Creates a new frame to replace the old one
    private void resetFrame() {

        frame.getContentPane().removeAll();
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
        createMenuBar();
        createTextBarWithButtons();
        createNewCenterPanel();

    }

    //Creates the menu bar with file and edit
    private void createMenuBar() {

        JMenuBar jbar = new JMenuBar();
        JMenu save = new JMenu("File");
        JMenu edit = new JMenu("Edit");

        giveMenuFunctionality(save, edit);
        jbar.add(save);
        jbar.add(edit);

        frame.getContentPane().add(BorderLayout.NORTH, jbar);

    }

    //Creates the botton panel with textbar and buttons
    private void createTextBarWithButtons() {

        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter Text");
        JTextField tf = new JTextField(10);
        tf.setDocument(new JTextFieldLimit(40)); //Limits JTextField to only 40 characters
        tf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {

                //When enter is pressed, add panel with tf string as input
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {

                    if (!tf.getText().equals("")) {

                        addStringToGUI(tf);
                        tf.setText(null);
                        tf.requestFocusInWindow();

                        saveScores();

                    }

                }

            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        //JButton[] buttomButtons = {new JButton("Send"), new JButton("Delete"), new JButton("Reset")};
        JButton send = new JButton("Post");
        JButton delete = new JButton("Delete");
        JButton reset = new JButton("Reset");
        //send.setPreferredSize(new Dimension(70, 30));
        //delete.setPreferredSize(new Dimension(70, 30));
        //reset.setPreferredSize(new Dimension(70, 30));

        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(send);
        panel.add(delete);
        panel.add(reset);
        giveSendFunctionality(send, tf);
        giveDeleteFunctionality(delete);
        giveResetFunctionality(reset);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        tf.requestFocusInWindow();

    }


    private void createNewCenterPanel() {

        center = new JPanel();
        mainArea = new JScrollPane(center);
        mainArea.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //mainArea.getHorizontalScrollBar().setPreferredSize(new Dimension(5,2));
        mainArea.getVerticalScrollBar().setPreferredSize(new Dimension(2, 5));
        mainArea.getVerticalScrollBar().setUnitIncrement(2);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        frame.getContentPane().add(BorderLayout.CENTER, mainArea);

    }

    private void loadPreviousGUI() {

        addLabels(file);
        loadScores();

    }

    private void giveMenuFunctionality(JMenu save, JMenu edit) {

        JMenuItem m1 = new JMenuItem("Save");
        m1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (int i = 0; i < textAreas.size(); i++) {

                    fileModifier.fileAppendForTA(score, textAreas);
                    fileModifier.fileAppendForTA(date, dates);

                }

            }
        });

        JMenuItem m2 = new JMenuItem("Change color");
        m2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setFocusableWindowState(false);
                JFrame colorFrame = new JFrame();
                colorFrame.setLocationRelativeTo(frame);
                colorFrame.setBackground(Color.WHITE);
                colorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                colorFrame.setSize(250,125);
                colorFrame.setResizable(false);

                JPanel labelPanel = new JPanel();
                labelPanel.add(new JLabel("Which color would you like to apply?"));
                colorFrame.getContentPane().add(labelPanel, BorderLayout.NORTH);

                JPanel colorPanel = new JPanel();
                String[] colorStrings = {"Select a color", "Pink", "Red", "Orange", "Yellow", "Green", "Light Blue", "Lavender", "Gray", "White"};
                JComboBox colors = new JComboBox(colorStrings);
                Color oldColor = c;
                colors.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        String s = (String)colors.getSelectedItem();
                        colorSelector(s);
                        fileModifier.replaceLine(file, 1, s);

                    }
                });
                colorPanel.add(colors);
                colorFrame.getContentPane().add(colorPanel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                buttonPanel.add(new JLabel("        "));
                JButton done = new JButton("Done");
                done.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        saveScores();
                        resetFrame();
                        if (!fileModifier.isEmpty(file, 2)) addLabels(file);

                        colorFrame.dispose();
                        frame.setFocusableWindowState(true);

                    }
                });
                buttonPanel.add(done);

                JButton cancel = new JButton("Cancel");
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        c = oldColor;
                        colorFrame.dispose();
                        frame.setFocusableWindowState(true);

                    }
                });
                buttonPanel.add(cancel);
                colorFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
                colorFrame.setVisible(true);

            }
        });

        JMenuItem m3;
        if (!showScore) {
            m3 = new JMenuItem("Add Score");
            m3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    showScore = true;
                    saveScores();
                    resetFrame();
                    //loadScores();
                    fileModifier.replaceLine(file, 2, "showScore");
                    if (!fileModifier.isEmpty(file, 2)) addLabels(file);

                }
            });
        } else if (showScore) {

            m3 = new JMenuItem("Hide Score");
            m3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    showScore = false;
                    saveScores();
                    resetFrame();
                    //loadScores();
                    fileModifier.replaceLine(file, 2, "hideScore");
                    if (!fileModifier.isEmpty(file, 2)) addLabels(file);

                }
            });

        } else { m3 = new JMenuItem("something went wrong"); }

        save.add(m1);
        edit.add(m2);
        edit.add(m3);

    }

    private void giveSendFunctionality(JButton send, JTextField tf) {

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!tf.getText().equals("")) {

                    addStringToGUI(tf);
                    tf.setText(null);
                    tf.requestFocusInWindow();

                    saveScores();

                }

            }
        });

    }

    private void giveDeleteFunctionality(JButton delete) {

        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                fileModifier.deleteLineInFile(file, 2);
                fileModifier.deleteLineInFile(score);
                fileModifier.deleteLineInFile(date);
                if (textAreas.size() > 0) textAreas.remove(textAreas.size() - 1);
                if (dates.size() > 0) dates.remove(dates.size() - 1);
                saveScores();

                resetFrame();
                addLabels(file);


            }
        });

    }

    private void giveResetFunctionality(JButton clear) {

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                frame.setFocusableWindowState(false);

                //Creates a new popup frame
                JFrame resetFrame = new JFrame();
                resetFrame.setAlwaysOnTop(true);
                resetFrame.setLocationRelativeTo(frame);
                resetFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                resetFrame.setBackground(Color.WHITE);
                resetFrame.setSize(250,100);
                resetFrame.setResizable(false);

                JPanel panel = new JPanel();
                panel.add(new JLabel("Are you sure?"));
                resetFrame.getContentPane().add(panel, BorderLayout.NORTH);

                panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
                JButton yes = new JButton("Yes");
                JButton cancel = new JButton("Cancel");
                giveYesFunctionality(yes, resetFrame);
                giveCancelFunctionality(cancel, resetFrame);

                panel.add(new JLabel("       "));
                panel.add(yes);
                panel.add(cancel);
                resetFrame.getContentPane().add(panel, BorderLayout.CENTER);

                resetFrame.setVisible(true);

            }
        });

    }

    protected void giveYesFunctionality(JButton yes, JFrame resetFrame) {

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //This clears file (check fileAppend method in fileModifier)
                ArrayList<String> colors = fileModifier.fileReset(file, 2);
                fileModifier.fileAppend(score, "|||clear|");
                fileModifier.fileAppend(score, "|||clear|");
                textAreas = new ArrayList<MyTextArea>();
                dates = new ArrayList<MyTextArea>();

                for (int i = 0; i < colors.size(); i++) { fileModifier.fileAppend(file, colors.get(i)); }

                if (frame == null) createBasicGUI();
                resetFrame();
                frame.getContentPane().add(BorderLayout.CENTER, mainArea);

                resetFrame.dispose();
                frame.setFocusableWindowState(true);

            }
        });

    }

    protected void giveCancelFunctionality(JButton cancel, JFrame resetFrame) {

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                resetFrame.dispose();
                frame.setFocusableWindowState(true);

            }
        });

    }

    //Color selector
    private void colorSelector(String s) {

        switch(s) {
            case "Pink":
                c = Color.PINK;
                break;
            case "Red":
                c = Color.RED;
                break;
            case "Orange":
                c = Color.ORANGE;
                break;
            case "Yellow":
                c = Color.YELLOW;
                break;
            case "Green":
                c = Color.GREEN;
                break;
            case "Light Blue":
                c = Color.CYAN;
                break;
            case "Lavender":
                c = new Color(230, 230, 250);
                break;
            case "Gray":
                c = Color.LIGHT_GRAY;
                break;
            case "White":
                c = Color.WHITE;
                break;
            case "default":
                c = new Color(230, 230, 230);
                break;

        }

    }

    private void scoreVisibility(String s) {

        switch(s) {
            case("showScore"):
                showScore = true;
                break;
            case("hideScore"):
                showScore = false;
                break;
        }

    }

    //Adds a new panel to center and appends strings in JTextField to file
    private void addStringToGUI(JTextField tf) {

        //Write to file
        fileModifier.fileAppend(file, tf.getText());

        JPanel subPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        subPanel.setBackground(this.c);
        subPanel.setMaximumSize(new Dimension(600, 30));
        subPanel.setPreferredSize(new Dimension(600, 30));
        subPanel.setMinimumSize(new Dimension(450, 30));

        if (showScore) {

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 4;
            subPanel.add(new JLabel("Score: "), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 5;
            MyTextArea ta = new MyTextArea(1, 3, "", "");
            ta.setDocument(new JTextFieldLimit(3));
            textAreas.add(ta);
            subPanel.add(ta, c);

            //Adds space to the right (aesthetics)
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.00001;
            c.gridx = 6;
            subPanel.add(new JLabel("    "), c);

        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        subPanel.add(new JLabel(tf.getText()), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.00001;
        c.gridx = 0;
        subPanel.add(new JLabel(" "), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        MyTextArea jt = new MyTextArea(1,7,"", "");
        dates.add(jt);
        jt.setDocument(new JTextFieldLimit(10));
        jt.setText(java.time.LocalDate.now().toString());
        subPanel.add(jt);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.00001;
        c.gridx = 2;
        subPanel.add(new JLabel("  "), c);

        center.add(subPanel);
        frame.getContentPane().add(BorderLayout.CENTER, mainArea);
        frame.setVisible(true);

    }

    //Adds a new panel to center with string as JLabel
    private void addStringWithoutAppend(String string) {

        JPanel subPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        subPanel.setBackground(this.c);
        subPanel.setMaximumSize(new Dimension(600, 30));
        subPanel.setPreferredSize(new Dimension(600, 30));
        subPanel.setMinimumSize(new Dimension(600, 30));

        if (showScore) {

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 4;
            subPanel.add(new JLabel("Score: "), c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0;
            c.gridx = 5;
            MyTextArea ta = new MyTextArea(1, 3, "", "");
            ta.setDocument(new JTextFieldLimit(3));
            textAreas.add(ta);
            subPanel.add(ta, c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 0.00001;
            c.gridx = 6;
            subPanel.add(new JLabel("    "), c);

        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 3;
        subPanel.add(new JLabel(string), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.00001;
        c.gridx = 0;
        subPanel.add(new JLabel(" "), c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        MyTextArea jt = new MyTextArea(1,7,"", "");
        dates.add(jt);
        jt.setDocument(new JTextFieldLimit(10));
        subPanel.add(jt);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.00001;
        c.gridx = 2;
        subPanel.add(new JLabel("  "), c);

        center.add(subPanel);
        frame.getContentPane().add(BorderLayout.CENTER, mainArea);
        frame.setVisible(true);

    }

    //Add panels to frame
    private void addLabels(File file) {

        try {
            //First half reads the file to reconstruct GUI
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

            //Retrieves last used color
            colorSelector(reader.readLine());

            //Shows or hides score on startup
            scoreVisibility(reader.readLine());

            String stringToAdd = reader.readLine();
            int oldSize = textAreas.size();
            int datesSize = dates.size();

            while(stringToAdd != null) {

                addStringWithoutAppend(stringToAdd);
                stringToAdd = reader.readLine();

            }

            reader.close();

            //Second half reconstructs only the MyTextAreas
            if (showScore) {

                //Updates textAreas with new references (since JTextAreas were recreated)
                for (int i = 0; i < oldSize; i++) { textAreas.get(i + oldSize).setText(textAreas.get(i).getScore()); }

                //Removes old JTextArea references (since they point to JTextAreas that are not visible)
                for (int i = 0; i < oldSize; i++) { textAreas.remove(0); }

            }

            //Same as other two for loops, but for dates instead
            for (int i = 0; i < datesSize; i++) { dates.get(i + datesSize).setText(dates.get(i).getDate());}
            for (int i = 0; i < datesSize; i++) { dates.remove(0); }

            frame.getContentPane().add(BorderLayout.CENTER, mainArea);
            frame.setVisible(true);

        } catch (Exception e) { System.exit(4); }

    }

    //Appends scores to scores.txt and changes MyTextArea scores
    private void saveScores() {

        for (int i = 0; i < textAreas.size(); i++) { textAreas.get(i).modifyScores(textAreas.get(i).getText()); }
        fileModifier.fileAppendForTA(score, textAreas);

        for (int i = 0; i < dates.size(); i++) { dates.get(i).modifyDates(dates.get(i).getText()); }
        fileModifier.fileAppendForTA(date, dates);

    }

    //Gets scores from scores.txt, adds them to GUI, and modify score values of JTextFields
    private void loadScores() {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(score.getAbsolutePath()));
            for (int i = 0; i < textAreas.size(); i++) {

                String line = reader.readLine();

                textAreas.get(i).setText(line);
                textAreas.get(i).modifyScores(line);

            }

            reader = new BufferedReader(new FileReader(date.getAbsolutePath()));
            for (int i = 0; i < dates.size(); i++) {

                String line = reader.readLine();

                dates.get(i).setText(line);
                dates.get(i).modifyDates(line);

            }

            frame.setVisible(true);

        } catch (Exception e) { System.exit(6); }

    }
}
