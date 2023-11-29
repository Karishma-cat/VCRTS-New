import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Logingui extends JFrame {
    private JTextField usernameField;
    private JCheckBox ownerCheckBox;

    private ArrayList<vehicleowner> ownerList;

    public Logingui(ArrayList<vehicleowner> ownerList) {
        this.ownerList = ownerList;

        initializeLoginGUI();
    }

    // This method creates and configures a styled JButton.
    private static JButton createStyledButton(String text) {
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

    // This method creates and configures a styled JLabel.
    private static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(new Color(128, 0, 32));

        return label;
    }

    private void initializeLoginGUI() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = createStyledLabel("Username:");
        loginPanel.add(usernameLabel);

        usernameField = new JTextField();
        loginPanel.add(usernameField);

        ownerCheckBox = new JCheckBox("Owner");
        loginPanel.add(ownerCheckBox);

        JButton loginButton = createStyledButton("Login");
        loginPanel.add(loginButton);

        loginPanel.add(new JLabel());

        add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                boolean isOwner = ownerCheckBox.isSelected();
                authenticateUser(username, isOwner);
            }
        });

        setVisible(true);
    }

    private void authenticateUser(String username, boolean isOwner) {
        if (isOwner) {
            // Show Owner Panel
            showOwnerPanel();
        } else {
            // Show Client Panel
            showClientPanel();
        }
    
        //Open the VRCTSJFrame GUI and pass the isOwner information
        VRCTSJFrame vrctsJFrame = new VRCTSJFrame();
       vrctsJFrame.initializeGUI();
    
        // Save user input to the action log file
        saveUserInfoToLogFile(username, isOwner);
    
        // Close the login window
        dispose();
    }
    

    private void saveUserInfoToLogFile(String username, boolean isOwner) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        String timestamp = currentTime.format(formatter);

        String userType = isOwner ? "Owner" : "Client";

        // Prepare data for writing to a file
        String data = "Timestamp: " + timestamp + "\n" +
                "Username: " + username + "\n" +
                "User Type: " + userType + "\n";

        String fileName = "actionlog.txt";
        writeToFile(data, fileName);
    }

    private static boolean writeToFile(String data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showOwnerPanel() {
        JOptionPane.showMessageDialog(this, "Welcome, Owner!");
        dispose(); // Closes the login window
    }

    private void showClientPanel() {
        JOptionPane.showMessageDialog(this, "Welcome, Client!");
    }

    public static void main(String[] args) {
        // testing
        ArrayList<vehicleowner> ownerList = new ArrayList<>();
        Logingui logingui = new Logingui(ownerList);
    }
}

