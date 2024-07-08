import java.io.File;
import java.io.IOException;

public class CreateFile {
    public File createUsersFile() {
        try {
            File arq = new File("User_data/usuarios.txt");
            arq.createNewFile();
            return arq;
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
          }
        
    }
    
}
