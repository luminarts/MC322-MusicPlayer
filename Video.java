public class Video extends Media {
    private int resolution;

    public int getResolution() {
        return this.resolution;
    }

    public void setResolution(int r) {
        this.resolution = r;
    }

    public Video(String n, String g, String artista, int d, int ano, String album, String foto) {
        this.setNome(n);
        this.setGenero(g);
        this.setArtista(artista);
        this.setDuracao(d);
        this.setAnoLanc(ano);
        this.setAlbum(album);
        this.setFotoCapa(foto);
    }

}
