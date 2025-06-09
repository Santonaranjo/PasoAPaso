import java.sql.*;

public class BaseDeDatos {
    private static final String URL = "jdbc:sqlite:usuarios.db";

    public static void inicializar() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                         "usuario TEXT PRIMARY KEY, " +
                         "contrasena TEXT NOT NULL)");
            stmt.execute("INSERT OR IGNORE INTO usuarios (usuario, contrasena) VALUES ('admin', '1234')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean verificarUsuario(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
