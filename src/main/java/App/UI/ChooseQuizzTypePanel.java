package App.UI;

import javax.swing.*;
import java.awt.*;

public class ChooseQuizzTypePanel extends JPanel {
    MainPanel previous;
    JFrame parentFrame;
    public ChooseQuizzTypePanel(MainPanel pre, JFrame parent) {
        this.previous = pre;
        this.parentFrame = parent;

        setPreferredSize(new Dimension(300, 200));
    }
}
