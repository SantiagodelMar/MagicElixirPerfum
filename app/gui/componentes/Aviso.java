package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Mensaje flotante abajo a la derecha que se esconde solo despues de unos segundos.
// Verde = salio bien, rojo = hay que revisar algo.
public class Aviso extends JComponent {

    private static final Color PUNTO_EXITO = new Color(0x8F, 0xC9, 0x9B);
    private static final Color PUNTO_ERROR = new Color(0xE3, 0x8B, 0x80);

    private String mensaje = "";
    private boolean exito = true;
    private final Timer temporizador;

    public Aviso() {
        setFont(Estilo.sans(13.5f));
        setVisible(false);

        temporizador = new Timer(3500, e -> setVisible(false));
        temporizador.setRepeats(false);

        // Si le dan clic se cierra de una vez
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
            }
        });
    }

    // anchoVentana y altoVentana son del panel donde se dibuja el aviso
    public void mostrar(String mensaje, boolean exito, int anchoVentana, int altoVentana) {
        this.mensaje = mensaje;
        this.exito = exito;

        FontMetrics medidas = getFontMetrics(getFont());
        int ancho = Math.min(medidas.stringWidth(mensaje) + 66, anchoVentana - 60);
        int alto = 48;
        setBounds(anchoVentana - ancho - 28, altoVentana - alto - 28, ancho, alto);

        setVisible(true);
        repaint();
        temporizador.restart();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setColor(Estilo.TINTA);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

        g2.setColor(exito ? PUNTO_EXITO : PUNTO_ERROR);
        g2.fillOval(20, getHeight() / 2 - 4, 8, 8);

        g2.setFont(getFont());
        g2.setColor(Estilo.CREMA);
        FontMetrics medidas = g2.getFontMetrics();
        int y = (getHeight() - medidas.getHeight()) / 2 + medidas.getAscent();
        g2.drawString(mensaje, 40, y);
        g2.dispose();
    }

}//Fin public class
