import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class LoginFrame extends JFrame implements ActionListener{
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton submitButton = new JButton("Submit");

    LoginFrame() {
        this.setSize(1080, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
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

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        inputPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        usernameField = new JTextField(15);
        usernameField.setMaximumSize(new Dimension(100, 30));
        inputPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        inputPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        passwordField = new JPasswordField(15);
        inputPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        
        submitButton.setBounds(new Rectangle(100, 50));
        submitButton.setBackground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        inputPanel.add(submitButton, gbc);
        submitButton.addActionListener(this);

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

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            usernameField.setText("");
            passwordField.setText("");
            this.setVisible(false);

            System.out.println("Username: " + username);
            System.out.println("Password: " + password);
        }
    }
}
