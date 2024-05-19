import java.util.ArrayList;

public class Biblioteca {
    // variaveis da classe com seus metodos de acesso
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albuns;
    private Usuario dono;

    // construtor da classe
    public Biblioteca(Usuario dono) {
        this.playlists = new ArrayList<>();
        this.albuns = new ArrayList<>();
        this.dono = dono;
    }

    // metodos getters e setters para acesso e alteração das informações
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    public ArrayList<Album> getAlbuns() {
        return albuns;
    }

    public void setAlbuns(ArrayList<Album> albuns) {
        this.albuns = albuns;
    }

    public Usuario getDono() {
        return dono;
    }

    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
    }

    public void removePlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
    }

    public void addAlbum(Album album) {
        this.albuns.add(album);
    }

    public void removeAlbum(Album album) {
        this.albuns.remove(album);
    }

    // função toString para visualização dos dados cadastrados
    public String toString() {
        String aux = "";
        aux += "Dono da Biblioteca: " + this.dono.getNome() + "\n";
        aux += "Playlists:\n";
        for (Playlist playlist : playlists) {
            aux += playlist.toString() + "\n";
        }
        aux += "Álbuns:\n";
        for (Album album : albuns) {
            aux += album.toString() + "\n";
        }
        return aux;
    }
}
