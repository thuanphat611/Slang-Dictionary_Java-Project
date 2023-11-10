package App.UI;

import App.Function.SlangFunction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MainPanel extends JPanel {
    SlangFunction controller;
    String filePath;
    JFrame parentFrame;
    public MainPanel(String filePath, JFrame parent) {
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

    public SlangFunction getController() {
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

        SearchResultPanel resultPanel = new SearchResultPanel(this, parentFrame);
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

        HistoryPanel historyPnl = new HistoryPanel(this, parentFrame);
        AddSlangPanel addSlangPnl = new AddSlangPanel(this, parentFrame);
        SlangQuizzPanel quizzPnl = new SlangQuizzPanel(this, parentFrame);
        quizzBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ChooseQuizzTypePanel userChoice = new ChooseQuizzTypePanel(quizzPnl.previous, parentFrame);
                String[] options = {"Slang Quizz", "Definition Quizz"};
                int quizzMode = JOptionPane.showOptionDialog(parentFrame, "What kind of quizz you wanna try?", "Quizz mode", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                quizzPnl.createQuizz(quizzMode + 1);
                parentFrame.setContentPane(quizzPnl);
                parentFrame.validate();
            }
        });
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
