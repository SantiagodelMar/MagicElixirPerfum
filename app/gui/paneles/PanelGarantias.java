package gui.paneles;

import domain.Garantia;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.estilo.Formato;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.GarantiaServiceInterface;

import java.awt.BorderLayout;
import java.time.LocalDate;

// Modulo de Garantias
// HU-20: registrar la garantia con la fecha de inicio y los meses de cobertura
// HU-21: consultar si la garantia sigue vigente (la regla esta en GarantiaServiceImp)
public class PanelGarantias extends PanelModulo {

    private final GarantiaServiceInterface garantiaService;

    private final CampoTexto txtId = new CampoTexto("Ej: 2");
    private final CampoTexto txtIdPedido = new CampoTexto("Ej: 3");
    private final CampoTexto txtFechaInicio = new CampoTexto("aaaa-mm-dd");
    private final CampoTexto txtMeses = new CampoTexto("Ej: 6");
    private final CampoTexto txtDescripcion = new CampoTexto("¿Qué cubre la garantía?");

    private final Boton btnGuardar = new Boton("Registrar garantía", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnVigencia = new Boton("Consultar vigencia", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos de la garantía", 5);
    private final TarjetaTabla listado = new TarjetaTabla("Garantías", "Buscar pedido o estado...",
            new String[]{"ID", "Pedido", "Inicio", "Vence", "Meses", "Cobertura", "Estado"});

    private int idSeleccionado = -1;

    public PanelGarantias(VentanaPrincipal ventana, GarantiaServiceInterface garantiaService) {
        super(ventana, "Ventas", "Garantías",
                "Registra cuántos meses cubre la garantía de un pedido y consulta si todavía está vigente.");
        this.garantiaService = garantiaService;

        formulario.agregarCampo("ID garantía", txtId);
        formulario.agregarCampo("ID del pedido", txtIdPedido);
        formulario.agregarCampo("Fecha de inicio", txtFechaInicio);
        formulario.agregarCampo("Meses de cobertura", txtMeses);
        formulario.agregarCampo("Qué cubre", txtDescripcion);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnVigencia);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnVigencia.addActionListener(e -> consultarVigencia());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        Tabla tabla = listado.getTabla();
        tabla.anchos(45, 70, 110, 110, 80, 280, 110);
        tabla.columnaEstado(6);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
    }

    @Override
    public void refrescar() {
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        Tabla tabla = listado.getTabla();
        tabla.limpiar();
        for (Garantia garantia : garantiaService.selectAllGarantias()) {
            tabla.agregarFila(
                    garantia.getIdGarantia(),
                    "#" + garantia.getPedido().getIdPedido(),
                    garantia.getFechaInicio(),
                    garantia.getFechaVencimiento(),
                    garantia.getMesesCobertura(),
                    garantia.getDescripcion(),
                    garantiaService.estaVigente(garantia.getIdGarantia()) ? "Vigente" : "Vencida");
        }
        listado.actualizarConteo();
    }

    // HU-20
    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID garantía");
            if (garantiaService.selectGarantiaById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe una garantía con el ID " + id);
                return;
            }
            int idPedido = ValidadorCampos.entero(txtIdPedido, "ID del pedido");
            LocalDate fechaInicio = ValidadorCampos.fecha(txtFechaInicio, "La fecha de inicio");
            if (fechaInicio.isAfter(LocalDate.now())) {
                txtFechaInicio.marcarError();
                mostrarError("La fecha de inicio no puede ser después de hoy");
                return;
            }
            int meses = ValidadorCampos.entero(txtMeses, "Meses de cobertura");
            String descripcion = ValidadorCampos.texto(txtDescripcion, "Qué cubre");

            Garantia garantia = garantiaService.createGarantia(id, idPedido, fechaInicio, meses, descripcion);
            if (garantia == null) {
                mostrarError("No se pudo registrar: revisa que el pedido #" + idPedido
                        + " exista y que los meses estén entre 1 y 36");
                return;
            }

            mostrarExito("Garantía registrada. Vence el " + Formato.fecha(garantia.getFechaVencimiento()));
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            int meses = ValidadorCampos.entero(txtMeses, "Meses de cobertura");
            String descripcion = ValidadorCampos.texto(txtDescripcion, "Qué cubre");

            Garantia garantia = garantiaService.updateGarantia(idSeleccionado, meses, descripcion);
            if (garantia == null) {
                txtMeses.marcarError();
                mostrarError("Los meses de cobertura deben estar entre 1 y 36");
                return;
            }
            mostrarExito("Garantía actualizada. Ahora vence el " + Formato.fecha(garantia.getFechaVencimiento()));
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // HU-21
    private void consultarVigencia() {
        Garantia garantia = garantiaService.selectGarantiaById(idSeleccionado);
        if (garantia == null) {
            return;
        }
        if (garantiaService.estaVigente(idSeleccionado)) {
            mostrarExito("La garantía sigue vigente hasta el " + Formato.fecha(garantia.getFechaVencimiento()));
        } else {
            mostrarError("La garantía ya venció el " + Formato.fecha(garantia.getFechaVencimiento()));
        }
    }

    private void eliminar() {
        boolean confirmado = confirmar("¿Eliminar garantía?",
                "Se borrará la garantía #" + idSeleccionado + " del pedido #" + txtIdPedido.getTexto() + ".", "Eliminar");
        if (confirmado) {
            garantiaService.deleteGarantiaById(idSeleccionado);
            mostrarExito("Garantía eliminada");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Garantia garantia = garantiaService.selectGarantiaById(id);
        if (garantia == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(garantia.getIdGarantia()));
        txtIdPedido.setText(String.valueOf(garantia.getPedido().getIdPedido()));
        txtFechaInicio.setText(garantia.getFechaInicio().toString());
        txtMeses.setText(String.valueOf(garantia.getMesesCobertura()));
        txtDescripcion.setText(garantia.getDescripcion());
        quitarErrores(txtId, txtIdPedido, txtFechaInicio, txtMeses, txtDescripcion);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtIdPedido.limpiar();
        txtFechaInicio.limpiar();
        txtMeses.limpiar();
        txtDescripcion.limpiar();
        txtFechaInicio.setText(LocalDate.now().toString());
        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        txtIdPedido.setBloqueado(editando);
        txtFechaInicio.setBloqueado(editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnVigencia.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Se pueden cambiar los meses de cobertura y la descripción.");
        } else {
            formulario.setModo(false, "Nueva garantía");
            formulario.setNota("La garantía vence cuando pasan los meses de cobertura desde la fecha de inicio.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Garantia garantia : garantiaService.selectAllGarantias()) {
            mayor = Math.max(mayor, garantia.getIdGarantia());
        }
        return mayor + 1;
    }

}//Fin public class
