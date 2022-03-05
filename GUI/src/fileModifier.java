
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/*
fileModifier functions:

    It can read lines or the entirity of a file
    It can create, append, overwrite, and clear a file

*/
public class fileModifier {

    //Creates file if it doesn't already exist
    public static void createFile(File file) {

        try { if (!file.exists()) file.createNewFile(); }
        catch (Exception e) { System.exit(5); }

    }

    //Write strings to the specified file
    public static void fileAppend(File file, String stringToAppend) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));

            //Use "|||clear|" for stringToAppend to reset the file
            if (stringToAppend.equals("|||clear|")) { resetFile(file, writer, 0); }
            else {

                stringToAppend += "\n";
                writer.append(stringToAppend);
                writer.flush();

            }

            writer.close();

        } catch (Exception e) {

            System.exit(1);

        }

    }

    public static ArrayList<String> fileReset (File file, int saveLines) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));
            return resetFile(file, writer, saveLines);


        } catch (Exception e) {

            System.exit(1);

        }

        return null;

    }

    public static void fileAppendForTA(File file, ArrayList<MyTextArea> textAreas) {

        try {

            BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), false));

            for (int i = 0; i < textAreas.size(); i++) {

                writer.append(textAreas.get(i).getText() + "\n");

            }

            writer.flush();
            writer.close();

        } catch (Exception e) {

            System.exit(1);

        }

    }

    //Resets a given file and saves a given number of lines from the previous file to transfer to the next file
    private static ArrayList<String> resetFile(File file, BufferedWriter writer, int saveLines) {

        try {

            //Saves lines
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            ArrayList<String> savedLines = new ArrayList<String>();
            for (int i = 0; i < saveLines; i++) { savedLines.add(reader.readLine()); }

            //Resets file
            writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), false));
            writer = new BufferedWriter(new FileWriter(file.getAbsolutePath(), true));

            return savedLines;

        } catch (Exception e) { System.exit(1); }

        return null;

    }

    //Deletes the last line in the file
    public static void deleteLineInFile(File file) {

        try {

            //BufferedReader will read lines in file
            //Scanner will keep track of how far the reader gets into the file
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            Scanner scanner = new Scanner(file);

            //Do nothing if file is empty
            if (!scanner.hasNextLine()) return;

            //Create a new file that will replace the old one
            File replacement = new File("next");
            BufferedWriter writer = new BufferedWriter(new FileWriter(replacement.getAbsolutePath()));

            //Forwards the scanner one line (so the writer will skip last line in while loop)
            scanner.nextLine();

            //Adds all lines from old file to new file (except last line)
            while(scanner.hasNextLine()) {

                writer.append(reader.readLine() + "\n");
                scanner.nextLine();
                writer.flush();

            }

            writer.close();
            replacement.renameTo(file);

        } catch (Exception e) { System.exit(2); }

    }

    //Deletes lines in the file ONLY if they aren't one of the saveLines
    public static void deleteLineInFile(File file, int saveLines) {

        try {

            //Create new file
            File replacement = new File("next");
            BufferedWriter writer = new BufferedWriter(new FileWriter(replacement.getAbsolutePath()));

            //BufferedReader will read lines in file
            //Scanner will keep track of how far the reader gets into the file
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            Scanner scanner = new Scanner(file);

            //Do nothing if file is empty
            if (!scanner.hasNextLine()) return;

            //Stops delete if only a certain number of lines are left
            for (int i = 0; i < saveLines; i++) {

                scanner.nextLine();
                if (!scanner.hasNextLine()) return;

            }

            scanner = new Scanner(file);

            //Forwards the scanner one line (so the writer will skip last line in while loop)
            scanner.nextLine();

            while(scanner.hasNextLine()) {

                writer.append(reader.readLine() + "\n");
                scanner.nextLine();
                writer.flush();

            }

            writer.close();
            replacement.renameTo(file);

        } catch (Exception e) { System.exit(2); }

    }

    //Replaces a line in the file
    public static void replaceLine(File file, int line, String s) {

        try {

            //Create new file
            File replacement = new File("next");
            BufferedWriter writer = new BufferedWriter(new FileWriter(replacement.getAbsolutePath()));

            //BufferedReader will read lines in file
            //Scanner will keep track of how far the reader gets into the file
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            Scanner scanner = new Scanner(file);

            //Do nothing if file is empty
            if (!scanner.hasNextLine()) return;

            
            while(scanner.hasNextLine()) {

                if (line == 1) {

                    writer.append(s + "\n");
                    scanner.nextLine();
                    reader.readLine();
                    if (!scanner.hasNextLine()) break;

                }
                writer.append(reader.readLine() + "\n");
                scanner.nextLine();
                writer.flush();
                line--;

            }

            writer.close();
            replacement.renameTo(file);

        } catch (Exception e) { System.exit(2); }

    }

    //Reads a specific line in the file
    public static String readLine(File file, int line) {

        try {

            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));

            for (int i = 0; i < line - 1; i++) { reader.readLine(); }
            return reader.readLine();

        } catch (Exception e) { System.exit(8); }

        return "";

    }

    //Checks if file is empty
    public static boolean isEmpty(File file, int ignoreLines) {

        try {

            Scanner checker = new Scanner(file);
            for (int i = 0; i < ignoreLines; i++) {

                if(!checker.hasNextLine()) return true;
                checker.nextLine();

            }
            return !checker.hasNextLine();

        } catch (Exception e) { System.exit(7); }

        return false;

    }

}
