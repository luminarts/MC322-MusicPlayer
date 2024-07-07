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
import java.util.ArrayList;



public class MainFrame extends JFrame{ 
    JFrame mainFrame; 
    private static MainFrame mainFrameInstance = null;
    private JLabel fileLabel;
    private JFileChooser fileChooser;
    private Clip audioClip = null;
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
                int result = fileChooser.showOpenDialog(MainFrame.this);
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

        JToggleButton showPlaylists = new JToggleButton("Painel de Playlists");
        
        JButton removeImportedSong = new JButton("Remover Música");
        
        

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
        leftPanel.add(removeImportedSong, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 3;
        leftPanel.add(showPlaylists, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 4;
        leftPanel.add(songList, leftPanelGbc);

        
        songList.setBackground(Color.BLACK);
        songList.setForeground(Color.WHITE);
        
        

        leftPanel.setBounds(0, 0, 500, 720);
        songList.setBounds(0, 0, 580, 720);

         
        

        // Componentes do Painel da Direita

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
                    if (songList.getSelectedValue() != null) {
                        songPlayingLabel.setText("Música selecionada: " + songList.getSelectedValue().getNome());
                        try {
                            if (audioClip == null || audioClip.isRunning() == false) {
                                File songFile = new File(songList.getSelectedValue().getPath());
                                AudioInputStream audioStream = AudioSystem.getAudioInputStream(songFile);
                                audioClip = AudioSystem.getClip();
                                songList.getSelectedValue().setDuracao((int) audioClip.getMicrosecondLength());
                                audioClip.open(audioStream);
                                songDurationSlider.setMaximum((int) audioClip.getMicrosecondLength()/1000);
                                songProgression.setMaximum((int) audioClip.getMicrosecondLength()/1000);
                            }
                        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e2) {
                            e2.printStackTrace();
                            JOptionPane.showMessageDialog(mainFrame, "Erro ao carregar arquivo de áudio");
                            return;
                        }
                    }
                } else {
                    songPlayingLabel.setText("Pegando música");
                }
            }
        });

        

        DefaultListModel<Playlist> auxPlaylistList = new DefaultListModel<>();
        JList<Playlist> playlistList = new JList<>(auxPlaylistList);
        playlistList.setCellRenderer(new SongListCellRenderer());

        DefaultListModel<Musica> auxPlaylistSongs = new DefaultListModel<>();
        JList<Musica> playlistSongs = new JList<>(auxPlaylistSongs);

        JLabel playlistLabel = new JLabel("Playlists:");
        JLabel playlistsSongsLabel = new JLabel("");

        playlistList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    
                    if (playlistList.getSelectedValue() != null) {
                        
                        auxPlaylistSongs.clear();

                        ArrayList<Musica> musicas = playlistList.getSelectedValue().getMusicas();
                        
                        for (Musica musica : musicas) {
                            auxPlaylistSongs.addElement(musica);
                        }
                        playlistSongs.setModel(auxPlaylistSongs);
                        playlistSongs.revalidate();
                        playlistSongs.repaint();

                        playlistsSongsLabel.setText("Músicas da Playlist " + playlistList.getSelectedValue().getNome() + ":");
                        playlistsSongsLabel.revalidate();
                        playlistsSongsLabel.repaint();
                        
                    }
                }
            }
        });

        playlistSongs.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                songList.setSelectedValue(playlistSongs.getSelectedValue(), true);
            }
        });

        // ActionListener do botõa Remover Música do painel esquerdo
        removeImportedSong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Musica selectedSong = songList.getSelectedValue();
                String warningMessage = "Você tem certeza que quer remover a música " + (selectedSong != null? selectedSong.getNome() : "") + "? Essa ação é irreversível.";
                if (selectedSong != null) {
                    int userIsSure = JOptionPane.showConfirmDialog(MainFrame.this, warningMessage, "Cuidado!", JOptionPane.YES_NO_OPTION);
                    if (userIsSure == 0) {
                        auxSongList.removeElement(selectedSong);
                        songList.clearSelection();
                        songList.setModel(auxSongList);
                        songList.revalidate();
                        songList.repaint();

                        
                        removeSongInPlaylist(selectedSong, playlistList);
                        playlistSongs.setModel(auxPlaylistSongs);

                        playlistList.revalidate();
                        playlistList.repaint();
                        playlistSongs.revalidate();
                        playlistSongs.repaint();
                    } 
                }

                
            }
        });
        
        
        JButton createPlaylist = new JButton("Criar uma Playlist");
        createPlaylist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                // ALTERAR DONO DE NULL PARA USUÁRIO ATUAL
                String nomePlaylist = JOptionPane.showInputDialog("Dê um nome para sua nova playlist!");
                if (nomePlaylist.length() >= 1) {
                    auxPlaylistList.addElement(new Playlist(null, nomePlaylist)); 
                
                    playlistList.setModel(auxPlaylistList);
                    playlistList.revalidate();
                    playlistList.repaint();
                } else if (nomePlaylist.length() < 1) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Nome da Playlist não pode ser vazio, insira um nome válido.");
                }
                
            }
        });

        JButton addToPlaylist = new JButton("Adicionar música à Playlist " + (playlistList.getSelectedValue() != null? playlistList.getSelectedValue().getNome() : ""));
        addToPlaylist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (songList.getSelectedValue() != null && playlistList.getSelectedValue() != null) {
                    
                    playlistList.getSelectedValue().addMusica(songList.getSelectedValue());

                    ArrayList<Musica> musicas = playlistList.getSelectedValue().getMusicas();

                    auxPlaylistSongs.clear();
                    for (Musica musica : musicas) {
                        auxPlaylistSongs.addElement(musica);
                    }
                    playlistSongs.setModel(auxPlaylistSongs);
                    playlistSongs.revalidate();
                    playlistSongs.repaint();

                    playlistsSongsLabel.setText("Músicas da Playlist " + playlistList.getSelectedValue().getNome() + ":");
                    playlistsSongsLabel.revalidate();
                    playlistsSongsLabel.repaint();

                } else if (playlistList.getSelectedValue() == null) {
                    JOptionPane.showMessageDialog(MainFrame.this, "Por favor, escolha uma Playlist primeiro.");
                } else { 
                    JOptionPane.showMessageDialog(MainFrame.this, "Por favor, escolha uma música para adicionar à sua Playlist.");
                }
            }
        });

        JButton removePlaylist = new JButton("Remover Playlist");
        removePlaylist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Playlist selectedPlaylist = playlistList.getSelectedValue();
                String warningMessage = "Você tem certeza que quer apagar a playlist " + selectedPlaylist.getNome() + "? Essa ação é irreversível.";
                if (selectedPlaylist != null) {
                    int userIsSure = JOptionPane.showConfirmDialog(MainFrame.this, warningMessage, "Cuidado!", JOptionPane.YES_NO_OPTION);
                    if (userIsSure == 0) {
                        auxPlaylistList.removeElementAt(playlistList.getSelectedIndex());

                        playlistList.setModel(auxPlaylistList);
                        playlistList.revalidate();
                        playlistList.repaint();
                    } 
                }
            }
        });

        

        // Ação do botão no painel da esquerda de mostrar painel de playlist 
        

        rightPanelGbc.gridx = 0;
        rightPanelGbc.gridy = 0;
        
        rightPanel.add(songPlayingLabel,rightPanelGbc);

        showPlaylists.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (showPlaylists.isSelected()) {
                    rightPanelGbc.gridy = 1;
                    rightPanel.add(createPlaylist, rightPanelGbc);
                    rightPanelGbc.gridy = 2;
                    rightPanel.add(removePlaylist, rightPanelGbc);
                    rightPanelGbc.gridy = 3;
                    rightPanel.add(addToPlaylist, rightPanelGbc);
                    rightPanelGbc.gridy = 4;
                    rightPanel.add(playlistLabel,rightPanelGbc);
                    rightPanelGbc.gridy = 5;
                    rightPanel.add(playlistList, rightPanelGbc);
                    rightPanelGbc.gridy = 6;
                    rightPanel.add(playlistsSongsLabel,rightPanelGbc);
                    rightPanelGbc.gridy = 7;
                    rightPanel.add(playlistSongs, rightPanelGbc);

                    rightPanel.revalidate();
                    rightPanel.repaint();
                } else {
                    rightPanel.remove(createPlaylist);
                    rightPanel.remove(removePlaylist);
                    rightPanel.remove(addToPlaylist);
                    rightPanel.remove(playlistSongs);
                    rightPanel.remove(playlistList);

                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            }
        });


        

        // Componentes do Painel Inferior
        
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
                    float dB  = (float) (volume * volume * volume) / 21f;
                    float dB_actual = 0;
                    if (dB > 6.0) {
                        dB_actual = 6.0f;
                    } else if (volume == -10) {
                        dB_actual = -79f;
                    } else {
                        dB_actual = dB;
                    }
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

    public void removeSongInPlaylist(Musica musica, JList<Playlist> playlistLists) {
        for (int i = 0; i < playlistLists.getModel().getSize(); i++) {
            playlistLists.getModel().getElementAt(i).removeMusica(musica);
        }
    }

    public class SongListCellRenderer extends DefaultListCellRenderer {
        
        public Component getListCellRendererComponent(JList<?> songList, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel)super.getListCellRendererComponent(songList, value, index, isSelected, cellHasFocus);

            if (value instanceof Musica) {
                Musica musica = (Musica) value;
                label.setText(musica.getNome());
            } else if (value instanceof Playlist) {
                Playlist playlist = (Playlist) value;
                label.setText(playlist.getNome());
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

