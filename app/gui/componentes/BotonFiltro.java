package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Boton en forma de pastilla para filtrar (ej: las categorias de la vitrina).
// El que esta escogido se ve negro y los demas blancos con borde.
public class BotonFiltro extends JButton {

    private boolean seleccionado;
    private boolean encima;

    public BotonFiltro(String texto) {
        super(texto);
        setFont(Estilo.sansBold(12.5f));
        setForeground(Estilo.TINTA);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                encima = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encima = false;
                repaint();
            }
        });
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        setForeground(seleccionado ? Estilo.CREMA : Estilo.TINTA);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int alto = getHeight();
        if (seleccionado) {
            g2.setColor(Estilo.TINTA);
            g2.fillRoundRect(0, 0, getWidth(), alto, alto, alto);
        } else {
            g2.setColor(encima ? Estilo.LIENZO : Estilo.BLANCO);
            g2.fillRoundRect(0, 0, getWidth(), alto, alto, alto);
            g2.setColor(encima ? Estilo.GRIS_CLARO : Estilo.LINEA);
            g2.drawRoundRect(0, 0, getWidth() - 1, alto - 1, alto, alto);
        }
        g2.dispose();

        super.paintComponent(g);
    }

}//Fin public class
