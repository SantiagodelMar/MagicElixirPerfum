import api.ApiServer;
import config.Contexto;
import ui.UserInterface;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {

        // Un solo Contexto: la consola y la tienda web comparten los mismos datos
        Contexto contexto = new Contexto();

        ApiServer apiServer = null;
        try {
            apiServer = new ApiServer(contexto, ApiServer.PUERTO);
            apiServer.iniciar();
        } catch (IOException e) {
            System.out.println("No se pudo iniciar la tienda web en el puerto " + ApiServer.PUERTO
                    + " (puede estar ocupado). La consola funciona igual.");
        }

        UserInterface userInterface = new UserInterface(contexto);
        userInterface.menuApp();

        if (apiServer != null) {
            apiServer.detener();
        }

    }

}
