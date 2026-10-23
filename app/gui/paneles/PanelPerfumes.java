package gui.paneles;

import domain.Categoria;
import domain.Inventario;
import domain.Perfume;
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
import service.inputport.CategoriaServiceInterface;
import service.inputport.InventarioServiceInterface;
import service.inputport.PerfumeServiceInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

// Modulo de Catalogo - HU-04 (registrar perfumes), HU-05 (cambiar el precio)
// y HU-06 (ver los perfumes de una categoria)
public class PanelPerfumes extends PanelModulo {

    private final PerfumeServiceInterface perfumeService;
    private final CategoriaServiceInterface categoriaService;
    private final InventarioServiceInterface inventarioService;

    // Campos del formulario
    private final CampoTexto txtId = new CampoTexto("Ej: 202");
    private final CampoTexto txtNombre = new CampoTexto("Ej: Khamrah");
    private final CampoTexto txtMarca = new CampoTexto("Ej: Lattafa");
    private final Combo<Categoria> cbCategoria = new Combo<>(categoria -> categoria.getNombre());
    private final CampoTexto txtPrecio = new CampoTexto("Ej: 250000");
    private final CampoTexto txtMililitros = new CampoTexto("Ej: 100");
    private final CampoTexto txtNotas = new CampoTexto("Ej: Canela, Nuez moscada, Praliné");

    // Filtro de la tabla por categoria (la categoria con id 0 significa "todas")
    private final Combo<Categoria> cbFiltro = new Combo<>(categoria -> categoria.getNombre());

    private final Boton btnGuardar = new Boton("Guardar", Boton.PRINCIPAL);
    private final Boton btnActualizar = new Boton("Actualizar", Boton.SECUNDARIO);
    private final Boton btnEliminar = new Boton("Eliminar", Boton.PELIGRO);
    private final Boton btnLimpiar = new Boton("Limpiar", Boton.SECUNDARIO);

    private final TarjetaFormulario formulario = new TarjetaFormulario("Datos del perfume", 4);
    private final TarjetaTabla listado = new TarjetaTabla("Catálogo", "Buscar perfume o marca...",
            new String[]{"ID", "Nombre", "Marca", "Categoría", "ml", "Precio", "Notas olfativas"});

    private int idSeleccionado = -1;
    // Mientras se llenan los combos no se recarga la tabla con cada opcion
    private boolean cargandoCategorias = false;

    public PanelPerfumes(VentanaPrincipal ventana, PerfumeServiceInterface perfumeService,
                         CategoriaServiceInterface categoriaService, InventarioServiceInterface inventarioService) {
        super(ventana, "Catálogo  ·  HU-04, HU-05 y HU-06", "Perfumes",
                "El catálogo completo con precio, mililitros y notas olfativas de cada fragancia.");
        this.perfumeService = perfumeService;
        this.categoriaService = categoriaService;
        this.inventarioService = inventarioService;

        formulario.agregarCampo("ID", txtId);
        formulario.agregarCampo("Nombre", txtNombre);
        formulario.agregarCampo("Marca", txtMarca);
        formulario.agregarCampo("Categoría", cbCategoria);
        formulario.agregarCampo("Precio", txtPrecio);
        formulario.agregarCampo("Mililitros", txtMililitros);
        formulario.agregarCampo("Notas olfativas", txtNotas, 2);

        formulario.agregarBoton(btnLimpiar);
        formulario.agregarBoton(btnEliminar);
        formulario.agregarBoton(btnActualizar);
        formulario.agregarBoton(btnGuardar);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // HU-06: filtro por categoria al lado del buscador
        cbFiltro.setPreferredSize(new Dimension(210, 36));
        cbFiltro.addActionListener(e -> {
            if (!cargandoCategorias) {
                cargarTabla();
            }
        });
        listado.agregarAlEncabezado(cbFiltro);

        Tabla tabla = listado.getTabla();
        tabla.anchos(50, 210, 150, 160, 60, 110, 300);
        tabla.alSeleccionarFila(id -> cargarEnFormulario(id));

        add(unirFormularioYTabla(formulario, listado), BorderLayout.CENTER);
        cargarCategorias();
        cargarTabla();
        limpiarFormulario();
    }

    @Override
    public void refrescar() {
        cargarCategorias();
        cargarTabla();
        limpiarFormulario();
    }

    // Llena el combo del formulario y el del filtro (por si crearon categorias nuevas)
    private void cargarCategorias() {
        cargandoCategorias = true;
        List<Categoria> categorias = categoriaService.selectAllCategorias();
        Categoria filtroActual = cbFiltro.getSeleccionado();

        cbCategoria.cargar(categorias);

        cbFiltro.removeAllItems();
        Categoria todas = new Categoria(0, "Todas las categorías", "");
        cbFiltro.addItem(todas);
        for (Categoria categoria : categorias) {
            cbFiltro.addItem(categoria);
        }
        // Se deja el filtro que estaba escogido
        if (filtroActual != null && filtroActual.getIdCategoria() != 0) {
            cbFiltro.setSelectedItem(filtroActual);
        }
        cargandoCategorias = false;
    }

