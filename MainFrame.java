import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame { 
    JFrame mainFrame; 
    
    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);

        
        
        // Left Panel components

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.LIGHT_GRAY); 

        final DefaultListModel<String> auxSongList = new DefaultListModel<>();  
        auxSongList.addElement("Song A");  
        auxSongList.addElement("Song B");  
        auxSongList.addElement("Song C");  
        auxSongList.addElement("Song D");  
        final JList<String> songList = new JList<>(auxSongList);
        
        leftPanel.add(songList);
        songList.setBackground(Color.BLACK);
        songList.setForeground(Color.WHITE);
        

        leftPanel.setBounds(0, 0, 500, 720);
        songList.setBounds(0, 0, 580, 720);

        // b.addActionListener(new ActionListener() {  
        //     public void actionPerformed(ActionEvent e) {   
        //        String data = "";  
        //        if (list1.getSelectedIndex() != -1) {                       
        //           data = "Programming language Selected: " + list1.getSelectedValue();   
        //           label.setText(data);  
        //        }  
        //        if(list2.getSelectedIndex() != -1){  
        //           data += ", FrameWork Selected: ";  
        //           for(Object frame :list2.getSelectedValues()){  
        //              data += frame + " ";  
        //           }  
        //        }  
        //        label.setText(data);  
        //     }  
        //  }); 
        

        // Right Panel components
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY); 
        GridBagConstraints rightPanelGbc = new GridBagConstraints();
        
        JLabel songPlayingLabel = new JLabel("ALBUM + NOME + FOTO ALBUM + LABEL PLAYING NOW");
        
        JButton playButton = new JButton("Play");
        
        JButton pauseButton = new JButton("Pause");

        JButton stopButton = new JButton("Stop");

        JSplitPane aux_controlPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, playButton, pauseButton);
        aux_controlPane.setResizeWeight(0.5);
        aux_controlPane.setDividerSize(0);
        aux_controlPane.setBackground(rightPanel.getBackground());

        JSplitPane controlPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, aux_controlPane, stopButton);
        controlPane.setResizeWeight(0.666);
        controlPane.setDividerSize(0);
        controlPane.setBackground(rightPanel.getBackground());


        rightPanelGbc.gridx = 0;
        rightPanelGbc.gridy = 0;
        rightPanel.add(songPlayingLabel,rightPanelGbc);

        rightPanelGbc.gridx = 0;
        rightPanelGbc.gridy = 1;
        rightPanelGbc.insets = new Insets(10, 10, 10, 10);
        rightPanel.add(controlPane, rightPanelGbc);
        

        
        // Bottom Panel components
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bottomPanelGbc = new GridBagConstraints();
        bottomPanel.setBackground(Color.GRAY);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setBackground(bottomPanel.getBackground());
        
        JSlider songDurationSlider = new JSlider();
        songDurationSlider.setMajorTickSpacing(10);
        songDurationSlider.setMinorTickSpacing(1);
        songDurationSlider.setBackground(bottomPanel.getBackground());
        
        bottomPanelGbc.gridx = 0;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(controlPane, bottomPanelGbc);

        bottomPanelGbc.gridx = 1;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(volumeSlider, bottomPanelGbc);

        bottomPanelGbc.gridx = 2;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(songDurationSlider, bottomPanelGbc);


        // Separate right and left Panels
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        horizontalSplitPane.setResizeWeight(0.2); // Distribute space equally

        // add to mainPanel
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, bottomPanel);
        mainPanel.setDividerSize(0);
        mainPanel.setResizeWeight(0.85);

        this.setContentPane(mainPanel);

    }
}  

