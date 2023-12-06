import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class ClientGUI extends LoginGUI {
    String messageOut = "";
    String messageIn = "";
    ServerSocket serverSocket;
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    static Job subJob1;

    public void createClientGUI() {
        JFrame clientGUILogin = new JFrame("Client Panel");
        clientGUILogin.setSize(300, 650);
        clientGUILogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel clientGUIWelcome = createStyledLabel("Please select from one of the options below: ");
        clientGUIWelcome.setBounds(20, 20, 250, 30);
        clientGUILogin.add(clientGUIWelcome);

        JButton clientSubmitJob = createStyledButton("Submit a Job");
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
        ClientJobFrame.setSize(600, 350);

        JLabel jobID = createStyledLabel("Client ID:");
        jobID.setBounds(20, 20, 250, 30);
        ClientJobFrame.add(jobID);

        JLabel jobDuration = createStyledLabel("Approximate duration of Task (in minutes):");
        jobDuration.setBounds(20, 90, 250, 30);
        ClientJobFrame.add(jobDuration);

        JTextField jobDurationTextField = new JTextField("");
        jobDurationTextField.setBounds(20, 120, 250, 30);
        ClientJobFrame.add(jobDurationTextField);

        JLabel jobDeadline = createStyledLabel("Job Deadline:(mm/dd/yyyy) ");
        jobDeadline.setBounds(20, 160, 250, 30);
        ClientJobFrame.add(jobDeadline);

        JTextField jobDeadlineTextField = new JTextField("");
        jobDeadlineTextField.setBounds(20, 190, 250, 30);
        ClientJobFrame.add(jobDeadlineTextField);

        JButton submitButton = createStyledButton("Submit");
        submitButton.setBounds(20, 230, 100, 40);

        JTextField clientIdField = new JTextField("");
        clientIdField.setBounds(20, 60, 200, 30);
        ClientJobFrame.add(clientIdField);

        JButton calButton = createStyledButton("Calculate completion time");
        calButton.setBounds(20, 290, 250, 30);
        calButton.setPreferredSize(new Dimension(200, 40));
        calButton.setFont(new Font("Arial", Font.BOLD, 14));
        ClientJobFrame.add(calButton);

        ClientJobFrame.add(submitButton);
        ClientJobFrame.setLayout(null);
        ClientJobFrame.setVisible(true);

        calButton.addActionListener(event -> {
            int clientID = Integer.parseInt(clientIdField.getText());
            String deadline = jobDeadlineTextField.getText();
            int duration = Integer.parseInt(jobDurationTextField.getText());
            int completionTime = VC.calcCompTime(duration);
            Job subJob = new Job(clientID, VC.getSize() + 1, duration, deadline, completionTime);
            if (!subJob.equals(subJob1)) {
                subJob1 = subJob;
            }
            System.out.println("Calculated Time: " + subJob1.getCompletionTime());
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientid = clientIdField.getText();
                String duration = jobDurationTextField.getText();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date deadline = null;
                try {
                    deadline = dateFormat.parse(jobDeadlineTextField.getText());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
                messageOut = "job" + "," + clientid + "," + duration + "," + deadline;
                try {
                    socket = new Socket("localhost", 9808);
                    inputStream = new DataInputStream(socket.getInputStream());
                    outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.writeUTF(messageOut);
                    messageIn = inputStream.readUTF();
                    if (messageIn.equals("Accept")) {
                        JOptionPane.showMessageDialog(null, "Job has been submitted");
                        ClientJobFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Job was not submitted");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                System.out.println("Client ID: " + clientid);
                System.out.println("Duration: " + duration + " minutes");
                System.out.println("Deadline: " + deadline);

                savetoDataBase(clientid, duration, deadline);

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

    private void savetoDataBase(String clientid, String duration, Date deadline) {
        String url = "jdbc:mysql://localhost:3306/VC3";
        String user = "root";
        String pass = "Aniram9835";
        java.sql.Date sqlDate = new java.sql.Date(deadline.getTime());

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {
            String sql = "INSERT INTO clientSubmissions(client_ID, duration_minutes, submission_date) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, clientid);
                statement.setString(2, duration);
                statement.setDate(3, sqlDate);

                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Data has been saved");

                } else {
                    System.out.println("Data failed to save");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
