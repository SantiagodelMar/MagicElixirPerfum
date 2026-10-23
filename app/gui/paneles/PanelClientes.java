package gui.paneles;

import domain.Cliente;
import domain.enums.TipoDocumento;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.ClienteServiceInterface;

import java.awt.BorderLayout;
import java.util.Arrays;

// Modulo de Usuarios - HU-03: registrar a los clientes con su celular y direccion de envio
public class PanelClientes extends PanelModulo {

    private final ClienteServiceInterface clienteService;

    // Campos del formulario
    private final CampoTexto txtId = new CampoTexto("Ej: 5");
    private final CampoTexto txtNombre = new CampoTexto("Nombre");
    private final CampoTexto txtApellido = new CampoTexto("Apellido");
    private final Combo<TipoDocumento> cbTipoDoc = new Combo<>(tipo -> tipo.getDescripcion());
    private final CampoTexto txtCorreo = new CampoTexto("nombre@gmail.com");
    private final CampoTexto txtCelular = new CampoTexto("3001234567");
    private final CampoTexto txtFechaNacimiento = new CampoTexto("aaaa-mm-dd");
    private final Combo<String> cbTipoCliente = new Combo<>();
    private final CampoTexto txtDireccion = new CampoTexto("Calle 10 #20-30");
    private final CampoTexto txtDireccionEnvio = new CampoTexto("Dirección de entrega");

    // Botones
    private final Boton btnGuardar = new Boton("Guardar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del cliente", 5);
    private final TarjetaTabla listado = new TarjetaTabla("Clientes registrados", "Buscar cliente...",
            new String[]{"ID", "Nombre", "Doc.", "Correo", "Celular", "Tipo", "Dirección de envío"});

    // ID del cliente que se esta editando. -1 = ninguno (se esta creando uno nuevo)
    private int idSeleccionado = -1;

    public PanelClientes(VentanaPrincipal ventana, ClienteServiceInterface clienteService) {
        super(ventana, "Usuarios  ·  HU-03", "Clientes",
                "Personas que compran por WhatsApp, Instagram, Facebook o directamente en el local.");
        this.clienteService = clienteService;

        armarFormulario();
        listado.getTabla().anchos(50, 180, 60, 210, 110, 100, 250);
        listado.getTabla().alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
        limpiarFormulario();
    }

    private void armarFormulario() {
        cbTipoDoc.cargar(Arrays.asList(TipoDocumento.values()));
        cbTipoCliente.cargar(Arrays.asList("Nuevo", "Frecuente", "Mayorista"));

        formulario.agregarCampo("ID", txtId);
        formulario.agregarCampo("Nombre", txtNombre);
        formulario.agregarCampo("Apellido", txtApellido);
        formulario.agregarCampo("Tipo de documento", cbTipoDoc);
        formulario.agregarCampo("Correo", txtCorreo);
        formulario.agregarCampo("Celular", txtCelular);
        formulario.agregarCampo("Fecha de nacimiento", txtFechaNacimiento);
        formulario.agregarCampo("Tipo de cliente", cbTipoCliente);
        formulario.agregarCampo("Dirección", txtDireccion);
        formulario.agregarCampo("Dirección de envío", txtDireccionEnvio);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
    }

    // Cada vez que se abre la pantalla se recarga la tabla y el formulario queda limpio
    @Override
    public void refrescar() {
        cargarTabla();
        limpiarFormulario();
    }

