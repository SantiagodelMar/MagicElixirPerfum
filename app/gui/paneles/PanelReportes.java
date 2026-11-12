package gui.paneles;

import domain.ItemReporte;
import gui.VentanaPrincipal;
import gui.componentes.Boton;
import gui.componentes.CampoTexto;
import gui.componentes.Tabla;
import gui.componentes.TarjetaDato;
import gui.componentes.TarjetaFormulario;
import gui.componentes.TarjetaTabla;
import gui.estilo.Formato;
import gui.validaciones.DatoInvalidoException;
import gui.validaciones.ValidadorCampos;
import service.inputport.ReporteServiceInterface;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

// Modulo de Reportes e Indicadores (solo lectura)
// HU-22: total vendido en un rango de fechas   HU-23: perfumes mas vendidos   HU-24: ventas por canal
public class PanelReportes extends PanelModulo {

    private static final int TOP_PERFUMES = 10;

    private final ReporteServiceInterface reporteService;

    private final CampoTexto txtDesde = new CampoTexto("aaaa-mm-dd");
    private final CampoTexto txtHasta = new CampoTexto("aaaa-mm-dd");
    private final Boton btnConsultar = new Boton("Consultar", Boton.PRINCIPAL);
    private final Boton btnEsteMes = new Boton("Este mes", Boton.SECUNDARIO);

    private final TarjetaDato tarjetaTotal = new TarjetaDato("Total vendido");
    private final TarjetaDato tarjetaPedidos = new TarjetaDato("Pedidos vendidos");

    private final TarjetaFormulario formulario = new TarjetaFormulario("Rango de fechas", 4);
    private final TarjetaTabla listadoPerfumes = new TarjetaTabla("Perfumes más vendidos (top " + TOP_PERFUMES + ")",
            "Buscar perfume...", new String[]{"#", "Perfume", "Unidades", "Vendido"});
    private final TarjetaTabla listadoCanales = new TarjetaTabla("Ventas por canal", "Buscar canal...",
            new String[]{"#", "Canal", "Pedidos", "Vendido"});

    public PanelReportes(VentanaPrincipal ventana, ReporteServiceInterface reporteService) {
        super(ventana, "Reportes e indicadores  ·  HU-22, HU-23 y HU-24", "Reportes",
                "Solo cuentan los pedidos pagados, enviados o entregados. Los pendientes y cancelados no suman.");
        this.reporteService = reporteService;

        formulario.agregarCampo("Desde", txtDesde);
        formulario.agregarCampo("Hasta", txtHasta);
        formulario.agregarBoton(btnEsteMes);
        formulario.agregarBoton(btnConsultar);
        formulario.setModo(false, "Consulta");
        formulario.setNota("Escribe las fechas y pulsa Consultar.");

        btnConsultar.addActionListener(e -> consultar());
        btnEsteMes.addActionListener(e -> {
            LocalDate hoy = LocalDate.now();
            txtDesde.setText(hoy.withDayOfMonth(1).toString());
            txtHasta.setText(hoy.toString());
            consultar();
        });

        listadoPerfumes.getTabla().anchos(45, 260, 90, 120);
        listadoCanales.getTabla().anchos(45, 200, 90, 120);

        JPanel filaDatos = new JPanel(new GridLayout(1, 2, 16, 0));
        filaDatos.setOpaque(false);
        filaDatos.add(tarjetaTotal);
        filaDatos.add(tarjetaPedidos);

        JPanel filaTablas = new JPanel(new GridLayout(1, 2, 16, 0));
        filaTablas.setOpaque(false);
        filaTablas.add(listadoPerfumes);
        filaTablas.add(listadoCanales);

        JPanel arriba = new JPanel(new BorderLayout(0, 16));
        arriba.setOpaque(false);
        arriba.add(formulario, BorderLayout.NORTH);
        arriba.add(filaDatos, BorderLayout.CENTER);

        JPanel cuerpo = new JPanel(new BorderLayout(0, 16));
        cuerpo.setOpaque(false);
        cuerpo.add(arriba, BorderLayout.NORTH);
        cuerpo.add(filaTablas, BorderLayout.CENTER);
        add(cuerpo, BorderLayout.CENTER);
    }

    // Cada vez que se abre la pantalla se muestra el mes actual
    @Override
    public void refrescar() {
        LocalDate hoy = LocalDate.now();
        txtDesde.setText(hoy.withDayOfMonth(1).toString());
        txtHasta.setText(hoy.toString());
        quitarErrores(txtDesde, txtHasta);
        consultar();
    }

    private void consultar() {
        try {
            LocalDate desde = ValidadorCampos.fecha(txtDesde, "La fecha inicial");
            LocalDate hasta = ValidadorCampos.fecha(txtHasta, "La fecha final");
            if (hasta.isBefore(desde)) {
                txtHasta.marcarError();
                mostrarError("La fecha final no puede ser antes de la inicial");
                return;
            }
            txtDesde.limpiarError();
            txtHasta.limpiarError();

            // HU-22
            double total = reporteService.totalVendido(desde, hasta);
            List<ItemReporte> canales = reporteService.ventasPorCanal(desde, hasta);
            int pedidos = 0;
            for (ItemReporte canal : canales) {
                pedidos += canal.getCantidad();
            }
            tarjetaTotal.setDatos(Formato.dinero(total), Formato.fecha(desde) + " al " + Formato.fecha(hasta));
            tarjetaPedidos.setDatos(String.valueOf(pedidos), "pedidos pagados en el rango");

            // HU-23
            llenar(listadoPerfumes.getTabla(), reporteService.perfumesMasVendidos(desde, hasta, TOP_PERFUMES));
            listadoPerfumes.actualizarConteo();

            // HU-24
            llenar(listadoCanales.getTabla(), canales);
            listadoCanales.actualizarConteo();

            formulario.setNota("Mostrando del " + Formato.fecha(desde) + " al " + Formato.fecha(hasta) + ".");
        } catch (DatoInvalidoException e) {
            mostrarError(e.getMessage());
        }
    }

    private void llenar(Tabla tabla, List<ItemReporte> items) {
        tabla.limpiar();
        int puesto = 1;
        for (ItemReporte item : items) {
            tabla.agregarFila(puesto, item.getNombre(), item.getCantidad(), item.getTotal());
            puesto++;
        }
    }

}//Fin public class
