
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {

    private static RegisterFrame registerFrameInstance = null;

    private JTextField nomeField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    RegisterFrame() {
        this.setSize(400,400); // Aumentar o tamanho da caixa de registro
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Nome
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("Nome:"), gbc);

        gbc.gridx = 1;
        nomeField = new JTextField(20);
        this.add(nomeField, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("Usuário:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        this.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("E-mail:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        this.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        this.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Enviar");
        JButton switchButton = new JButton("Voltar para Login");

        buttonPanel.add(submitButton);
        buttonPanel.add(switchButton);
        this.add(buttonPanel, gbc);

        // Action listeners
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = nomeField.getText();
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                Usuario usuario = new Usuario(nome, email,"", password,username);
                Usuario.addUsuario(usuario); // Adicionar usuário à lista
                JOptionPane.showMessageDialog(RegisterFrame.this, "Usuário registrado com sucesso!");
                
                LoginFrame loginFrame = LoginFrame.getLoginFrameInstance();
                loginFrame.setVisible(true);
                setVisible(false);
            }
        });

        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = LoginFrame.getLoginFrameInstance();
                loginFrame.setVisible(true);
                setVisible(false);
            }
        });
    }
    
    public static RegisterFrame getRegisterFrameInstance() {
        if (registerFrameInstance == null) {
            registerFrameInstance = new RegisterFrame();
        }
        return registerFrameInstance;
    }
}