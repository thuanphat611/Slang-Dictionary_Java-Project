import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.jar.JarEntry;
import javax.swing.*;

class historyPanel extends JPanel {
    JPanel previous;
    JFrame parentFrame;
    String textPanelContent;
    JTextPane historyText;
    public historyPanel(JPanel pre, JFrame parentFrame) {
        this.previous = pre;
        this.parentFrame = parentFrame;
        textPanelContent = "No history";
        historyText = new JTextPane();
        historyText.setText(textPanelContent);

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Search history");
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(label);

        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.LINE_AXIS));

        historyText.setEditable(false);
        historyText.setCaretColor(Color.WHITE);
        JScrollPane textSP = new JScrollPane(historyText);

        JButton backBtn = new JButton("Back");
        JPanel buttonPnl = new JPanel();
        buttonPnl.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPnl.add(backBtn);

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(previous);
                parentFrame.validate();
            }
        });

        add(labelPanel, BorderLayout.PAGE_START);
        add(textSP, BorderLayout.CENTER);
        add(buttonPnl, BorderLayout.PAGE_END);
    }

    void historyToString(ArrayList<String> history) {
        if (history == null) {
            return;
        }
        if (history.isEmpty()) {
            return;
        }
        textPanelContent = "";

        for (int i = 0; i< history.size(); i++) {
            textPanelContent = textPanelContent + history.get(i);
            if (i != history.size() - 1)
                textPanelContent = textPanelContent + "\n";
        }

        historyText.setText(textPanelContent);
    }
}

class AddSlangPanel extends JPanel {
    mainPanel previous;
    JFrame parentFrame;
    public AddSlangPanel(mainPanel pre, JFrame parent) {
        this.previous = pre;
        this.parentFrame = parent;

        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel panelLabel = new JLabel("Add slang word");
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(panelLabel);

        JLabel slangLabel = new JLabel("Slang word:");
        JLabel meaningLabel = new JLabel("Meaning:");
        JTextField slangTextField = new JTextField();
        JTextField meaningTextField = new JTextField();
        JPanel slangRow = new JPanel();
        slangRow.setLayout(new BoxLayout(slangRow, BoxLayout.LINE_AXIS));
        slangRow.add(Box.createRigidArea(new Dimension(10, 0)));
        slangRow.add(slangLabel);
        slangRow.add(Box.createRigidArea(new Dimension(10, 0)));
        slangRow.add(slangTextField);
        slangRow.add(Box.createRigidArea(new Dimension(10, 0)));
        JPanel meaningRow = new JPanel();
        meaningRow.setLayout(new BoxLayout(meaningRow, BoxLayout.LINE_AXIS));
        meaningRow.add(Box.createRigidArea(new Dimension(10, 0)));
        meaningRow.add(meaningLabel);
        meaningRow.add(Box.createRigidArea(new Dimension(23, 0)));
        meaningRow.add(meaningTextField);
        meaningRow.add(Box.createRigidArea(new Dimension(10, 0)));
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(slangRow, BorderLayout.PAGE_START);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        inputPanel.add(meaningRow, BorderLayout.PAGE_END);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.LINE_AXIS));
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SlangFunction controller = previous.getController();
                String slang = slangTextField.getText();
                String meaning = meaningTextField.getText();
                if (slang.isEmpty() || meaning.isEmpty())
                    return;
                String[] exist = controller.findBySlang(slang, false);
                if (exist == null) { // new slang
                    controller.addSlang(slang, meaning, true);
                    JOptionPane.showMessageDialog(parentFrame, "Added new slang word");
                    slangTextField.setText("");
                    meaningTextField.setText("");
                }
                else {//slang exist
                    //show popup for user to confirm
                    int userChoice = JOptionPane.showConfirmDialog(parentFrame, "Slang is already exist, do you want to duplicate?\n (if you choose No, the slang is overwrited)");
                    if (userChoice == 0) //Yes
                        controller.addSlang(slang, meaning, true);
                    else if (userChoice == 1) {//No
                        controller.addSlang(slang, meaning, false);
                    }
                }
            }
        });
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                slangTextField.setText("");
                meaningTextField.setText("");
                parentFrame.setContentPane(previous);
                parentFrame.validate();
            }
        });
        buttonGroup.add(addBtn);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(backBtn);
        JPanel buttonGroupPnl = new JPanel();
        buttonGroupPnl.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonGroupPnl.add(buttonGroup);

        topPanel.add(labelPanel, BorderLayout.PAGE_START);
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonGroupPnl, BorderLayout.PAGE_END);

        add(topPanel, BorderLayout.PAGE_START);
    }
}

