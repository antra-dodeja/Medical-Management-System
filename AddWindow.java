import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddWindow extends JFrame {
    private String entityType;
    private ListWindow parentWindow;
    private JTextField[] textFields;
    private String[] fieldNames;
    
    public AddWindow(String entityType, ListWindow parentWindow) {
        this.entityType = entityType;
        this.parentWindow = parentWindow;
        
        setTitle("Add " + entityType);
        setSize(400, 300);
        setLocationRelativeTo(parentWindow);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Add New " + entityType);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Get field names based on entity type
        fieldNames = getFieldNames();
        textFields = new JTextField[fieldNames.length];
        
        for (int i = 0; i < fieldNames.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(fieldNames[i] + ":");
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(label, gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            textFields[i] = new JTextField(20);
            textFields[i].setFont(new Font("Arial", Font.PLAIN, 12));
            formPanel.add(textFields[i], gbc);
            gbc.fill = GridBagConstraints.NONE;
            gbc.weightx = 0;
        }
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton saveBtn = new JButton("Save");
        saveBtn.setBackground(new Color(60, 179, 113));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 12));
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> saveRecord());
        
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(220, 20, 60));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 12));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private String[] getFieldNames() {
        switch (entityType) {
            case "Doctor":
                return new String[]{"Name", "Specialization", "Phone", "Email"};
            case "Patient":
                return new String[]{"Name", "Age", "Phone", "Address"};
            case "Appointment":
                return new String[]{"Patient Name", "Doctor Name", "Date (YYYY-MM-DD)", "Time (HH:MM)"};
            case "Department":
                return new String[]{"Name", "Head Doctor", "Location"};
            default:
                return new String[]{};
        }
    }
    
    private void saveRecord() {
        // Validate input
        for (int i = 0; i < textFields.length; i++) {
            if (textFields[i].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = getInsertQuery();
            PreparedStatement pstmt = conn.prepareStatement(query);
            
            // Set parameters based on entity type
            setParameters(pstmt);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, entityType + " added successfully!");
                parentWindow.refreshData();
                dispose();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving record: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getInsertQuery() {
        switch (entityType) {
            case "Doctor":
                return "INSERT INTO doctors (name, specialization, phone, email) VALUES (?, ?, ?, ?)";
            case "Patient":
                return "INSERT INTO patients (name, age, phone, address) VALUES (?, ?, ?, ?)";
            case "Appointment":
                return "INSERT INTO appointments (patient_name, doctor_name, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
            case "Department":
                return "INSERT INTO departments (name, head_doctor, location) VALUES (?, ?, ?)";
            default:
                return "";
        }
    }
    
    private void setParameters(PreparedStatement pstmt) throws SQLException {
        switch (entityType) {
            case "Doctor":
                pstmt.setString(1, textFields[0].getText().trim());
                pstmt.setString(2, textFields[1].getText().trim());
                pstmt.setString(3, textFields[2].getText().trim());
                pstmt.setString(4, textFields[3].getText().trim());
                break;
            case "Patient":
                pstmt.setString(1, textFields[0].getText().trim());
                pstmt.setInt(2, Integer.parseInt(textFields[1].getText().trim()));
                pstmt.setString(3, textFields[2].getText().trim());
                pstmt.setString(4, textFields[3].getText().trim());
                break;
            case "Appointment":
                pstmt.setString(1, textFields[0].getText().trim());
                pstmt.setString(2, textFields[1].getText().trim());
                pstmt.setDate(3, Date.valueOf(textFields[2].getText().trim()));
                pstmt.setTime(4, Time.valueOf(textFields[3].getText().trim() + ":00"));
                break;
            case "Department":
                pstmt.setString(1, textFields[0].getText().trim());
                pstmt.setString(2, textFields[1].getText().trim());
                pstmt.setString(3, textFields[2].getText().trim());
                break;
        }
    }
}