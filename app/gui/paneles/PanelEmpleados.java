package gui.paneles;

import domain.Empleado;
import domain.enums.TipoDocumento;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoClave;
import gui.componentes.CampoTexto;
import gui.componentes.Combo;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.EmpleadoServiceInterface;

import java.awt.BorderLayout;
import java.util.Arrays;

// Modulo de Usuarios - HU-01 (iniciar sesion) y HU-02 (activar / desactivar cuentas)
public class PanelEmpleados extends PanelModulo {

    private final EmpleadoServiceInterface empleadoService;

    // Campos del formulario
    private final CampoTexto txtId = new CampoTexto("Ej: 3");
    private final CampoTexto txtNombre = new CampoTexto("Nombre");
    private final CampoTexto txtApellido = new CampoTexto("Apellido");
    private final Combo<TipoDocumento> cbTipoDoc = new Combo<>(tipo -> tipo.getDescripcion());
    private final CampoTexto txtCorreo = new CampoTexto("nombre@magicelixir.com");
    private final CampoTexto txtCelular = new CampoTexto("3001234567");
    private final CampoTexto txtDireccion = new CampoTexto("Barrio o dirección");
    private final CampoTexto txtFechaNacimiento = new CampoTexto("aaaa-mm-dd");
    private final Combo<String> cbRol = new Combo<>();
    private final CampoClave txtClave = new CampoClave("Mínimo 4 caracteres");

    // Botones
    private final Boton btnGuardar = new Boton("Guardar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEstado = new Boton("Desactivar", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del empleado", 5);
    private final TarjetaTabla listado = new TarjetaTabla("Empleados", "Buscar empleado...",
            new String[]{"ID", "Nombre", "Correo", "Rol", "Celular", "Estado"});

    private int idSeleccionado = -1;

    public PanelEmpleados(VentanaPrincipal ventana, EmpleadoServiceInterface empleadoService) {
        super(ventana, "Usuarios  ·  HU-01 y HU-02", "Empleados",
                "Cuentas del personal que usa el sistema. Una cuenta desactivada no puede iniciar sesión.");
        this.empleadoService = empleadoService;

        armarFormulario();
        Tabla tabla = listado.getTabla();
        tabla.anchos(50, 190, 230, 120, 120, 110);
        tabla.columnaEstado(5);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
        limpiarFormulario();
    }