class searchResultPanel extends JPanel {
    mainPanel previous;
    JFrame parentFrame;
    String searchBy;//"slang" | "definition"
    JLabel searchLabel;
    JPanel resultPanel;
    public searchResultPanel(mainPanel pre, JFrame parentFrame) {
        this.previous = pre;
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout());

        this.resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(this.resultPanel, BoxLayout.PAGE_AXIS));
        this.searchLabel = new JLabel("Search result");
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(this.searchLabel);

        JScrollPane resultSP = new JScrollPane(this.resultPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton backBtn = new JButton("Back");
        buttonPanel.add(backBtn);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(previous);
                parentFrame.validate();
            }
        });
        add(labelPanel, BorderLayout.PAGE_START);
        add(resultSP, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    void setSearchBy(String method) {
        this.searchBy = method;
    }

    String getSearchBy() {
        return searchBy;
    }

    void setSearchLabel(String keyword) {
        searchLabel.setText("Search result for \"" + keyword + "\"");
    }

    void setResultFindByMeaning(ArrayList<String[]> slangWords) {
        this.resultPanel.removeAll();
        if (slangWords.isEmpty()) {
            JTextArea noResult = new JTextArea();
            noResult.setWrapStyleWord(true);
            noResult.setEditable(false);
            noResult.setCaretColor(Color.WHITE);
            noResult.setText("No result found \nMake sure you type the correct keyword and then try again.");
            resultPanel.add(noResult);
        }
        for (String[] slangWord : slangWords)
            resultPanel.add(resultToPanel(slangWord));
    }

    void setResultFindBySlang(String[] slangWord) {
        this.resultPanel.removeAll();
        if (slangWord == null) {
            JTextArea noResult = new JTextArea();
            noResult.setWrapStyleWord(true);
            noResult.setEditable(false);
            noResult.setCaretColor(Color.WHITE);
            noResult.setText("No result found \nMake sure you type the correct keyword and then try again.");
            resultPanel.add(noResult);
        }
        else {
            resultPanel.add(resultToPanel(slangWord));
        }
    }

    JPanel resultToPanel(String[] data) {
        String slang = data[0];
        String meaning = data[1];
        JPanel resultItemPanel = new JPanel();
        JPanel resultPanelWrapper = new JPanel();
        resultItemPanel.setLayout(new BorderLayout());
        resultItemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel buttonGroupWrapper = new JPanel();
        buttonGroupWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.LINE_AXIS));
        buttonGroupWrapper.add(buttonGroup);
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");

        editSlangPanel editPanel = new editSlangPanel(previous, parentFrame);

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPanel.setContent(data);
                parentFrame.setContentPane(editPanel);
                parentFrame.validate();
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userChoice = JOptionPane.showConfirmDialog(parentFrame, "Do you really want to delete this slang word?");
                if (userChoice == 0) {
                    SlangFunction controller = previous.getController();
                    controller.deleteSlang(slang);
                    JOptionPane.showMessageDialog(parentFrame, "Delete success");
                    resultPanel.remove(resultPanelWrapper);
                    parentFrame.setContentPane(previous);
                    parentFrame.validate();
                }
            }
        });
        buttonGroup.add(editBtn);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(deleteBtn);

        JPanel slangPanel = new JPanel();
        slangPanel.setLayout(new BoxLayout(slangPanel, BoxLayout.PAGE_AXIS));
        JLabel slangLabel = new JLabel("Slang: " + slang);
        JLabel meaningLabel = new JLabel("Meaning:");
        slangPanel.add(slangLabel);
        slangPanel.add(meaningLabel);

        resultItemPanel.add(slangPanel, BorderLayout.CENTER);
        resultItemPanel.add(buttonGroupWrapper, BorderLayout.PAGE_END);
        for (String i : meaning.split("\\|")) {
            JLabel meaningText = new JLabel("-" + i.trim());
            slangPanel.add(meaningText);
        }

        resultPanelWrapper.setLayout(new BorderLayout());
        resultPanelWrapper.add(resultItemPanel, BorderLayout.PAGE_START);
        return resultPanelWrapper;
    }
}

