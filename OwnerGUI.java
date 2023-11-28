import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class OwnerGUI extends LoginGUI {
    private Socket serverSocket; 

    public void createOwnerGUI(Socket serverSocket) {
        JFrame ownerGUILogin = new JFrame("Owner Panel");
        ownerGUILogin.setSize(300, 450);
        ownerGUILogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel ownerGUIWelcome = createStyledLabel("Please enter owner information");
        ownerGUIWelcome.setBounds(20, 20, 350, 30);
        ownerGUILogin.add(ownerGUIWelcome);

        JTextField ownerIDTextField = createStyledTextField("Owner ID:");
        ownerIDTextField.setBounds(20, 70, 350, 30);
        ownerGUILogin.add(ownerIDTextField);

        JTextField vehicleInfoTextField = createStyledTextField("Vehicle Information:");
        vehicleInfoTextField.setBounds(20, 110, 350, 30);
        ownerGUILogin.add(vehicleInfoTextField);

        JTextField residencyTimeTextField = createStyledTextField("Residency Time:");
        residencyTimeTextField.setBounds(20, 150, 350, 30);
        ownerGUILogin.add(residencyTimeTextField);

        JButton submitButton = createStyledButton("Submit");
        submitButton.setBounds(20, 190, 350, 40);
        ownerGUILogin.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ownerID = ownerIDTextField.getText();
                String vehicleInfo = vehicleInfoTextField.getText();
                String residencyTime = residencyTimeTextField.getText();
    
                writeToFile(ownerID, vehicleInfo);

                sendDataToServer(ownerID, vehicleInfo, residencyTime);
            }
        });
    
        ownerGUILogin.setLayout(null);
        ownerGUILogin.setVisible(true);
    }


    private void sendDataToServer(String ownerID, String vehicleInfo, String residencyTime) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            String data = ownerID + "," + vehicleInfo + "," + residencyTime;
            writer.println(data);

            String response = reader.readLine();
            System.out.println("Received response from server: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try (Socket serverSocket = new Socket("localhost", 12345)) {
            new OwnerGUI().createOwnerGUI(serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
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
    private static boolean writeToFile(String data, String fileName) 
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) 
        {
            writer.write(data);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}