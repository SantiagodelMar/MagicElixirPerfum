package api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Lectura y escritura de JSON a mano, para no depender de librerias externas
// (el proyecto no usa Maven ni Gradle)
public final class Json {

    private Json() {
    }

    // ---- Escribir ----

    public static String texto(String valor) {
        if (valor == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("\"");
        for (char c : valor.toCharArray()) {
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.append('"').toString();
    }

    public static String numero(double valor) {
        if (valor == Math.rint(valor) && !Double.isInfinite(valor)) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }

    public static String lista(List<String> elementos) {
        return "[" + String.join(",", elementos) + "]";
    }

    // Arma un objeto JSON campo por campo: new Json.Objeto().campo("id", 1).campo("nombre", "Asad")
    public static class Objeto {

        private final StringBuilder campos = new StringBuilder();

        public Objeto campo(String nombre, String valor) {
            return crudo(nombre, texto(valor));
        }

        public Objeto campo(String nombre, double valor) {
            return crudo(nombre, numero(valor));
        }

        // Para meter un objeto o lista que ya esta en JSON
        public Objeto crudo(String nombre, String json) {
            if (campos.length() > 0) {
                campos.append(',');
            }
            campos.append(texto(nombre)).append(':').append(json);
            return this;
        }

        @Override
        public String toString() {
            return "{" + campos + "}";
        }
    }

    // ---- Leer ----

    // Devuelve Map (objeto), List (arreglo), String, Double, Boolean o null
    public static Object leer(String json) {
        Lector lector = new Lector(json);
        Object valor = lector.valor();
        lector.saltarEspacios();
        if (lector.pos != json.length()) {
            throw new IllegalArgumentException("JSON invalido");
        }
        return valor;
    }

    private static class Lector {

        private final String json;
        private int pos;

        Lector(String json) {
            this.json = json;
        }

        Object valor() {
            saltarEspacios();
            if (pos >= json.length()) {
                throw new IllegalArgumentException("JSON incompleto");
            }
            char c = json.charAt(pos);
            if (c == '{') {
                return objeto();
            } else if (c == '[') {
                return arreglo();
            } else if (c == '"') {
                return cadena();
            } else if (json.startsWith("true", pos)) {
                pos += 4;
                return Boolean.TRUE;
            } else if (json.startsWith("false", pos)) {
                pos += 5;
                return Boolean.FALSE;
            } else if (json.startsWith("null", pos)) {
                pos += 4;
                return null;
            }
            return numero();
        }

        Map<String, Object> objeto() {
            Map<String, Object> mapa = new LinkedHashMap<>();
            pos++;
            saltarEspacios();
            if (json.charAt(pos) == '}') {
                pos++;
                return mapa;
            }
            while (true) {
                saltarEspacios();
                String clave = cadena();
                saltarEspacios();
                esperar(':');
                mapa.put(clave, valor());
                saltarEspacios();
                if (json.charAt(pos) == ',') {
                    pos++;
                } else {
                    esperar('}');
                    return mapa;
                }
            }
        }

        List<Object> arreglo() {
            List<Object> lista = new ArrayList<>();
            pos++;
            saltarEspacios();
            if (json.charAt(pos) == ']') {
                pos++;
                return lista;
            }
            while (true) {
                lista.add(valor());
                saltarEspacios();
                if (json.charAt(pos) == ',') {
                    pos++;
                } else {
                    esperar(']');
                    return lista;
                }
            }
        }

        String cadena() {
            esperar('"');
            StringBuilder sb = new StringBuilder();
            while (json.charAt(pos) != '"') {
                char c = json.charAt(pos++);
                if (c == '\\') {
                    char escape = json.charAt(pos++);
                    switch (escape) {
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(json.substring(pos, pos + 4), 16));
                            pos += 4;
                            break;
                        default:
                            sb.append(escape);
                    }
                } else {
                    sb.append(c);
                }
            }
            pos++;
            return sb.toString();
        }

        Double numero() {
            int inicio = pos;
            while (pos < json.length() && "+-0123456789.eE".indexOf(json.charAt(pos)) >= 0) {
                pos++;
            }
            if (inicio == pos) {
                throw new IllegalArgumentException("JSON invalido");
            }
            return Double.parseDouble(json.substring(inicio, pos));
        }

        void esperar(char caracter) {
            if (pos >= json.length() || json.charAt(pos) != caracter) {
                throw new IllegalArgumentException("JSON invalido: se esperaba " + caracter);
            }
            pos++;
        }

        void saltarEspacios() {
            while (pos < json.length() && Character.isWhitespace(json.charAt(pos))) {
                pos++;
            }
        }
    }

}