    private void armarFormulario() {
        cbTipoDoc.cargar(Arrays.asList(TipoDocumento.values()));
        cbRol.cargar(Arrays.asList("Vendedor", "Cajero", "Bodega", "Logística"));

        formulario.agregarCampo("ID", txtId);
        formulario.agregarCampo("Nombre", txtNombre);
        formulario.agregarCampo("Apellido", txtApellido);
        formulario.agregarCampo("Tipo de documento", cbTipoDoc);
        formulario.agregarCampo("Correo (usuario)", txtCorreo);
        formulario.agregarCampo("Celular", txtCelular);
        formulario.agregarCampo("Dirección", txtDireccion);
        formulario.agregarCampo("Fecha de nacimiento", txtFechaNacimiento);
        formulario.agregarCampo("Rol", cbRol);
        formulario.agregarCampo("Contraseña", txtClave);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnEstado);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEstado.addActionListener(e -> cambiarEstado());
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
        for (Empleado empleado : empleadoService.selectAllEmpleados()) {
            tabla.agregarFila(
                    empleado.getIdUsuario(),
                    empleado.getNombre() + " " + empleado.getApellido(),
                    empleado.getCorreo(),
                    empleado.getRole(),
                    empleado.getCelular(),
                    empleado.isActivo() ? "Activo" : "Inactivo");
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID");
            if (empleadoService.selectEmpleadoById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un empleado con el ID " + id);
                return;
            }

            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String apellido = ValidadorCampos.texto(txtApellido, "Apellido");
            TipoDocumento tipoDoc = ValidadorCampos.seleccion(cbTipoDoc, "el tipo de documento");
            String correo = ValidadorCampos.correo(txtCorreo);

            // El correo es el usuario para iniciar sesion, no se puede repetir
            if (correoEnUso(correo)) {
                txtCorreo.marcarError();
                mostrarError("Ese correo ya lo usa otro empleado");
                return;
            }

            String celular = ValidadorCampos.telefono(txtCelular, "El celular");
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección");
            String fechaNacimiento = ValidadorCampos.fecha(txtFechaNacimiento, "La fecha de nacimiento").toString();
            String rol = ValidadorCampos.seleccion(cbRol, "el rol");
            String clave = ValidadorCampos.clave(txtClave);

            empleadoService.createEmpleado(id, nombre, apellido, correo, tipoDoc, celular, direccion,
                    fechaNacimiento, rol, clave);

            mostrarExito("Empleado " + nombre + " registrado. Ya puede iniciar sesión");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // El service solo deja cambiar el celular y el rol
    private void actualizar() {
        try {
            String celular = ValidadorCampos.telefono(txtCelular, "El celular");
            String rol = ValidadorCampos.seleccion(cbRol, "el rol");

            empleadoService.updateEmpleado(idSeleccionado, celular, rol);

            mostrarExito("Empleado #" + idSeleccionado + " actualizado");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // HU-02: activar o desactivar la cuenta
    private void cambiarEstado() {
        if (esElUsuarioActual()) {
            mostrarError("No puedes desactivar tu propia cuenta mientras la estás usando");
            return;
        }

        Empleado empleado = empleadoService.cambiarEstadoEmpleado(idSeleccionado);
        if (empleado != null) {
            mostrarExito("La cuenta de " + empleado.getNombre() + " quedó " + (empleado.isActivo() ? "activa" : "desactivada"));
            cargarTabla();
            listado.getTabla().seleccionarId(empleado.getIdUsuario());
        }
    }

    private void eliminar() {
        if (esElUsuarioActual()) {
            mostrarError("No puedes eliminar tu propia cuenta mientras la estás usando");
            return;
        }

        Empleado empleado = empleadoService.selectEmpleadoById(idSeleccionado);
        if (empleado == null) {
            return;
        }

        boolean confirmado = confirmar("¿Eliminar empleado?",
                "Se eliminará la cuenta de " + empleado.getNombre() + " " + empleado.getApellido()
                        + ". Si solo quieres quitarle el acceso es mejor desactivarla.",
                "Eliminar");
        if (confirmado) {
            empleadoService.deleteEmpleadoById(idSeleccionado);
            mostrarExito("Empleado eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Empleado empleado = empleadoService.selectEmpleadoById(id);
        if (empleado == null) {
            return;
        }

        idSeleccionado = id;
        txtId.setText(String.valueOf(empleado.getIdUsuario()));
        txtNombre.setText(empleado.getNombre());
        txtApellido.setText(empleado.getApellido());
        cbTipoDoc.setSelectedItem(empleado.getTipoDoc());
        txtCorreo.setText(empleado.getCorreo());
        txtCelular.setText(empleado.getCelular());
        txtDireccion.setText(empleado.getDireccion());
        txtFechaNacimiento.setText(empleado.getFechaNacimiento());
        cbRol.seleccionar(empleado.getRole());
        // La contraseña no se muestra por seguridad
        txtClave.setText("");

        btnEstado.setText(empleado.isActivo() ? "Desactivar" : "Activar");
        quitarErrores(txtId, txtNombre, txtApellido, txtCorreo, txtCelular, txtDireccion, txtFechaNacimiento);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtNombre.limpiar();
        txtApellido.limpiar();
        txtCorreo.limpiar();
        txtCelular.limpiar();
        txtDireccion.limpiar();
        txtFechaNacimiento.limpiar();
        txtClave.limpiar();
        cbTipoDoc.setSelectedIndex(0);
        cbRol.setSelectedIndex(0);
        btnEstado.setText("Desactivar");

        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        txtNombre.setBloqueado(editando);
        txtApellido.setBloqueado(editando);
        cbTipoDoc.setBloqueado(editando);
        txtCorreo.setBloqueado(editando);
        txtDireccion.setBloqueado(editando);
        txtFechaNacimiento.setBloqueado(editando);
        txtClave.setBloqueado(editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEstado.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Al editar solo se puede cambiar el celular y el rol.");
        } else {
            formulario.setModo(false, "Nuevo registro");
            formulario.setNota("Selecciona un empleado para editarlo o desactivarlo.");
        }
    }

    private boolean esElUsuarioActual() {
        return ventana.getEmpleadoActivo().getIdUsuario() == idSeleccionado;
    }

    private boolean correoEnUso(String correo) {
        for (Empleado empleado : empleadoService.selectAllEmpleados()) {
            if (empleado.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    private int siguienteId() {
        int mayor = 0;
        for (Empleado empleado : empleadoService.selectAllEmpleados()) {
            mayor = Math.max(mayor, empleado.getIdUsuario());
        }
        return mayor + 1;
    }

}//Fin public class