class editSlangPanel extends JPanel {
    mainPanel previous;
    JFrame parentFrame;
    JPanel contentPanel;
    ArrayList<JTextField> textFieldList;
    String[] currentWord;
    public editSlangPanel(mainPanel pre, JFrame parent) {
        this.previous = pre;
        this.parentFrame = parent;

        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        textFieldList =  new ArrayList<JTextField>();

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel panelLabel = new JLabel("Add slang word");
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(panelLabel);

        JScrollPane slangEditPanel = new JScrollPane(contentPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        JPanel buttonPanelWrapper = new JPanel();
        buttonPanelWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanelWrapper.add(buttonPanel);
        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");
        buttonPanel.add(saveBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(backBtn);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SlangFunction controller = previous.getController();
                String[] newWord = new String[2];
                newWord[1] = "";
                newWord[0] = textFieldList.get(0).getText().trim();
                for (int i = 1; i < textFieldList.size(); i++)
                    if (!newWord[1].isEmpty())
                        newWord[1] = newWord[1] + " | " + textFieldList.get(i).getText().trim();
                    else
                        newWord[1] = textFieldList.get(i).getText().trim();
                controller.editSlang(currentWord, newWord);
                JOptionPane.showMessageDialog(parentFrame, "Edit slang word success");
                for (JTextField tf : textFieldList)
                    tf.setText("");
                parentFrame.setContentPane(previous);
                parentFrame.validate();
            }
        });
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(previous);
                parentFrame.validate();
            }
        });

        add(labelPanel, BorderLayout.PAGE_START);
        add(slangEditPanel, BorderLayout.CENTER);
        add(buttonPanelWrapper, BorderLayout.PAGE_END);
    }

    void setContent(String[] slangWord) {
        currentWord = slangWord;
        contentPanel.removeAll();
        textFieldList.clear();
        JPanel slangPanelWrapper = new JPanel();
        slangPanelWrapper.setLayout(new BorderLayout());
        JPanel slangPanel = new JPanel();
        slangPanel.setLayout(new BoxLayout(slangPanel, BoxLayout.PAGE_AXIS));
        slangPanelWrapper.add(slangPanel, BorderLayout.PAGE_START);

        SlangFunction controller = previous.getController();

        JPanel slangInput = new JPanel();
        slangInput.setLayout(new BoxLayout(slangInput, BoxLayout.LINE_AXIS));
        JLabel slangLabel = new JLabel("slang:");
        JTextField slangTF = new JTextField();
        slangTF.setText(slangWord[0]);
        slangInput.add(Box.createRigidArea(new Dimension(10, 0)));
        slangInput.add(slangLabel);
        slangInput.add(Box.createRigidArea(new Dimension(10, 0)));
        slangInput.add(slangTF);
        slangInput.add(Box.createRigidArea(new Dimension(10, 0)));

        slangPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        slangPanel.add(slangInput);
        textFieldList.add(slangTF);
        slangPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel meaningLabelWrapper = new JPanel();
        meaningLabelWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel meaningLabel = new JLabel("  Meaning:");
        meaningLabelWrapper.add(meaningLabel);
        slangPanel.add(meaningLabelWrapper);
        slangPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] meaningList = slangWord[1].split("\\|");
        for (int i = 0; i < meaningList.length; i++) {
            JPanel meaningWrapper = new JPanel();
            meaningWrapper.setLayout(new BoxLayout(meaningWrapper, BoxLayout.LINE_AXIS));
            JTextField meaningTF = new JTextField();
            meaningTF.setText(meaningList[i].trim());
            textFieldList.add(meaningTF);
            meaningWrapper.add(Box.createRigidArea(new Dimension(10, 0)));
            meaningWrapper.add(meaningTF);
            meaningWrapper.add(Box.createRigidArea(new Dimension(10, 0)));
            slangPanel.add(meaningWrapper);
            slangPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contentPanel.add(slangPanelWrapper);
    }
}

