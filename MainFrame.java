import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
// import java.util.ArrayList;
import java.util.ArrayList;
import java.util.ArrayList;



public class MainFrame extends JFrame{ 
    JFrame mainFrame; 
    private static MainFrame mainFrameInstance = null;
    private JLabel fileLabel;
    private JFileChooser fileChooser;
    private Clip audioClip;
    private DefaultListModel<Album> albumListModel;
    private boolean sliderisChanging = false;
    private Thread progressBarThread;
    private JSlider songDurationSlider;
    private int currentTime;

    public int getCurrentTime() {
        return currentTime;
    }

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
        
        JLabel currentUserLabel = new JLabel("Bem vindo(a) " + (LoginFrame.getLoginFrameInstance().getCurrentUser() != null? LoginFrame.getLoginFrameInstance().getCurrentUser().getUsername() + "!" : "Pessoa!"));

        JButton backToLoginButton = new JButton("Voltar pra tela de login");
        backToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginFrame.getLoginFrameInstance().setVisible(true);
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

        JToggleButton toggleAlbumsButton = new JToggleButton("Painel de Álbuns");
        

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
        leftPanel.add(currentUserLabel, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 1;
        leftPanelGbc.insets = new Insets(5,5, 5, 5);
        leftPanel.add(backToLoginButton, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 2;
        leftPanel.add(importMusic, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 3;
        leftPanel.add(removeImportedSong, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 4;
        leftPanel.add(toggleAlbumsButton, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 5;
        leftPanel.add(showPlaylists, leftPanelGbc);

        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 6;
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

        JButton addAlbumButton = new JButton("Adicionar novo álbum");
        addAlbumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AlbumFrame albumFrame = new AlbumFrame();
                albumFrame.setVisible(true);
            }
        });

        JList<Album> albumList;

        DefaultListModel<Musica> albumSongListModel;
        albumSongListModel = new DefaultListModel<>();

        JList<Musica> albumSongList = new JList<>(albumSongListModel);
        albumSongList.setCellRenderer(new SongListCellRenderer());
        
        albumListModel = new DefaultListModel<>();
        albumList = new JList<>(albumListModel);
        albumList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (albumList.getSelectedValue() != null) {
                        albumSongListModel.clear();

                        ArrayList<Musica> musicas = albumList.getSelectedValue().getMusicas();
                        
                        for (Musica musica : musicas) {
                            albumSongListModel.addElement(musica);
                            auxSongList.addElement(musica);
                        }

                        albumSongList.setModel(albumSongListModel);
                        albumSongList.revalidate();
                        albumSongList.repaint();
                    
                        songList.setModel(auxSongList);
                        songList.revalidate();
                        songList.repaint();
                    }
                }
            }
        });

        albumSongList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                songList.setSelectedValue(albumSongList.getSelectedValue(), true);
            }
        });

        JPanel albumPanel = new JPanel(new GridBagLayout());
        GridBagConstraints albumPanelGbc = new GridBagConstraints();
        albumPanel.setBackground(Color.LIGHT_GRAY);
        //adicao de album usando o AlbumFrame
        albumPanelGbc.gridx = 0;
        albumPanelGbc.gridy = 0;
        albumPanel.add(addAlbumButton, albumPanelGbc);

        albumPanelGbc.gridy = 1;
        albumPanelGbc.weightx = 1.0;
        albumPanelGbc.weighty = 1.0;
        albumPanelGbc.fill = GridBagConstraints.BOTH;
        albumPanel.add(albumList, albumPanelGbc);
        //exibir musicas do album selecionado no Label de musicas
        albumPanelGbc.gridy = 2;
        albumPanelGbc.weighty = 0;
        albumPanel.add(new JLabel("Músicas do álbum selecionado:"), albumPanelGbc);

        albumPanelGbc.gridy = 3;
        albumPanelGbc.weighty = 1.0;
        albumPanel.add(albumSongList, albumPanelGbc);

        toggleAlbumsButton.addChangeListener(new ChangeListener() {
            public void stateChanged (ChangeEvent e) {
                if (toggleAlbumsButton.isSelected()) {
                    rightPanel.remove(songPlayingLabel);
                    rightPanelGbc.gridx = 0;
                    rightPanelGbc.gridy = 1;
                    rightPanel.add(albumPanel, rightPanelGbc);
                    showPlaylists.setSelected(false);
                    rightPanel.revalidate();
                    rightPanel.repaint();

                } else {
                    rightPanel.removeAll();
                    rightPanelGbc.gridx = 0;
                    rightPanelGbc.gridy = 0;
                    rightPanel.add(songPlayingLabel, rightPanelGbc);
                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            }
        });

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

        showPlaylists.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
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

                    toggleAlbumsButton.setSelected(false);

                    rightPanel.revalidate();
                    rightPanel.repaint();
                } else {
                    rightPanel.removeAll();
                    rightPanel.add(songPlayingLabel);

                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            }
        });


        

        // Componentes do Painel Inferior
        
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bottomPanelGbc = new GridBagConstraints();
        bottomPanel.setBackground(Color.GRAY);
        int ic_width = 30;
        int ic_height = 30;
        JButton playButton = new JButton(resizeButton(new ImageIcon("Assets/play.png"), ic_width, ic_height));
        playButton.setBackground(bottomPanel.getBackground());
        JButton pauseButton = new JButton(resizeButton(new ImageIcon("Assets/pause.png"), ic_width, ic_height));
        pauseButton.setBackground(bottomPanel.getBackground());
        JButton stopButton = new JButton(resizeButton(new ImageIcon("Assets/stop.png"), ic_width, ic_height));
        stopButton.setBackground(bottomPanel.getBackground());
        JButton nextButton = new JButton(resizeButton(new ImageIcon("Assets/next.png"), ic_width, ic_height));
        nextButton.setBackground(bottomPanel.getBackground());
        JButton previousButton = new JButton(resizeButton(new ImageIcon("Assets/previous.png"), ic_width, ic_height));
        previousButton.setBackground(bottomPanel.getBackground());
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        JLabel zeroLabel = new JLabel("00:00");
        JLabel songLengthLabel = new JLabel("00:00");




        

        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (audioClip != null && !audioClip.isRunning()) {
                    Integer songLength_minutes = (int) audioClip.getMicrosecondLength()/60000000;
                    float songLength_seconds = (audioClip.getMicrosecondLength()/60000000f - songLength_minutes) * 60;
                    Integer songLengthSeconds_int = (int) songLength_seconds;

                    songLengthLabel.setText(songLength_minutes.toString() + ":" + songLengthSeconds_int.toString());
                    songLengthLabel.revalidate();
                    songLengthLabel.repaint();

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
        volumeSlider.setPreferredSize(new Dimension(volumeSlider.getPreferredSize().width - 80, volumeSlider.getPreferredSize().height));
        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                float volume = volumeSlider.getValue()/100f;
                if (audioClip != null) {
                    FloatControl gainControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                    gainControl.setValue(dB);
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
        songDurationSlider.setPreferredSize(new Dimension(songDurationSlider.getPreferredSize().width + 100, songDurationSlider.getPreferredSize().height));
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
        bottomPanel.add(zeroLabel,bottomPanelGbc);

        bottomPanelGbc.gridx = 3;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(songDurationSlider, bottomPanelGbc);
        // bottomPanel.add(songProgression, bottomPanelGbc);
        bottomPanelGbc.gridx = 4;
        bottomPanelGbc.gridy = 0;
        bottomPanel.add(songLengthLabel,bottomPanelGbc);
        // Separate right and left Panels
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        horizontalSplitPane.setResizeWeight(0.2); // Distribute space equally
        horizontalSplitPane.setDividerSize(3);

        // add to mainPanel
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, bottomPanel);
        mainPanel.setDividerSize(0);
        mainPanel.setResizeWeight(0.85);

        this.setContentPane(mainPanel);
        

    }

    public void startPlaybackThread() {
        JLabel currentTime = new JLabel();
        progressBarThread = new Thread(new Runnable(){
            @Override
            public void run() {
                while(audioClip.isRunning() && audioClip != null) {
                    currentTime.setText(Long.toString(audioClip.getMicrosecondPosition()));
                    System.out.println(currentTime.getText());
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
    public ImageIcon resizeButton(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
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

    public void addAlbumToList(Album album) {
        albumListModel.addElement(album);
    }

    public static MainFrame getMainFrameInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }


}  

