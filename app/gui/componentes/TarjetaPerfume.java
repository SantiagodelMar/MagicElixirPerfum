package gui.componentes;

import domain.Inventario;
import domain.Perfume;
import gui.estilo.Estilo;
import gui.estilo.Formato;
import gui.estilo.Recursos;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Tarjeta de la vitrina: foto, categoria, nombre, marca, precio y cuantas unidades quedan.
// La pantalla de la vitrina le agrega el clic para abrir la ficha del perfume.
public class TarjetaPerfume extends PanelRedondo {

    public static final int ANCHO = 206;
    public static final int ALTO = 390;

    private static final int ANCHO_FOTO = 176;
    private static final int ALTO_FOTO = 220;

    private final Perfume perfume;

    public TarjetaPerfume(Perfume perfume, Inventario inventario) {
        super(new BorderLayout(0, 12), 18);
        this.perfume = perfume;

        setBorder(BorderFactory.createEmptyBorder(10, 10, 14, 10));
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(crearFoto(), BorderLayout.NORTH);
        add(crearDatos(), BorderLayout.CENTER);
        add(crearPie(inventario), BorderLayout.SOUTH);

        // Al pasar el mouse el borde se pone negro para que se note que se puede abrir
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorde(Estilo.TINTA);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorde(Estilo.LINEA);
            }
        });
    }

    public Perfume getPerfume() {
        return perfume;
    }

    // La foto va sobre un fondo crema con las esquinas redondeadas
    private JPanel crearFoto() {
        PanelRedondo marco = new PanelRedondo(new BorderLayout(), 14);
        marco.setBackground(Estilo.LIENZO);
        marco.setBorde(null);
        marco.setPreferredSize(new Dimension(ANCHO_FOTO, ALTO_FOTO + 8));

        JLabel lblFoto = new JLabel(Recursos.fotoPerfume(perfume.getMarca(), perfume.getNombre(), ANCHO_FOTO, ALTO_FOTO));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        marco.add(lblFoto, BorderLayout.CENTER);
        return marco;
    }

    private JPanel crearDatos() {
        JPanel datos = new JPanel();
        datos.setLayout(new BoxLayout(datos, BoxLayout.Y_AXIS));
        datos.setOpaque(false);
        datos.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));

        JLabel lblCategoria = Estilo.micro(perfume.getCategoria().getNombre());

        // Si el nombre es largo baja a una segunda linea
        JPanel panelNombre = Estilo.textoEnLineas(perfume.getNombre(), Estilo.serifBold(15.5f), Estilo.TINTA, 168, 2);

        JLabel lblMarca = Estilo.texto(perfume.getMarca() + "  ·  " + perfume.getMililitros() + " ml", 12.5f, Estilo.GRIS);

        lblCategoria.setAlignmentX(LEFT_ALIGNMENT);
        panelNombre.setAlignmentX(LEFT_ALIGNMENT);
        lblMarca.setAlignmentX(LEFT_ALIGNMENT);

        datos.add(lblCategoria);
        datos.add(Box.createVerticalStrut(5));
        datos.add(panelNombre);
        datos.add(Box.createVerticalStrut(3));
        datos.add(lblMarca);
        return datos;
    }

    // Abajo: el precio y una pastilla con las unidades disponibles
    private JPanel crearPie(Inventario inventario) {
        JPanel pie = new JPanel(new BorderLayout(8, 0));
        pie.setOpaque(false);
        pie.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 2));

        JLabel lblPrecio = Estilo.titulo(Formato.dinero(perfume.getPrecio()), 17f);

        pie.add(lblPrecio, BorderLayout.WEST);
        pie.add(crearEtiquetaStock(inventario), BorderLayout.EAST);
        return pie;
    }

    // Disponible -> "8 und." en verde, Bajo stock -> amarillo, Agotado -> rojo
    public static EtiquetaEstado crearEtiquetaStock(Inventario inventario) {
        if (inventario == null || inventario.getEstadoDisponibilidad() == null) {
            EtiquetaEstado sinDatos = new EtiquetaEstado("Sin inventario");
            return sinDatos;
        }

        String estado = inventario.getEstadoDisponibilidad().getDescripcion();
        EtiquetaEstado etiqueta = new EtiquetaEstado(estado);
        if (inventario.getStockDisponible() > 0) {
            // se deja el color del estado pero se muestran las unidades
            etiqueta.setText(inventario.getStockDisponible() + " und.");
        }
        return etiqueta;
    }

}//Fin public class
