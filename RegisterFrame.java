import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame {
    private JTextField nomeField;
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    RegisterFrame(String title) {
        super(title);
        this.setSize(400, 400); // Aumentar o tamanho da caixa de registro
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
        this.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        this.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        this.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 3;
        this.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        this.add(passwordField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Submit");
        JButton switchButton = new JButton("Switch to Login");

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
                
                try {
                    // Verifica se os campos estão vazios
                    if (nome.isEmpty()) {
                        throw new VazioException("Nome");
                    }
                    if (username.isEmpty()) {
                        throw new VazioException("Username");
                    }
                    if (email.isEmpty()) {
                        throw new VazioException("Email");
                    }
                    if (password.isEmpty()) {
                        throw new VazioException("Password");
                    }

                    // Verifica o tamanho do username
                    if (username.length() > 25) {
                        throw new TamanhoUserException("O nome de usuário não pode ter mais que 25 caracteres.");
                    }
                    if (password.length() < 5) {
                        throw new SenhaException("A senha deve ter pelo menos 5 caracteres.");
                    }

                    // Verifica se o username contém palavras proibidas
                    if (verificaPalavrao(username)) {
                        throw new PalavroesUserException("O username contém uma palavra proibida.");
                    }

                    Usuario usuario = new Usuario(nome, email, "", password, username);
                    Usuario.addUsuario(usuario); // Adicionar usuário à lista
                    JOptionPane.showMessageDialog(RegisterFrame.this, "User registered successfully!");
                    switchToLoginMode();
                } catch (VazioException | TamanhoUserException | PalavroesUserException | SenhaException ex) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToLoginMode();
            }
        });
    }

    private void switchToLoginMode() {
        LoginFrame loginFrame = LoginFrame.getLoginFrameInstance();
        loginFrame.setVisible(true);
        this.setVisible(false);
    }
    
    private boolean verificaPalavrao(String username) throws PalavroesUserException {
        String[] palavrao = {"porra", "buceta", "caralho", "pinto", "gozo", "gozador", "puta", "penis", "xereca"};
        for (String palavra : palavrao) {
            if (username.toLowerCase().contains(palavra)) {
                throw new PalavroesUserException("O username contém uma palavra proibida.");
            }
        }
        return false;
    }
}
