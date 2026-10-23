package gui.estilo;

import domain.enums.MetodoDePago;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Metodos para mostrar bonito los datos en pantalla (plata, fechas y enums)
public class Formato {

    private static final Locale COLOMBIA = Locale.forLanguageTag("es-CO");
    private static final DateTimeFormatter FECHA_CORTA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_LARGA = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM 'de' yyyy", COLOMBIA);

    // 240000.0 -> "$ 240.000" (en pesos no usamos decimales)
    public static String dinero(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        DecimalFormat formato = new DecimalFormat("#,##0", simbolos);
        return "$ " + formato.format(valor);
    }

    // 1250 -> "1.250"
    public static String numero(int valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        DecimalFormat formato = new DecimalFormat("#,##0", simbolos);
        return formato.format(valor);
    }

    public static String fecha(LocalDate fecha) {
        if (fecha == null) {
            return "—";
        }
        return fecha.format(FECHA_CORTA);
    }

    // "viernes 23 de octubre de 2026" con la primera letra en mayuscula
    public static String fechaLarga(LocalDate fecha) {
        String texto = fecha.format(FECHA_LARGA);
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    // EN_CAMINO -> "En camino", BAJO_STOCK -> "Bajo stock"
    public static String nombreEnum(Enum<?> valor) {
        if (valor == null) {
            return "—";
        }
        String texto = valor.name().replace("_", " ").toLowerCase();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    // Nombre corto del metodo de pago para las tablas
    public static String metodoPago(MetodoDePago metodo) {
        if (metodo == null) {
            return "—";
        }
        switch (metodo) {
            case EFECTIVO:
                return "Efectivo";
            case PSE:
                return "PSE";
            case T_CREDITO:
                return "Tarjeta crédito";
            case T_DEBITO:
                return "Tarjeta débito";
            default:
                return metodo.name();
        }
    }

    // "Anyi", "Cañas" -> "AC" (para el circulo del usuario)
    public static String iniciales(String nombre, String apellido) {
        String iniciales = "";
        if (nombre != null && !nombre.isEmpty()) {
            iniciales += nombre.substring(0, 1);
        }
        if (apellido != null && !apellido.isEmpty()) {
            iniciales += apellido.substring(0, 1);
        }
        return iniciales.toUpperCase();
    }

}//Fin public class
