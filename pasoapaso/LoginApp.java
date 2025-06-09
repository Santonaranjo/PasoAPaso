import javax.swing.*;
import java.awt.*;

public class LoginApp extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private final Font mainFont = new Font("SansSerif", Font.BOLD, 16);

    public LoginApp() {
        setTitle("Inicio de Sesión");
        setSize(400, 250);
        setMinimumSize(new Dimension(350, 200));
        setMaximumSize(new Dimension(500, 300));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar ventana

        // Estilo visual del panel
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(137, 191, 201)); // Azul claro

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(mainFont);
        panel.add(userLabel);

        userField = new JTextField();
        userField.setFont(mainFont);
        panel.add(userField);

        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(mainFont);
        panel.add(passLabel);

        passField = new JPasswordField();
        passField.setFont(mainFont);
        panel.add(passField);

        panel.add(new JLabel()); // Espacio vacío

        loginButton = new JButton("Iniciar sesión");
        loginButton.setFont(mainFont);
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> verificarCredenciales());

        setVisible(true);
    }

    private void verificarCredenciales() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (BaseDeDatos.verificarUsuario(username, password)) {
            JOptionPane.showMessageDialog(this, "¡Login exitoso!");
            // Aquí podrías abrir otro JFrame o continuar el programa
        } else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
        }
    }

    public static void main(String[] args) {
        BaseDeDatos.inicializar(); // Crear DB si no existe
        new LoginApp();
    }
}
