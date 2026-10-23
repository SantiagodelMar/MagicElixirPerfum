package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

// Tarjeta blanca con los campos del formulario organizados en columnas
// y los botones abajo a la derecha.
public class TarjetaFormulario extends PanelRedondo {

    private final int columnas;
    private final JPanel campos = new JPanel(new GridBagLayout());
    private final JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    private final EtiquetaEstado chipModo = new EtiquetaEstado("");
    private final JLabel lblNota = Estilo.texto("", 12.5f, Estilo.GRIS);

    // Posicion donde va el siguiente campo
    private int columnaActual = 0;
    private int filaActual = 0;

    public TarjetaFormulario(String titulo, int columnas) {
        super(new BorderLayout(0, 14), 16);
        this.columnas = columnas;
        setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        // Encabezado: titulo a la izquierda y el modo (nuevo / editando) a la derecha
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);
        JLabel lblTitulo = Estilo.micro(titulo);
        lblTitulo.setForeground(Estilo.TINTA);
        encabezado.add(lblTitulo, BorderLayout.WEST);
        encabezado.add(chipModo, BorderLayout.EAST);

        campos.setOpaque(false);
        agregarRellenoDeColumnas();

        // Pie: una nota a la izquierda y los botones a la derecha
        JPanel pie = new JPanel(new BorderLayout());
        pie.setOpaque(false);
        botones.setOpaque(false);
        // La nota va en el centro: si no cabe se corta con "..." y no se monta sobre los botones
        lblNota.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 16));
        pie.add(lblNota, BorderLayout.CENTER);
        pie.add(botones, BorderLayout.EAST);

        add(encabezado, BorderLayout.NORTH);
        add(campos, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);

        setModo(false, "Nuevo registro");
    }

    public void agregarCampo(String etiqueta, JComponent campo) {
        agregarCampo(etiqueta, campo, 1);
    }

    // ancho = cuantas columnas ocupa el campo (ej: la descripcion puede ocupar 2)
    public void agregarCampo(String etiqueta, JComponent campo, int ancho) {
        if (columnaActual + ancho > columnas) {
            columnaActual = 0;
            filaActual++;
        }

        JPanel celda = new JPanel(new BorderLayout(0, 6));
        celda.setOpaque(false);
        celda.add(Estilo.micro(etiqueta), BorderLayout.NORTH);
        celda.add(campo, BorderLayout.CENTER);
        // Ancho pequeno a proposito: asi todas las columnas quedan del mismo tamano
        celda.setPreferredSize(new Dimension(40, 60));

        GridBagConstraints posicion = new GridBagConstraints();
        posicion.gridx = columnaActual;
        posicion.gridy = filaActual;
        posicion.gridwidth = ancho;
        posicion.weightx = ancho;
        posicion.fill = GridBagConstraints.HORIZONTAL;
        boolean ultimaColumna = columnaActual + ancho == columnas;
        posicion.insets = new Insets(0, 0, 12, ultimaColumna ? 0 : 16);
        campos.add(celda, posicion);

        columnaActual += ancho;
    }

    public void agregarBoton(JComponent boton) {
        botones.add(boton);
    }

    // Cambia la etiqueta de arriba a la derecha: "Nuevo registro" o "Editando #3"
    public void setModo(boolean editando, String texto) {
        chipModo.setText(texto);
        if (editando) {
            chipModo.setColores(Estilo.AMBAR, Estilo.AMBAR_FONDO);
        } else {
            chipModo.setColores(Estilo.GRIS, Estilo.LIENZO);
        }
    }

    public void setNota(String texto) {
        lblNota.setText(texto);
    }

    // Una fila invisible al final con una caja por columna.
    // Sirve para que las columnas existan aunque una fila tenga menos campos.
    private void agregarRellenoDeColumnas() {
        for (int i = 0; i < columnas; i++) {
            GridBagConstraints posicion = new GridBagConstraints();
            posicion.gridx = i;
            posicion.gridy = 100;
            posicion.weightx = 1;
            posicion.fill = GridBagConstraints.HORIZONTAL;
            posicion.insets = new Insets(0, 0, 0, i == columnas - 1 ? 0 : 16);
            campos.add(Box.createRigidArea(new Dimension(40, 0)), posicion);
        }
    }

}//Fin public class
