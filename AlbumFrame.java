import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AlbumFrame extends JFrame {
    
    private static AlbumFrame albumFrameInstance = null;
    
    private JTextField albumNameField;
    private JTextField artistField;
    private JTextField yearField;
    private DefaultListModel<Musica> musicListModel;
    private JFileChooser fileChooser;
    

    public AlbumFrame() {
        setTitle("Adicionar Álbum");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel albumNameLabel = new JLabel("Nome do Álbum:");
        albumNameField = new JTextField(20);
        JLabel artistLabel = new JLabel("Artista:");
        artistField = new JTextField(20);
        JLabel yearLabel = new JLabel("Ano:");
        yearField = new JTextField(20);
        JButton addMusicButton = new JButton("Adicionar Música");
        
        //tratamento do campo ano para exibir apenas numeros
        ((AbstractDocument) yearField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newText.matches("\\d*")) {
                    super.replace(fb, offset, length, text, attrs);
                } else {
                	JOptionPane.showMessageDialog(AlbumFrame.this,
                            "Insira apenas numeros.",
                            "Erro de Entrada",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(albumNameLabel, gbc);
        gbc.gridx = 1;
        add(albumNameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(artistLabel, gbc);
        gbc.gridx = 1;
        add(artistField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(yearLabel, gbc);
        gbc.gridx = 1;
        add(yearField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(addMusicButton, gbc);

        musicListModel = new DefaultListModel<>();
        JList<Musica> musicList = new JList<>(musicListModel);
        JScrollPane musicScrollPane = new JScrollPane(musicList);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        add(musicScrollPane, gbc);

        addMusicButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
                int returnValue = fileChooser.showOpenDialog(AlbumFrame.this);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Musica importedSong = new Musica(selectedFile.getName(), null, selectedFile.getParent(), 0, 0, null, null, selectedFile.getAbsolutePath());
                    musicListModel.addElement(importedSong);
                }
            }
        });

        JButton saveAlbumButton = new JButton("Salvar Álbum");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        add(saveAlbumButton, gbc);

        saveAlbumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String albumName = albumNameField.getText();
                String artist = artistField.getText();
                int year = Integer.parseInt(yearField.getText());

                Album newAlbum = new Album(albumName, artist, year);

                for (int i = 0; i < musicListModel.size(); i++) {
                    Musica song = musicListModel.getElementAt(i);
                    newAlbum.addMusicaAlbum(song);
                }

                // Aqui você deve salvar o álbum em algum lugar apropriado, como uma lista na MainFrame
                MainFrame.getMainFrameInstance().addAlbumToList(newAlbum);

                // Fechar o JFrame após salvar
                setVisible(false);
            }
        });
        
     
    }

    public static AlbumFrame getAlbumFrameInstance() {
        if (albumFrameInstance == null) {
            albumFrameInstance = new AlbumFrame();
        }
        return albumFrameInstance;
    }
}

