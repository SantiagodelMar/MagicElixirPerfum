package gui.componentes;

import gui.estilo.Estilo;
import gui.estilo.Formato;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.function.IntConsumer;
import java.util.regex.Pattern;

// Tabla con el estilo del sistema.
// - La columna 0 siempre es el ID del registro.
// - Los Double se muestran como plata ($ 240.000) y los LocalDate como fecha.
// - Se puede ordenar dando clic en el encabezado y filtrar con el buscador.
public class Tabla extends JTable {

    private static final Color FILA_ENCIMA = new Color(0xFA, 0xF8, 0xF5);

    private final DefaultTableModel modelo;
    private int filaEncima = -1;

    public Tabla(String[] columnas) {
        modelo = new DefaultTableModel(columnas, 0) {
            // Nadie puede escribir directo en la tabla, se edita desde el formulario
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }

            // Asi los numeros se ordenan como numeros y no como texto
            @Override
            public Class<?> getColumnClass(int columna) {
                if (getRowCount() > 0 && getValueAt(0, columna) != null) {
                    return getValueAt(0, columna).getClass();
                }
                return Object.class;
            }
        };
        setModel(modelo);
        setAutoCreateRowSorter(true);

        setRowHeight(40);
        setFont(Estilo.sans(13f));
        setForeground(Estilo.TINTA);
        setBackground(Estilo.BLANCO);
        setSelectionBackground(Estilo.SELECCION);
        setSelectionForeground(Estilo.TINTA);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(0xEE, 0xEB, 0xE6));
        setIntercellSpacing(new Dimension(0, 1));
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setDefaultRenderer(Object.class, new CeldaTexto());
        setDefaultRenderer(Integer.class, new CeldaTexto());
        setDefaultRenderer(Double.class, new CeldaDinero());
        setDefaultRenderer(LocalDate.class, new CeldaFecha());

        JTableHeader encabezado = getTableHeader();
        encabezado.setDefaultRenderer(new CeldaEncabezado());
        encabezado.setReorderingAllowed(false);
        encabezado.setPreferredSize(new Dimension(0, 40));
        encabezado.setBackground(Estilo.BLANCO);

        // Resalta la fila por donde pasa el mouse
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int fila = rowAtPoint(e.getPoint());
                if (fila != filaEncima) {
                    filaEncima = fila;
                    repaint();
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                filaEncima = -1;
                repaint();
            }
        });
    }

    //Metodos para llenar la tabla

    public void agregarFila(Object... valores) {
        modelo.addRow(valores);
    }

    public void limpiar() {
        modelo.setRowCount(0);
    }

    // Devuelve el ID (columna 0) de la fila seleccionada, o -1 si no hay ninguna
    public int getIdSeleccionado() {
        int fila = getSelectedRow();
        if (fila == -1) {
            return -1;
        }
        return (Integer) getValueAt(fila, 0);
    }

    // Vuelve a seleccionar la fila de un ID (por ejemplo despues de actualizar)
    public void seleccionarId(int id) {
        for (int fila = 0; fila < getRowCount(); fila++) {
            if ((Integer) getValueAt(fila, 0) == id) {
                setRowSelectionInterval(fila, fila);
                scrollRectToVisible(getCellRect(fila, 0, true));
                return;
            }
        }
    }

    // Ejecuta la accion cada vez que el usuario selecciona una fila
    public void alSeleccionarFila(IntConsumer accion) {
        getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && getIdSeleccionado() != -1) {
                accion.accept(getIdSeleccionado());
            }
        });
    }

    // Deja ver solo las filas que tengan el texto buscado (sin importar mayusculas)
    @SuppressWarnings("unchecked")
    public void filtrar(String texto) {
        TableRowSorter<TableModel> ordenador = (TableRowSorter<TableModel>) getRowSorter();
        if (texto == null || texto.trim().isEmpty()) {
            ordenador.setRowFilter(null);
        } else {
            ordenador.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(texto.trim())));
        }
    }

    // Marca una columna para que se dibuje como pastilla de estado
    public void columnaEstado(int columna) {
        getColumnModel().getColumn(columna).setCellRenderer(new CeldaEstado());
    }

    // Anchos sugeridos de cada columna (en el mismo orden de las columnas)
    public void anchos(int... anchos) {
        for (int i = 0; i < anchos.length && i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
    }

    //Celdas (como se dibuja cada tipo de dato)

    private void pintarFondo(Component celda, boolean seleccionada, int fila) {
        if (seleccionada) {
            celda.setBackground(Estilo.SELECCION);
        } else if (fila == filaEncima) {
            celda.setBackground(FILA_ENCIMA);
        } else {
            celda.setBackground(Estilo.BLANCO);
        }
        celda.setForeground(Estilo.TINTA);
    }

    private class CeldaTexto extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                       boolean conFoco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, seleccionada, false, fila, columna);
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 6));
            setHorizontalAlignment(LEFT);
            pintarFondo(this, seleccionada, fila);
            // El ID va en gris para que no robe atencion
            if (columna == 0) {
                setForeground(Estilo.GRIS);
            }
            return this;
        }
    }

    private class CeldaDinero extends CeldaTexto {
        @Override
        protected void setValue(Object valor) {
            if (valor instanceof Double) {
                setText(Formato.dinero((Double) valor));
            } else {
                setText("");
            }
        }
    }

    private class CeldaFecha extends CeldaTexto {
        @Override
        protected void setValue(Object valor) {
            if (valor instanceof LocalDate) {
                setText(Formato.fecha((LocalDate) valor));
            } else {
                setText("—");
            }
        }
    }

    private class CeldaEstado implements TableCellRenderer {
        private final JPanel celda = new JPanel(new GridBagLayout());
        private final EtiquetaEstado etiqueta = new EtiquetaEstado("");

        CeldaEstado() {
            GridBagConstraints posicion = new GridBagConstraints();
            posicion.anchor = GridBagConstraints.WEST;
            posicion.weightx = 1;
            posicion.insets = new Insets(0, 12, 0, 0);
            celda.add(etiqueta, posicion);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                       boolean conFoco, int fila, int columna) {
            etiqueta.setEstado(valor == null ? "" : valor.toString());
            pintarFondo(celda, seleccionada, fila);
            return celda;
        }
    }

    private class CeldaEncabezado extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean seleccionada,
                                                       boolean conFoco, int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            setText(valor == null ? "" : valor.toString().toUpperCase());
            setFont(Estilo.espaciada(Estilo.sansBold(10.5f), 0.1f));
            setForeground(Estilo.GRIS);
            setBackground(Estilo.BLANCO);
            setHorizontalAlignment(LEFT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Estilo.LINEA),
                    BorderFactory.createEmptyBorder(0, 14, 0, 6)));
            return this;
        }
    }

}//Fin public class
