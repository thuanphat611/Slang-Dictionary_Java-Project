import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SlangUI extends JPanel {
    public SlangUI() {
        setLayout(new BorderLayout());
    }
    public static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new SlangUI();

        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }
}

class SlangFunction {
    ArrayList<String[]> data = new ArrayList<String[]>(); //used to store the data
    HashMap<String, Integer> slangHashMap = new HashMap<String, Integer>();
    HashMap<String, Integer> meaningHashMap = new HashMap<String, Integer>();
    String filePath;
    public SlangFunction(String filePath) {
        BufferedReader br;
        String[] columnNames, student;
        String line;
        String[] meaningList;

        this.filePath = filePath;
        try {
            int currentIndex = 0;
            String twoMeaningSlang;
            br = new BufferedReader(new FileReader(filePath));

            //remove the first line - instruction line(Slag`Meaning)
            br.readLine().split("`");
            while ((line = br.readLine()) != null) {
                data.add(line.split("`"));
                slangHashMap.put(data.get(currentIndex)[0], currentIndex);//put slang into slang hashmap for fast searching
                if (data.get(currentIndex)[1].indexOf("|") == -1) { // if slang has 1 meaning
                    meaningHashMap.put(data.get(currentIndex)[1].trim(), currentIndex);
                }
                else { // if slang has 2 or more meanings
                    meaningList = data.get(currentIndex)[1].split("|");
                    for (int i = 0; i < meaningList.length; i++)
                        meaningHashMap.put(meaningList[i].trim(), currentIndex);
                }
                currentIndex++;
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void save() {
        BufferedWriter bw;
        try {
            String line;
            bw = new BufferedWriter(new FileWriter(filePath));

            bw.write("Slag`Meaning");
            bw.newLine();
            for (int i = 0; i < data.size(); i++) {
                line = String.join("`", data.get(i));
                bw.write(line);
                if (i != data.size() - 1) {
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    void print() {
        for (int i = 0; i< data.size(); i++)
            System.out.println(data.get(i)[0] + " : " + data.get(i)[1]);
    }
}

public class Project {
    static final String filePath = "C:\\LTUD Java\\Project01\\slang.txt";
    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                SlangUI.createAndShowGUI();
//            }
//        });

        SlangFunction sf = new SlangFunction(filePath);
//        sf.print();
    }
}
