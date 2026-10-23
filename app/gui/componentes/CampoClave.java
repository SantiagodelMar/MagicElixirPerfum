package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.JPasswordField;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Igual que CampoTexto pero para contraseñas (muestra puntos en vez de letras)
public class CampoClave extends JPasswordField {

    private final String placeholder;
    private final BordeRedondo borde = new BordeRedondo(Estilo.LINEA, 10, 8, 12, 8, 12);
    private boolean conError;

    public CampoClave(String placeholder) {
        this.placeholder = placeholder;

        setFont(Estilo.sans(13.5f));
        setForeground(Estilo.TINTA);
        setBackground(Estilo.BLANCO);
        setCaretColor(Estilo.TINTA);
        setSelectionColor(Estilo.SELECCION);
        setBorder(borde);
        setOpaque(false);
        setPreferredSize(new Dimension(120, 38));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (!conError) {
                    borde.setColor(Estilo.TINTA);
                    repaint();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (!conError) {
                    borde.setColor(Estilo.LINEA);
                    repaint();
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                limpiarError();
            }
        });
    }

    // getPassword() devuelve un arreglo de char, lo pasamos a String
    public String getClave() {
        return new String(getPassword());
    }

    public void limpiar() {
        setText("");
        limpiarError();
    }

    public void marcarError() {
        conError = true;
        borde.setColor(Estilo.ROJO);
        repaint();
    }

    public void limpiarError() {
        conError = false;
        borde.setColor(isFocusOwner() ? Estilo.TINTA : Estilo.LINEA);
        repaint();
    }

    public void setBloqueado(boolean bloqueado) {
        setEditable(!bloqueado);
        setFocusable(!bloqueado);
        setBackground(bloqueado ? Estilo.LIENZO : Estilo.BLANCO);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        if (getPassword().length == 0 && placeholder != null && isEditable()) {
            Insets margen = getInsets();
            FontMetrics medidas = g2.getFontMetrics(getFont());
            int y = margen.top + (getHeight() - margen.top - margen.bottom - medidas.getHeight()) / 2 + medidas.getAscent();
            g2.setFont(getFont());
            g2.setColor(Estilo.GRIS_CLARO);
            g2.drawString(placeholder, margen.left, y);
        }
        g2.dispose();

        super.paintComponent(g);
    }

}//Fin public class
