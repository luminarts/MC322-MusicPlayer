import java.util.ArrayList;

public class Album {
    // variaveis da classe com seus metodos de acesso
    private String nome;
    private String autor;
    private int ano;
    private ArrayList<Musica> musicas;

    // Construtor da classe
    public Album(String nome, String autor, int ano) {
        this.nome = nome;
        this.autor = autor;
        this.ano = ano;
        this.musicas = new ArrayList<>();
    }

    // Getters e Setters para consulta e alteração dos dados
    public ArrayList<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(ArrayList<Musica> musicas) {
        this.musicas = musicas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    // Metodo toString para mostrar o adicionado
    public String toString() {

        String aux = "";
        aux += "Álbum : " + this.nome + " ";
        aux += "Autor: " + this.autor + " ";
        aux += "Ano: " + this.ano + " ";
        aux += "Musicas: " + " ";
        for (Musica musica : musicas) {
            aux += musica.toString();
        }
        aux += "\n";
        return aux;
    }

    // função que adiciona musicas a um album a partir do album cadastrado no
    // cadastro da musica na classe Musica
    public void addMusicaAlbum(Musica musica) {
        this.musicas.add(musica);
    }
}