package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

// Tarjeta del inicio con un numero grande (ej: "201" perfumes en catalogo)
public class TarjetaDato extends PanelRedondo {

    private final JLabel lblValor = Estilo.titulo("0", 34f);
    private final JLabel lblDetalle = Estilo.texto(" ", 12.5f, Estilo.GRIS);

    public TarjetaDato(String titulo) {
        super(null, 16);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel lblTitulo = Estilo.micro(titulo);
        // En BoxLayout hay que alinear todo a la izquierda, si no se centra
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        lblValor.setAlignmentX(LEFT_ALIGNMENT);
        lblDetalle.setAlignmentX(LEFT_ALIGNMENT);

        add(lblTitulo);
        add(Box.createVerticalStrut(10));
        add(lblValor);
        add(Box.createVerticalStrut(4));
        add(lblDetalle);
    }

    public void setDatos(String valor, String detalle) {
        lblValor.setText(valor);
        lblDetalle.setText(detalle);
    }

}//Fin public class
