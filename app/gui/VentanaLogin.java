package gui;

import config.Contexto;
import domain.Empleado;
import gui.componentes.Boton;
import gui.componentes.CampoClave;
import gui.componentes.CampoTexto;
import gui.componentes.PanelRedondo;
import gui.estilo.Estilo;
import gui.estilo.Recursos;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

// HU-01: el empleado inicia sesion con su correo y contraseña.
// Si la cuenta esta desactivada (HU-02) no lo deja entrar.
public class VentanaLogin extends JFrame {

    private final Contexto contexto;

    private final CampoTexto txtCorreo = new CampoTexto("correo@magicelixir.com");
    private final CampoClave txtClave = new CampoClave("Tu contraseña");
    private final JLabel lblError = Estilo.texto(" ", 12.5f, Estilo.ROJO);

    public VentanaLogin(Contexto contexto) {
        this.contexto = contexto;

        setTitle("Magic Elixir Perfum  |  Iniciar sesión");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 620);
        setResizable(false);
        setLocationRelativeTo(null);
        setIconImage(Recursos.imagen("logo", 64, 64).getImage());

        JPanel raiz = new JPanel(new GridLayout(1, 2));
        raiz.add(crearPanelMarca());
        raiz.add(crearPanelFormulario());
        setContentPane(raiz);
    }

    // Lado izquierdo: fondo negro con el logo
    private JPanel crearPanelMarca() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Estilo.TINTA);

        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setOpaque(false);

        JLabel lblLogo = new JLabel(Recursos.imagen("logo", 132, 132));
        JLabel lblNombre = Estilo.titulo("Magic Elixir", 40f);
        lblNombre.setForeground(Estilo.CREMA);
        JLabel lblPerfum = Estilo.micro("P e r f u m");
        lblPerfum.setForeground(Estilo.CREMA_TENUE);
        JLabel lblFrase = new JLabel("Fragancias árabes y de diseñador");
        lblFrase.setFont(Estilo.serif(17f));
        lblFrase.setForeground(Estilo.CREMA_TENUE);

        centrar(lblLogo, lblNombre, lblPerfum, lblFrase);
        columna.add(lblLogo);
        columna.add(Box.createVerticalStrut(28));
        columna.add(lblNombre);
        columna.add(Box.createVerticalStrut(6));
        columna.add(lblPerfum);
        columna.add(Box.createVerticalStrut(24));
        columna.add(lblFrase);

        // GridBagLayout sin restricciones deja la columna centrada
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(columna);

        JLabel lblDireccion = Estilo.micro("C.C. Monterrey  ·  El Poblado, Medellín");
        lblDireccion.setForeground(new Color(0x6B, 0x69, 0x65));
        lblDireccion.setHorizontalAlignment(SwingConstants.CENTER);
        lblDireccion.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        panel.add(centro, BorderLayout.CENTER);
        panel.add(lblDireccion, BorderLayout.SOUTH);
        return panel;
    }

    // Lado derecho: el formulario de inicio de sesion
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Estilo.BLANCO);

        JPanel columna = new JPanel();
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setOpaque(false);

        JLabel lblSistema = Estilo.micro("Sistema de gestión");
        JLabel lblTitulo = Estilo.titulo("Bienvenido", 36f);
        JLabel lblSubtitulo = Estilo.texto("Ingresa con tu correo de empleado para continuar.", 13.5f, Estilo.GRIS);
        JLabel lblCorreo = Estilo.micro("Correo");
        JLabel lblClave = Estilo.micro("Contraseña");

        txtCorreo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        txtClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        Boton btnIngresar = new Boton("Ingresar", Boton.PRINCIPAL);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnIngresar.addActionListener(e -> iniciarSesion());
        // Con Enter tambien se inicia sesion
        getRootPane().setDefaultButton(btnIngresar);

        // Caja con el usuario de prueba para la presentacion
        PanelRedondo cajaPrueba = new PanelRedondo(new BorderLayout(0, 4), 12);
        cajaPrueba.setBackground(Estilo.LIENZO);
        cajaPrueba.setBorde(null);
        cajaPrueba.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        cajaPrueba.add(Estilo.micro("Usuario de prueba"), BorderLayout.NORTH);
        cajaPrueba.add(Estilo.texto("admin@magicelixir.com   ·   clave 1234", 13f, Estilo.TINTA), BorderLayout.CENTER);
        cajaPrueba.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        // Esta caja invisible le da el ancho a la columna
        JComponent ancho = (JComponent) Box.createRigidArea(new Dimension(340, 0));

        alinearIzquierda(ancho, lblSistema, lblTitulo, lblSubtitulo, lblCorreo, txtCorreo, lblClave, txtClave,
                lblError, btnIngresar, cajaPrueba);

        columna.add(ancho);
        columna.add(lblSistema);
        columna.add(Box.createVerticalStrut(10));
        columna.add(lblTitulo);
        columna.add(Box.createVerticalStrut(6));
        columna.add(lblSubtitulo);
        columna.add(Box.createVerticalStrut(32));
        columna.add(lblCorreo);
        columna.add(Box.createVerticalStrut(6));
        columna.add(txtCorreo);
        columna.add(Box.createVerticalStrut(18));
        columna.add(lblClave);
        columna.add(Box.createVerticalStrut(6));
        columna.add(txtClave);
        columna.add(Box.createVerticalStrut(10));
        columna.add(lblError);
        columna.add(Box.createVerticalStrut(14));
        columna.add(btnIngresar);
        columna.add(Box.createVerticalStrut(28));
        columna.add(cajaPrueba);

        panel.add(columna);
        return panel;
    }

    private void iniciarSesion() {
        txtCorreo.limpiarError();
        txtClave.limpiarError();

        String correo = txtCorreo.getTexto();
        String clave = txtClave.getClave();

        if (correo.isEmpty() || clave.isEmpty()) {
            lblError.setText("Escribe tu correo y tu contraseña.");
            if (correo.isEmpty()) {
                txtCorreo.marcarError();
            }
            if (clave.isEmpty()) {
                txtClave.marcarError();
            }
            return;
        }

        // Se usa el mismo service que la consola (EmpleadoView.iniciarSesion)
        Empleado empleado = contexto.getEmpleadoService().iniciarSesion(correo, clave);

        if (empleado == null) {
            lblError.setText("Correo o contraseña incorrectos, o la cuenta está desactivada.");
            txtCorreo.marcarError();
            txtClave.marcarError();
            txtClave.setText("");
            return;
        }

        VentanaPrincipal principal = new VentanaPrincipal(contexto, empleado);
        principal.setVisible(true);
        dispose();
    }

    private void centrar(JComponent... componentes) {
        for (JComponent componente : componentes) {
            componente.setAlignmentX(CENTER_ALIGNMENT);
        }
    }

    private void alinearIzquierda(JComponent... componentes) {
        for (JComponent componente : componentes) {
            componente.setAlignmentX(LEFT_ALIGNMENT);
        }
    }

}//Fin public class
