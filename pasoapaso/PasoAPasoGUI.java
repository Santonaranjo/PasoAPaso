import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

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
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Crear pantallas
        createLoginPanel();
        createMainMenuPanel();
        createAddHabitPanel();
        createProfilesPanel();
        createSettingsPanel();
        
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
        
        JButton statsBtn = createMenuButton("Perfiles", "👩🏼‍🦱");
        statsBtn.addActionListener(e -> showProfilesPanel());
        
        JButton settingsBtn = createMenuButton("Configuración", "🛠");
        settingsBtn.addActionListener(e -> showSettingsPanel());
        
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
        JPanel addHabitPanel = new JPanel(new BorderLayout());
        addHabitPanel.setBackground(Color.WHITE);
        
        // Título
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Agregar Nuevo Hábito");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 130, 180));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.WHITE);
        
        // Panel principal del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos básicos del formulario
        JLabel nameLabel = new JLabel("Nombre del hábito:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(25);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(nameField, gbc);
        
        JLabel categoryLabel = new JLabel("Categoría:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        formPanel.add(categoryLabel, gbc);
        
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "Salud", "Ejercicio", "Estudio", "Trabajo", "Mindfulness", "Hobbies", "Otro"
        });
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        formPanel.add(categoryCombo, gbc);
        
        JLabel descLabel = new JLabel("Descripción:");
        descLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(descLabel, gbc);
        
        JTextArea descArea = new JTextArea(3, 25);
        descArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descArea);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(scrollPane, gbc);
        
        JLabel reminderLabel = new JLabel("Tipo de recordatorio:");
        reminderLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(reminderLabel, gbc);
        
        JComboBox<String> reminderCombo = new JComboBox<>(new String[]{
            "Diario", "Fecha específica", "Fecha de finalización"
        });
        reminderCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(reminderCombo, gbc);
        
        // Panel de fecha y hora
        JPanel dateTimePanel = createDateTimePanel();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(dateTimePanel, gbc);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton saveButton = new JButton("💾 Guardar Hábito");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setPreferredSize(new Dimension(160, 40));
        
        JButton backButton = new JButton("🔙 Volver al Menú");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(160, 40));
        backButton.addActionListener(e -> showMainMenu());
        
        // Obtener referencias a los componentes de fecha y hora
        JSpinner dateSpinner = (JSpinner) ((JPanel) dateTimePanel.getComponent(1)).getComponent(0);
        JSpinner timeSpinner = (JSpinner) ((JPanel) dateTimePanel.getComponent(3)).getComponent(0);
        
        saveButton.addActionListener(e -> {
            try {
                String reminderTime = "";
                String endDate = null;
                
                // Obtener la fecha seleccionada
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.setTime((java.util.Date) dateSpinner.getValue());
                
                // Obtener la hora seleccionada
                Calendar selectedTime = Calendar.getInstance();
                selectedTime.setTime((java.util.Date) timeSpinner.getValue());
                
                String reminderType = getReminderType(reminderCombo.getSelectedItem().toString());
                
                if ("daily".equals(reminderType)) {
                    // Para recordatorios diarios, solo necesitamos la hora
                    reminderTime = String.format("%02d:%02d", 
                        selectedTime.get(Calendar.HOUR_OF_DAY),
                        selectedTime.get(Calendar.MINUTE));
                } else {
                    // Para fechas específicas o finales, combinamos fecha y hora
                    LocalDate date = LocalDate.of(
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH) + 1,
                        selectedDate.get(Calendar.DAY_OF_MONTH)
                    );
                    LocalTime time = LocalTime.of(
                        selectedTime.get(Calendar.HOUR_OF_DAY),
                        selectedTime.get(Calendar.MINUTE)
                    );
                    
                    reminderTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
                    endDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                
                Habit habit = new Habit(
                    nameField.getText().trim(),
                    categoryCombo.getSelectedItem().toString(),
                    descArea.getText().trim(),
                    reminderType,
                    reminderTime,
                    endDate
                );
                
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre para el hábito", 
                        "Campo requerido", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                if (dbManager.addHabit(currentUser.getId(), habit)) {
                    notificationManager.scheduleHabitReminder(habit);
                    JOptionPane.showMessageDialog(this, "¡Hábito agregado exitosamente!", 
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    clearAddHabitForm(nameField, descArea);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el hábito", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(buttonPanel, gbc);
        
        // Scroll para el formulario
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formScrollPane.setBorder(null);
        
        addHabitPanel.add(titlePanel, BorderLayout.NORTH);
        addHabitPanel.add(formScrollPane, BorderLayout.CENTER);
        
        mainPanel.add(addHabitPanel, "ADD_HABIT");
    }
    
    private JPanel createDateTimePanel() {
        JPanel dateTimePanel = new JPanel(new GridBagLayout());
        dateTimePanel.setBackground(Color.WHITE);
        dateTimePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "📅 Programación de Recordatorio",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Selector de fecha
        JLabel dateLabel = new JLabel("📅 Fecha:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0;
        dateTimePanel.add(dateLabel, gbc);
        
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        datePanel.setBackground(Color.WHITE);
        
        // Spinner para la fecha
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setPreferredSize(new Dimension(120, 30));
        dateSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Botón para abrir calendario
        JButton calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(40, 30));
        calendarButton.setToolTipText("Abrir calendario");
        calendarButton.addActionListener(e -> showCalendarDialog(dateSpinner));
        
        datePanel.add(dateSpinner);
        datePanel.add(Box.createHorizontalStrut(5));
        datePanel.add(calendarButton);
        
        gbc.gridx = 1; gbc.gridy = 0;
        dateTimePanel.add(datePanel, gbc);
        
        // Selector de hora
        JLabel timeLabel = new JLabel("🕐 Hora:");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        dateTimePanel.add(timeLabel, gbc);
        
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timePanel.setBackground(Color.WHITE);
        
        // Spinner para la hora
        JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setPreferredSize(new Dimension(80, 30));
        timeSpinner.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Botones de hora rápida
        JButton[] quickTimeButtons = {
            new JButton("🌅 6:00"), new JButton("🌞 12:00"), 
            new JButton("🌇 18:00"), new JButton("🌙 21:00")
        };
        
        for (JButton btn : quickTimeButtons) {
            btn.setFont(new Font("Arial", Font.PLAIN, 10));
            btn.setPreferredSize(new Dimension(70, 25));
            btn.addActionListener(e -> {
                String timeText = btn.getText().substring(2); // Remover emoji
                String[] parts = timeText.split(":");
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
                cal.set(Calendar.SECOND, 0);
                timeSpinner.setValue(cal.getTime());
            });
        }
        
        timePanel.add(timeSpinner);
        timePanel.add(Box.createHorizontalStrut(10));
        for (JButton btn : quickTimeButtons) {
            timePanel.add(btn);
            timePanel.add(Box.createHorizontalStrut(2));
        }
        
        gbc.gridx = 1; gbc.gridy = 1;
        dateTimePanel.add(timePanel, gbc);
        
        return dateTimePanel;
    }
    
    private void showCalendarDialog(JSpinner dateSpinner) {
        JDialog calendarDialog = new JDialog(this, "Seleccionar Fecha", true);
        calendarDialog.setSize(400, 300);
        calendarDialog.setLocationRelativeTo(this);
        
        // Crear un calendario simple
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime((java.util.Date) dateSpinner.getValue());
        
        JPanel calendarPanel = new JPanel(new BorderLayout());
        
        // Panel de navegación
        JPanel navPanel = new JPanel(new FlowLayout());
        JButton prevMonth = new JButton("◀");
        JButton nextMonth = new JButton("▶");
        JLabel monthLabel = new JLabel();
        
        // Actualizar etiqueta del mes
        monthLabel.setText(String.format("%s %d", 
            getMonthName(currentCal.get(Calendar.MONTH)), 
            currentCal.get(Calendar.YEAR)));
        monthLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        navPanel.add(prevMonth);
        navPanel.add(monthLabel);
        navPanel.add(nextMonth);
        
        // Panel de días
        JPanel daysPanel = new JPanel(new GridLayout(7, 7, 2, 2));
        
        // Días de la semana
        String[] dayNames = {"Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"};
        for (String day : dayNames) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 12));
            dayLabel.setOpaque(true);
            dayLabel.setBackground(new Color(70, 130, 180));
            dayLabel.setForeground(Color.WHITE);
            daysPanel.add(dayLabel);
        }
        
        // Función para actualizar el calendario
        Runnable updateCalendar = () -> {
            // Remover botones de días anteriores
            Component[] components = daysPanel.getComponents();
            for (int i = 7; i < components.length; i++) {
                daysPanel.remove(components[i]);
            }
            
            Calendar tempCal = (Calendar) currentCal.clone();
            tempCal.set(Calendar.DAY_OF_MONTH, 1);
            int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
            int daysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            // Espacios en blanco antes del primer día
            for (int i = 0; i < firstDayOfWeek; i++) {
                daysPanel.add(new JLabel(""));
            }
            
            // Días del mes
            for (int day = 1; day <= daysInMonth; day++) {
                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setPreferredSize(new Dimension(40, 30));
                
                final int finalDay = day;
                dayButton.addActionListener(e -> {
                    currentCal.set(Calendar.DAY_OF_MONTH, finalDay);
                    dateSpinner.setValue(currentCal.getTime());
                    calendarDialog.dispose();
                });
                
                // Resaltar día actual
                Calendar today = Calendar.getInstance();
                if (currentCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    currentCal.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                    dayButton.setBackground(new Color(70, 130, 180));
                    dayButton.setForeground(Color.WHITE);
                }
                
                daysPanel.add(dayButton);
            }
            
            monthLabel.setText(String.format("%s %d", 
                getMonthName(currentCal.get(Calendar.MONTH)), 
                currentCal.get(Calendar.YEAR)));
                
            daysPanel.revalidate();
            daysPanel.repaint();
        };
        
        prevMonth.addActionListener(e -> {
            currentCal.add(Calendar.MONTH, -1);
            updateCalendar.run();
        });
        
        nextMonth.addActionListener(e -> {
            currentCal.add(Calendar.MONTH, 1);
            updateCalendar.run();
        });
        
        updateCalendar.run();
        
        calendarPanel.add(navPanel, BorderLayout.NORTH);
        calendarPanel.add(daysPanel, BorderLayout.CENTER);
        
        JButton todayButton = new JButton("Hoy");
        todayButton.addActionListener(e -> {
            dateSpinner.setValue(new java.util.Date());
            calendarDialog.dispose();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(todayButton);
        calendarPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        calendarDialog.add(calendarPanel);
        calendarDialog.setVisible(true);
    }
    
    private String getMonthName(int month) {
        String[] months = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return months[month];
    }
    
    private String getReminderType(String selection) {
        switch (selection) {
            case "Diario": return "daily";
            case "Fecha específica": return "specific_date";
            case "Fecha de finalización": return "end_date";
            default: return "daily";
        }
    }
    
    private void clearAddHabitForm(JTextField nameField, JTextArea descArea) {
        nameField.setText("");
        descArea.setText("");
    }
    
    private void showUserHabits() {
        List<Habit> habits = dbManager.getUserHabits(currentUser.getId());
        
        JFrame habitsFrame = new JFrame("Mis Hábitos");
        habitsFrame.setSize(700, 500);
        habitsFrame.setLocationRelativeTo(this);
        
        String[] columnNames = {"Nombre", "Categoría", "Descripción", "Recordatorio", "Hora", "Fecha"};
        Object[][] data = new Object[habits.size()][6];
        
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            data[i][0] = habit.getName();
            data[i][1] = habit.getCategory();
            data[i][2] = habit.getDescription();
            data[i][3] = habit.getReminderType();
            data[i][4] = habit.getReminderTime();
            data[i][5] = habit.getEndDate();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
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
    
    private void showProfilesPanel() {
        cardLayout.show(mainPanel, "PROFILES");
    }
    
    private void showSettingsPanel() {
        cardLayout.show(mainPanel, "SETTINGS");
    }
    
    private void createProfilesPanel() {
        JPanel profilesPanel = new JPanel(new BorderLayout());
        profilesPanel.setBackground(Color.WHITE);
        
        // Título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("👥 Perfiles de Usuarios");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de información del usuario actual
        JPanel currentUserPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        currentUserPanel.setBackground(new Color(240, 248, 255));
        currentUserPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Usuario Actual",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        JLabel currentUserLabel = new JLabel("👤 " + (currentUser != null ? currentUser.getUsername() : "No disponible"));
        currentUserLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentUserLabel.setForeground(new Color(70, 130, 180));
        currentUserPanel.add(currentUserLabel);
        
        // Panel para la lista de usuarios
        JPanel usersListPanel = new JPanel(new BorderLayout());
        usersListPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Todos los Usuarios Registrados",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        // Crear tabla de usuarios
        refreshUsersTable(usersListPanel);
        
        // Botón de actualizar
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton refreshButton = new JButton("🔄 Actualizar Lista");
        refreshButton.setBackground(new Color(40, 167, 69));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
        refreshButton.addActionListener(e -> refreshUsersTable(usersListPanel));
        
        JButton backButton = new JButton("🔙 Volver al Menú");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.addActionListener(e -> showMainMenu());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        
        contentPanel.add(currentUserPanel, BorderLayout.NORTH);
        contentPanel.add(usersListPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        profilesPanel.add(titlePanel, BorderLayout.NORTH);
        profilesPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(profilesPanel, "PROFILES");
    }
    
    private void refreshUsersTable(JPanel usersListPanel) {
        // Remover componentes existentes
        usersListPanel.removeAll();
        
        List<User> users = dbManager.getAllUsers();
        
        if (users.isEmpty()) {
            JLabel noUsersLabel = new JLabel("No hay usuarios registrados", JLabel.CENTER);
            noUsersLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            noUsersLabel.setForeground(Color.GRAY);
            usersListPanel.add(noUsersLabel, BorderLayout.CENTER);
        } else {
            String[] columnNames = {"ID", "👤 Usuario", "📅 Fecha de Registro", "📊 Total de Hábitos"};
            Object[][] data = new Object[users.size()][4];
            
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                data[i][0] = user.getId();
                data[i][1] = user.getUsername();
                data[i][2] = user.getCreatedAt() != null ? user.getCreatedAt() : "No disponible";
                data[i][3] = dbManager.getUserHabitsCount(user.getId());
            }
            
            JTable table = new JTable(data, columnNames);
            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 12));
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
            table.getTableHeader().setBackground(new Color(70, 130, 180));
            table.getTableHeader().setForeground(Color.WHITE);
            
            // Configurar ancho de columnas
            table.getColumnModel().getColumn(0).setPreferredWidth(50);
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(2).setPreferredWidth(150);
            table.getColumnModel().getColumn(3).setPreferredWidth(120);
            
            // Resaltar usuario actual
            table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, 
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (currentUser != null && table.getValueAt(row, 0).equals(currentUser.getId())) {
                        c.setBackground(new Color(255, 248, 220)); // Color dorado claro
                        if (!isSelected) {
                            c.setForeground(new Color(70, 130, 180));
                        }
                    } else {
                        c.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
                        c.setForeground(isSelected ? table.getSelectionForeground() : Color.BLACK);
                    }
                    return c;
                }
            });
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(500, 200));
            usersListPanel.add(scrollPane, BorderLayout.CENTER);
        }
        
        usersListPanel.revalidate();
        usersListPanel.repaint();
    }
    
    private void createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBackground(Color.WHITE);
        
        // Título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("⚙️ Configuración");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        
        // Panel principal
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        
        // Información del usuario
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.setBackground(new Color(240, 248, 255));
        userInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Información de la Sesión",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        JLabel userInfoLabel = new JLabel();
        if (currentUser != null) {
            userInfoLabel.setText(String.format(
                "<html><b>👤 Usuario:</b> %s<br><b>🆔 ID:</b> %d<br><b>📊 Hábitos registrados:</b> %d</html>",
                currentUser.getUsername(),
                currentUser.getId(),
                dbManager.getUserHabitsCount(currentUser.getId())
            ));
        } else {
            userInfoLabel.setText("No hay usuario conectado");
        }
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userInfoPanel.add(userInfoLabel);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(userInfoPanel, gbc);
        
        // Opciones de configuración
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        
        // Botón de cerrar sesión
        JButton logoutButton = new JButton("🚪 Cerrar Sesión");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setPreferredSize(new Dimension(200, 50));
        logoutButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (option == JOptionPane.YES_OPTION) {
                currentUser = null;
                JOptionPane.showMessageDialog(
                    this,
                    "Sesión cerrada exitosamente",
                    "Sesión Cerrada",
                    JOptionPane.INFORMATION_MESSAGE
                );
                showLoginPanel();
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(logoutButton, gbc);
        
        // Información adicional de la aplicación
        JPanel appInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        appInfoPanel.setBackground(new Color(248, 249, 250));
        appInfoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Información de la Aplicación",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(108, 117, 125)
        ));
        
        JLabel appInfoLabel = new JLabel(
            "<html><center>" +
            "<b>📱 PasoAPaso - Habit Tracker</b><br>" +
            "Versión 2.0<br>" +
            "© 2024 - Desarrollado en Java Swing<br>" +
            "🗄️ Base de datos: SQLite<br>" +
            "</center></html>"
        );
        appInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        appInfoLabel.setForeground(new Color(108, 117, 125));
        appInfoPanel.add(appInfoLabel);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        contentPanel.add(appInfoPanel, gbc);
        
        // Botón para volver
        JButton backButton = new JButton("🔙 Volver al Menú Principal");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(220, 40));
        backButton.addActionListener(e -> showMainMenu());
        
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.insets = new Insets(20, 15, 15, 15);
        contentPanel.add(backButton, gbc);
        
        settingsPanel.add(titlePanel, BorderLayout.NORTH);
        settingsPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(settingsPanel, "SETTINGS");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
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