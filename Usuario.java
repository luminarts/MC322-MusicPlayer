import java.util.ArrayList;

public class Usuario {
    private static ArrayList<Usuario> usuariosCadastrados = new ArrayList<>();

    private int id = 0;
    private String nome;
    private String email;
    private String foto;
    private String senha;
    private String username;

    public Usuario(String nome, String email, String foto, String senha, String username) throws TamanhoUserException, PalavroesUserException {
        if (username.length() > 25) {
            throw new TamanhoUserException("O nome de usuário não pode ter mais que 25 caracteres.");
        }
        if (verificaPalavrao(username)) {
            throw new PalavroesUserException("O username contém uma palavra proibida.");
        }
        this.id = usuariosCadastrados.size() + 1; // Atribui um ID sequencial
        this.nome = nome;
        this.email = (email != null) ? email : "";
        this.foto = (foto != null) ? foto : "";
        this.senha = senha;
        this.username = username;
        usuariosCadastrados.add(this); // Adiciona o usuário à lista de cadastrados
    }

    public static ArrayList<Usuario> getUsuariosCadastrados() {
        return usuariosCadastrados;
    }

    public static void setUsuariosCadastrados(ArrayList<Usuario> usuariosCadastrados) {
        Usuario.usuariosCadastrados = usuariosCadastrados;
    }

    public static Usuario verificarLogin(String username, String senha) {
        for (Usuario usuario : usuariosCadastrados) {
            if (usuario.getUsername().equals(username) && usuario.getSenha().equals(senha)) {
                return usuario; // Retorna o usuário encontrado
            }
        }
        return null; // Retorna null se não encontrar nenhum usuário válido
    }

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

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String p) {
        this.senha = p;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public static void addUsuario(Usuario usuario) {
        usuariosCadastrados.add(usuario);
    }

    // @Override
    public String ToString() {
        String aux = "";
        aux += "Nome : " + this.nome + "\n";
        aux += "E-mail: " + this.email + "\n";
        aux += "Foto: " + this.foto + "\n";
        aux += "Username: " + this.username;

        return aux;
    }

    private boolean verificaPalavrao(String username) throws PalavroesUserException {
        String[] palavrao = {"porra", "buceta", "caralho", "pinto", "gozo", "gozador", "puta", "penis", "xereca"};
        for (String palavra : palavrao) {
            if (username.toLowerCase().contains(palavra)) {
                throw new PalavroesUserException("O username contém uma palavra proibida.");
            }
        }
        return false;
    }
}

