package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Opcion del menu lateral negro. Guarda el nombre de la vista que abre.
public class BotonMenu extends JButton {

    private final String vista;
    private boolean activo;
    private boolean encima;

    public BotonMenu(String texto, String vista) {
        super(texto);
        this.vista = vista;

        setFont(Estilo.sans(13.5f));
        setForeground(Estilo.CREMA_TENUE);
        setHorizontalAlignment(LEFT);
        setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 14));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                encima = true;
                if (!activo) {
                    setForeground(Estilo.CREMA);
                }
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                encima = false;
                if (!activo) {
                    setForeground(Estilo.CREMA_TENUE);
                }
                repaint();
            }
        });
    }

    public String getVista() {
        return vista;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        setForeground(activo ? Estilo.CREMA : Estilo.CREMA_TENUE);
        setFont(activo ? Estilo.sansBold(13.5f) : Estilo.sans(13.5f));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (activo) {
            g2.setColor(Estilo.MENU_ACTIVO);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            // Puntico a la derecha para saber en que pantalla estamos
            g2.setColor(Estilo.CREMA);
            g2.fillOval(getWidth() - 20, getHeight() / 2 - 3, 6, 6);
        } else if (encima) {
            g2.setColor(Estilo.MENU_ENCIMA);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        }
        g2.dispose();

        super.paintComponent(g);
    }

}//Fin public class
