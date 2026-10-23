package gui.componentes;

import javax.swing.border.AbstractBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

// Borde con esquinas redondeadas para los campos de texto y los combos.
// Se le puede cambiar el color (gris normal, negro con foco, rojo con error).
public class BordeRedondo extends AbstractBorder {

    private Color color;
    private final int radio;
    private final Insets margen;

    public BordeRedondo(Color color, int radio, int arriba, int izquierda, int abajo, int derecha) {
        this.color = color;
        this.radio = radio;
        this.margen = new Insets(arriba, izquierda, abajo, derecha);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int ancho, int alto) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.drawRoundRect(x, y, ancho - 1, alto - 1, radio, radio);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(margen.top, margen.left, margen.bottom, margen.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = margen.top;
        insets.left = margen.left;
        insets.bottom = margen.bottom;
        insets.right = margen.right;
        return insets;
    }

}//Fin public class
