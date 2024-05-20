import java.util.ArrayList;

public class Musica extends Media {
    private ArrayList<Musica> musicasDisponiveis;
    
    public Musica(String n, String g, String artista, int d, int ano, String album, String foto, String path) {
        this.setNome(n);
        this.setGenero(g);
        this.setArtista(artista);
        this.setDuracao(d);
        this.setAnoLanc(ano);
        this.setAlbum(album);
        this.setFotoCapa(foto);
        this.setPath(path);
    }

    public void addMusica(Musica musica) {
        this.musicasDisponiveis.add(musica);
    }

    public void removeMusica(Musica musica) {
        this.musicasDisponiveis.remove(musica);
    }


}