class mainPanel extends JPanel {
    SlangFunction controller;
    String filePath;
    JFrame parentFrame;
    public mainPanel(String filePath, JFrame parent) {
        this.parentFrame = parent;
        this.filePath = filePath;
        this.controller = new SlangFunction(filePath);

        parentFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.save();
            }
        });
        setLayout(new BorderLayout());

        JScrollPane search = createSearchSection();
        JPanel buttonGroup = createButtonGroup();
        JScrollPane randomSlang = createRandomSlang(controller.ramdomSlang());

        add(search, BorderLayout.PAGE_START);
        add(randomSlang, BorderLayout.CENTER);
        add(buttonGroup, BorderLayout.PAGE_END);
    }

    SlangFunction getController() {
        return controller;
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
        searchTypeCB.setPreferredSize(new Dimension(100, 25));
        JPanel searchType = new JPanel();
        JPanel searchTypeWrapper = new JPanel();
        searchTypeWrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchTypeWrapper.add(searchType);
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
        searchPanel.add(searchTypeWrapper);
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        searchResultPanel resultPanel = new searchResultPanel(this, parentFrame);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String type = searchTypeCB.getSelectedItem().toString();
                String keyword = searchTextField.getText();
                resultPanel.setSearchBy(type);
                resultPanel.setSearchLabel(keyword);

                if (keyword.isEmpty())
                    return;

                if (type.equals("slang")) {
                    String[] result = controller.findBySlang(keyword, true);
                    resultPanel.setResultFindBySlang(result);
                }
                else {
                    ArrayList<String[]> result = controller.findByMeaning(keyword, true);
                    resultPanel.setResultFindByMeaning(result);
                }
                searchTextField.setText("");
                parentFrame.setContentPane(resultPanel);
                parentFrame.validate();
            }
        });
        return new JScrollPane(searchPanel);
    }

    JPanel createButtonGroup() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.LINE_AXIS));

        JButton historyBtn = new JButton("History");
        JButton addBtn = new JButton("Add slang");
        JButton quizzBtn= new JButton("Quizz");
        JButton resetBtn = new JButton("Reset");

        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(historyBtn);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(addBtn);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(quizzBtn);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonGroup.add(resetBtn);
        buttonGroup.add(Box.createRigidArea(new Dimension(10, 0)));

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(buttonGroup);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        historyPanel historyPnl = new historyPanel(this, parentFrame);
        AddSlangPanel addSlangPnl = new AddSlangPanel(this, parentFrame);
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int userChoice = JOptionPane.showConfirmDialog(parentFrame, "Do you really want to reset what you have done so far?");
                if (userChoice == 0)
                    controller.reset();
            }
        });
        historyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyPnl.historyToString(controller.getHistory());
                parentFrame.setContentPane(historyPnl);
                parentFrame.validate();
            }
        });

        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.setContentPane(addSlangPnl);
                parentFrame.validate();
            }
        });
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

        randomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        randomPanel.add(labelPanel);
        randomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        randomPanel.add(textPane);
        randomPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        randomPanel.add(buttonPanel);
        randomPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        return new JScrollPane(randomPanel);
    }
}

class SlangUI extends JFrame {
    public SlangUI(String title) {
        this.setTitle(title);
    }

