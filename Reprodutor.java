import java.util.ArrayList;

public class Reprodutor {
    private ArrayList<Musica> fila;
    private Musica currMusica;
    // Fazer método de se tentar Retroceder ou Avançar e não tiver mais pra onde ir, pegar uma música aleatória do banco de dados

    public Musica getCurrMusica() {
        return currMusica;
    }

    public void setCurrMusica(Musica cM) {
        this.currMusica = cM;
    }

    public void addToFila(Musica m) {
        fila.add(m);
    }

    public Musica removeFromFila(Musica m) {
        Musica musicaRmv = fila.remove(fila.indexOf(m));
        return musicaRmv;
    }
    
    public ArrayList<Musica> getFila() {
        return this.fila;
    }

    public void Play(Musica m){
        currMusica = m;
        // como implementar isso?
    }

    public void Pause() {

    }

    public void Pular() {
        
    }

    public void Retroceder() {

    }

}
