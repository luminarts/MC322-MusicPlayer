import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.*;

public class LoginFrame extends JFrame {
    LoginFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1080,720);
        this.setLayout(null);
        
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(100,100,100,50);
        submitButton.setBackground(Color.CYAN);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        this.add(submitButton);

        JPanel panel1 = new JPanel();
        panel1.setBounds(new Rectangle(50,50,200,200));
        panel1.setBackground(Color.BLACK);
        this.add(panel1);


        this.setVisible(true);

    }
}

