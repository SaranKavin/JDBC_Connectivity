package java_programs;
import java.sql.*;
import java.util.Scanner;

public class VehicleManagementSystem {
    
    private static final String URL = "jdbc:mysql://localhost:3306/VehicleMarket";
    private static final String USER = "root";
    private static final String PASSWORD = "Root123";
   
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            boolean boo = true;

            while (boo) {
                System.out.println("Vehicle Management System");
                System.out.println("1. Add a Vehicle");
                System.out.println("2. Update Vehicle as Sold");
                System.out.println("3. Delete a Vehicle");
                System.out.println("4. List All Vehicles");
                System.out.println("5. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        addVehicle(connection, scanner);
                        break;
                    case 2:
                        updateVehicleAsSold(connection, scanner);
                        break;
                    case 3:
                        deleteVehicle(connection, scanner);
                        break;
                    case 4:
                        listAllVehicles(connection);
                        break;
                    case 5:
                        boo = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            scanner.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    	private static void addVehicle(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter vehicle type (car, bike, etc.): ");
        String type = scanner.nextLine();
        System.out.print("Enter brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter model: ");
        String model = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        String query = "INSERT INTO Vehicles (type, brand, model, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, type);
            pstmt.setString(2, brand);
            pstmt.setString(3, model);
            pstmt.setDouble(4, price);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Vehicle added successfully. Rows affected: " + rowsAffected);
        }
    }

    // Method to update a vehicle as sold
    private static void updateVehicleAsSold(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter vehicle ID to mark as sold: ");
        int id = scanner.nextInt();

        String query = "UPDATE Vehicles SET is_sold = TRUE WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle marked as sold. Rows affected: " + rowsAffected);
            } else {
                System.out.println("Vehicle with ID " + id + " not found.");
            }
        }
    }

    private static void deleteVehicle(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter vehicle ID to delete: ");
        int id = scanner.nextInt();

        String query = "DELETE FROM Vehicles WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle deleted successfully. Rows affected: " + rowsAffected);
            } else {
                System.out.println("Vehicle with ID " + id + " not found.");
            }
        }
    }

    private static void listAllVehicles(Connection connection) throws SQLException {
        String query = "SELECT * FROM Vehicles";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.println("ID  | Type | Brand | Model | Price | Availability");
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int price = rs.getInt("price");
                boolean isSold = rs.getBoolean("is_sold");
                String availability = isSold ? "Not Available" : "Available";
                
                System.out.println(id + "  | " + type + "   |  " + brand + "  |  " + model + "  |  " + price + "  |  " + availability);
            }
        }
    }

}

