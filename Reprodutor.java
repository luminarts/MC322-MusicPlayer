import java.util.ArrayList;

public class Reprodutor extends Musica {
    private ArrayList<Musica> fila;

    public void addToFila(Musica m) {
        fila.add(m);
    }

    public Musica removeFromFila(Musica m) {
        Musica musicaRmv = fila.remove(fila.indexOf(m));
        return musicaRmv;
    }

    
}