    private void cargarTabla() {
        Categoria filtro = cbFiltro.getSeleccionado();
        List<Perfume> perfumes;
        if (filtro == null || filtro.getIdCategoria() == 0) {
            perfumes = perfumeService.selectAllPerfumes();
        } else {
            perfumes = perfumeService.selectPerfumesByCategoria(filtro.getIdCategoria());
        }

        Tabla tabla = listado.getTabla();
        tabla.limpiar();
        for (Perfume perfume : perfumes) {
            tabla.agregarFila(
                    perfume.getIdPerfume(),
                    perfume.getNombre(),
                    perfume.getMarca(),
                    perfume.getCategoria().getNombre(),
                    perfume.getMililitros(),
                    perfume.getPrecio(),
                    perfume.getNotasOlfativas());
        }
        listado.actualizarConteo();
    }

    private void guardar() {
        try {
            int id = ValidadorCampos.entero(txtId, "ID");
            if (perfumeService.selectPerfumeById(id) != null) {
                txtId.marcarError();
                mostrarError("Ya existe un perfume con el ID " + id);
                return;
            }
            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String marca = ValidadorCampos.texto(txtMarca, "Marca");
            Categoria categoria = ValidadorCampos.seleccion(cbCategoria, "la categoría");
            double precio = ValidadorCampos.dinero(txtPrecio, "El precio");
            int mililitros = ValidadorCampos.entero(txtMililitros, "Mililitros");
            String notas = ValidadorCampos.texto(txtNotas, "Notas olfativas");

            perfumeService.createPerfume(id, nombre, marca, precio, mililitros, notas, categoria.getIdCategoria());

            mostrarExito(nombre + " agregado al catálogo. Recuerda registrarle inventario");
            cargarTabla();
            limpiarFormulario();
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void actualizar() {
        try {
            String nombre = ValidadorCampos.texto(txtNombre, "Nombre");
            String marca = ValidadorCampos.texto(txtMarca, "Marca");
            Categoria categoria = ValidadorCampos.seleccion(cbCategoria, "la categoría");
            double precio = ValidadorCampos.dinero(txtPrecio, "El precio");
            int mililitros = ValidadorCampos.entero(txtMililitros, "Mililitros");
            String notas = ValidadorCampos.texto(txtNotas, "Notas olfativas");

            Perfume perfume = perfumeService.selectPerfumeById(idSeleccionado);
            double precioAnterior = perfume.getPrecio();

            perfumeService.updatePerfume(idSeleccionado, nombre, marca, mililitros, notas, categoria.getIdCategoria());

            // HU-05: el precio tiene su propio metodo en el service
            if (precio != precioAnterior) {
                perfumeService.actualizarPrecio(idSeleccionado, precio);
                mostrarExito("Precio actualizado: " + Formato.dinero(precioAnterior) + " → " + Formato.dinero(precio));
            } else {
                mostrarExito(nombre + " actualizado");
            }

            int id = idSeleccionado;
            cargarTabla();
            listado.getTabla().seleccionarId(id);
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminar() {
        Perfume perfume = perfumeService.selectPerfumeById(idSeleccionado);
        if (perfume == null) {
            return;
        }

        boolean confirmado = confirmar("¿Eliminar perfume?",
                "Se quitará " + perfume.getNombre() + " de " + perfume.getMarca()
                        + " del catálogo junto con su inventario.", "Eliminar");
        if (confirmado) {
            // Tambien se borra su inventario para que no quede un registro sin perfume
            Inventario inventario = inventarioService.selectInventarioByPerfume(idSeleccionado);
            if (inventario != null) {
                inventarioService.deleteInventarioById(inventario.getIdInventario());
            }
            perfumeService.deletePerfumeById(idSeleccionado);

            mostrarExito("Perfume eliminado del catálogo");
            cargarTabla();
            limpiarFormulario();
        }
    }

    private void cargarEnFormulario(int id) {
        Perfume perfume = perfumeService.selectPerfumeById(id);
        if (perfume == null) {
            return;
        }
        idSeleccionado = id;
        txtId.setText(String.valueOf(perfume.getIdPerfume()));
        txtNombre.setText(perfume.getNombre());
        txtMarca.setText(perfume.getMarca());
        cbCategoria.setSelectedItem(perfume.getCategoria());
        // Sin decimales para que se lea facil (250000 y no 250000.0)
        txtPrecio.setText(String.valueOf((long) perfume.getPrecio()));
        txtMililitros.setText(String.valueOf(perfume.getMililitros()));
        txtNotas.setText(perfume.getNotasOlfativas());

        quitarErrores(txtId, txtNombre, txtMarca, txtPrecio, txtMililitros, txtNotas);
        modoEdicion(true);
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        txtId.limpiar();
        txtNombre.limpiar();
        txtMarca.limpiar();
        txtPrecio.limpiar();
        txtMililitros.limpiar();
        txtNotas.limpiar();
        if (cbCategoria.getItemCount() > 0) {
            cbCategoria.setSelectedIndex(0);
        }

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
            formulario.setNota("Si cambias el precio queda registrado como actualización de precio (HU-05).");
        } else {
            formulario.setModo(false, "Nuevo perfume");
            formulario.setNota("Selecciona un perfume de la tabla para editarlo.");
        }
    }

    private int siguienteId() {
        int mayor = 0;
        for (Perfume perfume : perfumeService.selectAllPerfumes()) {
            mayor = Math.max(mayor, perfume.getIdPerfume());
        }
        return mayor + 1;
    }

}//Fin public class
