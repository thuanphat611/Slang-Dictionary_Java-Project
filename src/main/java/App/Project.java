package App;

import App.UI.*;
import App.Function.SlangFunction;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class SlangUI extends JFrame {
    public SlangUI(String title) {
        this.setTitle(title);
    }

    public void createAndShowGUI(String filePath) {
        setDefaultLookAndFeelDecorated(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(500, 400));

        JPanel newContentPane = new MainPanel(filePath, this);

        newContentPane.setOpaque(true);
        this.setContentPane(newContentPane);
        this.pack();
        this.setVisible(true);
    }
}

public class Project {
    static final String filePath = "C:\\LTUD Java\\Project01\\slang.txt";
    public static void main(String[] args) {
        SlangUI app = new SlangUI("Slang Word");
        app.createAndShowGUI(filePath);
    }
}
