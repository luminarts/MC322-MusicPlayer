import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AlbumFrame extends JFrame {
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
                    newAlbum.addMusica(song);
                }

                // Aqui você deve salvar o álbum em algum lugar apropriado, como uma lista na MainFrame
                MainFrame.getInstance().addAlbumToList(newAlbum);

                // Fechar o JFrame após salvar
                dispose();
            }
        });
    }
}


