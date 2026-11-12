import config.Contexto;
import ui.UserInterface;

// Version por consola (CLI) del taller: los menus de UserInterface con Scanner.
// Usa el mismo Contexto que la interfaz grafica, asi que se conecta a la misma base de datos.
public class ApplicationConsola {

    public static void main(String[] args) {

        Contexto contexto;
        try {
            contexto = new Contexto(true);
        } catch (RuntimeException e) {
            System.out.println("No hay conexion con la base de datos: " + e.getMessage());
            System.out.println("Se abre en modo de PRUEBA (memoria): los datos no se guardan.");
            contexto = new Contexto(false);
        }

        UserInterface userInterface = new UserInterface(contexto);
        userInterface.menuApp();

    }

}
