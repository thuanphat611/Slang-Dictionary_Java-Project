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
    ArrayList<String> history = new ArrayList<String>();
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

    // return the meaning of an input keyword, if not found return ""
    String findMeaning(String keyword) {
        keyword = keyword.trim();
        history.add(keyword); // add keyword to history ArrayList for history function
       if (slangHashMap.get(keyword) == null) {
           return "";
       }
       return data.get(slangHashMap.get(keyword))[1];
    }

    HashSet<Integer> findSlang(String keyword) {
        keyword = keyword.trim();
        history.add(keyword); // add keyword to history ArrayList for history function
        HashSet<Integer> result = new HashSet<Integer>();
        for (String key : meaningHashMap.keySet())
            if (key.indexOf(keyword) != -1)
                result.add(meaningHashMap.get(key));
        return result;
    }

    void addSlang(String slang, String meaning, boolean duplicate) {
        slang = slang.trim();
        meaning = meaning.trim();
        if (findMeaning(slang).isEmpty()) { // new slang
            String[] newSlang = new String[2];
            newSlang[0] = slang;
            newSlang[1] = meaning;
            slangHashMap.put(slang, data.size());
            meaningHashMap.put(meaning, data.size());
            data.add(newSlang);
            return;
        }
        String duplicateMeaning = data.get(slangHashMap.get(slang))[1];
        if (duplicate) { // duplicate meaning if slang exist
            duplicateMeaning = duplicateMeaning + " | " + meaning;
            return;
        }
        duplicateMeaning = meaning; // overwrite meaning if slang exist
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

    String findByIndex (int index) {
        if (index > data.size())
            return "wrong index";
        return data.get(index)[0];
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
        Set<Integer> result;
        result = sf.findSlang("someone");
        if (result.isEmpty())
            System.out.println("empty");
        else
            for (int item : result)
                System.out.println(sf.findByIndex(item));
        sf.save();
//        sf.print();
    }
}
