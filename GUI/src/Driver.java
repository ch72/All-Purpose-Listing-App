import java.io.File;

public class Driver {

    public static void main (String[] args) {

        //Create directory within current directory if it does not exist
        boolean dirSuccess = true;
        if (!(new File(new File(".").getAbsolutePath() + "/ListApp").exists())) 
            dirSuccess = new File(new File(".").getAbsolutePath() + "/ListApp").mkdirs();
        
        //Creates and adds file if directory was successfully created or exists 
        //Exits otherwise
        if (!dirSuccess) { System.out.println("Failed to create directory for ListApp"); System.exit(0); }
        File output = new File(new File(".").getAbsolutePath() + "/ListApp/outputs.txt");  //saves user output (event)
        File score = new File(new File(".").getAbsolutePath() + "/ListApp/score.txt");     //saves scores
        File dates = new File(new File(".").getAbsolutePath() + "/ListApp/dates.txt");     //saves dates
        fileModifier.createFile(output);
        fileModifier.createFile(score);
        fileModifier.createFile(dates);

        GUIStartingInterface g1 = new GUIStartingInterface(output, score, dates);
        g1.createGUI();

    }
}
