package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.KeyEvent;

// Ventanita para preguntar antes de hacer algo que no se puede deshacer (ej: eliminar).
// Uso: if (DialogoConfirmar.mostrar(this, "¿Eliminar cliente?", "Se borrara ...", "Eliminar")) { ... }
public class DialogoConfirmar extends JDialog {

    private boolean confirmado = false;

    private DialogoConfirmar(Window padre, String titulo, String mensaje, String textoConfirmar) {
        super(padre, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);

        JPanel contenido = new JPanel(new BorderLayout(0, 22));
        contenido.setBackground(Estilo.BLANCO);
        contenido.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.LINEA),
                BorderFactory.createEmptyBorder(26, 28, 22, 28)));

        // Textos
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        JLabel lblTitulo = Estilo.titulo(titulo, 22f);
        // Con html el texto baja de linea solo si es muy largo
        JLabel lblMensaje = Estilo.texto("<html><div style='width:320px'>" + mensaje + "</div></html>", 13.5f, Estilo.GRIS);
        JLabel lblMicro = Estilo.micro("Confirmar");
        lblMicro.setAlignmentX(LEFT_ALIGNMENT);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        lblMensaje.setAlignmentX(LEFT_ALIGNMENT);
        textos.add(lblMicro);
        textos.add(Box.createVerticalStrut(8));
        textos.add(lblTitulo);
        textos.add(Box.createVerticalStrut(8));
        textos.add(lblMensaje);

        // Botones
        Boton btnCancelar = new Boton("Cancelar", Boton.SECUNDARIO);
        Boton btnConfirmar = new Boton(textoConfirmar, Boton.PRINCIPAL);
        btnCancelar.addActionListener(e -> dispose());
        btnConfirmar.addActionListener(e -> {
            confirmado = true;
            dispose();
        });
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);
        botones.add(btnCancelar);
        botones.add(btnConfirmar);

        contenido.add(textos, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        setContentPane(contenido);

        // Escape = cancelar
        contenido.registerKeyboardAction(e -> dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().setDefaultButton(btnCancelar);

        pack();
        setLocationRelativeTo(padre);
    }

    // Devuelve true si el usuario confirmo
    public static boolean mostrar(Component origen, String titulo, String mensaje, String textoConfirmar) {
        Window padre = origen instanceof Window ? (Window) origen : SwingUtilities.getWindowAncestor(origen);
        DialogoConfirmar dialogo = new DialogoConfirmar(padre, titulo, mensaje, textoConfirmar);
        dialogo.setVisible(true);
        return dialogo.confirmado;
    }

}//Fin public class
