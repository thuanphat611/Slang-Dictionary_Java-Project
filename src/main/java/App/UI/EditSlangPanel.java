package App.UI;

import App.Function.SlangFunction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditSlangPanel extends JPanel {
    MainPanel previous;
    JFrame parentFrame;
    JPanel contentPanel;
    ArrayList<JTextField> textFieldList;
    String[] currentWord;
    public EditSlangPanel(MainPanel pre, JFrame parent) {
        this.previous = pre;
        this.parentFrame = parent;

        setLayout(new BorderLayout());
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        textFieldList =  new ArrayList<JTextField>();

        JLabel panelLabel = new JLabel("Edit slang word");
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

    public void setContent(String[] slangWord) {
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
