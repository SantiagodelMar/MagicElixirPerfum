package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

// Grafico de barras horizontales sencillo (se usa en el inicio: perfumes por categoria)
public class GraficoBarras extends JComponent {

    private static final int ALTO_FILA = 48;

    private final List<String> nombres = new ArrayList<>();
    private final List<Integer> valores = new ArrayList<>();

    public void limpiar() {
        nombres.clear();
        valores.clear();
    }

    public void agregar(String nombre, int valor) {
        nombres.add(nombre);
        valores.add(valor);
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, nombres.size() * ALTO_FILA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // El valor mas alto ocupa toda la barra
        int maximo = 1;
        for (int valor : valores) {
            maximo = Math.max(maximo, valor);
        }

        int anchoBarra = getWidth() - 44;

        for (int i = 0; i < nombres.size(); i++) {
            int y = i * ALTO_FILA;

            g2.setFont(Estilo.sans(13f));
            g2.setColor(Estilo.TINTA);
            g2.drawString(nombres.get(i), 0, y + 16);

            // Fondo de la barra
            g2.setColor(Estilo.LIENZO);
            g2.fillRoundRect(0, y + 26, anchoBarra, 10, 10, 10);

            // Barra llena segun el valor
            int largo = (int) ((double) valores.get(i) / maximo * anchoBarra);
            g2.setColor(Estilo.TINTA);
            g2.fillRoundRect(0, y + 26, Math.max(largo, 10), 10, 10, 10);

            // Numero al final
            g2.setFont(Estilo.sansBold(13f));
            FontMetrics medidas = g2.getFontMetrics();
            String texto = String.valueOf(valores.get(i));
            g2.drawString(texto, getWidth() - medidas.stringWidth(texto), y + 36);
        }
        g2.dispose();
    }

}//Fin public class
