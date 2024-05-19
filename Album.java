import java.util.ArrayList;

public class Album {
    // variaveis da classe com seus metodos de acesso
    private ArrayList<Musica> musicas;
    private String nome;
    private String autor;
    private int ano;

    // Construtor da classe
    public Album(String nome, String autor, int ano) {
        this.musicas = new ArrayList<>();
        this.nome = nome;
        this.autor = autor;
        this.ano = ano;
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
        aux += "Nome : " + this.nome + "\n";
        aux += "Autor: " + this.autor + "\n";
        aux += "Ano: " + this.ano + "\n";
        aux += "Musicas: " + "\n";
        for (Musica musica : musicas) {
            aux += musica.toString() + "\n";
        }
        aux += "\n";

        return aux;

    }

    // função que adiciona musicas a um album a partir do album cadastrado no
    // cadastro da musica na classe Musica
    public void addMscAlbum(Musica musica) {
        if (musica.getAlbum().equals(this.nome)) {
            this.musicas.add(musica);
        } else {
            System.out.println("Musica nao pertencente ao album: " + musica.getNome());
        }
    }
}