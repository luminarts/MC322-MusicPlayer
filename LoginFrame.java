import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private static LoginFrame loginFrameInstance = null;
    private JTextField usernameField;
    private JPasswordField passwordField;

    LoginFrame(String title) {
        super(title);
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 1));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton submitButton = new JButton("Submit");
        JButton switchButton = new JButton("Switch to Register");

        JPanel panel1 = new JPanel();
        panel1.add(new JLabel("Username:"));
        panel1.add(usernameField);

        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("Password:"));
        panel2.add(passwordField);

        JPanel panel3 = new JPanel();
        panel3.add(submitButton);
        panel3.add(switchButton);

        this.add(panel1);
        this.add(panel2);
        this.add(panel3);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                Usuario usuario = Usuario.verificarLogin(username, password);
                if (usuario != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login successful!");
                    MainFrame.getInstance().setCurrentUser(usuario.getNome());
                    setVisible(false);
                    MainFrame.getInstance().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login failed. Invalid username or password.");
                }
            }
        });

        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToRegisterMode();
            }
        });
    }

    private void switchToRegisterMode() {
        RegisterFrame registerFrame = new RegisterFrame("Register");
        registerFrame.setVisible(true);
        this.setVisible(false);
    }
    
    public static LoginFrame getLoginFrameInstance() {
        if (loginFrameInstance == null) {
            loginFrameInstance = new LoginFrame("Login");
        }
        return loginFrameInstance;
    }
}

