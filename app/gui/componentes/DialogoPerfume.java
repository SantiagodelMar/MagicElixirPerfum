package gui.componentes;

import domain.Inventario;
import domain.Perfume;
import gui.estilo.Estilo;
import gui.estilo.Formato;
import gui.estilo.Recursos;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;

// Ficha de un perfume: se abre al hacer clic en una tarjeta de la vitrina.
// Muestra la foto grande, las notas olfativas, el precio y las unidades en bodega.
// Uso igual que DialogoConfirmar: if (DialogoPerfume.mostrar(this, perfume, inventario)) { ...editar... }
public class DialogoPerfume extends JDialog {

    private boolean quiereEditar = false;

    private DialogoPerfume(Window padre, Perfume perfume, Inventario inventario) {
        super(padre, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);

        JPanel contenido = new JPanel(new BorderLayout(30, 0));
        contenido.setBackground(Estilo.BLANCO);
        contenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.LINEA),
                BorderFactory.createEmptyBorder(24, 24, 24, 30)));

        contenido.add(crearFoto(perfume), BorderLayout.WEST);
        contenido.add(crearInformacion(perfume, inventario), BorderLayout.CENTER);
        setContentPane(contenido);

        // Escape = cerrar
        contenido.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        pack();
        setLocationRelativeTo(padre);
    }

    private JPanel crearFoto(Perfume perfume) {
        PanelRedondo marco = new PanelRedondo(new BorderLayout(), 16);
        marco.setBackground(Estilo.LIENZO);
        marco.setBorde(null);
        marco.setPreferredSize(new Dimension(300, 375));

        JLabel lblFoto = new JLabel(Recursos.fotoPerfume(perfume.getMarca(), perfume.getNombre(), 280, 350));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        marco.add(lblFoto, BorderLayout.CENTER);
        return marco;
    }

    private JPanel crearInformacion(Perfume perfume, Inventario inventario) {
        JPanel info = new JPanel(new BorderLayout());
        info.setOpaque(false);
        info.setPreferredSize(new Dimension(360, 375));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        JLabel lblCategoria = Estilo.micro(perfume.getCategoria().getNombre());
        JPanel panelNombre = Estilo.textoEnLineas(perfume.getNombre(), Estilo.serifBold(26f), Estilo.TINTA, 350, 2);
        JLabel lblMarca = Estilo.texto(perfume.getMarca() + "  ·  " + perfume.getMililitros() + " ml", 14f, Estilo.GRIS);

        // Las notas se separan con un punto para que se lean mejor
        String notas = perfume.getNotasOlfativas().replace(", ", "  ·  ");
        JLabel lblNotas = Estilo.texto("<html><div style='width:340px'>" + notas + "</div></html>", 13.5f, Estilo.TINTA);

        JLabel lblPrecio = Estilo.titulo(Formato.dinero(perfume.getPrecio()), 30f);

        textos.add(alinear(lblCategoria));
        textos.add(Box.createVerticalStrut(8));
        textos.add(alinear(panelNombre));
        textos.add(Box.createVerticalStrut(4));
        textos.add(alinear(lblMarca));
        textos.add(Box.createVerticalStrut(22));
        textos.add(alinear(Estilo.micro("Notas olfativas")));
        textos.add(Box.createVerticalStrut(6));
        textos.add(alinear(lblNotas));
        textos.add(Box.createVerticalStrut(22));
        textos.add(alinear(Estilo.micro("Precio")));
        textos.add(Box.createVerticalStrut(2));
        textos.add(alinear(lblPrecio));
        textos.add(Box.createVerticalStrut(14));
        textos.add(alinear(Estilo.micro("Disponibilidad")));
        textos.add(Box.createVerticalStrut(8));
        textos.add(alinear(crearFilaStock(inventario)));

        // Botones abajo a la derecha
        Boton btnCerrar = new Boton("Cerrar", Boton.SECUNDARIO);
        Boton btnEditar = new Boton("Editar perfume", Boton.PRINCIPAL);
        btnCerrar.addActionListener(e -> dispose());
        btnEditar.addActionListener(e -> {
            quiereEditar = true;
            dispose();
        });
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCerrar);
        botones.add(btnEditar);
        getRootPane().setDefaultButton(btnCerrar);

        info.add(textos, BorderLayout.NORTH);
        info.add(botones, BorderLayout.SOUTH);
        return info;
    }

    private JPanel crearFilaStock(Inventario inventario) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila.setOpaque(false);

        EtiquetaEstado etiqueta = TarjetaPerfume.crearEtiquetaStock(inventario);
        if (inventario != null && inventario.getEstadoDisponibilidad() != null) {
            etiqueta.setText(inventario.getEstadoDisponibilidad().getDescripcion());
        }
        fila.add(etiqueta);

        String detalle;
        if (inventario == null) {
            detalle = "Todavía no tiene inventario registrado";
        } else if (inventario.getStockDisponible() == 1) {
            detalle = "1 unidad en bodega";
        } else {
            detalle = inventario.getStockDisponible() + " unidades en bodega";
        }
        fila.add(Box.createHorizontalStrut(10));
        fila.add(Estilo.texto(detalle, 13f, Estilo.GRIS));
        return fila;
    }

    private JComponent alinear(JComponent componente) {
        componente.setAlignmentX(LEFT_ALIGNMENT);
        return componente;
    }

    // Devuelve true si el usuario pulso "Editar perfume"
    public static boolean mostrar(Component origen, Perfume perfume, Inventario inventario) {
        Window padre = origen instanceof Window ? (Window) origen : SwingUtilities.getWindowAncestor(origen);
        DialogoPerfume dialogo = new DialogoPerfume(padre, perfume, inventario);
        dialogo.setVisible(true);
        return dialogo.quiereEditar;
    }

}//Fin public class
