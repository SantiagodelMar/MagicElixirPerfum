package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Boton plano de la marca.
// PRINCIPAL = negro (la accion mas importante), SECUNDARIO = blanco con borde,
// PELIGRO = letra roja (para eliminar).
public class Boton extends JButton {

    public static final int PRINCIPAL = 1;
    public static final int SECUNDARIO = 2;
    public static final int PELIGRO = 3;

    private final int tipo;
    private boolean encima;

    public Boton(String texto, int tipo) {
        super(texto);
        this.tipo = tipo;

        setFont(Estilo.espaciada(Estilo.sansBold(11f), 0.1f));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(11, 18, 11, 18));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (tipo == PRINCIPAL) {
            setForeground(Estilo.CREMA);
        } else if (tipo == PELIGRO) {
            setForeground(Estilo.ROJO);
        } else {
            setForeground(Estilo.TINTA);
        }

        // Efecto al pasar el mouse por encima
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

    // Los botones siempre se ven en mayuscula
    @Override
    public void setText(String texto) {
        super.setText(texto == null ? null : texto.toUpperCase());
    }

    @Override
    public void setEnabled(boolean habilitado) {
        super.setEnabled(habilitado);
        setCursor(new Cursor(habilitado ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fondo;
        Color borde = null;

        if (!isEnabled()) {
            fondo = Estilo.LIENZO;
            borde = Estilo.LINEA;
        } else if (tipo == PRINCIPAL) {
            fondo = encima ? Estilo.TINTA_SUAVE : Estilo.TINTA;
        } else if (tipo == PELIGRO) {
            fondo = encima ? Estilo.ROJO_FONDO : Estilo.BLANCO;
            borde = encima ? Estilo.ROJO_FONDO : Estilo.LINEA;
        } else {
            fondo = encima ? Estilo.LIENZO : Estilo.BLANCO;
            borde = Estilo.LINEA;
        }

        g2.setColor(fondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        if (borde != null) {
            g2.setColor(borde);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        }
        g2.dispose();

        super.paintComponent(g);
    }

}//Fin public class
