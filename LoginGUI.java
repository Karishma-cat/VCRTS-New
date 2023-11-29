import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class LoginGUI extends JFrame 
{
    private Socket serverSocket;
    private JCheckBox ownerCheckBox;
    private ArrayList<RegisterAccountClick> userList = new ArrayList<>();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/VC3";
    private static final String DB_USER = "localhost";
    private static final String DB_PASSWORD = "Aniraam9835";

    public static void main(String[] args) {
        new LoginGUI();
    }

    public LoginGUI() 
    {
        initializeLoginGUI();
        setVisible(true);
    }

    private void initializeLoginGUI() 
    {
        setTitle("Welcome to the Vehicle Cloud Real Time System Login Page!");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JLabel welcomeLabel = createStyledLabel("Welcome to the Vehicular Cloud Real Time System");
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JLabel messageLabel = createStyledLabel("Please select one of the options below:");
        buttonPanel.add(messageLabel);

        JButton registerButton = createStyledButton("Register an account");
        buttonPanel.add(registerButton);

        JButton loginButton = createStyledButton("Login");
        buttonPanel.add(loginButton);

        //ImageIcon imageIcon = new ImageIcon("FordMustang.png");
        //JLabel imageLabel = new JLabel(imageIcon);
        //imageIcon.getScaledInstance(100, 100, imageIcon.SCALE_SMOOTh);

        //mainPanel.add(imageLabel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> RegisterAccountClick());
        loginButton.addActionListener(e -> LoginAccountClick());
    }

   private void RegisterAccountClick() 
   {
    JFrame registerButtonFrame = new JFrame("Register an account");
    registerButtonFrame.setSize(300, 350);

    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(10, 10));
    registerButtonFrame.add(panel);

    JPanel inputPanel = new JPanel();
    inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

    JLabel nameLabel = createStyledLabel("Please enter your full name:");
    JTextField nameTextField = new JTextField();
    JLabel setUserId = createStyledLabel("Please enter an ID (numbers only):");
    JTextField userIdTextField = new JTextField();

    inputPanel.add(nameLabel);
    inputPanel.add(nameTextField);
    inputPanel.add(setUserId);
    inputPanel.add(userIdTextField);

    ownerCheckBox = new JCheckBox("Are you an owner?");
    inputPanel.add(ownerCheckBox);

    panel.add(inputPanel, BorderLayout.CENTER);

    JButton submitRegistrationInfo = createStyledButton("Submit");
    panel.add(submitRegistrationInfo, BorderLayout.SOUTH);

    registerButtonFrame.setVisible(true);

    submitRegistrationInfo.addActionListener(x -> {
        String userName = nameTextField.getText();
        String ownerIdString = userIdTextField.getText();

        if (userName.isEmpty() || ownerIdString.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter both name and ID");
            return;
        }

        int ownerId = Integer.parseInt(ownerIdString);
        boolean isOwner = ownerCheckBox.isSelected();

        if (insertUserIntoDatabase(ownerId, userName, isOwner)) {
            JOptionPane.showMessageDialog(null, "You have been registered");
            userList.add(new RegisterAccountClick(ownerId, userName));
            registerButtonFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to register. Please try again.");
        }
    });
}
private void LoginAccountClick()
{
JFrame LoginAccountFrame = new JFrame("Login");
        LoginAccountFrame.setSize(300, 350);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        LoginAccountFrame.add(panel);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JLabel LoginNameLabel = createStyledLabel("Please enter your full name:");
        JTextField LoginNameText = new JTextField();
        JLabel userIDLabel = createStyledLabel("Please enter an ID (numbers only):");
        JTextField userIDText = new JTextField();

        inputPanel.add(LoginNameLabel);
        inputPanel.add(LoginNameText);
        inputPanel.add(userIDLabel);
        inputPanel.add(userIDText);

        JButton LoginToGUI = createStyledButton("Click To Login");

        LoginToGUI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fullName = LoginNameText.getText();
                String userID = userIDText.getText();

                boolean isOwner = checkIfOwner(fullName, userID);

                if (isOwner) {
                    OwnerGUI ownerGUI = new OwnerGUI();
                    ownerGUI.createOwnerGUI(serverSocket);
                } else {
                    ClientGUI clientGUI = new ClientGUI();
                    clientGUI.createClientGUI();
                }
            }

        });

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(LoginToGUI, BorderLayout.SOUTH);

        LoginAccountFrame.setVisible(true);
    }
    private boolean checkIfOwner(String fullName, String userID) {
        try (BufferedReader br = new BufferedReader(new FileReader("actionlog.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("User " + fullName) && br.readLine().startsWith("ID: " + userID)) {
                    String roleLine = br.readLine();
                    return roleLine != null && roleLine.contains("Owner");
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        return false;
    }

    // TRIED WORKING ON WRITING LOGIN TO THE TABLE. KEEPS CRASHING ME.
    private boolean insertUserIntoDatabase(int ownerId, String userName, boolean isOwner) {
        try {
            SwingUtilities.invokeLater(() -> {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");

                    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

                        String query = "INSERT INTO user_table (user_id, full_name, is_owner) VALUES (?, ?, ?)";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                            preparedStatement.setInt(1, ownerId);
                            preparedStatement.setString(2, userName);
                            preparedStatement.setBoolean(3, isOwner);

                            int rowsAffected = preparedStatement.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("User inserted successfully.");
                            } else {
                                System.out.println("User insertion failed.");
                            }
                        }
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }





    private JLabel createStyledLabel(String text) 
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(128, 0, 32));
        return label;
    }

    private JButton createStyledButton(String text) 
    {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 40));
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
}

class RegisterAccountClick 
{
    private int ownerId;
    private String userName;

    public RegisterAccountClick(int ownerId, String userName) 
    {
        this.ownerId = ownerId;
        this.userName = userName;
    }
}