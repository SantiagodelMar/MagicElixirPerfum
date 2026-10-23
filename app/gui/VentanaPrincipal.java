package gui;

import config.Contexto;
import domain.Empleado;
import gui.componentes.Aviso;
import gui.componentes.Avatar;
import gui.componentes.BarraScroll;
import gui.componentes.BotonMenu;
import gui.componentes.DialogoConfirmar;
import gui.estilo.Estilo;
import gui.estilo.Formato;
import gui.estilo.Recursos;
import gui.paneles.PanelAdministradores;
import gui.paneles.PanelCategorias;
import gui.paneles.PanelClientes;
import gui.paneles.PanelDevoluciones;
import gui.paneles.PanelEmpleados;
import gui.paneles.PanelEnvios;
import gui.paneles.PanelInicio;
import gui.paneles.PanelInventario;
import gui.paneles.PanelModulo;
import gui.paneles.PanelOrdenesCompra;
import gui.paneles.PanelPedidos;
import gui.paneles.PanelPerfumes;
import gui.paneles.PanelProveedores;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Ventana principal: menu lateral negro a la izquierda y las pantallas a la derecha.
// Las pantallas se cambian con un CardLayout (como pasar las cartas de un mazo).
public class VentanaPrincipal extends JFrame {

    private final Contexto contexto;
    private final Empleado empleadoActivo;

    private final CardLayout vistas = new CardLayout();
    private final JPanel contenido = new JPanel(vistas);
    private final List<BotonMenu> botonesMenu = new ArrayList<>();
    private final Map<String, PanelModulo> paneles = new HashMap<>();
    private final Aviso aviso = new Aviso();

