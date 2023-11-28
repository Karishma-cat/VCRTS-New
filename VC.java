import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VC {
    private ServerSocket serverSocket;
    private boolean acceptingRequests;
    private Connection databaseConnection;

    public VC(int port) {
        try {
            serverSocket = new ServerSocket(port);
            acceptingRequests = true;
            databaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/SoftwareEng", "x", "x");
            System.out.println("VC Controller is running and waiting for connections on port " + port);

            new Thread(this::acceptClientConnections).start();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void acceptClientConnections() {
        while (acceptingRequests) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {

            String dataFromClient = reader.readLine();
            System.out.println("Received data from client: " + dataFromClient);

            boolean authorizeData = authorizeData(dataFromClient);


            String response = authorizeData ? "Accepted" : "Rejected";
            writer.println(response);
            System.out.println("Sent response to client: " + response);


            if (authorizeData) {
                insertDataIntoDatabase(dataFromClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean authorizeData(String data) {
        return true;
    }

    private void insertDataIntoDatabase(String data) {
        try {
            String[] parts = data.split(",");
            String ownerID = parts[0];
            String vehicleInfo = parts[1];
            String residencyTime = parts[2];

            String insertQuery = "INSERT INTO job_submissions (owner_id, vehicle_info, residency_time, status) VALUES (?, ?, ?, 'Accepted')";
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

    public void stop() {
        acceptingRequests = false;
        try {
            serverSocket.close();
            databaseConnection.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 12345; 
        VC vc = new VC(port);

    }
}
