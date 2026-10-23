package gui.estilo;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

// Paleta de colores y tipografias de todo el sistema.
// Es la misma marca del CarritoMagicElixir: negro y crema como el logo,
// letra con serifa (Playfair) para los titulos y letra sans (Inter) para leer.
public class Estilo {

    // Colores de la marca
    public static final Color BLANCO = Color.WHITE;
    public static final Color LIENZO = new Color(0xF4, 0xF2, 0xEE);       // fondo crema de las pantallas
    public static final Color TINTA = new Color(0x0C, 0x0C, 0x0D);        // negro del logo
    public static final Color TINTA_SUAVE = new Color(0x2B, 0x2B, 0x2E);  // negro al pasar el mouse
    public static final Color GRIS = new Color(0x74, 0x72, 0x6E);         // textos secundarios
    public static final Color GRIS_CLARO = new Color(0xB4, 0xB0, 0xA9);   // placeholder y deshabilitados
    public static final Color LINEA = new Color(0xE3, 0xE0, 0xDA);        // bordes y separadores
    public static final Color SELECCION = new Color(0xEF, 0xEB, 0xE4);    // fila seleccionada
    public static final Color CREMA = new Color(0xF6, 0xF4, 0xF0);        // texto sobre fondo negro
    public static final Color CREMA_TENUE = new Color(0x9A, 0x97, 0x92);

    // Menu lateral
    public static final Color MENU_ACTIVO = new Color(0x24, 0x24, 0x27);
    public static final Color MENU_ENCIMA = new Color(0x18, 0x18, 0x1A);

    // Colores de los estados: texto oscuro sobre un fondo suave
    public static final Color VERDE = new Color(0x2F, 0x6B, 0x3F);
    public static final Color VERDE_FONDO = new Color(0xE5, 0xEF, 0xE7);
    public static final Color AMBAR = new Color(0x8A, 0x5A, 0x12);
    public static final Color AMBAR_FONDO = new Color(0xF6, 0xEC, 0xD9);
    public static final Color AZUL = new Color(0x2C, 0x4A, 0x73);
    public static final Color AZUL_FONDO = new Color(0xE4, 0xEA, 0xF3);
    public static final Color ROJO = new Color(0x8C, 0x2F, 0x2F);
    public static final Color ROJO_FONDO = new Color(0xF4, 0xE1, 0xDF);

    // Fuentes (se cargan una sola vez desde app/recursos/fuentes)
    private static final Font SERIF = Recursos.fuente("PlayfairDisplay_400Regular.ttf", Font.SERIF);
    private static final Font SERIF_BOLD = Recursos.fuente("PlayfairDisplay_700Bold.ttf", Font.SERIF);
    private static final Font SANS = Recursos.fuente("Inter_400Regular.ttf", Font.SANS_SERIF);
    private static final Font SANS_BOLD = Recursos.fuente("Inter_600SemiBold.ttf", Font.SANS_SERIF);

    // Se llama una vez al iniciar el programa para que los componentes de Swing
    // usen nuestras fuentes y colores por defecto
    public static void aplicar() {
        String[] clavesFuente = {"Label.font", "Button.font", "TextField.font", "PasswordField.font",
                "ComboBox.font", "List.font", "Table.font", "TableHeader.font", "TextArea.font",
                "OptionPane.messageFont", "OptionPane.buttonFont", "ToolTip.font"};
        for (String clave : clavesFuente) {
            UIManager.put(clave, sans(13f));
        }

        UIManager.put("Panel.background", LIENZO);
        UIManager.put("Button.disabledText", GRIS_CLARO);
        UIManager.put("ComboBox.background", BLANCO);
        UIManager.put("ComboBox.selectionBackground", SELECCION);
        UIManager.put("ComboBox.selectionForeground", TINTA);
        UIManager.put("ComboBox.disabledBackground", LIENZO);
        UIManager.put("ComboBox.disabledForeground", GRIS);
        UIManager.put("List.selectionBackground", SELECCION);
        UIManager.put("List.selectionForeground", TINTA);
        UIManager.put("TextField.inactiveForeground", GRIS);
        UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
        UIManager.put("ToolTip.background", TINTA);
        UIManager.put("ToolTip.foreground", CREMA);
        UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder(6, 10, 6, 10));
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("Table.scrollPaneBorder", BorderFactory.createEmptyBorder());
    }

    public static Font serif(float tamano) {
        return SERIF.deriveFont(tamano);
    }

    public static Font serifBold(float tamano) {
        return SERIF_BOLD.deriveFont(tamano);
    }

    public static Font sans(float tamano) {
        return SANS.deriveFont(tamano);
    }

    public static Font sansBold(float tamano) {
        return SANS_BOLD.deriveFont(tamano);
    }

    // Separa un poco las letras. Se usa en los rotulos pequenos en mayuscula
    public static Font espaciada(Font fuente, float separacion) {
        Map<TextAttribute, Object> atributos = new HashMap<>();
        atributos.put(TextAttribute.TRACKING, separacion);
        return fuente.deriveFont(atributos);
    }

    // Rotulo pequeno en mayuscula (ej: "NOMBRE", "USUARIOS")
    public static JLabel micro(String texto) {
        JLabel etiqueta = new JLabel(texto.toUpperCase());
        etiqueta.setFont(espaciada(sansBold(10.5f), 0.12f));
        etiqueta.setForeground(GRIS);
        return etiqueta;
    }

    public static JLabel titulo(String texto, float tamano) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(serifBold(tamano));
        etiqueta.setForeground(TINTA);
        return etiqueta;
    }

    public static JLabel texto(String contenido, float tamano, Color color) {
        JLabel etiqueta = new JLabel(contenido);
        etiqueta.setFont(sans(tamano));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    // Devuelve el color de texto y de fondo segun el estado que se muestra en las tablas.
    // posicion 0 = color del texto, posicion 1 = color del fondo
    public static Color[] coloresEstado(String estado) {
        String texto = estado.toLowerCase();

        if (texto.equals("activo") || texto.equals("disponible") || texto.equals("entregado")
                || texto.equals("aprobada") || texto.equals("recibida") || texto.equals("reembolsada")
                || texto.equals("pagado")) {
            return new Color[]{VERDE, VERDE_FONDO};
        }
        if (texto.equals("bajo stock") || texto.equals("pendiente") || texto.equals("solicitada")
                || texto.equals("creado")) {
            return new Color[]{AMBAR, AMBAR_FONDO};
        }
        if (texto.equals("en camino") || texto.equals("enviado")) {
            return new Color[]{AZUL, AZUL_FONDO};
        }
        if (texto.equals("agotado") || texto.equals("inactivo") || texto.equals("cancelado")
                || texto.equals("cancelada") || texto.equals("rechazada")) {
            return new Color[]{ROJO, ROJO_FONDO};
        }
        return new Color[]{GRIS, LIENZO};
    }

}//Fin public class
