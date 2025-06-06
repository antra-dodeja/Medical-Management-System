import javax.swing.*;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {

    private JPanel mainPanel;
    private JLabel titleLabel;
    private JButton addPatientButton;
    private JButton viewPatientsButton;
    private JButton searchPatientButton;
    private JButton billingButton;
    private JButton reportsButton;
    private JButton settingsButton;

    public HomePage() {
        setTitle("Medical Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Title
        titleLabel = new JLabel("Medical Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Create buttons
        JButton doctorBtn = createStyledButton("Doctor", new Color(70, 130, 180));
        JButton patientBtn = createStyledButton("Patient", new Color(60, 179, 113));
        JButton appointmentBtn = createStyledButton("Appointment", new Color(255, 140, 0));
        JButton departmentBtn = createStyledButton("Department", new Color(220, 20, 60));

        // Add action listeners
        doctorBtn.addActionListener(e -> new ListWindow("Doctor").setVisible(true));
        patientBtn.addActionListener(e -> new ListWindow("Patient").setVisible(true));
        appointmentBtn.addActionListener(e -> new ListWindow("Appointment").setVisible(true));
        departmentBtn.addActionListener(e -> new ListWindow("Department").setVisible(true));

        // Add buttons to panel
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(doctorBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(patientBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(appointmentBtn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(departmentBtn, gbc);

        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 80));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            new HomePage().setVisible(true);
        });
    }
}