public class Main {
    public static void main(String[] args) {
        LoginFrame loginFrame = new LoginFrame(); 
        RegisterFrame registerFrame = new RegisterFrame();
        MainFrame mainFrame = new MainFrame();

        loginFrame.setVisible(true); 
        registerFrame.setVisible(false);
        mainFrame.setVisible(false);
    }
}