
public class VazioException extends Exception{
	public VazioException(String campo) {
        super("O campo " + campo + " não pode estar vazio.");
    }
}
