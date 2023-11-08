import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SlangUI extends JPanel {
    SlangFunction controller;
    String filePath;
    public SlangUI(String filePath) {
        this.filePath = filePath;
        this.controller = new SlangFunction(filePath);

        setLayout(new BorderLayout());

        JScrollPane search = createSearchSection();
        JPanel buttonGroup = createButtonGroup();
        JScrollPane randomSlang = createRandomSlang(controller.ramdomSlang());

        add(search, BorderLayout.PAGE_START);
        add(randomSlang, BorderLayout.CENTER);
        add(buttonGroup, BorderLayout.PAGE_END);
    }

    JScrollPane createSearchSection() {
        JTextField searchTextField = new JTextField();
        JButton searchButton = new JButton("Find");
        JPanel searchbar = new JPanel();
        searchbar.setLayout(new BoxLayout(searchbar, BoxLayout.LINE_AXIS));
        searchbar.add(Box.createRigidArea(new Dimension(10, 0)));
        searchbar.add(searchTextField);
        searchbar.add(Box.createRigidArea(new Dimension(10, 0)));
        searchbar.add(searchButton);
        searchbar.add(Box.createRigidArea(new Dimension(10, 0)));

        JLabel searchTypeLabel = new JLabel("Search by:");
        String[] searchTypeList = {"slang", "definition"};
        JComboBox searchTypeCB = new JComboBox(searchTypeList);
        searchTypeCB.setPreferredSize(new Dimension(20, 20));
        JPanel searchType = new JPanel();
        searchType.setLayout(new BoxLayout(searchType, BoxLayout.LINE_AXIS));
        searchType.add(Box.createRigidArea(new Dimension(10, 0)));
        searchType.add(searchTypeLabel);
        searchType.add(Box.createRigidArea(new Dimension(10, 0)));
        searchType.add(searchTypeCB);
        searchType.add(Box.createRigidArea(new Dimension(10, 0)));

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.PAGE_AXIS));
        JPanel searchLabelPanel = new JPanel();
        searchLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel searchLabel = new JLabel("Search");
        searchLabelPanel.add(searchLabel);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        searchPanel.add(searchLabelPanel);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        searchPanel.add(searchbar);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        searchPanel.add(searchType);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return new JScrollPane(searchPanel);
    }

    JPanel createButtonGroup() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.LINE_AXIS));

        JButton history = new JButton("History");
        JButton add = new JButton("Add slang");
        JButton quizz= new JButton("Quizz");
        JButton reset = new JButton("Reset");

        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(history);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(add);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(quizz);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(reset);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(buttonGroup);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return buttonPanel;
    }

    JScrollPane createRandomSlang(String[] slang) {
        StringBuilder contentText = new StringBuilder("Slang: " + slang[0] + "\nMeaning:\n");
        String[] meaningList = slang[1].split("\\|");
        for (int i = 0; i < meaningList.length; i++) {
            contentText.append("-").append(meaningList[i].trim());
            if (i != (meaningList.length - 1)) {
                contentText.append("\n");
            }
        }
        JPanel randomPanel = new JPanel();
        randomPanel.setLayout(new BoxLayout(randomPanel, BoxLayout.PAGE_AXIS));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel("On this day slang word");
        labelPanel.add(label);

        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.LINE_AXIS));
        JTextPane content = new JTextPane();
        content.setEditable(false);
        content.setCaretColor(Color.WHITE);
        JScrollPane textSP = new JScrollPane(content);
        textSP.setPreferredSize(new Dimension(0, 100));
        content.setText(contentText.toString());
        textPane.add(Box.createRigidArea(new Dimension(10, 0)));
        textPane.add(textSP);
        textPane.add(Box.createRigidArea(new Dimension(10, 0)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton reload = new JButton("Change");
        buttonPanel.add(reload);
        reload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] newSlang = controller.ramdomSlang();
                StringBuilder contentText = new StringBuilder("Slang: " + newSlang[0] + "\nMeaning:\n");
                String[] meaningList = newSlang[1].split("\\|");
                for (int i = 0; i < meaningList.length; i++) {
                    contentText.append("-").append(meaningList[i].trim());
                    if (i != (meaningList.length - 1)) {
                        contentText.append("\n");
                    }
                }
                content.setText(contentText.toString());
            }
        });

        randomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        randomPanel.add(labelPanel);
        randomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        randomPanel.add(textPane);
        randomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        randomPanel.add(buttonPanel);
        randomPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        return new JScrollPane(randomPanel);
    }

    public static void createAndShowGUI(String filePath) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame frame = new JFrame("Slang words");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JComponent newContentPane = new SlangUI(filePath);

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
        String line;
        String[] meaningList;

        this.filePath = filePath;
        try {
            int currentIndex = 0;
            br = new BufferedReader(new FileReader(filePath));

            //remove the first line - instruction line(Slag`Meaning)
            br.readLine();
            while ((line = br.readLine()) != null) {
                data.add(line.split("`"));
                slangHashMap.put(data.get(currentIndex)[0], currentIndex);//put slang into slang hashmap for fast searching
                if (!data.get(currentIndex)[1].contains("|")) { // if slang has 1 meaning
                    meaningHashMap.put(data.get(currentIndex)[1].trim(), currentIndex);
                }
                else { // if slang has 2 or more meanings
                    meaningList = data.get(currentIndex)[1].split("\\|");
                    for (int i = 0; i < meaningList.length; i++) {
                        meaningHashMap.put(meaningList[i].trim(), currentIndex);
                    }
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
            if (key.contains(keyword))
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

    void editSlang(String oldMeaning, String newMeaning) {
        oldMeaning = oldMeaning.trim();
        newMeaning = newMeaning.trim();
        int index = meaningHashMap.get(oldMeaning);
        String currentMeaning = data.get(index)[1];

        if (!currentMeaning.contains("|")) { // slang only has one meaning
            currentMeaning = newMeaning;
            return;
        }
        // Slang has many meaning(duplicate)
        String[] meaningList = currentMeaning.split("\\|");
        for (String meaning : meaningList)
            if (meaning.contains(oldMeaning))
                meaning = newMeaning;
        currentMeaning = String.join(" | ", meaningList);
    }

    void deleteSlang(String slang) {
        slang = slang.trim();
        int index = slangHashMap.get(slang);
        String[] meaningList = data.get(index)[1].split("\\|");

        data.remove(index);
        slangHashMap.remove(slang);
        for (String meaning : meaningList) {
            meaning = meaning.trim();
            meaningHashMap.remove(meaning);
        }
    }

    String[] ramdomSlang() {
        int random = (int) (Math.random() * data.size());
        return data.get(random);
    }

    ArrayList<String[]> slangQuizz() {
        ArrayList<String[]> randomList = new ArrayList<String[]>();
        int[] randomIndexes = new int[4];
        int random = (int) (Math.random() * data.size());

        randomList.add(data.get(random));
        randomIndexes[0] = random;
        for (int i = 0; i < 3; i++) {
            do {
                random = (int) (Math.random() * data.size());
            }
            while (Arrays.asList(randomIndexes).contains(random));
            randomList.add(data.get(random));
            randomIndexes[randomIndexes.length] = random;
        }

        return randomList;
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

    void reset() {
        data.clear();
        slangHashMap.clear();
        meaningHashMap.clear();

        BufferedReader br;
        String line;
        String[] meaningList;

        try {
            int currentIndex = 0;
            String twoMeaningSlang;
            br = new BufferedReader(new FileReader(filePath));

            //remove the first line - instruction line(Slag`Meaning)
            br.readLine();
            while ((line = br.readLine()) != null) {
                data.add(line.split("`"));
                slangHashMap.put(data.get(currentIndex)[0], currentIndex);//put slang into slang hashmap for fast searching
                if (!data.get(currentIndex)[1].contains("|")) { // if slang has 1 meaning
                    meaningHashMap.put(data.get(currentIndex)[1].trim(), currentIndex);
                }
                else { // if slang has 2 or more meanings
                    meaningList = data.get(currentIndex)[1].split("\\|");
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                SlangUI.createAndShowGUI(filePath);
            }
        });

//        SlangFunction sf = new SlangFunction(filePath);
//        Set<Integer> result;
//        result = sf.findSlang("one");
//        if (result.isEmpty())
//            System.out.println("empty");
//        else
//            for (int item : result)
//                System.out.println(sf.findByIndex(item));
//        sf.save();
//        sf.print();
    }
}
