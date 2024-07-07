import java.util.ArrayList;

public class Playlist {
    // variaveis da classe e seus metodos de acesso
    private ArrayList<Musica> musicas;
    private Usuario dono;
    private String titulo;

    // construtor da classe
    public Playlist(Usuario dono, String titulo) {
        this.musicas = new ArrayList<>();
        this.dono = dono;
        this.titulo = titulo;
    }

    // getters e setters para acesso e alteração de dados
    public String getNome() {
        return this.titulo;
    }

    public void setNome(String titulo) {
        this.titulo = titulo;
    }

    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    public Usuario getDono() {
        return this.dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    // metodos para adição e remoção de musicas em uma playlist e reorganizaqção da
    // ordem das musicas
    public void addMusica(Musica musica) {
        this.musicas.add(musica);
    }

    public void removeMusica(Musica musica) {
        this.musicas.remove(musica);
    }

    public ArrayList<Musica> reorgMusica(ArrayList<Musica> newOrder) {
        this.musicas = newOrder;
        return this.musicas;
    }

    // metodo toString para visualização dos dados cadastrados
    public String toString() {
        return this.getNome();
    }
}
