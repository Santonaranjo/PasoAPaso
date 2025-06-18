import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:pasoapaso.db";
    private Connection connection;
    
    public DatabaseManager() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createTables() {
        try {
            Statement stmt = connection.createStatement();
            
            // Tabla de usuarios
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """;
            
            // Tabla de hábitos
            String createHabitsTable = """
                CREATE TABLE IF NOT EXISTS habits (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    description TEXT,
                    reminder_type TEXT NOT NULL,
                    reminder_time TEXT,
                    end_date TEXT,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                )
            """;
            
            // Tabla de completados
            String createCompletionsTable = """
                CREATE TABLE IF NOT EXISTS habit_completions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    habit_id INTEGER NOT NULL,
                    completed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY(habit_id) REFERENCES habits(id)
                )
            """;
            
            stmt.execute(createUsersTable);
            stmt.execute(createHabitsTable);
            stmt.execute(createCompletionsTable);
            stmt.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean registerUser(String username, String password) {
        try {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // En producción, usar hash
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public User loginUser(String username, String password) {
        try {
            String sql = "SELECT id, username FROM users WHERE username = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean addHabit(int userId, Habit habit) {
        try {
            String sql = """
                INSERT INTO habits (user_id, name, category, description, 
                                  reminder_type, reminder_time, end_date) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, habit.getName());
            pstmt.setString(3, habit.getCategory());
            pstmt.setString(4, habit.getDescription());
            pstmt.setString(5, habit.getReminderType());
            pstmt.setString(6, habit.getReminderTime());
            pstmt.setString(7, habit.getEndDate());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Habit> getUserHabits(int userId) {
        List<Habit> habits = new ArrayList<>();
        try {
            String sql = "SELECT * FROM habits WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Habit habit = new Habit(
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("reminder_type"),
                    rs.getString("reminder_time"),
                    rs.getString("end_date")
                );
                habit.setId(rs.getInt("id"));
                habits.add(habit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return habits;
    }
    
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}