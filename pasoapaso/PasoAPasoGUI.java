import javax.swing.*;
import javax.swing.UIManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PasoAPasoGUI extends JFrame {
    private DatabaseManager dbManager;
    private NotificationManager notificationManager;
    private User currentUser;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public PasoAPasoGUI() {
        dbManager = new DatabaseManager();
        notificationManager = new NotificationManager();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("PasoAPaso - Habit Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Crear pantallas
        createLoginPanel();
        createMainMenuPanel();
        createAddHabitPanel();
        
        add(mainPanel);
        showLoginPanel();
    }
    
    private void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Título
        JLabel titleLabel = new JLabel("PasoAPaso");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(70, 130, 180));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 30, 0);
        loginPanel.add(titleLabel, gbc);
        
        // Campos de login
        gbc.gridwidth = 1; gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel userLabel = new JLabel("Usuario:");
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(userLabel, gbc);
        
        JTextField userField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(userField, gbc);
        
        JLabel passLabel = new JLabel("Contraseña:");
        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(passLabel, gbc);
        
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(passField, gbc);
        
        // Botones
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            currentUser = dbManager.loginUser(username, password);
            
            if (currentUser != null) {
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos");
            }
        });
        
        JButton registerButton = new JButton("Registrarse");
        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            
            if (dbManager.registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Usuario registrado exitosamente");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar usuario");
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.insets = new Insets(20, 5, 5, 5);
        loginPanel.add(loginButton, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        loginPanel.add(registerButton, gbc);
        
        mainPanel.add(loginPanel, "LOGIN");
    }
    
    private void createMainMenuPanel() {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(Color.WHITE);
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel welcomeLabel = new JLabel("Bienvenido a PasoAPaso");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(welcomeLabel);
        
        // Botones principales
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JButton addHabitBtn = createMenuButton("Agregar Hábito", "📝");
        addHabitBtn.addActionListener(e -> showAddHabitPanel());
        
        JButton viewHabitsBtn = createMenuButton("Ver Mis Hábitos", "📋");
        viewHabitsBtn.addActionListener(e -> showUserHabits());
        
        JButton statsBtn = createMenuButton("Estadísticas", "📊");
        JButton settingsBtn = createMenuButton("Configuración", "🛠");
        
        buttonPanel.add(addHabitBtn);
        buttonPanel.add(viewHabitsBtn);
        buttonPanel.add(statsBtn);
        buttonPanel.add(settingsBtn);
        
        menuPanel.add(headerPanel, BorderLayout.NORTH);
        menuPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(menuPanel, "MENU");
    }
    
    private JButton createMenuButton(String text, String emoji) {
        JButton button = new JButton("<html><center>" + emoji + "<br>" + text + "</center></html>");
        button.setPreferredSize(new Dimension(200, 100));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(240, 248, 255));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        return button;
    }
    
    private void createAddHabitPanel() {
        JPanel addHabitPanel = new JPanel(new GridBagLayout());
        addHabitPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel titleLabel = new JLabel("Agregar Nuevo Hábito");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        addHabitPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Campos del formulario
        JLabel nameLabel = new JLabel("Nombre del hábito:");
        gbc.gridx = 0; gbc.gridy = 1;
        addHabitPanel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        addHabitPanel.add(nameField, gbc);
        
        JLabel categoryLabel = new JLabel("Categoría:");
        gbc.gridx = 0; gbc.gridy = 2;
        addHabitPanel.add(categoryLabel, gbc);
        
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Salud", "Ejercicio", "Estudio", "Trabajo", "Mindfulness", "Hobbies", "Otro"
        });
        gbc.gridx = 1; gbc.gridy = 2;
        addHabitPanel.add(categoryCombo, gbc);
        
        JLabel descLabel = new JLabel("Descripción:");
        gbc.gridx = 0; gbc.gridy = 3;
        addHabitPanel.add(descLabel, gbc);
        
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane scrollPane = new JScrollPane(descArea);
        gbc.gridx = 1; gbc.gridy = 3;
        addHabitPanel.add(scrollPane, gbc);
        
        JLabel reminderLabel = new JLabel("Tipo de recordatorio:");
        gbc.gridx = 0; gbc.gridy = 4;
        addHabitPanel.add(reminderLabel, gbc);
        
        JComboBox<String> reminderCombo = new JComboBox<>(new String[]{
            "Diario", "Fecha específica", "Fecha de finalización"
        });
        gbc.gridx = 1; gbc.gridy = 4;
        addHabitPanel.add(reminderCombo, gbc);
        
        JLabel timeLabel = new JLabel("Hora/Fecha:");
        gbc.gridx = 0; gbc.gridy = 5;
        addHabitPanel.add(timeLabel, gbc);
        
        JTextField timeField = new JTextField(20);
        timeField.setToolTipText("Formato: HH:mm para diario, YYYY-MM-DD para fechas");
        gbc.gridx = 1; gbc.gridy = 5;
        addHabitPanel.add(timeField, gbc);
        
        // Botones
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar Hábito");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            Habit habit = new Habit(
                nameField.getText(),
                categoryCombo.getSelectedItem().toString(),
                descArea.getText(),
                getReminderType(reminderCombo.getSelectedItem().toString()),
                timeField.getText(),
                null
            );
            
            if (dbManager.addHabit(currentUser.getId(), habit)) {
                notificationManager.scheduleHabitReminder(habit);
                JOptionPane.showMessageDialog(this, "Hábito agregado exitosamente");
                clearAddHabitForm(nameField, descArea, timeField);
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar hábito");
            }
        });
        
        JButton backButton = new JButton("Volver al Menú");
        backButton.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        addHabitPanel.add(buttonPanel, gbc);
        
        mainPanel.add(addHabitPanel, "ADD_HABIT");
    }
    
    private String getReminderType(String selection) {
        switch (selection) {
            case "Diario": return "daily";
            case "Fecha específica": return "specific_date";
            case "Fecha de finalización": return "end_date";
            default: return "daily";
        }
    }
    
    private void clearAddHabitForm(JTextField nameField, JTextArea descArea, JTextField timeField) {
        nameField.setText("");
        descArea.setText("");
        timeField.setText("");
    }
    
    private void showUserHabits() {
        List<Habit> habits = dbManager.getUserHabits(currentUser.getId());
        
        JFrame habitsFrame = new JFrame("Mis Hábitos");
        habitsFrame.setSize(600, 400);
        habitsFrame.setLocationRelativeTo(this);
        
        String[] columnNames = {"Nombre", "Categoría", "Descripción", "Recordatorio"};
        Object[][] data = new Object[habits.size()][4];
        
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            data[i][0] = habit.getName();
            data[i][1] = habit.getCategory();
            data[i][2] = habit.getDescription();
            data[i][3] = habit.getReminderType();
        }
        
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        
        habitsFrame.add(scrollPane);
        habitsFrame.setVisible(true);
    }
    
    private void showLoginPanel() {
        cardLayout.show(mainPanel, "LOGIN");
    }
    
    private void showMainMenu() {
        cardLayout.show(mainPanel, "MENU");
    }
    
    private void showAddHabitPanel() {
        cardLayout.show(mainPanel, "ADD_HABIT");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Alternativa más segura
                String systemLookAndFeel = UIManager.getLookAndFeel().getClass().getName();
                UIManager.setLookAndFeel(systemLookAndFeel);
            } catch (Exception e) {
                System.err.println("No se pudo establecer el Look and Feel del sistema: " + e.getMessage());
                // Intentar con Nimbus como alternativa
                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Usando Look and Feel por defecto");
                }
            }
            
            new PasoAPasoGUI().setVisible(true);
        });
    }
}