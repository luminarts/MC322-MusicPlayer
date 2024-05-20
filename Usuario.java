public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String foto;

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String n) {
        this.nome = n;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getFoto() {
        return this.foto;
    }

    public void setFoto(String f) {
        this.foto = f;
    }

    public Usuario(String n, String e, String f) {
        this.nome = n;
        this.email = e;
        this.foto = f;
    }

    // @Override
    public String ToString() {
        String aux = "";
        aux += "Nome : " + this.nome +"\n";
        aux += "E-mail: " + this.email +"\n";
        aux += "Foto: " + this.foto;

        return aux;
    }

    
}
