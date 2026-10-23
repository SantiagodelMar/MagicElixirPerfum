package gui.paneles;

import domain.Administrador;
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
import service.inputport.AdministradorServiceInterface;

import java.awt.BorderLayout;
import java.util.Arrays;

// Modulo de Usuarios - administradores del sistema con sus permisos y nivel
public class PanelAdministradores extends PanelModulo {

    private final AdministradorServiceInterface administradorService;

    // Campos del formulario
    private final CampoTexto txtId = new CampoTexto("Ej: 2");
    private final CampoTexto txtNombre = new CampoTexto("Nombre");
    private final CampoTexto txtApellido = new CampoTexto("Apellido");
    private final Combo<TipoDocumento> cbTipoDoc = new Combo<>(tipo -> tipo.getDescripcion());
    private final CampoTexto txtCorreo = new CampoTexto("nombre@magicelixir.com");
    private final CampoTexto txtCelular = new CampoTexto("3001234567");
    private final CampoTexto txtDireccion = new CampoTexto("Barrio o dirección");
    private final CampoTexto txtFechaNacimiento = new CampoTexto("aaaa-mm-dd");
    private final Combo<String> cbPermisos = new Combo<>();
    private final Combo<String> cbNivel = new Combo<>(nivel -> "Nivel " + nivel);

    // Botones
    private final Boton btnGuardar = new Boton("Guardar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del administrador", 5);
    private final TarjetaTabla listado = new TarjetaTabla("Administradores", "Buscar administrador...",
            new String[]{"ID", "Nombre", "Correo", "Celular", "Permisos", "Nivel"});

    private int idSeleccionado = -1;

    public PanelAdministradores(VentanaPrincipal ventana, AdministradorServiceInterface administradorService) {
        super(ventana, "Usuarios", "Administradores",
                "Personas que administran la perfumería y el nivel de acceso que tienen.");
        this.administradorService = administradorService;

        armarFormulario();
        listado.getTabla().anchos(50, 200, 240, 130, 130, 90);
        listado.getTabla().alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
        limpiarFormulario();
    }

    private void armarFormulario() {
        cbTipoDoc.cargar(Arrays.asList(TipoDocumento.values()));
        cbPermisos.cargar(Arrays.asList("Total", "Ventas", "Inventario", "Solo lectura"));
        cbNivel.cargar(Arrays.asList("1", "2", "3"));

        formulario.agregarCampo("ID", txtId);
        formulario.agregarCampo("Nombre", txtNombre);
        formulario.agregarCampo("Apellido", txtApellido);
        formulario.agregarCampo("Tipo de documento", cbTipoDoc);
        formulario.agregarCampo("Correo", txtCorreo);
        formulario.agregarCampo("Celular", txtCelular);
        formulario.agregarCampo("Dirección", txtDireccion);
        formulario.agregarCampo("Fecha de nacimiento", txtFechaNacimiento);
        formulario.agregarCampo("Permisos", cbPermisos);
        formulario.agregarCampo("Nivel", cbNivel);

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
        for (Administrador administrador : administradorService.selectAllAdministradores()) {
            tabla.agregarFila(
                    administrador.getIdUsuario(),
                    administrador.getNombre() + " " + administrador.getApellido(),
                    administrador.getCorreo(),
                    administrador.getCelular(),
                    administrador.getPermissions(),
                    "Nivel " + administrador.getLevel());
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID");
            if (administradorService.selectAdministradorById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un administrador con el ID " + id);
                return;
            }

            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String apellido = ValidadorCampos.texto(txtApellido, "Apellido");
            TipoDocumento tipoDoc = ValidadorCampos.seleccion(cbTipoDoc, "el tipo de documento");
            String correo = ValidadorCampos.correo(txtCorreo);
            String celular = ValidadorCampos.telefono(txtCelular, "El celular");
            String direccion = ValidadorCampos.texto(txtDireccion, "Dirección");
            String fechaNacimiento = ValidadorCampos.fecha(txtFechaNacimiento, "La fecha de nacimiento").toString();
            String permisos = ValidadorCampos.seleccion(cbPermisos, "los permisos");
            String nivel = ValidadorCampos.seleccion(cbNivel, "el nivel");

            administradorService.createAdministrador(id, nombre, apellido, correo, tipoDoc, celular, direccion,
                    fechaNacimiento, permisos, nivel);

            mostrarExito("Administrador " + nombre + " registrado");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    // Solo se pueden cambiar los permisos y el nivel
    private void actualizar() {
        try {
            String permisos = ValidadorCampos.seleccion(cbPermisos, "los permisos");
            String nivel = ValidadorCampos.seleccion(cbNivel, "el nivel");

            administradorService.updateAdministrador(idSeleccionado, permisos, nivel);

            mostrarExito("Administrador #" + idSeleccionado + " actualizado");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        Administrador administrador = administradorService.selectAdministradorById(idSeleccionado);
        if (administrador == null) {
            return;
        }

        boolean confirmado = confirmar("¿Eliminar administrador?",
                "Se eliminará a " + administrador.getNombre() + " " + administrador.getApellido() + ".",
                "Eliminar");
        if (confirmado) {
            administradorService.deleteAdministradorById(idSeleccionado);
            mostrarExito("Administrador eliminado");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Administrador administrador = administradorService.selectAdministradorById(id);
        if (administrador == null) {
            return;
        }

        idSeleccionado = id;
        txtId.setText(String.valueOf(administrador.getIdUsuario()));
        txtNombre.setText(administrador.getNombre());
        txtApellido.setText(administrador.getApellido());
        cbTipoDoc.setSelectedItem(administrador.getTipoDoc());
        txtCorreo.setText(administrador.getCorreo());
        txtCelular.setText(administrador.getCelular());
        txtDireccion.setText(administrador.getDireccion());
        txtFechaNacimiento.setText(administrador.getFechaNacimiento());
        cbPermisos.seleccionar(administrador.getPermissions());
        cbNivel.seleccionar(administrador.getLevel());

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
        cbTipoDoc.setSelectedIndex(0);
        cbPermisos.setSelectedIndex(0);
        cbNivel.setSelectedIndex(0);

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
        txtCelular.setBloqueado(editando);
        txtDireccion.setBloqueado(editando);
        txtFechaNacimiento.setBloqueado(editando);

        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Al editar solo se pueden cambiar los permisos y el nivel.");
        } else {
            formulario.setModo(false, "Nuevo registro");
            formulario.setNota("Selecciona un administrador de la tabla para editarlo.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Administrador administrador : administradorService.selectAllAdministradores()) {
            mayor = Math.max(mayor, administrador.getIdUsuario());
        }
        return mayor + 1;
    }

}//Fin public class
