package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// Tarjeta blanca con titulo, contador de registros, buscador y la tabla.
public class TarjetaTabla extends PanelRedondo {

    private final Tabla tabla;
    private final JLabel lblConteo = Estilo.texto("", 12.5f, Estilo.GRIS);
    private final CampoTexto txtBuscar;
    private final JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

    public TarjetaTabla(String titulo, String textoBuscar, String[] columnas) {
        super(new BorderLayout(0, 12), 16);
        setBorder(BorderFactory.createEmptyBorder(18, 20, 14, 20));

        tabla = new Tabla(columnas);

        // Encabezado: titulo y conteo a la izquierda, buscador a la derecha
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        // GridBagLayout centra el titulo verticalmente con el buscador
        JPanel izquierda = new JPanel(new GridBagLayout());
        izquierda.setOpaque(false);
        JLabel lblTitulo = Estilo.micro(titulo);
        lblTitulo.setForeground(Estilo.TINTA);
        izquierda.add(lblTitulo);
        izquierda.add(Box.createHorizontalStrut(12));
        izquierda.add(lblConteo);

        txtBuscar = new CampoTexto(textoBuscar);
        txtBuscar.setPreferredSize(new Dimension(240, 36));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                tabla.filtrar(txtBuscar.getTexto());
                actualizarConteo();
            }
        });
        derecha.setOpaque(false);
        derecha.add(txtBuscar);

        encabezado.add(izquierda, BorderLayout.WEST);
        encabezado.add(derecha, BorderLayout.EAST);

        JScrollPane scroll = new JScrollPane(tabla);
        BarraScroll.aplicar(scroll, Estilo.BLANCO, new Color(0xCF, 0xCB, 0xC4));

        add(encabezado, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public Tabla getTabla() {
        return tabla;
    }

    // Para poner un filtro extra al lado del buscador (ej: categoria)
    public void agregarAlEncabezado(JComponent componente) {
        derecha.add(componente, 0);
    }

    // Se llama despues de cargar los datos
    public void actualizarConteo() {
        int cantidad = tabla.getRowCount();
        lblConteo.setText(cantidad == 1 ? "1 registro" : cantidad + " registros");
    }

}//Fin public class
