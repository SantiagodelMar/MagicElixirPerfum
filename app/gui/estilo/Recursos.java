package gui.estilo;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

// Carga las imagenes y las fuentes que estan en la carpeta app/recursos
public class Recursos {

    // Guardamos las imagenes ya cargadas para no leer el archivo cada vez
    private static final Map<String, ImageIcon> imagenesCargadas = new HashMap<>();

    // Primero lo busca en el classpath (IntelliJ copia la carpeta recursos al compilar).
    // Si no lo encuentra, lo lee directo de la carpeta del proyecto.
    private static InputStream abrir(String ruta) {
        InputStream flujo = Recursos.class.getResourceAsStream("/recursos/" + ruta);
        if (flujo != null) {
            return flujo;
        }
        try {
            return new FileInputStream(new File("app/recursos/" + ruta));
        } catch (Exception e) {
            return null;
        }
    }

    // Si la fuente no carga se usa una del sistema para que el programa no se caiga
    public static Font fuente(String archivo, String fuenteDeRespaldo) {
        try (InputStream flujo = abrir("fuentes/" + archivo)) {
            Font fuente = Font.createFont(Font.TRUETYPE_FONT, flujo);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(fuente);
            return fuente;
        } catch (Exception e) {
            System.out.println("No se pudo cargar la fuente " + archivo + ", se usa " + fuenteDeRespaldo);
            return new Font(fuenteDeRespaldo, Font.PLAIN, 13);
        }
    }

    public static ImageIcon imagen(String nombre, int ancho, int alto) {
        String clave = nombre + "-" + ancho + "x" + alto;
        if (imagenesCargadas.containsKey(clave)) {
            return imagenesCargadas.get(clave);
        }

        ImageIcon icono;
        try (InputStream flujo = abrir("img/" + nombre + ".png")) {
            Image imagen = ImageIO.read(flujo).getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            icono = new ImageIcon(imagen);
        } catch (Exception e) {
            // Imagen vacia para que no salga error si falta el archivo
            icono = new ImageIcon(new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB));
        }

        imagenesCargadas.put(clave, icono);
        return icono;
    }

}//Fin public class
