package App.UI;

import App.Function.SlangFunction;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SlangQuizzPanel extends JPanel {
    MainPanel previous;
    JFrame parentFrame;
    JPanel answersPanel;
    JLabel questionNumber;
    ArrayList<String[]> questionList;
    int currentQuestion;
    int numCorrect;
    public SlangQuizzPanel(MainPanel pre, JFrame parent) {
        this.previous = pre;
        this.parentFrame = parent;
        SlangFunction controller = previous.getController();
        this.questionList = controller.slangQuizz();
        this.currentQuestion = 1;
        this.numCorrect = 0;
        answersPanel = new JPanel();
        answersPanel.setLayout(new BoxLayout(answersPanel, BoxLayout.PAGE_AXIS));
        questionNumber = new JLabel();

        setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(questionNumber);

        JScrollPane answerPanelSP = new JScrollPane(answersPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        JPanel buttonPanelWrapper = new JPanel();
        buttonPanelWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanelWrapper.add(buttonPanel);
        JButton exitBtn = new JButton("Exit");
        buttonPanel.add(exitBtn);
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentQuestion = 1;
                questionList = controller.slangQuizz();
                numCorrect = 0;
                createQuizz(2);
                parentFrame.setContentPane(previous);
                parentFrame.validate();
            }
        });

        add(labelPanel, BorderLayout.PAGE_START);
        add(answerPanelSP, BorderLayout.CENTER);
        add(buttonPanelWrapper, BorderLayout.PAGE_END);
    }

    void createQuizz(int type) {
        questionNumber.setText("Question " + currentQuestion + "/5");
        answersPanel.removeAll();

        int answerIndex = (int) (Math.random() * 4);

        JLabel questionLabel = new JLabel();
        if (type == 1)
            questionLabel.setText(questionList.get(answerIndex)[0] + " means");
        else
            questionLabel.setText("slang for " + questionList.get(answerIndex)[1].split("\\|")[0].trim());

        JPanel question = new JPanel();
        question.setLayout(new FlowLayout(FlowLayout.CENTER));
        question.add(questionLabel);
        JScrollPane questionSP = new JScrollPane(question);
        answersPanel.add(questionSP);
        answersPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        if (type == 1)
            for (int i  = 0; i < questionList.size(); i++) {
                int current = i;
                JPanel answer = new JPanel();
                answer.setLayout(new BorderLayout());
                JButton answerBtn = new JButton(questionList.get(i)[1].split("\\|")[0].trim());
                answer.add(answerBtn, BorderLayout.CENTER);
                answersPanel.add(answer);
                answersPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                answerBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean isAnswer = false;
                        if (current == answerIndex)
                            isAnswer = true;
                        if (isAnswer)
                            numCorrect++;
                        currentQuestion++;

                        if (currentQuestion <= 5) {
                            SlangFunction controller = previous.getController();
                            questionList = controller.slangQuizz();
                            createQuizz(1);
                        }
                        else {
                            JOptionPane.showMessageDialog(parentFrame, "Your score: " + numCorrect + "/5");
                            currentQuestion = 1;
                            numCorrect = 0;
                            createQuizz(1);
                            parentFrame.setContentPane(previous);
                            parentFrame.validate();
                        }

                    }
                });
            }
        else
            for (int i  = 0; i < questionList.size(); i++) {
                int current = i;
                JPanel answer = new JPanel();
                answer.setLayout(new BorderLayout());
                JButton answerBtn = new JButton(questionList.get(i)[0].trim());
                answer.add(answerBtn, BorderLayout.CENTER);
                answersPanel.add(answer);
                answersPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                answerBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean isAnswer = false;
                        if (current == answerIndex)
                            isAnswer = true;
                        if (isAnswer)
                            numCorrect++;
                        currentQuestion++;

                        if (currentQuestion <= 5) {
                            SlangFunction controller = previous.getController();
                            questionList = controller.slangQuizz();
                            createQuizz(2);
                        }
                        else {
                            JOptionPane.showMessageDialog(parentFrame, "Your score: " + numCorrect + "/5");
                            currentQuestion = 1;
                            numCorrect = 0;
                            createQuizz(2);
                            parentFrame.setContentPane(previous);
                            parentFrame.validate();
                        }

                    }
                });
            }
    }
}
