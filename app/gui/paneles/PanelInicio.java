package gui.paneles;

import domain.Categoria;
import domain.DetallePedido;
import domain.Inventario;
import domain.Pedido;
import domain.Perfume;
import domain.enums.EstadoInventario;
import domain.enums.EstadoPedido;
import gui.VentanaPrincipal;
import gui.componentes.BarraScroll;
import gui.componentes.Boton;
import gui.componentes.DialogoPerfume;
import gui.componentes.EtiquetaEstado;
import gui.componentes.GraficoBarras;
import gui.componentes.PanelRedondo;
import gui.componentes.TarjetaDato;
import gui.estilo.Estilo;
import gui.estilo.Formato;
import gui.estilo.Recursos;
import service.inputport.CategoriaServiceInterface;
import service.inputport.InventarioServiceInterface;
import service.inputport.PedidoServiceInterface;
import service.inputport.PerfumeServiceInterface;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Pantalla de inicio: un resumen rapido de como va la perfumeria.
public class PanelInicio extends PanelModulo {

    private final PerfumeServiceInterface perfumeService;
    private final CategoriaServiceInterface categoriaService;
    private final InventarioServiceInterface inventarioService;
    private final PedidoServiceInterface pedidoService;

    private final TarjetaDato tarjetaPerfumes = new TarjetaDato("Perfumes en catálogo");
    private final TarjetaDato tarjetaUnidades = new TarjetaDato("Unidades en bodega");
    private final TarjetaDato tarjetaPendientes = new TarjetaDato("Pedidos pendientes");
    private final TarjetaDato tarjetaVentas = new TarjetaDato("Total vendido");

    private final GraficoBarras grafico = new GraficoBarras();
    private final JPanel listaAlertas = crearLista();
    private final JPanel listaPedidos = crearLista();
    private final JPanel filaMasVendidos = new JPanel(new GridLayout(1, 5, 12, 0));

    private static final int CANTIDAD_MAS_VENDIDOS = 5;

