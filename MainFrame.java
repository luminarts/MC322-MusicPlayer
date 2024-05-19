import javax.swing.*;

public class MainFrame extends JFrame { 
    JFrame mainFrame; 
    
    MainFrame() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(1080, 720);
    
    JButton volumeButton = new JButton("Press");
        
    this.getContentPane().add(volumeButton);
    this.setVisible(true);
    }
}  