    public void createAndShowGUI(String filePath) {
        setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(500, 400));

        JPanel newContentPane = new mainPanel(filePath, this);

        newContentPane.setOpaque(true);
        this.setContentPane(newContentPane);
        this.pack();
        this.setVisible(true);
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
    String[] findBySlang(String keyword, boolean saveToHistory) {
        keyword = keyword.trim();
        if (saveToHistory)
            history.add(keyword); // add keyword to history ArrayList for history function
       if (slangHashMap.get(keyword) == null) {
           return null;
       }
       return data.get(slangHashMap.get(keyword));
    }

    ArrayList<String[]> findByMeaning(String keyword, boolean saveToHistory) {
        keyword = keyword.trim();
        if (saveToHistory)
            history.add(keyword); // add keyword to history ArrayList for history function
        HashSet<Integer> result = new HashSet<Integer>();
        for (String key : meaningHashMap.keySet())
            if (key.contains(keyword))
                result.add(meaningHashMap.get(key));
        ArrayList<String[]> slangList = new ArrayList<String[]>();
        for (int i : result) {
            slangList.add(data.get(i));
        }
        return slangList;
    }

    void addSlang(String slang, String meaning, boolean duplicate) {
        slang = slang.trim();
        meaning = meaning.trim();
        String[] newSlang = new String[2];
        newSlang[0] = slang;
        newSlang[1] = meaning;
        if (findBySlang(slang, false) == null) { // new slang
            slangHashMap.put(slang, data.size());
            meaningHashMap.put(meaning, data.size());
            data.add(newSlang);
            return;
        }
        String duplicateMeaning = data.get(slangHashMap.get(slang))[1];
        if (duplicate) { // duplicate meaning if slang exist
            duplicateMeaning = duplicateMeaning + " | " + meaning;
            newSlang[1] = duplicateMeaning;
            data.set(slangHashMap.get(slang), newSlang);
            return;
        }
        data.set(slangHashMap.get(slang), newSlang);
    }

    void editSlang(String[] oldWord, String[] newWord) {
        String oldSlang = oldWord[0].trim();
        String newSlang = newWord[0].trim();
        boolean existed = false;
        if (slangHashMap.get(newSlang) == null) {//change to a new slang
            int index = slangHashMap.get(oldSlang);
            slangHashMap.remove(oldSlang);
            for (String j : oldWord[1].split("\\|"))
                meaningHashMap.remove(j.trim());
            data.remove(index);

            slangHashMap.put(newSlang, index);
            for (String i : newWord[1].split("\\|"))
                meaningHashMap.put(i.trim(), index);
            data.add(index, newWord);
            return;
        }
        //Check if the user changes a slang word to an existing slang word
        if (slangHashMap.get(oldSlang) != slangHashMap.get(newSlang) && slangHashMap.get(newSlang) != null) {
            int index = slangHashMap.get(oldSlang);
            slangHashMap.remove(oldSlang);
            for (String j : oldWord[1].split("\\|"))
                meaningHashMap.remove(j.trim());
            data.remove(index);

            int existedWordIndex = slangHashMap.get(newSlang);
            String[] newMeaningList = newWord[1].split("\\|");
            for (int i = 0; i < newMeaningList.length; i++) {
                if (data.get(existedWordIndex)[1].contains(newMeaningList[i].trim())) {
                    continue;
                }
                meaningHashMap.put(newMeaningList[i].trim(), slangHashMap.get(newSlang));
                String[] newMeaning = data.get(existedWordIndex);
                if (!newMeaning[1].isEmpty())
                    newMeaning[1] = newMeaning[1] + " | " + newMeaningList[i].trim();
                else
                    newMeaning[1] = newMeaningList[i].trim();
                data.set(existedWordIndex, newMeaning);
            }
        }

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

    ArrayList<String> getHistory() {
        return history;
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
        SlangUI app = new SlangUI("Slang Word");
        app.createAndShowGUI(filePath);
    }
}