    private void cargarTabla() {
        Tabla tabla = listado.getTabla();
        tabla.limpiar();
        for (Cliente cliente : clienteService.selectAllClientes()) {
            tabla.agregarFila(
                    cliente.getIdUsuario(),
                    cliente.getNombre() + " " + cliente.getApellido(),
                    cliente.getTipoDoc().name(),
                    cliente.getCorreo(),
                    cliente.getCelular(),
                    cliente.getTipoCliente(),
                    cliente.getDireccionEnvio());
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID");
            if (clienteService.selectClienteById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un cliente con el ID " + id);
                return;
            }

            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String apellido = ValidadorCampos.texto(txtApellido, "Apellido");
            TipoDocumento tipoDoc = ValidadorCampos.seleccion(cbTipoDoc, "el tipo de documento");
            String correo = ValidadorCampos.correo(txtCorreo);
            String celular = ValidadorCampos.telefono(txtCelular, "El celular");
            String fechaNacimiento = ValidadorCampos.fecha(txtFechaNacimiento, "La fecha de nacimiento").toString();
            String tipoCliente = ValidadorCampos.seleccion(cbTipoCliente, "el tipo de cliente");
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección");
            String direccionEnvio = ValidadorCampos.texto(txtDireccionEnvio, "Dirección de envío");

            clienteService.createCliente(id, nombre, apellido, correo, tipoDoc, celular, direccion,
                    fechaNacimiento, tipoCliente, direccionEnvio);

            mostrarExito("Cliente " + nombre + " " + apellido + " registrado");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // Solo se puede cambiar el celular y las direcciones (asi esta en el service)
    private void actualizar() {
        try {
            String celular = ValidadorCampos.telefono(txtCelular, "El celular");
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección");
            String direccionEnvio = ValidadorCampos.texto(txtDireccionEnvio, "Dirección de envío");

            clienteService.updateCliente(idSeleccionado, celular, direccion, direccionEnvio);

            mostrarExito("Cliente #" + idSeleccionado + " actualizado");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        Cliente cliente = clienteService.selectClienteById(idSeleccionado);
        if (cliente == null) {
            return;
        }

        boolean confirmado = confirmar("¿Eliminar cliente?",
                "Se eliminará a " + cliente.getNombre() + " " + cliente.getApellido() + ". Esta acción no se puede deshacer.",
                "Eliminar");
        if (confirmado) {
            clienteService.deleteClienteById(idSeleccionado);
            mostrarExito("Cliente eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Cliente cliente = clienteService.selectClienteById(id);
        if (cliente == null) {
            return;
        }

        idSeleccionado = id;
        txtId.setText(String.valueOf(cliente.getIdUsuario()));
        txtNombre.setText(cliente.getNombre());
        txtApellido.setText(cliente.getApellido());
        cbTipoDoc.setSelectedItem(cliente.getTipoDoc());
        txtCorreo.setText(cliente.getCorreo());
        txtCelular.setText(cliente.getCelular());
        txtFechaNacimiento.setText(cliente.getFechaNacimiento());
        cbTipoCliente.seleccionar(cliente.getTipoCliente());
        txtDireccion.setText(cliente.getDireccion());
        txtDireccionEnvio.setText(cliente.getDireccionEnvio());

        quitarErrores(txtId, txtNombre, txtApellido, txtCorreo, txtCelular, txtFechaNacimiento, txtDireccion, txtDireccionEnvio);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtNombre.limpiar();
        txtApellido.limpiar();
        txtCorreo.limpiar();
        txtCelular.limpiar();
        txtFechaNacimiento.limpiar();
        txtDireccion.limpiar();
        txtDireccionEnvio.limpiar();
        cbTipoDoc.setSelectedIndex(0);
        cbTipoCliente.setSelectedIndex(0);

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    // En modo edicion se bloquean los datos que el service no deja cambiar
    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        txtNombre.setBloqueado(editando);
        txtApellido.setBloqueado(editando);
        cbTipoDoc.setBloqueado(editando);
        txtCorreo.setBloqueado(editando);
        txtFechaNacimiento.setBloqueado(editando);
        cbTipoCliente.setBloqueado(editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Al editar solo se puede cambiar el celular y las direcciones.");
        } else {
            formulario.setModo(false, "Nuevo registro");
            formulario.setNota("Selecciona un cliente de la tabla para editarlo.");
        }
    }

    // El siguiente ID libre (el mayor + 1) para no tener que pensarlo
    private int siguienteId() {
        int mayor = 0;
        for (Cliente cliente : clienteService.selectAllClientes()) {
            mayor = Math.max(mayor, cliente.getIdUsuario());
        }
        return mayor + 1;
    }

}//Fin public class
