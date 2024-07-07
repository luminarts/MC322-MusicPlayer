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

public class MainFrame extends JFrame {
    JFrame mainFrame;
    private static MainFrame mainFrameInstance = null;
    private JLabel fileLabel;
    private JFileChooser fileChooser;
    private Clip audioClip;
    private boolean sliderisChanging = false;
    private Thread progressBarThread;
    private JSlider songDurationSlider;
    private int currentTime;
    private JLabel currentUserLabel;
    private DefaultListModel<Album> albumListModel;
    private JList<Album> albumList;
    private DefaultListModel<Musica> albumSongListModel;
    private JList<Musica> albumSongList;
    private JLabel songPlayingLabel; // Mover a declaração para um campo de classe
    private JProgressBar songProgression; // Mover a declaração para um campo de classe



    public boolean getSliderIsChanging() {
        return sliderisChanging;
    }

    public void setCurrentUser(String user) {
        currentUserLabel.setText("Usuário Atual: " + user);
    }

    // Redimensionar imagem de ícone dos botões
    public ImageIcon resizeButton(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }

    MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);

        // Componentes do painel da esquerda
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints leftPanelGbc = new GridBagConstraints();
        leftPanel.setBackground(Color.LIGHT_GRAY);

        DefaultListModel<Musica> importedListModel = new DefaultListModel<>();
        JList<Musica> songList = new JList<>(importedListModel);
        songList.setCellRenderer(new SongListCellRenderer());

        currentUserLabel = new JLabel("Usuário Atual: ");
        leftPanelGbc.gridx = 0;
        leftPanelGbc.gridy = 0;
        leftPanelGbc.insets = new Insets(5, 5, 5, 5);
        leftPanel.add(currentUserLabel, leftPanelGbc);

        JButton backToLoginButton = new JButton("Voltar pra tela de login");
        backToLoginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                LoginFrame.getLoginFrameInstance().setVisible(true);
            }
        });
        leftPanelGbc.gridy = 1;
        leftPanel.add(backToLoginButton, leftPanelGbc);

        JButton importMusic = new JButton("Importar Música");
        importMusic.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
                    Musica importedSong = new Musica(selectedFile.getName(), null, selectedFile.getParent(), 0, 0, null, null, selectedFile.getAbsolutePath());
                    importedListModel.addElement(importedSong);
                    songList.setModel(importedListModel);
                    songList.revalidate();
                    songList.repaint();
                }
            }
        });
        leftPanelGbc.gridy = 2;
        leftPanel.add(importMusic, leftPanelGbc);
        
        JButton addAlbumButton = new JButton("Adicionar Álbum");
        addAlbumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AlbumFrame albumFrame = new AlbumFrame();
                albumFrame.setVisible(true);
            }
        });
        leftPanelGbc.gridy = 3;
        leftPanel.add(addAlbumButton, leftPanelGbc);

        fileLabel = new JLabel("Nenhuma música selecionada");
        leftPanelGbc.gridy = 4;
        leftPanel.add(fileLabel, leftPanelGbc);

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

        leftPanelGbc.gridy = 5;
        leftPanelGbc.fill = GridBagConstraints.BOTH;
        leftPanelGbc.weighty = 1.0;
        JScrollPane scrollPane = new JScrollPane(songList);
        leftPanel.add(scrollPane, leftPanelGbc);

        songList.setBackground(Color.GRAY);
        songList.setForeground(Color.WHITE);
        
        
     // Lista de álbuns
        albumSongListModel = new DefaultListModel<>();
        albumSongList = new JList<>(albumSongListModel);
        albumSongList.setCellRenderer(new SongListCellRenderer());
        albumSongList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumListModel = new DefaultListModel<>();
        albumList = new JList<>(albumListModel);
        albumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        albumList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Album selectedAlbum = albumList.getSelectedValue();
                    if (selectedAlbum != null) {
                        // Atualize a lista de músicas do álbum selecionado
                        importedListModel.clear();

                        albumSongListModel.clear();
                        for (int i = 0; i < selectedAlbum.getMusicas().size(); i++) {
                            albumSongListModel.addElement(selectedAlbum.getMusicas().getElementAt(i));
                        }
                        if (!albumSongListModel.isEmpty()) {
                            albumSongList.setSelectedIndex(0); // Seleciona a primeira música por padrão
                            Musica selectedSong = albumSongListModel.getElementAt(0);
                            updateCurrentlyPlayingSong(selectedSong);
                        } else {
                            clearCurrentlyPlayingSong();
                        }
                    } else {
                        clearCurrentlyPlayingSong();
                    }
                }
             }
            
            
        });
        
        leftPanelGbc.gridy = 6;
        leftPanel.add(new JScrollPane(albumList), leftPanelGbc);

        // Rótulo "Músicas do Álbum:"
        JLabel albumSongsLabel = new JLabel("Músicas do Álbum:");
        leftPanelGbc.gridy = 7;
        leftPanelGbc.anchor = GridBagConstraints.WEST; // Alinhar à esquerda
        leftPanelGbc.insets = new Insets(10, 5, 5, 5); // Espaçamento
        leftPanel.add(albumSongsLabel, leftPanelGbc);

        // Lista de músicas do álbum
        albumSongListModel = new DefaultListModel<>();
        JList<Musica> albumSongList = new JList<>(albumSongListModel);
        albumSongList.setCellRenderer(new SongListCellRenderer());

        leftPanelGbc.gridy = 8;
        leftPanelGbc.weighty = 1.0;
        leftPanelGbc.fill = GridBagConstraints.BOTH;
        leftPanel.add(new JScrollPane(albumSongList), leftPanelGbc);
        
        add(leftPanel);
        setVisible(true);
        
        
        
        
        // Componentes do painel da direita
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints rightPanelGbc = new GridBagConstraints();

        songPlayingLabel = new JLabel("Selecione uma música");
        songDurationSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        songProgression = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

        songList.addListSelectionListener(new ListSelectionListener() {
        	@Override
        	public void valueChanged(ListSelectionEvent e) {
        	    if (!e.getValueIsAdjusting()) {
        	        Musica selectedSong = songList.getSelectedValue();
        	        if (selectedSong != null) {
        	            songPlayingLabel.setText("Música selecionada: " + selectedSong.getNome());
        	            try {
        	                File songFile = new File(selectedSong.getPath());
        	                AudioInputStream audioStream = AudioSystem.getAudioInputStream(songFile);
        	                audioClip = AudioSystem.getClip();
        	                selectedSong.setDuracao((int) audioClip.getMicrosecondLength());
        	                audioClip.open(audioStream);
        	                songDurationSlider.setMaximum((int) audioClip.getMicrosecondLength() / 1000);
        	                songProgression.setMaximum((int) audioClip.getMicrosecondLength() / 1000);
        	            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e2) {
        	                e2.printStackTrace();
        	                JOptionPane.showMessageDialog(mainFrame, "Erro ao carregar arquivo de áudio");
        	            }
        	        } else {
        	            songPlayingLabel.setText("Nenhuma música selecionada");
        	        }
        	    }
        	}

        });

        rightPanelGbc.gridx = 0;
        rightPanelGbc.gridy = 0;
        rightPanel.add(songPlayingLabel, rightPanelGbc);

        // Componentes do painel inferior
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bottomPanelGbc = new GridBagConstraints();
        bottomPanel.setBackground(Color.GRAY);
        int ic_width = 30;
        int ic_height = 30;
        JButton playButton = new JButton(resizeButton(new ImageIcon("MC322-MusicPlayer-main/Assets/play.png"), ic_width, ic_height));
        JButton pauseButton = new JButton(resizeButton(new ImageIcon("MC322-MusicPlayer-main/Assets/pause.png"), ic_width, ic_height));
        JButton stopButton = new JButton(resizeButton(new ImageIcon("MC322-MusicPlayer-main/Assets/stop.png"), ic_width, ic_height));
        JButton nextButton = new JButton(resizeButton(new ImageIcon("MC322-MusicPlayer-main/Assets/next.png"), ic_width, ic_height));
        JButton previousButton = new JButton(resizeButton(new ImageIcon("MC322-MusicPlayer-main/Assets/previous.png"), ic_width, ic_height));
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
                if (songList.getModel().getSize() >= 2) {
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
                if (songList.getModel().getSize() >= 2) {
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

        JPanel controlPanel = new JPanel(new GridLayout(1, 5, 5, 0));
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
                float volume = volumeSlider.getValue() / 100f;
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

        // Separar painéis direito e esquerdo
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        horizontalSplitPane.setResizeWeight(0.2); // Distribuir espaço igualmente

        // Adicionar ao painel principal
        JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, horizontalSplitPane, bottomPanel);
        mainPanel.setDividerSize(0);
        mainPanel.setResizeWeight(0.85);

        this.setContentPane(mainPanel);
    }
    private void updateCurrentlyPlayingSong(Musica musica) {
        SwingUtilities.invokeLater(() -> {
            songPlayingLabel.setText("Música selecionada: " + musica.getNome());
            try {
                File songFile = new File(musica.getPath());
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(songFile);
                audioClip = AudioSystem.getClip();
                musica.setDuracao((int) audioClip.getMicrosecondLength());
                audioClip.open(audioStream);
                songDurationSlider.setMaximum((int) audioClip.getMicrosecondLength() / 1000);
                songProgression.setMaximum((int) audioClip.getMicrosecondLength() / 1000);
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(mainFrame, "Erro ao carregar arquivo de áudio");
            }
        });
    }

    private void clearCurrentlyPlayingSong() {
        SwingUtilities.invokeLater(() -> {
            songPlayingLabel.setText("Nenhuma música selecionada");
            songDurationSlider.setMaximum(0);
            songProgression.setMaximum(0);
        });
    }

    public void startPlaybackThread() {
        progressBarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (audioClip != null && audioClip.isRunning()) {
                    currentTime++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
        });
        progressBarThread.start();
    }

    public void stopPlaybackThread() {
        if (progressBarThread != null && progressBarThread.isAlive()) {
            progressBarThread.interrupt();
        }
    }

    public class SongListCellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList<?> songList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(songList, value, index, isSelected, cellHasFocus);
            if (value instanceof Musica) {
                Musica musica = (Musica) value;
                label.setText(musica.getNome());
            }
            return label;
        }
    }

    
    public void addAlbumToList(Album album) {
        albumListModel.addElement(album);
    }

    public static MainFrame getInstance() {
        if (mainFrameInstance == null) {
            mainFrameInstance = new MainFrame();
        }
        return mainFrameInstance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainFrame.getInstance().setVisible(true);
            }
        });
    }
}
