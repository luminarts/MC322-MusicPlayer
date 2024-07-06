import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
// import java.util.ArrayList;



public class MainFrame extends JFrame{ 
    JFrame mainFrame; 
    private static MainFrame mainFrameInstance = null;
    private JLabel fileLabel;
    private JFileChooser fileChooser;
    private Clip audioClip;
    private boolean sliderisChanging = false;
    private Thread progressBarThread;
    private JSlider songDurationSlider;
    private int currentTime;

    public boolean getSliderIsChanging(){
        return sliderisChanging;
    }

    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);

        
        
        // Componentes do painel da esquerda

        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints leftPanelGbc = new GridBagConstraints();
        leftPanel.setBackground(Color.LIGHT_GRAY); 

        DefaultListModel<Musica> auxSongList = new DefaultListModel<>();  
        
        JList<Musica> songList = new JList<>(auxSongList);
        songList.setCellRenderer(new SongListCellRenderer());
        
        

        JButton backToLoginButton = new JButton("Voltar pra tela de login");
        backToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginFrame.getLoginFrameloginFrameInstance().setVisible(true);
            }
        });

        JButton importMusic = new JButton("Importar Música");
        importMusic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
                    Musica importedSong = new Musica(selectedFile.getName(), null, selectedFile.getParent(), 0, 0, null, null, selectedFile.getAbsolutePath());
                    auxSongList.addElement(importedSong);
                    songList.setModel(auxSongList);
                    songList.revalidate();
                    songList.repaint();
                }
            }
        });

        fileLabel = new JLabel("Nenhuma música selecionada");

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".wav") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "WAV files (*.wav)";
            }
        });

        


        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 0;
        leftPanelGbc.insets = new Insets(5,5, 5, 5);
        leftPanel.add(backToLoginButton, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 1;
        leftPanel.add(importMusic, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 2;
        leftPanel.add(songList, leftPanelGbc);

        
        songList.setBackground(Color.BLACK);
        songList.setForeground(Color.WHITE);
        
        

        leftPanel.setBounds(0, 0, 500, 720);
        songList.setBounds(0, 0, 580, 720);

         
        

        // Right Panel components
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY); 
        GridBagConstraints rightPanelGbc = new GridBagConstraints();
        
        JLabel songPlayingLabel = new JLabel("Selecione uma música");
        songDurationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);    
        JProgressBar songProgression = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

        songList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    songPlayingLabel.setText("Música selecionada: " + songList.getSelectedValue().getNome());
                    try {

                        File songFile = new File(songList.getSelectedValue().getPath());
                        AudioInputStream audioStream = AudioSystem.getAudioInputStream(songFile);
                        audioClip = AudioSystem.getClip();
                        songList.getSelectedValue().setDuracao((int) audioClip.getMicrosecondLength());
                        audioClip.open(audioStream);
                        songDurationSlider.setMaximum((int) audioClip.getMicrosecondLength()/1000);
                        songProgression.setMaximum((int) audioClip.getMicrosecondLength()/1000);

                    } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e2) {
                        e2.printStackTrace();
                        JOptionPane.showMessageDialog(mainFrame, "Erro ao carregar arquivo de áudio");
                        return;
                    }
                } else {
                    songPlayingLabel.setText("Pegando música");
                }
            }
        });

        
        


        rightPanelGbc.gridx = 0;
        rightPanelGbc.gridy = 0;
        rightPanel.add(songPlayingLabel,rightPanelGbc);

        
        // Bottom Panel components
        
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bottomPanelGbc = new GridBagConstraints();
        bottomPanel.setBackground(Color.GRAY);

        JButton playButton = new JButton("Play");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Parar");
        JButton nextButton = new JButton("Próxima");
        JButton previousButton = new JButton("Anterior");
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (audioClip != null && !audioClip.isRunning()) {
                    audioClip.start();
                    startPlaybackThread();
                }
            }
        });
        
        
        pauseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (audioClip != null && audioClip.isRunning()) {
                    audioClip.stop();
                    stopPlaybackThread();
                }
            }
        });

        
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (audioClip != null && audioClip.isOpen()) {
                    audioClip.setFramePosition(0);
                    audioClip.stop();
                    songDurationSlider.setValue(0);
                    songProgression.setValue(0);
                    stopPlaybackThread();
                }
            }
        });

        
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (songList.getModel().getSize() >= 2){
                    int aux = songList.getSelectedIndex();
                    if (audioClip.isRunning()) {
                        audioClip.stop();
                        audioClip.setMicrosecondPosition(0);
                        songDurationSlider.setValue(0);
                        songProgression.setValue(0);
                        stopPlaybackThread();
                    }
                    if (aux < songList.getModel().getSize() - 1) {
                        songList.setSelectedIndex(aux + 1);
                    } else {
                        songList.setSelectedIndex(0);
                    }
                    audioClip.start();
                    startPlaybackThread();
                }
            }
        });

        
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (songList.getModel().getSize() >= 2){
                    int aux = songList.getSelectedIndex();
                    if (audioClip.isRunning()) {
                        audioClip.stop();
                        audioClip.setMicrosecondPosition(0);
                        songDurationSlider.setValue(0);
                        songProgression.setValue(0);
                        stopPlaybackThread();
                    }
                    if (aux > 0) {
                        songList.setSelectedIndex(aux - 1);
                    } else {
                        songList.setSelectedIndex(songList.getModel().getSize() - 1);
                    }
                    audioClip.start();
                    stopPlaybackThread();
                }
            }
        });

        JPanel controlPanel = new JPanel(new GridLayout(1,5,5,0));
        controlPanel.setBackground(bottomPanel.getBackground());
        controlPanel.add(previousButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(nextButton);

        
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setBackground(bottomPanel.getBackground());
        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                float volume = volumeSlider.getValue()*15f/100f -10f;
                if (audioClip != null) {
                    FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                    System.out.println(gainControl);
                    System.out.println(volume);
                    float dB  = (float) (volume * volume * volume) / 21f;
                    float dB_actual = 0;
                    if (dB > 6.0) {
                        dB_actual = 6.0f;
                    } else if (volume == -10) {
                        dB_actual = -79f;
                    } else {
                        dB_actual = dB;
                    }
                    
                    System.out.println(dB_actual);
                    gainControl.setValue(dB_actual);
                }
            }
        });
        
        songProgression.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sliderisChanging = true;
                if (audioClip.isRunning()) {
                    stopPlaybackThread();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                sliderisChanging = false;
                int songPosition = songProgression.getValue() * 1000;
                audioClip.setMicrosecondPosition(songPosition);
                if (audioClip.isRunning()) {
                    startPlaybackThread();
                }

            }
        });


        songDurationSlider.setMinorTickSpacing(1);
        songDurationSlider.setBackground(bottomPanel.getBackground());
        songDurationSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!songDurationSlider.getValueIsAdjusting()) {
                    if (!sliderisChanging) {
                        int position = songDurationSlider.getValue();
                        audioClip.setMicrosecondPosition(position * 1000);
                    }
                }
            }
        });

        songDurationSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                sliderisChanging = true;
                if (audioClip.isRunning()) {
                    stopPlaybackThread();
                }                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
                sliderisChanging = false;
                int songPosition = songDurationSlider.getValue();
                audioClip.setMicrosecondPosition(songPosition * 1000);
                if (audioClip.isRunning()) {
                    startPlaybackThread();
                }
            }
        });
        
        bottomPanelGbc.gridx = 0;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(controlPanel, bottomPanelGbc);

        bottomPanelGbc.gridx = 1;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(volumeSlider, bottomPanelGbc);

        bottomPanelGbc.gridx = 2;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(songDurationSlider, bottomPanelGbc);
        // bottomPanel.add(songProgression, bottomPanelGbc);


        // Separate right and left Panels
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        horizontalSplitPane.setResizeWeight(0.2); // Distribute space equally

        // add to mainPanel
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, bottomPanel);
        mainPanel.setDividerSize(0);
        mainPanel.setResizeWeight(0.85);

        this.setContentPane(mainPanel);
        

    }

    public void startPlaybackThread() {
        progressBarThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(audioClip.isRunning() && audioClip != null) {
                    currentTime++;
                }
            }
        });
    }

    public void stopPlaybackThread() {
        if (progressBarThread != null && progressBarThread.isAlive()) {
            progressBarThread.interrupt();
        }
    }

    public class SongListCellRenderer extends DefaultListCellRenderer {
        
        public Component getListCellRendererComponent(JList<?> songList, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel)super.getListCellRendererComponent(songList, value, index, isSelected, cellHasFocus);

            if (value instanceof Musica) {
                Musica musica = (Musica) value;
                label.setText(musica.getNome());
            }

        return label;
    }
    }

    public static MainFrame getMainFrameInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }


}  

