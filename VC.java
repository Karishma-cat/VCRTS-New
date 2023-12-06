import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class VC {
    static ServerSocket serverSocket;
    static Socket socket;
    static DataInputStream inputStream;
    static DataOutputStream outputStream;
    static Connection databaseConnection;
    static String url = "jdbc:mysql://localhost:3306/VC3?useTimezone=true&serverTimezone=UTC";
    static String username = "root";
    static String password = "Aniram9835";
    static Date third1;

    private static void saveOwnerToDB(String ownerID, String vehicleInfo, String residencyTime) {
        try {
            String insertQuery = "INSERT INTO jobSubmissions(owner_id, vehicle_info, residency_time, status) VALUES (?, ?, ?, 'Accepted')";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, ownerID);
                preparedStatement.setString(2, vehicleInfo);
                preparedStatement.setString(3, residencyTime);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void saveJobsToDB(String clientID, String duration, Date deadLine) {
        java.sql.Date sqlDate = new java.sql.Date(deadLine.getTime());
        try {
            String insertQuery = "INSERT INTO clientSubmissions(client_ID, duration_minutes, submission_date) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = databaseConnection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, clientID);
                preparedStatement.setString(2, duration);
                preparedStatement.setDate(3, sqlDate);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String messageIn= "";
        String messageOut = "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            // creating the server
            serverSocket = new ServerSocket(9808);
            databaseConnection = DriverManager.getConnection(url, username, password);

            socket = serverSocket.accept();

            while (!messageIn.equals("end")) {
                    // sever accepts connection request from client
                    socket = serverSocket.accept();

                    // server reads a message message from client
                    inputStream = new DataInputStream(socket.getInputStream());

                    // server sends a message to client
                    outputStream = new DataOutputStream(socket.getOutputStream());

                    messageIn = inputStream.readUTF();

                    String[] parts = messageIn.split(",");
                    String determine= parts[0];
                    String first = parts[1];
                    String second = parts[2];
                    String third = parts[3];

                    JFrame serverChoice = new JFrame("Server choice");
                    serverChoice.setSize(300, 450);
                    JButton pass = new JButton("Accept request");
                    pass.setBounds(20, 260, 250, 30);
                    serverChoice.add(pass);
                    JButton deny = new JButton("Reject request");
		            deny.setBounds(20, 320, 250, 30);
                    serverChoice.add(deny);

                    pass.addActionListener(x ->{
                        try {
                        third1 = dateFormat.parse(third);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (determine.equals("job")) {
                        Date completionTime = calculateCompletionTime(third1, second);
                        System.out.println("Job Completion Time: " + completionTime);
                    } else if (determine.equals("owner")) {
                        saveOwnerToDB(first, second, third);
                    }
                    serverChoice.dispose();
                    try{
                    outputStream.writeUTF("Accept");
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    });

                    deny.addActionListener(x -> {
                        try {
                            outputStream.writeUTF("deny");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        serverChoice.dispose();
                    
                    });

                }}catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static Date calculateCompletionTime(Date deadLine, String Duration) {
        long duration_minutes = Long.parseLong(Duration) * 60 * 1000;
        return new Date(deadLine.getTime() + duration_minutes);
    }
}
