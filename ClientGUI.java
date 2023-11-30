import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ClientGUI extends LoginGUI {

    public void createClientGUI() {
        JFrame clientGUILogin = new JFrame("Client Panel");
        clientGUILogin.setSize(300, 650);
        clientGUILogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel clientGUIWelcome = createStyledLabel("Please select from one of the options below");
        clientGUIWelcome.setBounds(20, 20, 250, 30);
        clientGUILogin.add(clientGUIWelcome);

        JButton clientSubmitJob = createStyledButton("Submit a job");
        clientSubmitJob.setBounds(20, 70, 250, 40);
        clientGUILogin.add(clientSubmitJob);

        clientSubmitJob.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ClientGUI().ClientSubmitJobClick();
            }
        });

        clientGUILogin.setLayout(null);
        clientGUILogin.setVisible(true);
    }

    public void ClientSubmitJobClick() {
        JFrame ClientJobFrame = new JFrame("Job Submission");
        ClientJobFrame.setSize(400, 250);

        JLabel jobTitle = createStyledLabel("What is the name of the task?");
        jobTitle.setBounds(20, 20, 250, 30);
        ClientJobFrame.add(jobTitle);

        JTextField jobTitleTextField = new JTextField("");
        jobTitleTextField.setBounds(20, 50, 250, 30);
        ClientJobFrame.add(jobTitleTextField);

        JLabel jobDuration = createStyledLabel("Approximate duration of task (in minutes):");
        jobDuration.setBounds(20, 90, 250, 30);
        ClientJobFrame.add(jobDuration);

        JTextField jobDurationTextField = new JTextField("");
        jobDurationTextField.setBounds(20, 120, 250, 30);
        ClientJobFrame.add(jobDurationTextField);

        JLabel jobDeadline = createStyledLabel("Job Deadline: (mm/dd/yyyy)");
        jobDeadline.setBounds(20, 160, 250, 30);
        ClientJobFrame.add(jobDeadline);

        JTextField jobDeadlineTextField = new JTextField("");
        jobDeadlineTextField.setBounds(20, 190, 250, 30);
        ClientJobFrame.add(jobDeadlineTextField);

        JButton submitButton = createStyledButton("Submit");
        submitButton.setBounds(150, 230, 100, 40);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String taskName = jobTitleTextField.getText();
                String duration = jobDurationTextField.getText();
                String deadline = jobDeadlineTextField.getText();

                System.out.println("Task Name: " + taskName);
                System.out.println("Duration: " + duration + " minutes");
                System.out.println("Deadline: " + deadline);
                
                
            }
        });
        ClientJobFrame.add(submitButton);

        ClientJobFrame.setLayout(null);
        ClientJobFrame.setVisible(true);
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setPreferredSize(new Dimension(250, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(128, 0, 32));
        button.setForeground(new Color(255, 255, 255));

        LineBorder border = new LineBorder(Color.PINK, 2);
        button.setBorder(border);

        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setOpaque(true);

        return button;
    }

    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(128, 0, 32));
        return label;
    }
    private void savetoDataBase(String taskName, String duration, String deadline){
        String url = "jdbc:mysql://localhost:3306/VC3";
        String user = "localhost";
        String pass =  "Aniram9835";

        try (Connection connection = DriverManager.getConnection(url, user, pass)){
            String sql = "INSERT INTO user_table(task_name, duration, deadline) VALUES (?, ?, ?)";
            try( PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, taskName);
                statement.setString(2, duration);
                statement.setString(3, deadline);

                statement.executeUpdate();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
