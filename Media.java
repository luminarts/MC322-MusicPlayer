public class Media {
    private String nome;
    private String genero;
    private String artista;
    private int duracao;
    private int anoLanc;
    private String album;
    private String fotoCapa;
    private String path;

    
    public String getNome() {
        return this.nome;
    }

    public void setNome(String n) {
        this.nome = n;
    }
    
    public String getGenero() {
        return this.genero;
    }

    public void setGenero(String g) {
        this.genero = g;
    }

    public String getArtista() {
        return this.artista;
    }

    public void setArtista(String a) {
        this.artista = a;
    }

    public int getDuracao() {
        return this.duracao;
    }

    public void setDuracao(int d) {
        this.duracao = d;
    }

    public int getAnoLanc() {
        return this.anoLanc;
    }

    public void setAnoLanc(int aL) {
        this.anoLanc = aL;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(String a) {
        this.album = a;
    }

    public String getFotoCapa() {
        return this.fotoCapa;
    }

    public void setFotoCapa(String fC) {
        this.fotoCapa = fC;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String p) {
        this.path = p;
    }
}
