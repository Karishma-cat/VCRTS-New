import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class OwnerGUI extends LoginGUI {

    public void createOwnerGUI() {
        JFrame ownerGUILogin = new JFrame("Owner Panel");
        ownerGUILogin.setSize(300, 450);
        ownerGUILogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel ownerGUIWelcome = createStyledLabel("Please enter owner information");
        ownerGUIWelcome.setBounds(20, 20, 250, 30);
        ownerGUILogin.add(ownerGUIWelcome);

        // Create text fields for user input
        JTextField ownerIDTextField = createStyledTextField("Owner ID:");
        ownerIDTextField.setBounds(20, 70, 250, 30);
        ownerGUILogin.add(ownerIDTextField);

        JTextField vehicleInfoTextField = createStyledTextField("Vehicle Information:");
        vehicleInfoTextField.setBounds(20, 110, 250, 30);
        ownerGUILogin.add(vehicleInfoTextField);

        JTextField residencyTimeTextField = createStyledTextField("Residency Time:");
        residencyTimeTextField.setBounds(20, 150, 250, 30);
        ownerGUILogin.add(residencyTimeTextField);

        JButton submitButton = createStyledButton("Submit");
        submitButton.setBounds(20, 190, 250, 40);
        ownerGUILogin.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ownerID = ownerIDTextField.getText();
                String vehicleInfo = vehicleInfoTextField.getText();
                String residencyTime = residencyTimeTextField.getText();

                
                saveToActionLog(ownerID, vehicleInfo, residencyTime);

                
            }
        });

        ownerGUILogin.setLayout(null);
        ownerGUILogin.setVisible(true);
    }

    private void saveToActionLog(String ownerID, String vehicleInfo, String residencyTime) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("actionlog.txt", true))) {
            writer.write("Owner ID: " + ownerID + "\n");
            writer.write("Vehicle Information: " + vehicleInfo + "\n");
            writer.write("Residency Time: " + residencyTime + "\n");
            writer.write("------------------------\n");
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }

    private static JTextField createStyledTextField(String labelText) {
        JTextField textField = new JTextField("");
        textField.setBounds(20, 50, 250, 30);

        JLabel label = createStyledLabel(labelText);
        label.setBounds(20, 20, 250, 30);

        return textField;
    }

    private static JButton createStyledButton(String text) {
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
}
