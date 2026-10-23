import config.Contexto;
import gui.VentanaLogin;
import gui.estilo.Estilo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;

// Punto de entrada del sistema con interfaz grafica (ventanas).
// La version por consola del taller sigue funcionando: ejecutar ApplicationConsola.java
public class Application {

    public static void main(String[] args) {

        // Letras suavizadas (se ven mejor las fuentes)
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Un solo Contexto: repositories, services y datos de prueba
        Contexto contexto = new Contexto();

        // Las ventanas de Swing siempre se crean en su propio hilo (invokeLater)
        SwingUtilities.invokeLater(() -> {
            try {
                // Apariencia de Java (Metal) para que se vea igual en Windows, Mac y Linux
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            } catch (Exception e) {
                System.out.println("No se pudo cambiar la apariencia, se usa la de por defecto");
            }
            Estilo.aplicar();

            VentanaLogin login = new VentanaLogin(contexto);
            login.setVisible(true);
        });

    }

}
