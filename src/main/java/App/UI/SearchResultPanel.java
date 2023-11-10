package App.UI;

import App.Function.SlangFunction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SearchResultPanel extends JPanel {
    MainPanel previous;
    JFrame parentFrame;
    String searchBy;//"slang" | "definition"
    JLabel searchLabel;
    JPanel resultPanel;
    public SearchResultPanel(MainPanel pre, JFrame parentFrame) {
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

    public void setResultFindBySlang(String[] slangWord) {
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

    public JPanel resultToPanel(String[] data) {
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

        EditSlangPanel editPanel = new EditSlangPanel(previous, parentFrame);

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