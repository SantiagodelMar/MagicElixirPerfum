package gui.paneles;

import domain.Categoria;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Tabla;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.CategoriaServiceInterface;
import service.inputport.PerfumeServiceInterface;

import java.awt.BorderLayout;

// Modulo de Catalogo - HU-06: las categorias del catalogo (las mismas de Instagram)
public class PanelCategorias extends PanelModulo {

    private final CategoriaServiceInterface categoriaService;
    private final PerfumeServiceInterface perfumeService;

    private final CampoTexto txtId = new CampoTexto("Ej: 5");
    private final CampoTexto txtNombre = new CampoTexto("Ej: Unisex");
    private final CampoTexto txtDescripcion = new CampoTexto("Descripción corta de la categoría");

    private final Boton btnGuardar = new Boton("Guardar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos de la categoría", 4);
    private final TarjetaTabla listado = new TarjetaTabla("Categorías", "Buscar categoría...",
            new String[]{"ID", "Nombre", "Descripción", "Perfumes"});

    private int idSeleccionado = -1;

    public PanelCategorias(VentanaPrincipal ventana, CategoriaServiceInterface categoriaService,
                           PerfumeServiceInterface perfumeService) {
        super(ventana, "Catálogo  ·  HU-06", "Categorías",
                "Así se organiza el catálogo: perfumería femenina, masculina y árabe.");
        this.categoriaService = categoriaService;
        this.perfumeService = perfumeService;

        formulario.agregarCampo("ID", txtId);
        formulario.agregarCampo("Nombre", txtNombre);
        formulario.agregarCampo("Descripción", txtDescripcion, 2);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        listado.getTabla().anchos(50, 220, 380, 100);
        listado.getTabla().alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
        limpiarFormulario();
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
        for (Categoria categoria : categoriaService.selectAllCategorias()) {
            tabla.agregarFila(
                    categoria.getIdCategoria(),
                    categoria.getNombre(),
                    categoria.getDescripcion(),
                    cantidadPerfumes(categoria.getIdCategoria()));
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID");
            if (categoriaService.selectCategoriaById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe una categoría con el ID " + id);
                return;
            }
            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String descripcion = ValidadorCampos.texto(txtDescripcion, "Descripción");

            categoriaService.createCategoria(id, nombre, descripcion);

            mostrarExito("Categoría " + nombre + " creada");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String descripcion = ValidadorCampos.texto(txtDescripcion, "Descripción");

            categoriaService.updateCategoria(idSeleccionado, nombre, descripcion);

            mostrarExito("Categoría actualizada");
            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        // Si tiene perfumes no se deja borrar, porque los perfumes quedarian sin categoria
        int perfumes = cantidadPerfumes(idSeleccionado);
        if (perfumes > 0) {
            mostrarError("No se puede eliminar: la categoría tiene " + perfumes + " perfumes");
            return;
        }

        boolean confirmado = confirmar("¿Eliminar categoría?",
                "Se eliminará la categoría " + txtNombre.getTexto() + ".", "Eliminar");
        if (confirmado) {
            categoriaService.deleteCategoriaById(idSeleccionado);
            mostrarExito("Categoría eliminada");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Categoria categoria = categoriaService.selectCategoriaById(id);
        if (categoria == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(categoria.getIdCategoria()));
        txtNombre.setText(categoria.getNombre());
        txtDescripcion.setText(categoria.getDescripcion());
        quitarErrores(txtId, txtNombre, txtDescripcion);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtNombre.limpiar();
        txtDescripcion.limpiar();
        txtId.setText(String.valueOf(siguienteId()));
        listado.getTabla().clearSelection();
        modoEdicion(false);
    }

    private void modoEdicion(boolean editando) {
        txtId.setBloqueado(editando);
        btnGuardar.setEnabled(!editando);
        btnActualizar.setEnabled(editando);
        btnEliminar.setEnabled(editando);

        if (editando) {
            formulario.setModo(true, "Editando #" + idSeleccionado);
            formulario.setNota("Solo se pueden eliminar categorías sin perfumes.");
        } else {
            formulario.setModo(false, "Nueva categoría");
            formulario.setNota("Selecciona una categoría de la tabla para editarla.");
        }
    }

    private int cantidadPerfumes(int idCategoria) {
        return perfumeService.selectPerfumesByCategoria(idCategoria).size();
    }

    private int siguienteId() {
        int mayor = 0;
        for (Categoria categoria : categoriaService.selectAllCategorias()) {
            mayor = Math.max(mayor, categoria.getIdCategoria());
        }
        return mayor + 1;
    }

}//Fin public class
