package gui.componentes;

import gui.estilo.Estilo;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;
import java.util.function.Function;

// Lista desplegable con el mismo estilo de los campos de texto.
// Se le puede pasar una funcion que dice que texto mostrar de cada objeto.
// Ej: new Combo<Cliente>(c -> c.getNombre() + " " + c.getApellido())
public class Combo<T> extends JComboBox<T> {

    private final Function<T, String> textoVisible;
    private final BordeRedondo borde = new BordeRedondo(Estilo.LINEA, 10, 4, 10, 4, 6);

    public Combo() {
        this(null);
    }

    public Combo(Function<T, String> textoVisible) {
        this.textoVisible = textoVisible;

        setFont(Estilo.sans(13.5f));
        setForeground(Estilo.TINTA);
        setBackground(Estilo.BLANCO);
        setBorder(borde);
        setOpaque(false);
        setPreferredSize(new Dimension(120, 38));
        setMaximumRowCount(10);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setUI(new ComboPlano());
        setRenderer(new FilaCombo());

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                borde.setColor(Estilo.TINTA);
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                borde.setColor(Estilo.LINEA);
                repaint();
            }
        });

        // Al escoger una opcion se quita el borde rojo de error
        addActionListener(e -> {
            borde.setColor(isFocusOwner() ? Estilo.TINTA : Estilo.LINEA);
            repaint();
        });
    }

    // Borra lo que tenia y carga la lista nueva
    public void cargar(List<T> elementos) {
        removeAllItems();
        for (T elemento : elementos) {
            addItem(elemento);
        }
    }

    // Selecciona el valor y si no esta en la lista lo agrega
    // (por si desde la consola escribieron un valor que no esta en las opciones)
    public void seleccionar(T valor) {
        boolean existe = false;
        for (int i = 0; i < getItemCount(); i++) {
            if (getItemAt(i).equals(valor)) {
                existe = true;
            }
        }
        if (!existe && valor != null) {
            addItem(valor);
        }
        setSelectedItem(valor);
    }

    public T getSeleccionado() {
        int indice = getSelectedIndex();
        if (indice < 0) {
            return null;
        }
        return getItemAt(indice);
    }

    public void setBloqueado(boolean bloqueado) {
        setEnabled(!bloqueado);
        setBackground(bloqueado ? Estilo.LIENZO : Estilo.BLANCO);
    }

    public void marcarError() {
        borde.setColor(Estilo.ROJO);
        repaint();
    }

    // Fondo redondeado igual que los campos de texto
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }

    @SuppressWarnings("unchecked")
    private String textoDe(Object valor) {
        if (valor == null) {
            return "";
        }
        if (textoVisible != null) {
            return textoVisible.apply((T) valor);
        }
        return valor.toString();
    }

    // Como se dibuja cada opcion de la lista
    private class FilaCombo extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                      boolean seleccionado, boolean conFoco) {
            super.getListCellRendererComponent(lista, valor, indice, seleccionado, false);
            setText(textoDe(valor));
            setFont(Estilo.sans(13.5f));
            setForeground(Estilo.TINTA);
            setBackground(seleccionado ? Estilo.SELECCION : Estilo.BLANCO);
            // indice -1 es el texto que se ve en la caja cerrada
            if (indice == -1) {
                setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
            } else {
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            }
            return this;
        }
    }

    // Quita el boton gris de Java y pone una flecha sencilla
    private class ComboPlano extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton flecha = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(comboBox.isEnabled() ? Estilo.GRIS : Estilo.GRIS_CLARO);
                    int x = getWidth() / 2;
                    int y = getHeight() / 2;
                    // Dos lineas que forman una "v"
                    g2.drawLine(x - 4, y - 2, x, y + 2);
                    g2.drawLine(x, y + 2, x + 4, y - 2);
                    g2.dispose();
                }
            };
            flecha.setBorder(BorderFactory.createEmptyBorder());
            flecha.setContentAreaFilled(false);
            flecha.setFocusable(false);
            return flecha;
        }

        // Sin el resaltado azul cuando tiene el foco
        @Override
        public void paintCurrentValue(Graphics g, Rectangle zona, boolean conFoco) {
            super.paintCurrentValue(g, zona, false);
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle zona, boolean conFoco) {
            g.setColor(comboBox.getBackground());
            g.fillRect(zona.x, zona.y, zona.width, zona.height);
        }

        // La lista que se despliega, con borde suave y scroll delgado
        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup lista = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroll = super.createScroller();
                    BarraScroll.aplicar(scroll, Estilo.BLANCO, new Color(0xCF, 0xCB, 0xC4));
                    return scroll;
                }
            };
            lista.setBorder(BorderFactory.createLineBorder(Estilo.LINEA));
            return lista;
        }
    }

}//Fin public class
