public class Musica extends Media {
    
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

    @Override
    public String toString() {
        return this.getNome();
    }


}
