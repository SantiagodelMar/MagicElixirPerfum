package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// "Pastilla" de color para mostrar estados: Activo, Agotado, En camino, etc.
// El color sale solo segun el texto (ver Estilo.coloresEstado)
public class EtiquetaEstado extends JLabel {

    private Color fondo;

    public EtiquetaEstado(String estado) {
        setFont(Estilo.sansBold(11.5f));
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        setOpaque(false);
        setEstado(estado);
    }

    public void setEstado(String estado) {
        Color[] colores = Estilo.coloresEstado(estado);
        setText(estado);
        setColores(colores[0], colores[1]);
    }

    public void setColores(Color texto, Color fondo) {
        this.fondo = fondo;
        setForeground(texto);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
        g2.dispose();
        super.paintComponent(g);
    }

}//Fin public class
