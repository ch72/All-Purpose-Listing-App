import java.io.File;

public class Driver {

    public static void main (String[] args) {

        //Creates output and save files
        File output = new File("outputs.txt");  //saves user output (event)
        File score = new File("score.txt");     //saves scores
        File dates = new File("dates.txt");     //saves dates
        fileModifier.createFile(output);
        fileModifier.createFile(score);
        fileModifier.createFile(dates);

        GUIStartingInterface g1 = new GUIStartingInterface(output, score, dates);
        g1.createGUI();

        //GUIMain p1 = new GUIMain(output, score);
        //p1.createAndRunGUI();

    }
}
