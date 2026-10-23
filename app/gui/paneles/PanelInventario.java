package gui.paneles;

import domain.Inventario;
import domain.Perfume;
import domain.enums.EstadoInventario;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.estilo.Formato;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.InventarioServiceInterface;
import service.inputport.PerfumeServiceInterface;

import java.awt.BorderLayout;

// Modulo de Inventario - HU-07: saber cuantas unidades hay de cada perfume.
// El estado (Disponible, Bajo stock, Agotado) lo calcula el service con SetInventarioHelper.
public class PanelInventario extends PanelModulo {

    private final InventarioServiceInterface inventarioService;
    private final PerfumeServiceInterface perfumeService;

    private final CampoTexto txtId = new CampoTexto("Ej: 202");
    private final Combo<Perfume> cbPerfume = new Combo<>(perfume -> perfume.getNombre() + "  ·  " + perfume.getMarca());
    private final CampoTexto txtStock = new CampoTexto("Unidades disponibles");

    private final Boton btnGuardar = new Boton("Registrar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar stock", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Stock de un perfume", 4);
    private final TarjetaTabla listado = new TarjetaTabla("Inventario", "Buscar perfume o estado...",
            new String[]{"ID", "Perfume", "Marca", "Categoría", "Stock", "Estado"});

    private int idSeleccionado = -1;

    public PanelInventario(VentanaPrincipal ventana, InventarioServiceInterface inventarioService,
                           PerfumeServiceInterface perfumeService) {
        super(ventana, "Inventario  ·  HU-07", "Inventario",
                "Unidades disponibles por perfume. Con 5 o menos queda en bajo stock y con 0 se marca agotado.");
        this.inventarioService = inventarioService;
        this.perfumeService = perfumeService;

        formulario.agregarCampo("ID inventario", txtId);
        formulario.agregarCampo("Perfume", cbPerfume, 2);
        formulario.agregarCampo("Stock disponible", txtStock);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        Tabla tabla = listado.getTabla();
        tabla.anchos(50, 250, 170, 180, 80, 130);
        tabla.columnaEstado(5);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
        cbPerfume.cargar(perfumeService.selectAllPerfumes());
        limpiarFormulario();
    }

    @Override
    public void refrescar() {
        cbPerfume.cargar(perfumeService.selectAllPerfumes());
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        Tabla tabla = listado.getTabla();
        tabla.limpiar();

        int disponibles = 0;
        int bajos = 0;
        int agotados = 0;

        for (Inventario inventario : inventarioService.selectAllInventarios()) {
            Perfume perfume = inventario.getPerfume();
            tabla.agregarFila(
                    inventario.getIdInventario(),
                    perfume.getNombre(),
                    perfume.getMarca(),
                    perfume.getCategoria().getNombre(),
                    inventario.getStockDisponible(),
                    Formato.nombreEnum(inventario.getEstadoDisponibilidad()));

            if (inventario.getEstadoDisponibilidad() == EstadoInventario.DISPONIBLE) {
                disponibles++;
            } else if (inventario.getEstadoDisponibilidad() == EstadoInventario.BAJO_STOCK) {
                bajos++;
            } else {
                agotados++;
            }
        }
        listado.actualizarConteo();
        formulario.setNota("Disponibles: " + disponibles + "   ·   Bajo stock: " + bajos + "   ·   Agotados: " + agotados);
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID inventario");
            if (inventarioService.selectInventarioById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un inventario con el ID " + id);
                return;
            }
            Perfume perfume = ValidadorCampos.seleccion(cbPerfume, "el perfume");
            if (inventarioService.selectInventarioByPerfume(perfume.getIdPerfume()) != null) {
                mostrarError(perfume.getNombre() + " ya tiene inventario, búscalo en la tabla y actualiza el stock");
                return;
            }
            int stock = ValidadorCampos.enteroConCero(txtStock, "El stock");

            inventarioService.createInventario(id, perfume.getIdPerfume(), stock);

            mostrarExito("Inventario de " + perfume.getNombre() + " registrado con " + stock + " unidades");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            int stock = ValidadorCampos.enteroConCero(txtStock, "El stock");
            Inventario inventario = inventarioService.updateStock(idSeleccionado, stock);

            mostrarExito("Stock actualizado: " + inventario.getPerfume().getNombre() + " quedó "
                    + Formato.nombreEnum(inventario.getEstadoDisponibilidad()).toLowerCase());
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        Inventario inventario = inventarioService.selectInventarioById(idSeleccionado);
        if (inventario == null) {
            return;
        }
        boolean confirmado = confirmar("¿Eliminar inventario?",
                "Se borrará el registro de stock de " + inventario.getPerfume().getNombre()
                        + ". El perfume sigue en el catálogo.", "Eliminar");
        if (confirmado) {
            inventarioService.deleteInventarioById(idSeleccionado);
            mostrarExito("Inventario eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Inventario inventario = inventarioService.selectInventarioById(id);
        if (inventario == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(inventario.getIdInventario()));
        cbPerfume.setSelectedItem(inventario.getPerfume());
        txtStock.setText(String.valueOf(inventario.getStockDisponible()));
        quitarErrores(txtId, txtStock);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtStock.limpiar();
        if (cbPerfume.getItemCount() > 0) {
            cbPerfume.setSelectedIndex(0);
        }
        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        cbPerfume.setBloqueado(editando);
        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEliminar.setEnabled(editando);
        formulario.setModo(editando, editando ? "Editando #" + idSeleccionado : "Nuevo registro");
    }

    private int siguienteId() {
        int mayor = 0;
        for (Inventario inventario : inventarioService.selectAllInventarios()) {
            mayor = Math.max(mayor, inventario.getIdInventario());
        }
        return mayor + 1;
    }

}//Fin public class
