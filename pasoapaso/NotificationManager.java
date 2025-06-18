import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationManager {
    private Timer timer;
    
    public NotificationManager() {
        timer = new Timer(true);
    }
    
    public void scheduleHabitReminder(Habit habit) {
        if ("daily".equals(habit.getReminderType()) && habit.getReminderTime() != null) {
            scheduleDailyReminder(habit);
        }
        // Aquí se pueden agregar más tipos de recordatorios
    }
    
    private void scheduleDailyReminder(Habit habit) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                showNotification(habit);
            }
        };
        
        // Programar para ejecutarse cada 24 horas (simplificado)
        timer.scheduleAtFixedRate(task, 1000, 24 * 60 * 60 * 1000);
    }
    
    private void showNotification(Habit habit) {
        SwingUtilities.invokeLater(() -> {
            JFrame notificationFrame = new JFrame("Recordatorio - PasoAPaso");
            notificationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            notificationFrame.setSize(400, 200);
            notificationFrame.setLocationRelativeTo(null);
            notificationFrame.setAlwaysOnTop(true);
            
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JLabel titleLabel = new JLabel("¡Es hora de tu hábito!", JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
            
            JTextArea messageArea = new JTextArea();
            messageArea.setText(String.format(
                "Hábito: %s\nCategoría: %s\nDescripción: %s",
                habit.getName(), habit.getCategory(), habit.getDescription()
            ));
            messageArea.setEditable(false);
            messageArea.setBackground(panel.getBackground());
            
            JButton completeButton = new JButton("¡Completado!");
            completeButton.addActionListener(e -> {
                notificationFrame.dispose();
                // Aquí se puede marcar como completado en la BD
            });
            
            panel.add(titleLabel, BorderLayout.NORTH);
            panel.add(messageArea, BorderLayout.CENTER);
            panel.add(completeButton, BorderLayout.SOUTH);
            
            notificationFrame.add(panel);
            notificationFrame.setVisible(true);
        });
    }
}