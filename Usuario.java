public class Usuario {
    private String nome;
    private String email;
    private String foto;

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

    // @Override
    public String ToString() {
        String aux = "";
        aux += "Nome : " + this.nome +"\n";
        aux += "E-mail: " + this.email +"\n";
        aux += "Foto: " + this.foto;

        return aux;
    }

    
}
