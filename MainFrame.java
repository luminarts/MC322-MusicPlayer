import javax.swing.*;
import java.awt.FlowLayout;

public class MainFrame extends JFrame { 
    JFrame mainFrame; 
    
    MainFrame() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(1080, 720);
    
    // Componee

    // Componentes do reprodutor
    JButton volumeButton = new JButton("Press");
        
    this.add(volumeButton);
    }
}  

