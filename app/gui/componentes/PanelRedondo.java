package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

// Caja blanca con esquinas redondeadas. Es la "tarjeta" donde van los formularios y las tablas.
public class PanelRedondo extends JPanel {

    private final int radio;
    private Color borde = Estilo.LINEA;

    public PanelRedondo(LayoutManager distribucion, int radio) {
        super(distribucion);
        this.radio = radio;
        setOpaque(false);
        setBackground(Estilo.BLANCO);
    }

    public void setBorde(Color color) {
        this.borde = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        if (borde != null) {
            g2.setColor(borde);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
        }
        g2.dispose();
        super.paintComponent(g);
    }

}//Fin public class
