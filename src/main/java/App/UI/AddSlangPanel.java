package App.UI;
import App.Function.SlangFunction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSlangPanel extends JPanel {
    MainPanel previous;
    JFrame parentFrame;
    public AddSlangPanel(MainPanel pre, JFrame parent) {
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
                    slangTextField.setText("");
                    meaningTextField.setText("");
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
