import javax.swing.DefaultListModel;

public class Album {
    private String nome;
    private String autor;
    private int ano;
    private DefaultListModel<Musica> musicas;

    // Construtor da classe
    public Album(String nome, String autor, int ano) {
        this.nome = nome;
        this.autor = autor;
        this.ano = ano;
        this.musicas = new DefaultListModel<>();
    }

    // Getters e Setters para consulta e alteração dos dados
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

    public DefaultListModel<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(DefaultListModel<Musica> musicas) {
        this.musicas = musicas;
    }

    // Metodo toString para mostrar o adicionado
    public String toString() {
        String aux = "";
        aux += "Albúm : " + this.nome + "\n ";
        aux += "Autor: " + this.autor + "\n ";
        aux += "Ano: " + this.ano + "\n ";
        aux += "Musicas: " + "\n ";

        for (int i = 0; i < musicas.size(); i++) {
            aux += musicas.get(i).getNome() + "\n";
        }

        aux += "\n";
        return aux;
    }

    // função que adiciona musicas a um album a partir do album cadastrado no
    // cadastro da musica na classe Musica
    public void addMusica(Musica musica) {
        this.musicas.addElement(musica);
    }
}
