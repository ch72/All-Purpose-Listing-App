import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/*
GUIStartingInterface Functions:

    On launch of the application, it can produce the initial GUI
    It adds a "new" or "resume" button that, when clicked, changes conditions for main GUI

*/
public class GUIStartingInterface {

    private JFrame frame;
    private File file;
    private File score;
    private File date;

    public GUIStartingInterface(File file, File score, File date) {

        this.file = file;
        this.score = score;
        this.date = date;

    }

    public void createGUI() {

        //Creates a basic frame for the GUI
        frame = new JFrame();
        frame.setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250,100);
        frame.setResizable(false);

        //Creates starting panel for GUI
        JPanel panel = new JPanel();
        panel.add(new JLabel("New list or resume previous list?"));
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        //Sets layout and adds "new" button on GUI
        panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        JButton newDoc = new JButton("New");
        giveNewDocFunction(newDoc);
        panel.add(new JLabel("       "));
        panel.add(newDoc);

        //Adds "resume" button if a previous save was found
        if (!fileModifier.isEmpty(file, 2)) {

            JButton resumeDoc = new JButton("Resume");
            giveResumeDocFunction(resumeDoc);
            panel.add(resumeDoc);

        }

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

    }

    //Adds functionality to the "new" button
    private void giveNewDocFunction(JButton newDoc) {

        newDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIMain g1 = new GUIMain(file, score, date);
                fileModifier.fileAppend(file, "|||clear|");
                fileModifier.fileAppend(score, "|||clear|");
                fileModifier.fileAppend(date, "|||clear|");
                g1.createAndRunGUI(frame);

                frame.dispose();

            }
        });

    }

    //Adds functionality to the "resume" button
    private void giveResumeDocFunction(JButton resumeDoc) {

        resumeDoc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GUIMain g1 = new GUIMain(file, score, date);
                g1.createAndRunGUI(frame);

                frame.dispose();

            }
        });

    }

}
