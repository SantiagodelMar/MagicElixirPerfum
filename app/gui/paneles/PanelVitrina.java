package gui.paneles;

import domain.Categoria;
import domain.Inventario;
import domain.Perfume;
import gui.VentanaPrincipal;
import gui.componentes.BarraScroll;
import gui.componentes.BotonFiltro;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;
import gui.componentes.DialogoPerfume;
import gui.componentes.PanelRedondo;
import gui.componentes.TarjetaPerfume;
import gui.estilo.Estilo;
import service.inputport.CategoriaServiceInterface;
import service.inputport.InventarioServiceInterface;
import service.inputport.PerfumeServiceInterface;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Vitrina: el catalogo real de la tienda con la foto de cada perfume.
// Se puede filtrar por categoria, buscar por nombre, marca o nota y ordenar por precio.
// Al hacer clic en una tarjeta se abre la ficha del perfume (DialogoPerfume).
public class PanelVitrina extends PanelModulo {

    private static final int SEPARACION = 16;

    private static final String ORDEN_CATALOGO = "Orden del catálogo";
    private static final String ORDEN_MENOR_PRECIO = "Precio: menor a mayor";
    private static final String ORDEN_MAYOR_PRECIO = "Precio: mayor a menor";
    private static final String ORDEN_NOMBRE = "Nombre (A - Z)";

    private final PerfumeServiceInterface perfumeService;
    private final CategoriaServiceInterface categoriaService;
    private final InventarioServiceInterface inventarioService;

    // Filtros de arriba
    private final JPanel filaCategorias = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private final List<BotonFiltro> botonesCategoria = new ArrayList<>();
    private final CampoTexto txtBuscar = new CampoTexto("Buscar perfume, marca o nota...");
    private final Combo<String> cbOrden = new Combo<>();
    private final JLabel lblConteo = Estilo.texto("", 13f, Estilo.GRIS);

    // Donde van las tarjetas
    private final JPanel rejilla = new JPanel(new GridLayout(0, 4, SEPARACION, SEPARACION));
    private final JLabel lblSinResultados = Estilo.texto("No hay perfumes que coincidan con la búsqueda.", 14f, Estilo.GRIS);
    private final JScrollPane scroll;

    // Datos que se cargan cada vez que se abre la pantalla
    private List<Perfume> perfumes = new ArrayList<>();
    private final Map<Integer, Inventario> inventarioPorPerfume = new HashMap<>();
    private final Map<Integer, TarjetaPerfume> tarjetas = new HashMap<>();
    private int idCategoriaFiltro = 0;   // 0 = todas las categorias
    private int columnas = 4;

