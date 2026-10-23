package gui.componentes;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

// Barra de scroll delgada y redondeada (la de Java por defecto se ve muy vieja)
public class BarraScroll extends BasicScrollBarUI {

    private final Color fondo;
    private final Color colorBarra;

    public BarraScroll(Color fondo, Color colorBarra) {
        this.fondo = fondo;
        this.colorBarra = colorBarra;
    }

    // Le pone la barra delgada a un JScrollPane
    public static void aplicar(JScrollPane scroll, Color fondo, Color colorBarra) {
        scroll.getVerticalScrollBar().setUI(new BarraScroll(fondo, colorBarra));
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        // La lista de los combos no tiene barra horizontal, por eso se pregunta si existe
        if (scroll.getHorizontalScrollBar() != null) {
            scroll.getHorizontalScrollBar().setUI(new BarraScroll(fondo, colorBarra));
            scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        }
        scroll.getVerticalScrollBar().setBackground(fondo);
        scroll.getViewport().setBackground(fondo);
        // Borde vacio (si se deja en null, JTable le pone uno gris por defecto)
        scroll.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected JButton createDecreaseButton(int orientacion) {
        return botonInvisible();
    }

    @Override
    protected JButton createIncreaseButton(int orientacion) {
        return botonInvisible();
    }

    // Sin flechas arriba y abajo
    private JButton botonInvisible() {
        JButton boton = new JButton();
        boton.setPreferredSize(new Dimension(0, 0));
        boton.setMinimumSize(new Dimension(0, 0));
        boton.setMaximumSize(new Dimension(0, 0));
        return boton;
    }

    // Que la barra no quede diminuta cuando hay muchos registros
    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(10, 40);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle zona) {
        g.setColor(fondo);
        g.fillRect(zona.x, zona.y, zona.width, zona.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle zona) {
        if (zona.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(colorBarra);
        g2.fillRoundRect(zona.x + 2, zona.y + 2, zona.width - 4, zona.height - 4, 6, 6);
        g2.dispose();
    }

}//Fin public class
