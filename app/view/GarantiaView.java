package view;

import domain.Garantia;
import service.inputport.GarantiaServiceInterface;
import service.validations.FormTypeValidator;

import java.time.LocalDate;
import java.util.List;

public class GarantiaView {

    private final GarantiaServiceInterface garantiaService;

    public GarantiaView(GarantiaServiceInterface garantiaService) {
        this.garantiaService = garantiaService;
    }

    // HU-20
    public void createGarantia() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la garantia");

        if (garantiaService.selectGarantiaById(id) != null) {
            System.out.println("Ya existe una garantia con ese id");
            return;
        }

        int idPedido = FormTypeValidator.intValidator("Ingrese el id del pedido");
        LocalDate fechaInicio = FormTypeValidator.dateValidator("Ingrese la fecha de inicio (formato: yyyy-MM-dd)");
        int meses = FormTypeValidator.intValidator("Ingrese los meses de cobertura (1 a 36)");
        String descripcion = FormTypeValidator.stringValidator("Ingrese que cubre la garantia");

        Garantia garantia = garantiaService.createGarantia(id, idPedido, fechaInicio, meses, descripcion);

        if (garantia == null) {
            System.out.println("No se pudo registrar: revise que el pedido exista, que los meses esten entre 1 y 36 y que la fecha no sea futura");
        } else {
            System.out.println("Garantia registrada. Vence el " + garantia.getFechaVencimiento());
        }
    }

    public void selectGarantiaById() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la garantia a buscar");
        Garantia garantia = garantiaService.selectGarantiaById(id);

        if (garantia == null) {
            System.out.println("No se encontro la garantia");
        } else {
            mostrarGarantia(garantia);
        }
    }

    public void selectAllGarantias() {
        List<Garantia> garantias = garantiaService.selectAllGarantias();

        if (garantias.isEmpty()) {
            System.out.println("No hay garantias registradas");
            return;
        }

        for (Garantia garantia : garantias) {
            mostrarGarantia(garantia);
        }
    }

    public void updateGarantia() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la garantia a actualizar");

        if (garantiaService.selectGarantiaById(id) == null) {
            System.out.println("No se encontro la garantia");
            return;
        }

        int meses = FormTypeValidator.intValidator("Ingrese los nuevos meses de cobertura (1 a 36)");
        String descripcion = FormTypeValidator.stringValidator("Ingrese la nueva descripcion");

        if (garantiaService.updateGarantia(id, meses, descripcion) == null) {
            System.out.println("Los meses deben estar entre 1 y 36");
        } else {
            System.out.println("Garantia actualizada");
        }
    }

    public void deleteGarantia() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la garantia a eliminar");

        if (garantiaService.selectGarantiaById(id) == null) {
            System.out.println("No se encontro la garantia");
            return;
        }

        garantiaService.deleteGarantiaById(id);
        System.out.println("Garantia eliminada");
    }

    // HU-21: el vendedor consulta si la garantia sigue vigente para responderle al cliente
    public void consultarVigencia() {
        int id = FormTypeValidator.intValidator("Ingrese el id de la garantia a consultar");
        Garantia garantia = garantiaService.selectGarantiaById(id);

        if (garantia == null) {
            System.out.println("No se encontro la garantia");
            return;
        }

        if (garantiaService.estaVigente(id)) {
            System.out.println("La garantia SIGUE VIGENTE hasta el " + garantia.getFechaVencimiento());
        } else {
            System.out.println("La garantia YA VENCIO el " + garantia.getFechaVencimiento());
        }
    }

    private void mostrarGarantia(Garantia garantia) {
        String estado = garantiaService.estaVigente(garantia.getIdGarantia()) ? "Vigente" : "Vencida";
        System.out.println("Garantia #" + garantia.getIdGarantia()
                + " | Pedido: " + garantia.getPedido().getIdPedido()
                + " | Inicio: " + garantia.getFechaInicio()
                + " | Meses: " + garantia.getMesesCobertura()
                + " | Vence: " + garantia.getFechaVencimiento()
                + " | " + estado
                + " | " + garantia.getDescripcion());
    }

}//Fin public class
