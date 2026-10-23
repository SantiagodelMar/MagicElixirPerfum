package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Circulo con las iniciales del usuario que inicio sesion (ej: "AM")
public class Avatar extends JComponent {

    private final String iniciales;

    public Avatar(String iniciales, int tamano) {
        this.iniciales = iniciales;
        setPreferredSize(new Dimension(tamano, tamano));
        setMinimumSize(new Dimension(tamano, tamano));
        setMaximumSize(new Dimension(tamano, tamano));
        setFont(Estilo.sansBold(tamano * 0.36f));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(Estilo.CREMA);
        g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

        g2.setColor(Estilo.TINTA);
        g2.setFont(getFont());
        FontMetrics medidas = g2.getFontMetrics();
        int x = (getWidth() - medidas.stringWidth(iniciales)) / 2;
        int y = (getHeight() - medidas.getHeight()) / 2 + medidas.getAscent();
        g2.drawString(iniciales, x, y);
        g2.dispose();
    }

}//Fin public class
