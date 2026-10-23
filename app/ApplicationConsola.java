import config.Contexto;
import ui.UserInterface;

// Version por consola (CLI) del taller: los menus de UserInterface con Scanner.
// Usa el mismo Contexto que la interfaz grafica, asi que tiene los mismos datos de prueba.
public class ApplicationConsola {

    public static void main(String[] args) {

        Contexto contexto = new Contexto();
        UserInterface userInterface = new UserInterface(contexto);
        userInterface.menuApp();

    }

}
