import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private static LoginFrame loginFrameInstance = null;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton submitButton = new JButton("Submit");
    private Usuario currentUser;
    
    CreateFile cf= new CreateFile();
    


    LoginFrame() {
        this.setSize(1080  , 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        FileWriter fileWriter = getFileWriter() != null ? getFileWriter() : null; 

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        mainPanel.setBackground(Color.LIGHT_GRAY);
        
        
        
        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(Color.LIGHT_GRAY);
        
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 50, 10, 50);

        usernameField = new JTextField(15);
        JLabel usernameLabel = new JLabel("Usuário:");
        usernameLabel.setForeground(Color.WHITE);
        inputPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        // usernameField.setMaximumSize(new Dimension(100, 30));
        inputPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Senha:");
        passwordLabel.setForeground(Color.WHITE);
        inputPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField(15);
        inputPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        
        submitButton.setBounds(new Rectangle(100, 50));
        submitButton.setBackground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAction(fileWriter);
            }
        });

        JButton registerButton = new JButton("Registre-se");
        registerButton.setBackground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                RegisterFrame.getRegisterFrameInstance().setVisible(true);
            }
        });

        JPanel buttonsPanel = new JPanel(new GridLayout(1,2,10,0));
        buttonsPanel.setBackground(inputPanel.getBackground());

        buttonsPanel.add(submitButton);
        buttonsPanel.add(registerButton);

        inputPanel.add(buttonsPanel, gbc);

        passwordField.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    submitAction(fileWriter);
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {

            }

        });

        GridBagConstraints containerGbc = new GridBagConstraints();
        containerGbc.gridx = 0;
        containerGbc.gridy = 0;
        containerGbc.insets = new Insets(10, 10, 10, 10);
        containerPanel.add(inputPanel, containerGbc);

        // Add container panel to the main panel with center alignment
        GridBagConstraints mainPanelGbc = new GridBagConstraints();
        mainPanelGbc.gridx = 0;
        mainPanelGbc.gridy = 0;

        mainPanel.add(containerPanel, mainPanelGbc);

        this.setContentPane(mainPanel);

        
    }

    public void submitAction(FileWriter uf) {
        String pswrd = new String(passwordField.getPassword());
        String usr = usernameField.getText();
        
        if (!pswrd.isEmpty() && !usr.isEmpty()) {
            Usuario usuario = Usuario.verificarLogin(usr, pswrd);
            if (usuario != null) {
                usernameField.setText("");
                passwordField.setText("");
                try {
                    uf.write(usr + "\n" + pswrd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setCurrentUser(usuario);
                setVisible(false);
                MainFrame.getMainFrameInstance().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Nome de usuário ou senha inválido.");
            }
        } else {
            JOptionPane.showMessageDialog(LoginFrame.this,"Não deixe espaços em branco!");  
        }
    }

    public Usuario getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Usuario usuario) {
        this.currentUser = usuario;
    }

    public FileWriter getFileWriter() {
        File usersFile = cf.createUsersFile();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(usersFile);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileWriter;
    }

    public static LoginFrame getLoginFrameInstance() {
        if (loginFrameInstance == null) {
            loginFrameInstance = new LoginFrame();
        }
        return loginFrameInstance;
    }
}

