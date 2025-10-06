package app.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.json.JSONException;

import api.MongoGradeDataBase;
import app.Config;
import entity.Grade;
import usecase.*;

/**
 * GUI class to run the GUI for the Grade App.
 */
public class Application {
    static final int ROWS = 4;
    static final int COLS = 2;
    static final int WIDTH = 850;
    static final int HEIGHT = 300;

    /**
     * Main method to run the GUI.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {

        // Initial setup for the program.
        // The config hides the details of which implementation of GradeDB
        // we are using in the program. If we were to use a different implementation
        // of GradeDB, this config is what we would change.
        final Config config = new Config();

        final GetGradeUseCase getGradeUseCase = config.getGradeUseCase();
        final LogGradeUseCase logGradeUseCase = config.logGradeUseCase();
        final FormTeamUseCase formTeamUseCase = config.formTeamUseCase();
        final JoinTeamUseCase joinTeamUseCase = config.joinTeamUseCase();
        final LeaveTeamUseCase leaveTeamUseCase = config.leaveTeamUseCase();
        final GetAverageGradeUseCase getAverageGradeUseCase = config.getAverageGradeUseCase();
        final GetTopGradeUseCase getTopGradeUseCase = config.getTopGradeUseCase();

        // this is the code that runs to set up our GUI
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new JFrame("Grade GUI App");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setSize(WIDTH, HEIGHT);

            final CardLayout cardLayout = new CardLayout();
            final JPanel cardPanel = new JPanel(cardLayout);

            final JPanel defaultCard = createDefaultCard();
            final JPanel getGradeCard = createGetGradeCard(frame, getGradeUseCase);
            final JPanel logGradeCard = createLogGradeCard(frame, logGradeUseCase);
            final JPanel formTeamCard = createFormTeamCard(frame, formTeamUseCase);
            final JPanel joinTeamCard = createJoinTeamCard(frame, joinTeamUseCase);
            final JPanel manageTeamCard = createManageTeamCard(frame, leaveTeamUseCase, getAverageGradeUseCase, getTopGradeUseCase);

            cardPanel.add(defaultCard, "DefaultCard");
            cardPanel.add(getGradeCard, "GetGradeCard");
            cardPanel.add(logGradeCard, "LogGradeCard");
            cardPanel.add(formTeamCard, "FormTeamCard");
            cardPanel.add(joinTeamCard, "JoinTeamCard");
            cardPanel.add(manageTeamCard, "ManageTeamCard");

            final JButton getGradeButton = new JButton("Get Grade");
            getGradeButton.addActionListener(new ActionListener() {
                /**
                * Invoked when an action occurs.
                *
                * @param e the event to be processed
                */
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "GetGradeCard");
                }
            });

            final JButton logGradeButton = new JButton("Log Grade");
            logGradeButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "LogGradeCard");
                }
            });

            final JButton formTeamButton = new JButton("Form a team");
            formTeamButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "FormTeamCard");
                }
            });

            final JButton joinTeamButton = new JButton("Join a team");
            joinTeamButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "JoinTeamCard");
                }
            });

            final JButton manageTeamButton = new JButton("My Team");
            manageTeamButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when an action occurs.
                 *
                 * @param e the event to be processed
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(cardPanel, "ManageTeamCard");
                }
            });

            final JPanel buttonPanel = new JPanel();
            buttonPanel.add(getGradeButton);
            buttonPanel.add(logGradeButton);
            buttonPanel.add(formTeamButton);
            buttonPanel.add(joinTeamButton);
            buttonPanel.add(manageTeamButton);

            frame.getContentPane().add(cardPanel, BorderLayout.CENTER);
            frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

            frame.setVisible(true);

        });
    }

    // utility methods that take care of setting up each JPanel to be displayed
    // in our GUI
    private static JPanel createDefaultCard() {
        final JPanel defaultCard = new JPanel();
        defaultCard.setLayout(new GridBagLayout());

        final JLabel infoLabel = new JLabel(String.format("<html>Welcome to the Grade App!<br><br>"
                + "Your api_token is:<br><br>%s</html>", MongoGradeDataBase.getAPIToken()));

        defaultCard.add(infoLabel);

        return defaultCard;
    }

    private static JPanel createGetGradeCard(JFrame jFrame, GetGradeUseCase getGradeUseCase) {
        final JPanel getGradeCard = new JPanel();
        getGradeCard.setLayout(new GridLayout(ROWS, COLS));

        final JTextField usernameField = new JTextField(20);
        final JTextField courseField = new JTextField(20);
        final JButton getButton = new JButton("Get");

        final JLabel resultLabel = new JLabel();

        getButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String username = usernameField.getText();
                final String course = courseField.getText();
                try {
                    final Grade grade = getGradeUseCase.getGrade(username, course);
                    JOptionPane.showMessageDialog(jFrame, String.format("Grade: %d", grade.getGrade()));
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });

        getGradeCard.add(new JLabel("Username:"));
        getGradeCard.add(usernameField);
        getGradeCard.add(new JLabel("Course:"));
        getGradeCard.add(courseField);
        getGradeCard.add(getButton);
        getGradeCard.add(resultLabel);

        return getGradeCard;
    }

    private static JPanel createLogGradeCard(JFrame jFrame, LogGradeUseCase logGradeUseCase) {
        final JPanel logGradeCard = new JPanel();
        logGradeCard.setLayout(new GridLayout(ROWS, COLS));
        final JTextField courseField = new JTextField(20);
        final JTextField gradeField = new JTextField(20);
        final JButton logButton = new JButton("Log");
        final JLabel resultLabel = new JLabel();

        logButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String course = courseField.getText();
                final String gradeStr = gradeField.getText();
                final int grade = Integer.parseInt(gradeStr);

                try {
                    logGradeUseCase.logGrade(course, grade);
                    JOptionPane.showMessageDialog(jFrame, "Grade Added successfully.");
                    courseField.setText("");
                    gradeField.setText("");
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });
        logGradeCard.add(new JLabel("Course:"));
        logGradeCard.add(courseField);
        logGradeCard.add(new JLabel("Grade:"));
        logGradeCard.add(gradeField);
        logGradeCard.add(logButton);
        logGradeCard.add(resultLabel);
        return logGradeCard;
    }

    private static JPanel createFormTeamCard(JFrame jFrame, FormTeamUseCase formTeamUseCase) {
        final JPanel theCard = new JPanel();
        theCard.setLayout(new GridLayout(ROWS, COLS));
        final JTextField nameField = new JTextField(20);
        final JButton submitButton = new JButton("Submit");
        final JLabel resultLabel = new JLabel();

        submitButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String name = nameField.getText();

                try {
                    formTeamUseCase.formTeam(name);
                    JOptionPane.showMessageDialog(jFrame, "Team formed!");
                    nameField.setText("");
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });
        theCard.add(new JLabel("Name (please choose a unique team name):"));
        theCard.add(nameField);
        theCard.add(submitButton);
        theCard.add(resultLabel);
        return theCard;
    }

    private static JPanel createJoinTeamCard(JFrame jFrame, JoinTeamUseCase joinTeamUseCase) {
        final JPanel theCard = new JPanel();
        theCard.setLayout(new GridLayout(ROWS, COLS));
        final JTextField nameField = new JTextField(20);
        final JButton submitButton = new JButton("Submit");
        final JLabel resultLabel = new JLabel();

        submitButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String name = nameField.getText();

                try {
                    joinTeamUseCase.joinTeam(name);
                    JOptionPane.showMessageDialog(jFrame, "Joined successfully");
                    nameField.setText("");
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });
        theCard.add(new JLabel("The team name:"));
        theCard.add(nameField);
        theCard.add(submitButton);
        theCard.add(resultLabel);
        return theCard;
    }

    // TODO Task 4: modify this method so that it takes in a getTopGradeUseCase
    //              Note: this will require you to update the code that calls this method.
    private static JPanel createManageTeamCard(JFrame jFrame, LeaveTeamUseCase leaveTeamUseCase,
                                               GetAverageGradeUseCase getAverageGradeUseCase, GetTopGradeUseCase getTopGradeUseCase) {
        final JPanel theCard = new JPanel();
        theCard.setLayout(new GridLayout(ROWS, COLS));
        final JTextField courseField = new JTextField(20);
        // make a separate line.
        final JButton getAverageButton = new JButton("Get Average Grade");
        // TODO Task 4: Add another button for "Get Top Grade" (check the getAverageButton for example)
        final JButton getTopButton = new JButton("Get Top Grade");

        final JButton leaveTeamButton = new JButton("Leave Team");
        final JLabel resultLabel = new JLabel();

        getAverageButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String course = courseField.getText();

                try {
                    final float avg = getAverageGradeUseCase.getAverageGrade(course);
                    JOptionPane.showMessageDialog(jFrame, "Average Grade: " + avg);
                    courseField.setText("");
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });

        // TODO Task 4: Add action listener for getTopGrade button, follow example of getAverageButton
        getTopButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                final String course = courseField.getText();

                try {
                    final float top = getTopGradeUseCase.getTopGrade(course);
                    JOptionPane.showMessageDialog(jFrame, "Top Grade: " + top);
                    courseField.setText("");
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });

        leaveTeamButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    leaveTeamUseCase.leaveTeam();
                    JOptionPane.showMessageDialog(jFrame, "Left team successfully.");
                }
                catch (JSONException ex) {
                    JOptionPane.showMessageDialog(jFrame, ex.getMessage());
                }
            }
        });

        theCard.add(new JLabel("The course you want to calculate the team average for:"));
        theCard.add(courseField);
        theCard.add(getAverageButton);
        theCard.add(getTopButton);
        theCard.add(leaveTeamButton);
        theCard.add(resultLabel);
        return theCard;

    }
}
