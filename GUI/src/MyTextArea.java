import javax.swing.*;

public class MyTextArea extends JTextArea {

    private String score;
    private String date;

    public MyTextArea(int rows, int columns, String score, String date) {

        super(rows, columns);
        this.score = score;
        this.date = date;

    }

    public String getDate() { return date; }

    public void modifyDates(String string) { date = string; }

    public String getScore() { return score; }

    public void modifyScores(String string) { score = string; }

}
