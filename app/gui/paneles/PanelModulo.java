package gui.paneles;

import gui.VentanaPrincipal;
import gui.componentes.CampoTexto;
import gui.componentes.DialogoConfirmar;
import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

// Clase padre de todas las pantallas del menu (herencia).
// Arma el encabezado con el titulo y trae metodos que usan todos los paneles:
// mostrar mensajes, confirmar y quitar los errores de los campos.
public abstract class PanelModulo extends JPanel {

    protected final VentanaPrincipal ventana;

    private final JLabel lblTitulo;
    private final JLabel lblDescripcion;

    public PanelModulo(VentanaPrincipal ventana, String seccion, String titulo, String descripcion) {
        this.ventana = ventana;
        setLayout(new BorderLayout(0, 20));
        setBackground(Estilo.LIENZO);
        setBorder(BorderFactory.createEmptyBorder(26, 32, 26, 32));

        // Encabezado de la pantalla
        JPanel encabezado = new JPanel();
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        encabezado.setOpaque(false);

        JLabel lblSeccion = Estilo.micro(seccion);
        lblTitulo = Estilo.titulo(titulo, 30f);
        lblDescripcion = Estilo.texto(descripcion, 13.5f, Estilo.GRIS);
        lblSeccion.setAlignmentX(LEFT_ALIGNMENT);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        lblDescripcion.setAlignmentX(LEFT_ALIGNMENT);

        encabezado.add(lblSeccion);
        encabezado.add(Box.createVerticalStrut(6));
        encabezado.add(lblTitulo);
        encabezado.add(Box.createVerticalStrut(4));
        encabezado.add(lblDescripcion);

        add(encabezado, BorderLayout.NORTH);
    }

    // Cada panel vuelve a cargar sus datos cuando se abre desde el menu
    public abstract void refrescar();

    protected void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }

    protected void setDescripcion(String descripcion) {
        lblDescripcion.setText(descripcion);
    }

    // Pone el formulario arriba y la tabla abajo (la tabla se estira con la ventana)
    protected JPanel unirFormularioYTabla(JComponent formulario, JComponent tabla) {
        JPanel cuerpo = new JPanel(new BorderLayout(0, 16));
        cuerpo.setOpaque(false);
        cuerpo.add(formulario, BorderLayout.NORTH);
        cuerpo.add(tabla, BorderLayout.CENTER);
        return cuerpo;
    }

    protected void mostrarExito(String mensaje) {
        ventana.mostrarAviso(mensaje, true);
    }

    protected void mostrarError(String mensaje) {
        ventana.mostrarAviso(mensaje, false);
    }

    protected boolean confirmar(String titulo, String mensaje, String textoBoton) {
        return DialogoConfirmar.mostrar(this, titulo, mensaje, textoBoton);
    }

    protected void quitarErrores(CampoTexto... campos) {
        for (CampoTexto campo : campos) {
            campo.limpiarError();
        }
    }

}//Fin public class
