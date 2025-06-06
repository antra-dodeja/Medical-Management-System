import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/medical_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234"; // Change this to your MySQL password
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            Statement stmt = conn.createStatement();
            
            // Create database if not exists
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS medical_db");
            stmt.executeUpdate("USE medical_db");
            
            // Create tables
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS doctors (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    specialization VARCHAR(100),
                    phone VARCHAR(15),
                    email VARCHAR(100)
                )
            """);
            
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS patients (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    age INT,
                    phone VARCHAR(15),
                    address TEXT
                )
            """);
            
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS appointments (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    patient_name VARCHAR(100) NOT NULL,
                    doctor_name VARCHAR(100) NOT NULL,
                    appointment_date DATE,
                    appointment_time TIME
                )
            """);
            
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS departments (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    head_doctor VARCHAR(100),
                    location VARCHAR(100)
                )
            """);
            
            System.out.println("Database initialized successfully!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}