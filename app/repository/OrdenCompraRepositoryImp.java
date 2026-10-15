package repository;

import domain.OrdenCompra;
import service.outputport.OrdenCompraRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class OrdenCompraRepositoryImp implements OrdenCompraRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<OrdenCompra> ordenesCompra = new ArrayList<>();

    @Override
    public OrdenCompra createOrdenCompra(OrdenCompra ordenCompra) {
        ordenesCompra.add(ordenCompra);
        return ordenCompra;
    }

    @Override
    public OrdenCompra selectOrdenCompraById(int id) {
        for (int i = 0; i < ordenesCompra.size(); i++) {
            if (ordenesCompra.get(i).getIdOrdenCompra() == id) {
                return ordenesCompra.get(i);
            }
        }
        return null;
    }

    @Override
    public List<OrdenCompra> selectAllOrdenesCompra() {
        return ordenesCompra;
    }

    @Override
    public OrdenCompra updateOrdenCompra(OrdenCompra ordenCompra) {
        for (int i = 0; i < ordenesCompra.size(); i++) {
            if (ordenesCompra.get(i).getIdOrdenCompra() == ordenCompra.getIdOrdenCompra()) {
                ordenesCompra.set(i, ordenCompra);
                return ordenCompra;
            }
        }
        return null;
    }

    @Override
    public void deleteOrdenCompraById(int id) {
        for (int i = 0; i < ordenesCompra.size(); i++) {
            if (ordenesCompra.get(i).getIdOrdenCompra() == id) {
                ordenesCompra.remove(i);
                return;
            }
        }
    }

}