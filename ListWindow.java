import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class ListWindow extends JFrame {
    private String entityType;
    private JTable table;
    private DefaultTableModel tableModel;
    
    public ListWindow(String entityType) {
        this.entityType = entityType;
        
        setTitle(entityType + " List");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initializeComponents();
        loadData();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel(entityType + " Records");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Table setup
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(240, 248, 255));
        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton addBtn = new JButton("Add");
        addBtn.setBackground(new Color(60, 179, 113));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> openAddWindow());
        
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(128, 128, 128));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(addBtn);
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = getSelectQuery();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            // Clear existing data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            
            // Set column names
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);
            
            // Add rows
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getSelectQuery() {
        switch (entityType) {
            case "Doctor":
                return "SELECT id, name, specialization, phone, email FROM doctors";
            case "Patient":
                return "SELECT id, name, age, phone, address FROM patients";
            case "Appointment":
                return "SELECT id, patient_name, doctor_name, appointment_date, appointment_time FROM appointments";
            case "Department":
                return "SELECT id, name, head_doctor, location FROM departments";
            default:
                return "";
        }
    }
    
    private void openAddWindow() {
        AddWindow addWindow = new AddWindow(entityType, this);
        addWindow.setVisible(true);
    }
    
    public void refreshData() {
        loadData();
    }
}