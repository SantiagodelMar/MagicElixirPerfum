package gui.estilo;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.Normalizer;
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

    //Fotos de los perfumes (vitrina)

    // Las fotos estan en app/recursos/img/perfumes y se llaman con la marca y el nombre
    // en minuscula, sin tildes y con guiones. Ej: Lattafa + Khamrah Qahwa -> lattafa-khamrah-qahwa.png
    // Para cambiar una foto basta con guardar la nueva con el mismo nombre.
    public static String nombreFoto(String marca, String nombre) {
        String texto = Normalizer.normalize(marca + " " + nombre, Normalizer.Form.NFD);
        texto = texto.replaceAll("\\p{M}", "");                  // quita las tildes
        texto = texto.toLowerCase().replaceAll("[^a-z0-9]+", "-"); // lo que no sea letra o numero queda como guion

        // quita los guiones que sobran al principio y al final
        while (texto.startsWith("-")) {
            texto = texto.substring(1);
        }
        while (texto.endsWith("-")) {
            texto = texto.substring(0, texto.length() - 1);
        }
        return texto;
    }

    // Devuelve la foto del perfume del tamaño pedido.
    // Si el perfume es nuevo y todavia no tiene foto se usa la generica de la tienda.
    public static ImageIcon fotoPerfume(String marca, String nombre, int ancho, int alto) {
        String archivo = nombreFoto(marca, nombre);
        String clave = "perfumes/" + archivo + "-" + ancho + "x" + alto;
        if (imagenesCargadas.containsKey(clave)) {
            return imagenesCargadas.get(clave);
        }

        BufferedImage original = leerImagen("img/perfumes/" + archivo + ".png");
        if (original == null) {
            original = leerImagen("img/perfumes/perfume-generico.png");
        }

        ImageIcon icono;
        if (original == null) {
            icono = new ImageIcon(new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB));
        } else {
            icono = new ImageIcon(escalar(original, ancho, alto));
        }

        imagenesCargadas.put(clave, icono);
        return icono;
    }

    private static BufferedImage leerImagen(String ruta) {
        try (InputStream flujo = abrir(ruta)) {
            if (flujo == null) {
                return null;
            }
            return ImageIO.read(flujo);
        } catch (Exception e) {
            return null;
        }
    }

    // Achica la imagen con buena calidad. Es mas rapido que getScaledInstance
    // y en la vitrina se cargan muchas fotos a la vez.
    private static BufferedImage escalar(BufferedImage original, int ancho, int alto) {
        BufferedImage nueva = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = nueva.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(original, 0, 0, ancho, alto, null);
        g2.dispose();
        return nueva;
    }

}//Fin public class
