import config.Contexto;
import gui.VentanaLogin;
import gui.estilo.Estilo;

import javax.swing.JOptionPane;
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

        // Si algo falla en la base de datos dentro de una ventana, se muestra el mensaje en vez de callarse
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) -> {
            error.printStackTrace();
            JOptionPane.showMessageDialog(null, error.getMessage(), "Magic Elixir Perfum", JOptionPane.ERROR_MESSAGE);
        });

        // Un solo Contexto: repositories y services. true = base de datos MySQL (XAMPP)
        Contexto contexto;
        try {
            contexto = new Contexto(true);
        } catch (RuntimeException e) {
            // Red de seguridad para la presentacion: si XAMPP no esta prendido se avisa y se trabaja en memoria
            JOptionPane.showMessageDialog(null,
                    e.getMessage() + "\n\nSe abrira en modo de PRUEBA (memoria): los datos no se guardan.",
                    "No hay conexion con la base de datos", JOptionPane.WARNING_MESSAGE);
            contexto = new Contexto(false);
        }
        final Contexto contextoFinal = contexto;

        // Las ventanas de Swing siempre se crean en su propio hilo (invokeLater)
        SwingUtilities.invokeLater(() -> {
            try {
                // Apariencia de Java (Metal) para que se vea igual en Windows, Mac y Linux
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            } catch (Exception e) {
                System.out.println("No se pudo cambiar la apariencia, se usa la de por defecto");
            }
            Estilo.aplicar();

            VentanaLogin login = new VentanaLogin(contextoFinal);
            login.setVisible(true);
        });

    }

}