    public PanelVitrina(VentanaPrincipal ventana, PerfumeServiceInterface perfumeService,
                        CategoriaServiceInterface categoriaService, InventarioServiceInterface inventarioService) {
        super(ventana, "Catálogo", "Vitrina",
                "El catálogo real de Magic Elixir: haz clic en un perfume para ver su ficha completa.");
        this.perfumeService = perfumeService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;

        // Rejilla de tarjetas dentro de un scroll. La rejilla va en NORTH para que
        // las tarjetas no se estiren hacia abajo cuando hay pocos resultados.
        rejilla.setOpaque(false);
        lblSinResultados.setHorizontalAlignment(SwingConstants.CENTER);
        lblSinResultados.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
        lblSinResultados.setVisible(false);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(Estilo.LIENZO);
        contenedor.setBorder(BorderFactory.createEmptyBorder(2, 0, 16, 6));
        contenedor.add(rejilla, BorderLayout.NORTH);
        contenedor.add(lblSinResultados, BorderLayout.CENTER);

        scroll = new JScrollPane(contenedor);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(28);
        scroll.getViewport().setBackground(Estilo.LIENZO);
        BarraScroll.aplicar(scroll, Estilo.LIENZO, new Color(0xCF, 0xCB, 0xC4));

        // Cuando cambia el tamaño de la ventana se recalcula cuantas tarjetas caben por fila
        scroll.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                actualizarColumnas();
            }
        });

        JPanel cuerpo = new JPanel(new BorderLayout(0, 18));
        cuerpo.setOpaque(false);
        cuerpo.add(crearBarraFiltros(), BorderLayout.NORTH);
        cuerpo.add(scroll, BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);
    }

    // Barra blanca con las categorias arriba y el buscador y el orden abajo
    private JPanel crearBarraFiltros() {
        PanelRedondo barra = new PanelRedondo(new BorderLayout(0, 14), 16);
        barra.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        filaCategorias.setOpaque(false);

        txtBuscar.setPreferredSize(new Dimension(260, 36));
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                mostrarTarjetas();
            }
        });

        cbOrden.cargar(Arrays.asList(ORDEN_CATALOGO, ORDEN_MENOR_PRECIO, ORDEN_MAYOR_PRECIO, ORDEN_NOMBRE));
        cbOrden.setPreferredSize(new Dimension(200, 36));
        cbOrden.addActionListener(e -> mostrarTarjetas());

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        derecha.setOpaque(false);
        derecha.add(txtBuscar);
        derecha.add(cbOrden);

        JPanel filaBuscar = new JPanel(new BorderLayout());
        filaBuscar.setOpaque(false);
        filaBuscar.add(lblConteo, BorderLayout.WEST);
        filaBuscar.add(derecha, BorderLayout.EAST);

        barra.add(filaCategorias, BorderLayout.NORTH);
        barra.add(filaBuscar, BorderLayout.SOUTH);
        return barra;
    }

    @Override
    public void refrescar() {
        cargarDatos();
        cargarCategorias();
        mostrarTarjetas();
    }

    // Se traen los perfumes y el inventario una sola vez (no una consulta por cada tarjeta)
    private void cargarDatos() {
        perfumes = perfumeService.selectAllPerfumes();

        inventarioPorPerfume.clear();
        for (Inventario inventario : inventarioService.selectAllInventarios()) {
            if (inventario.getPerfume() != null) {
                inventarioPorPerfume.put(inventario.getPerfume().getIdPerfume(), inventario);
            }
        }

        // Las tarjetas se crean de nuevo por si cambio un precio o el stock
        tarjetas.clear();
        for (Perfume perfume : perfumes) {
            TarjetaPerfume tarjeta = new TarjetaPerfume(perfume, inventarioPorPerfume.get(perfume.getIdPerfume()));
            tarjeta.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    verFicha(tarjeta.getPerfume());
                }
            });
            tarjetas.put(perfume.getIdPerfume(), tarjeta);
        }
    }

    // Un boton por categoria (+ "Todas"). Se arman otra vez por si crearon una categoria nueva.
    private void cargarCategorias() {
        filaCategorias.removeAll();
        botonesCategoria.clear();

        agregarBotonCategoria("Todas", 0);
        for (Categoria categoria : categoriaService.selectAllCategorias()) {
            agregarBotonCategoria(categoria.getNombre(), categoria.getIdCategoria());
        }

        // Si la categoria que estaba escogida ya no existe se vuelve a "Todas"
        boolean existe = false;
        for (BotonFiltro boton : botonesCategoria) {
            if (boton.isSeleccionado()) {
                existe = true;
            }
        }
        if (!existe) {
            idCategoriaFiltro = 0;
            botonesCategoria.get(0).setSeleccionado(true);
        }

        filaCategorias.revalidate();
        filaCategorias.repaint();
    }

    private void agregarBotonCategoria(String texto, int idCategoria) {
        BotonFiltro boton = new BotonFiltro(texto);
        boton.setSeleccionado(idCategoria == idCategoriaFiltro);
        boton.addActionListener(e -> {
            idCategoriaFiltro = idCategoria;
            for (BotonFiltro otro : botonesCategoria) {
                otro.setSeleccionado(otro == boton);
            }
            mostrarTarjetas();
        });
        botonesCategoria.add(boton);
        filaCategorias.add(boton);
    }

    // Aplica la categoria, la busqueda y el orden, y pone las tarjetas en la rejilla
    private void mostrarTarjetas() {
        // Se puede buscar con varias palabras (ej: "lattafa vainilla") y sin tildes
        String[] palabras = normalizar(txtBuscar.getTexto()).split(" ");

        List<Perfume> visibles = new ArrayList<>();
        for (Perfume perfume : perfumes) {
            boolean esDeLaCategoria = idCategoriaFiltro == 0
                    || perfume.getCategoria().getIdCategoria() == idCategoriaFiltro;

            String textoPerfume = normalizar(perfume.getNombre() + " " + perfume.getMarca() + " "
                    + perfume.getNotasOlfativas());
            boolean coincide = true;
            for (String palabra : palabras) {
                if (!textoPerfume.contains(palabra)) {
                    coincide = false;
                }
            }

            if (esDeLaCategoria && coincide) {
                visibles.add(perfume);
            }
        }

        ordenar(visibles);

        rejilla.removeAll();
        for (Perfume perfume : visibles) {
            rejilla.add(tarjetas.get(perfume.getIdPerfume()));
        }
        lblSinResultados.setVisible(visibles.isEmpty());
        lblConteo.setText(visibles.size() == 1 ? "1 perfume" : visibles.size() + " perfumes");

        rejilla.revalidate();
        rejilla.repaint();
        // Cada vez que se filtra se vuelve al inicio de la lista
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
    }

    // Minuscula y sin tildes, para que "cafe" encuentre "Café"
    private String normalizar(String texto) {
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase().trim();
    }

    private void ordenar(List<Perfume> lista) {
        String orden = cbOrden.getSeleccionado();
        if (ORDEN_MENOR_PRECIO.equals(orden)) {
            lista.sort((a, b) -> Double.compare(a.getPrecio(), b.getPrecio()));
        } else if (ORDEN_MAYOR_PRECIO.equals(orden)) {
            lista.sort((a, b) -> Double.compare(b.getPrecio(), a.getPrecio()));
        } else if (ORDEN_NOMBRE.equals(orden)) {
            lista.sort((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()));
        }
        // ORDEN_CATALOGO: se dejan como vienen (por ID)
    }

    // Cuantas tarjetas caben a lo ancho segun el espacio que hay
    private void actualizarColumnas() {
        int ancho = scroll.getViewport().getWidth() - 6;
        int nuevas = Math.max(1, (ancho + SEPARACION) / (TarjetaPerfume.ANCHO + SEPARACION));
        if (nuevas != columnas) {
            columnas = nuevas;
            rejilla.setLayout(new GridLayout(0, columnas, SEPARACION, SEPARACION));
            rejilla.revalidate();
        }
    }

    private void verFicha(Perfume perfume) {
        boolean editar = DialogoPerfume.mostrar(this, perfume, inventarioPorPerfume.get(perfume.getIdPerfume()));
        if (editar) {
            ventana.editarPerfume(perfume.getIdPerfume());
        }
    }

}//Fin public class