    public PanelInicio(VentanaPrincipal ventana, PerfumeServiceInterface perfumeService,
                       CategoriaServiceInterface categoriaService, InventarioServiceInterface inventarioService,
                       PedidoServiceInterface pedidoService) {
        super(ventana, "Resumen", "Hola", "");
        this.perfumeService = perfumeService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;
        this.pedidoService = pedidoService;

        // Fila de numeros grandes
        JPanel filaDatos = new JPanel(new GridLayout(1, 4, 16, 0));
        filaDatos.setOpaque(false);
        filaDatos.add(tarjetaPerfumes);
        filaDatos.add(tarjetaUnidades);
        filaDatos.add(tarjetaPendientes);
        filaDatos.add(tarjetaVentas);

        // Fila de tarjetas con detalle
        JPanel filaDetalle = new JPanel(new GridLayout(1, 3, 16, 0));
        filaDetalle.setOpaque(false);
        filaDetalle.add(crearTarjeta("Perfumes por categoría", "Ver todo", "perfumes", grafico));
        filaDetalle.add(crearTarjeta("Alertas de inventario", "Ver todo", "inventario", listaAlertas));
        filaDetalle.add(crearTarjeta("Últimos pedidos", "Ver todo", "pedidos", listaPedidos));

        // Fila con la foto de los perfumes que mas se venden
        filaMasVendidos.setOpaque(false);

        JPanel abajo = new JPanel(new BorderLayout(0, 16));
        abajo.setOpaque(false);
        abajo.add(crearTarjeta("Lo más vendido", "Ver vitrina", "vitrina", filaMasVendidos), BorderLayout.NORTH);
        abajo.add(crearAccesosRapidos(), BorderLayout.SOUTH);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 16));
        cuerpo.setOpaque(false);
        cuerpo.add(filaDatos, BorderLayout.NORTH);
        cuerpo.add(filaDetalle, BorderLayout.CENTER);
        cuerpo.add(abajo, BorderLayout.SOUTH);

        // En NORTH para que las tarjetas no se estiren hasta abajo y queden vacias
        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setBackground(Estilo.LIENZO);
        envoltura.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 6));
        envoltura.add(cuerpo, BorderLayout.NORTH);

        // Con scroll por si la pantalla del computador es pequeña
        JScrollPane scroll = new JScrollPane(envoltura);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(24);
        scroll.getViewport().setBackground(Estilo.LIENZO);
        BarraScroll.aplicar(scroll, Estilo.LIENZO, new Color(0xCF, 0xCB, 0xC4));
        add(scroll, BorderLayout.CENTER);
    }

    @Override
    public void refrescar() {
        String nombre = ventana.getEmpleadoActivo().getNombre();
        setTitulo(saludo() + ", " + nombre);
        setDescripcion(Formato.fechaLarga(LocalDate.now()) + "  ·  Así va Magic Elixir hoy.");

        cargarDatos();
        cargarGrafico();
        cargarAlertas();
        cargarUltimosPedidos();
        cargarMasVendidos();
        revalidate();
        repaint();
    }

    private String saludo() {
        int hora = LocalTime.now().getHour();
        if (hora < 12) {
            return "Buenos días";
        } else if (hora < 19) {
            return "Buenas tardes";
        }
        return "Buenas noches";
    }

    private void cargarDatos() {
        int perfumes = perfumeService.selectAllPerfumes().size();
        int categorias = categoriaService.selectAllCategorias().size();
        tarjetaPerfumes.setDatos(String.valueOf(perfumes), "en " + categorias + " categorías");

        int unidades = 0;
        int conAlerta = 0;
        for (Inventario inventario : inventarioService.selectAllInventarios()) {
            unidades += inventario.getStockDisponible();
            if (inventario.getEstadoDisponibilidad() != EstadoInventario.DISPONIBLE) {
                conAlerta++;
            }
        }
        tarjetaUnidades.setDatos(Formato.numero(unidades), conAlerta + " referencias con stock bajo");

        List<Pedido> pedidos = pedidoService.selectAllPedidos();
        int pendientes = 0;
        int pagados = 0;
        double vendido = 0;
        for (Pedido pedido : pedidos) {
            if (pedido.getEstadoPedido() == EstadoPedido.PENDIENTE) {
                pendientes++;
            }
            // Se cuenta como venta lo que ya se pago (pagado, enviado o entregado)
            if (pedido.getEstadoPedido() == EstadoPedido.PAGADO || pedido.getEstadoPedido() == EstadoPedido.ENVIADO
                    || pedido.getEstadoPedido() == EstadoPedido.ENTREGADO) {
                pagados++;
                vendido += pedido.getTotalCostoPedido();
            }
        }
        tarjetaPendientes.setDatos(String.valueOf(pendientes), "de " + pedidos.size() + " pedidos registrados");
        tarjetaVentas.setDatos(Formato.dinero(vendido), "en " + pagados + " pedidos pagados");
    }

    // HU-06: cuantos perfumes hay en cada categoria
    private void cargarGrafico() {
        grafico.limpiar();
        for (Categoria categoria : categoriaService.selectAllCategorias()) {
            int cantidad = perfumeService.selectPerfumesByCategoria(categoria.getIdCategoria()).size();
            grafico.agregar(categoria.getNombre(), cantidad);
        }
    }

    // HU-07: perfumes con poco stock o agotados
    private void cargarAlertas() {
        listaAlertas.removeAll();

        List<Inventario> alertas = new ArrayList<>();
        for (Inventario inventario : inventarioService.selectAllInventarios()) {
            if (inventario.getEstadoDisponibilidad() != EstadoInventario.DISPONIBLE) {
                alertas.add(inventario);
            }
        }
        // Primero los que tienen menos unidades
        alertas.sort((a, b) -> a.getStockDisponible() - b.getStockDisponible());

        if (alertas.isEmpty()) {
            listaAlertas.add(textoVacio("Todo el inventario está en orden."));
        }

        for (int i = 0; i < alertas.size() && i < 5; i++) {
            Inventario inventario = alertas.get(i);
            EtiquetaEstado etiqueta;
            if (inventario.getStockDisponible() == 0) {
                etiqueta = new EtiquetaEstado("Agotado");
            } else {
                etiqueta = new EtiquetaEstado(inventario.getStockDisponible() + " und.");
                etiqueta.setColores(Estilo.AMBAR, Estilo.AMBAR_FONDO);
            }
            listaAlertas.add(crearFila(inventario.getPerfume().getNombre(), inventario.getPerfume().getMarca(), etiqueta));
        }
    }

    private void cargarUltimosPedidos() {
        listaPedidos.removeAll();
        List<Pedido> pedidos = pedidoService.selectAllPedidos();

        if (pedidos.isEmpty()) {
            listaPedidos.add(textoVacio("Todavía no hay pedidos."));
        }

        // Se recorre desde el final para mostrar los mas nuevos primero
        int mostrados = 0;
        for (int i = pedidos.size() - 1; i >= 0 && mostrados < 5; i--) {
            Pedido pedido = pedidos.get(i);
            String cliente = pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido();
            String detalle = Formato.fecha(pedido.getFechaPedido()) + "  ·  " + Formato.dinero(pedido.getTotalCostoPedido());
            EtiquetaEstado etiqueta = new EtiquetaEstado(Formato.nombreEnum(pedido.getEstadoPedido()));
            listaPedidos.add(crearFila("#" + pedido.getIdPedido() + "  " + cliente, detalle, etiqueta));
            mostrados++;
        }
    }

    // Suma las unidades vendidas de cada perfume (pedidos pagados, enviados o entregados)
    // y muestra los 5 que mas se venden con su foto
    private void cargarMasVendidos() {
        filaMasVendidos.removeAll();

        Map<Integer, Integer> unidadesPorPerfume = new HashMap<>();
        Map<Integer, Perfume> perfumesVendidos = new HashMap<>();
        for (Pedido pedido : pedidoService.selectAllPedidos()) {
            boolean vendido = pedido.getEstadoPedido() == EstadoPedido.PAGADO
                    || pedido.getEstadoPedido() == EstadoPedido.ENVIADO
                    || pedido.getEstadoPedido() == EstadoPedido.ENTREGADO;
            if (!vendido || pedido.getDetalles() == null) {
                continue;
            }
            for (DetallePedido detalle : pedido.getDetalles()) {
                Perfume perfume = detalle.getPerfume();
                if (perfume == null) {
                    continue;
                }
                int id = perfume.getIdPerfume();
                int antes = unidadesPorPerfume.containsKey(id) ? unidadesPorPerfume.get(id) : 0;
                unidadesPorPerfume.put(id, antes + detalle.getCantidad());
                perfumesVendidos.put(id, perfume);
            }
        }

        List<Perfume> ranking = new ArrayList<>(perfumesVendidos.values());
        ranking.sort((a, b) -> unidadesPorPerfume.get(b.getIdPerfume()) - unidadesPorPerfume.get(a.getIdPerfume()));

        if (ranking.isEmpty()) {
            filaMasVendidos.setLayout(new GridLayout(1, 1));
            filaMasVendidos.add(textoVacio("Todavía no hay ventas pagadas."));
            return;
        }

        filaMasVendidos.setLayout(new GridLayout(1, CANTIDAD_MAS_VENDIDOS, 12, 0));
        for (int i = 0; i < ranking.size() && i < CANTIDAD_MAS_VENDIDOS; i++) {
            Perfume perfume = ranking.get(i);
            filaMasVendidos.add(crearMiniatura(perfume, unidadesPorPerfume.get(perfume.getIdPerfume()), i + 1));
        }
    }

    // Cajita con la foto pequeña, el nombre y cuantas unidades se han vendido.
    // Al hacer clic se abre la ficha del perfume.
    private JPanel crearMiniatura(Perfume perfume, int unidades, int puesto) {
        PanelRedondo caja = new PanelRedondo(new BorderLayout(12, 0), 14);
        caja.setBackground(Estilo.LIENZO);
        caja.setBorde(null);
        caja.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 10));
        caja.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblFoto = new JLabel(Recursos.fotoPerfume(perfume.getMarca(), perfume.getNombre(), 60, 75));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel lblPuesto = Estilo.micro("#" + puesto);
        JLabel lblNombre = Estilo.texto(perfume.getNombre(), 13f, Estilo.TINTA);
        lblNombre.setFont(Estilo.sansBold(13f));
        JLabel lblMarca = Estilo.texto(perfume.getMarca(), 12f, Estilo.GRIS);
        JLabel lblUnidades = Estilo.texto(unidades == 1 ? "1 vendido" : unidades + " vendidos", 12f, Estilo.GRIS);
        lblPuesto.setAlignmentX(LEFT_ALIGNMENT);
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);
        lblMarca.setAlignmentX(LEFT_ALIGNMENT);
        lblUnidades.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(Box.createVerticalGlue());
        textos.add(lblPuesto);
        textos.add(Box.createVerticalStrut(3));
        textos.add(lblNombre);
        textos.add(lblMarca);
        textos.add(Box.createVerticalStrut(3));
        textos.add(lblUnidades);
        textos.add(Box.createVerticalGlue());

        caja.add(lblFoto, BorderLayout.WEST);
        caja.add(textos, BorderLayout.CENTER);

        caja.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                caja.setBorde(Estilo.TINTA);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                caja.setBorde(null);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                Inventario inventario = inventarioService.selectInventarioByPerfume(perfume.getIdPerfume());
                if (DialogoPerfume.mostrar(PanelInicio.this, perfume, inventario)) {
                    ventana.editarPerfume(perfume.getIdPerfume());
                }
            }
        });
        return caja;
    }

    //Metodos para armar las tarjetas

    // Botones para ir directo a lo que mas se hace en el dia
    private PanelRedondo crearAccesosRapidos() {
        PanelRedondo tarjeta = new PanelRedondo(new BorderLayout(), 16);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        JLabel lblTitulo = Estilo.micro("Accesos rápidos");
        lblTitulo.setForeground(Estilo.TINTA);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.add(crearAcceso("Ver vitrina", "vitrina"));
        botones.add(crearAcceso("Registrar cliente", "clientes"));
        botones.add(crearAcceso("Nuevo envío", "envios"));
        botones.add(crearAcceso("Registrar devolución", "devoluciones"));
        botones.add(crearAcceso("Nuevo pedido", "pedidos"));

        tarjeta.add(lblTitulo, BorderLayout.WEST);
        tarjeta.add(botones, BorderLayout.EAST);
        return tarjeta;
    }

    private Boton crearAcceso(String texto, String vista) {
        Boton boton = new Boton(texto, vista.equals("pedidos") ? Boton.PRINCIPAL : Boton.SECUNDARIO);
        boton.addActionListener(e -> ventana.mostrar(vista));
        return boton;
    }

    private PanelRedondo crearTarjeta(String titulo, String textoEnlace, String vista, JComponent contenido) {
        PanelRedondo tarjeta = new PanelRedondo(new BorderLayout(0, 16), 16);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        // El titulo va en CENTER: si la ventana es angosta se corta con "..." y no se monta sobre el enlace
        JPanel encabezado = new JPanel(new BorderLayout(12, 0));
        encabezado.setOpaque(false);
        JLabel lblTitulo = Estilo.micro(titulo);
        lblTitulo.setForeground(Estilo.TINTA);
        encabezado.add(lblTitulo, BorderLayout.CENTER);
        encabezado.add(crearEnlace(textoEnlace, vista), BorderLayout.EAST);

        // En NORTH para que la lista no se estire hasta abajo
        JPanel envoltura = new JPanel(new BorderLayout());
        envoltura.setOpaque(false);
        envoltura.add(contenido, BorderLayout.NORTH);

        tarjeta.add(encabezado, BorderLayout.NORTH);
        tarjeta.add(envoltura, BorderLayout.CENTER);
        return tarjeta;
    }

    private JButton crearEnlace(String texto, String vista) {
        JButton enlace = new JButton(texto + "  →");
        enlace.setFont(Estilo.sans(12.5f));
        enlace.setForeground(Estilo.GRIS);
        enlace.setContentAreaFilled(false);
        enlace.setFocusPainted(false);
        enlace.setBorder(BorderFactory.createEmptyBorder());
        enlace.setCursor(new Cursor(Cursor.HAND_CURSOR));
        enlace.addActionListener(e -> ventana.mostrar(vista));
        enlace.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                enlace.setForeground(Estilo.TINTA);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                enlace.setForeground(Estilo.GRIS);
            }
        });
        return enlace;
    }

    private JPanel crearLista() {
        JPanel lista = new JPanel();
        lista.setLayout(new BoxLayout(lista, BoxLayout.Y_AXIS));
        lista.setOpaque(false);
        return lista;
    }

    // Una fila con texto principal, texto pequeño abajo y la etiqueta a la derecha
    private JPanel crearFila(String principal, String secundario, JComponent etiqueta) {
        JPanel fila = new JPanel(new BorderLayout(10, 0));
        fila.setOpaque(false);
        fila.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Estilo.LINEA),
                BorderFactory.createEmptyBorder(9, 0, 9, 0)));
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        fila.setAlignmentX(LEFT_ALIGNMENT);

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel lblPrincipal = Estilo.texto(principal, 13f, Estilo.TINTA);
        JLabel lblSecundario = Estilo.texto(secundario, 12f, Estilo.GRIS);
        lblPrincipal.setAlignmentX(LEFT_ALIGNMENT);
        lblSecundario.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(lblPrincipal);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblSecundario);

        // GridBagLayout centra la etiqueta verticalmente
        JPanel derecha = new JPanel(new GridBagLayout());
        derecha.setOpaque(false);
        derecha.add(etiqueta);

        fila.add(textos, BorderLayout.CENTER);
        fila.add(derecha, BorderLayout.EAST);
        return fila;
    }

    private JLabel textoVacio(String texto) {
        JLabel etiqueta = Estilo.texto(texto, 13f, Estilo.GRIS);
        etiqueta.setAlignmentX(LEFT_ALIGNMENT);
        return etiqueta;
    }

}//Fin public class