    public VentanaPrincipal(Contexto contexto, Empleado empleadoActivo) {
        this.contexto = contexto;
        this.empleadoActivo = empleadoActivo;

        setTitle("Magic Elixir Perfum  |  Sistema de gestión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 680));
        setSize(1360, 860);
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setIconImage(Recursos.imagen("logo", 64, 64).getImage());

        contenido.setBackground(Estilo.LIENZO);
        crearPaneles();

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.add(crearMenuLateral(), BorderLayout.WEST);
        raiz.add(contenido, BorderLayout.CENTER);
        setContentPane(raiz);

        // El aviso flota por encima de todo
        getLayeredPane().add(aviso, JLayeredPane.POPUP_LAYER);

        mostrar("inicio");
    }

    // Cada panel recibe por el constructor solo los services que necesita
    // (inyeccion de dependencias, igual que los View de la consola)
    private void crearPaneles() {
        agregarPanel("inicio", new PanelInicio(this, contexto.getPerfumeService(), contexto.getCategoriaService(),
                contexto.getInventarioService(), contexto.getPedidoService()));

        // Usuarios
        agregarPanel("clientes", new PanelClientes(this, contexto.getClienteService()));
        agregarPanel("empleados", new PanelEmpleados(this, contexto.getEmpleadoService()));
        agregarPanel("administradores", new PanelAdministradores(this, contexto.getAdministradorService()));

        // Catalogo e inventario
        agregarPanel("categorias", new PanelCategorias(this, contexto.getCategoriaService(), contexto.getPerfumeService()));
        agregarPanel("perfumes", new PanelPerfumes(this, contexto.getPerfumeService(), contexto.getCategoriaService(),
                contexto.getInventarioService()));
        agregarPanel("inventario", new PanelInventario(this, contexto.getInventarioService(), contexto.getPerfumeService()));

        // Ventas
        agregarPanel("pedidos", new PanelPedidos(this, contexto.getPedidoService(), contexto.getClienteService(),
                contexto.getPerfumeService(), contexto.getInventarioService()));
        agregarPanel("envios", new PanelEnvios(this, contexto.getEnvioService(), contexto.getPedidoService()));
        agregarPanel("devoluciones", new PanelDevoluciones(this, contexto.getDevolucionService(),
                contexto.getPedidoService(), contexto.getEnvioService()));

        // Abastecimiento
        agregarPanel("proveedores", new PanelProveedores(this, contexto.getProveedorService()));
        agregarPanel("ordenes", new PanelOrdenesCompra(this, contexto.getOrdenCompraService(), contexto.getProveedorService()));
    }

    private void agregarPanel(String vista, PanelModulo panel) {
        paneles.put(vista, panel);
        contenido.add(panel, vista);
    }

    //Menu lateral

    private JPanel crearMenuLateral() {
        JPanel lateral = new JPanel(new BorderLayout());
        lateral.setBackground(Estilo.TINTA);
        lateral.setPreferredSize(new Dimension(248, 0));

        lateral.add(crearMarca(), BorderLayout.NORTH);
        lateral.add(crearOpciones(), BorderLayout.CENTER);
        lateral.add(crearUsuario(), BorderLayout.SOUTH);
        return lateral;
    }

    private JPanel crearMarca() {
        JPanel marca = new JPanel(new BorderLayout(12, 0));
        marca.setOpaque(false);
        marca.setBorder(BorderFactory.createEmptyBorder(22, 22, 14, 22));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel lblNombre = Estilo.titulo("Magic Elixir", 19f);
        lblNombre.setForeground(Estilo.CREMA);
        JLabel lblSub = Estilo.micro("Perfum · Gestión");
        lblSub.setForeground(Estilo.CREMA_TENUE);
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);
        lblSub.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lblNombre);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblSub);

        marca.add(new JLabel(Recursos.imagen("logo", 44, 44)), BorderLayout.WEST);
        marca.add(textos, BorderLayout.CENTER);
        return marca;
    }

    private JScrollPane crearOpciones() {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Estilo.TINTA);
        menu.setBorder(BorderFactory.createEmptyBorder(4, 14, 12, 14));

        agregarOpcion(menu, "Inicio", "inicio");

        agregarSeccion(menu, "Usuarios");
        agregarOpcion(menu, "Clientes", "clientes");
        agregarOpcion(menu, "Empleados", "empleados");
        agregarOpcion(menu, "Administradores", "administradores");

        agregarSeccion(menu, "Catálogo");
        agregarOpcion(menu, "Categorías", "categorias");
        agregarOpcion(menu, "Perfumes", "perfumes");
        agregarOpcion(menu, "Inventario", "inventario");

        agregarSeccion(menu, "Ventas");
        agregarOpcion(menu, "Pedidos", "pedidos");
        agregarOpcion(menu, "Envíos", "envios");
        agregarOpcion(menu, "Devoluciones", "devoluciones");

        agregarSeccion(menu, "Abastecimiento");
        agregarOpcion(menu, "Proveedores", "proveedores");
        agregarOpcion(menu, "Órdenes de compra", "ordenes");

        // Si la pantalla es pequeña el menu se puede bajar con el scroll
        JScrollPane scroll = new JScrollPane(menu);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        BarraScroll.aplicar(scroll, Estilo.TINTA, new Color(0x3A, 0x3A, 0x3D));
        return scroll;
    }

    private void agregarSeccion(JPanel menu, String titulo) {
        JLabel lblSeccion = Estilo.micro(titulo);
        lblSeccion.setForeground(new Color(0x6B, 0x69, 0x65));
        lblSeccion.setBorder(BorderFactory.createEmptyBorder(14, 14, 6, 0));
        lblSeccion.setAlignmentX(LEFT_ALIGNMENT);
        menu.add(lblSeccion);
    }

    private void agregarOpcion(JPanel menu, String texto, String vista) {
        BotonMenu boton = new BotonMenu(texto, vista);
        boton.addActionListener(e -> mostrar(vista));
        botonesMenu.add(boton);
        menu.add(boton);
        menu.add(Box.createVerticalStrut(1));
    }

    // Abajo: quien inicio sesion y el boton para salir
    private JPanel crearUsuario() {
        JPanel usuario = new JPanel(new BorderLayout(12, 0));
        usuario.setOpaque(false);
        usuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0x23, 0x23, 0x26)),
                BorderFactory.createEmptyBorder(14, 22, 14, 18)));

        String iniciales = Formato.iniciales(empleadoActivo.getNombre(), empleadoActivo.getApellido());

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel lblNombre = Estilo.texto(empleadoActivo.getNombre() + " " + empleadoActivo.getApellido(), 13f, Estilo.CREMA);
        lblNombre.setFont(Estilo.sansBold(13f));
        JLabel lblRol = Estilo.texto(empleadoActivo.getRole(), 12f, Estilo.CREMA_TENUE);
        lblNombre.setAlignmentX(LEFT_ALIGNMENT);
        lblRol.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(lblNombre);
        textos.add(Box.createVerticalStrut(2));
        textos.add(lblRol);

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(Estilo.sansBold(12f));
        btnSalir.setForeground(Estilo.CREMA_TENUE);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setToolTipText("Cerrar sesión");
        btnSalir.addActionListener(e -> cerrarSesion());
        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnSalir.setForeground(Estilo.CREMA);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnSalir.setForeground(Estilo.CREMA_TENUE);
            }
        });

        usuario.add(new Avatar(iniciales, 38), BorderLayout.WEST);
        usuario.add(textos, BorderLayout.CENTER);
        usuario.add(btnSalir, BorderLayout.EAST);
        return usuario;
    }

    //Metodos que usan los paneles

    public void mostrar(String vista) {
        PanelModulo panel = paneles.get(vista);
        if (panel != null) {
            panel.refrescar();
        }
        vistas.show(contenido, vista);

        for (BotonMenu boton : botonesMenu) {
            boton.setActivo(boton.getVista().equals(vista));
        }
    }

    public void mostrarAviso(String mensaje, boolean exito) {
        aviso.mostrar(mensaje, exito, getLayeredPane().getWidth(), getLayeredPane().getHeight());
    }

    public Empleado getEmpleadoActivo() {
        return empleadoActivo;
    }

    private void cerrarSesion() {
        boolean salir = DialogoConfirmar.mostrar(this, "¿Cerrar sesión?",
                "Volverás a la pantalla de inicio. Los datos se conservan mientras el programa siga abierto.",
                "Cerrar sesión");
        if (salir) {
            dispose();
            new VentanaLogin(contexto).setVisible(true);
        }
    }

}//Fin public class
