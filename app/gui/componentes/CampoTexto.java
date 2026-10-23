package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.JTextField;
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

// Campo de texto con borde redondeado y un texto de ayuda (placeholder) en gris
// que se ve mientras el campo esta vacio.
public class CampoTexto extends JTextField {

    private final String placeholder;
    private final BordeRedondo borde = new BordeRedondo(Estilo.LINEA, 10, 8, 12, 8, 12);
    private boolean conError;

    public CampoTexto(String placeholder) {
        this.placeholder = placeholder;

        setFont(Estilo.sans(13.5f));
        setForeground(Estilo.TINTA);
        setBackground(Estilo.BLANCO);
        setCaretColor(Estilo.TINTA);
        setSelectionColor(Estilo.SELECCION);
        setSelectedTextColor(Estilo.TINTA);
        setBorder(borde);
        setOpaque(false);
        setPreferredSize(new Dimension(120, 38));

        // Con el foco el borde se pone negro
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

        // Apenas el usuario escribe se quita el borde rojo de error
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                limpiarError();
            }
        });
    }

    public String getTexto() {
        return getText().trim();
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

    // Bloqueado = se puede leer pero no editar (se usa en modo edicion para los datos que no cambian)
    public void setBloqueado(boolean bloqueado) {
        setEditable(!bloqueado);
        setFocusable(!bloqueado);
        setBackground(bloqueado ? Estilo.LIENZO : Estilo.BLANCO);
        setForeground(bloqueado ? Estilo.GRIS : Estilo.TINTA);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fondo redondeado (el campo no es opaco para que no se vean las esquinas)
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        // Texto de ayuda cuando esta vacio
        if (getText().isEmpty() && placeholder != null && isEditable()) {
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
