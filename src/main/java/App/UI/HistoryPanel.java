package App.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HistoryPanel extends JPanel {
    JPanel previous;
    JFrame parentFrame;
    JPanel slangHistory;
    public HistoryPanel(JPanel pre, JFrame parentFrame) {
        this.previous = pre;
        this.parentFrame = parentFrame;
        slangHistory = new JPanel();
        slangHistory.setLayout(new BoxLayout(slangHistory, BoxLayout.PAGE_AXIS));
        JPanel slangHistoryWrapper = new JPanel();
        slangHistoryWrapper.setLayout(new BorderLayout());
        slangHistoryWrapper.add(slangHistory, BorderLayout.PAGE_START);

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Search history");
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(label);

        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.LINE_AXIS));

        JScrollPane textSP = new JScrollPane(slangHistoryWrapper);

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

    public void historyToString(ArrayList<String[]> history) {
        if (history == null) {
            return;
        }
        if (history.isEmpty()) {
            return;
        }
        slangHistory.removeAll();
        for (int i = 0; i < history.size(); i++) {
            JPanel slangPanel = new JPanel();
            slangPanel.setLayout(new BoxLayout(slangPanel, BoxLayout.PAGE_AXIS));
            JLabel slangLabel = new JLabel("Slang: " + history.get(i)[0].trim());
            JLabel meaningLabel = new JLabel("Meaning:");
            slangPanel.add(slangLabel);
            slangPanel.add(meaningLabel);
            for (int j = 0; j < history.get(i)[1].split("\\|").length; j++) {
                JLabel meaningItem = new JLabel("-" + history.get(i)[1].split("\\|")[j].trim());
                slangPanel.add(meaningItem);
            }
            JPanel slangPanelWrapper = new JPanel();
            slangPanelWrapper.setLayout(new BorderLayout());
            slangPanelWrapper.add(slangPanel, BorderLayout.PAGE_START);
            slangPanelWrapper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            slangHistory.add(slangPanelWrapper);
        }
    }
}