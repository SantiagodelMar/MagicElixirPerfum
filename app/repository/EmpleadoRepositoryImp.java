package repository;

import domain.Empleado;
import service.outputport.EmpleadoRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoRepositoryImp implements EmpleadoRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Empleado> empleados = new ArrayList<>();

    @Override
    public Empleado createEmpleado(Empleado empleado) {
        empleados.add(empleado);
        return empleado;
    }

    @Override
    public Empleado selectEmpleadoById(int id) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getIdUsuario() == id) {
                return empleados.get(i);
            }
        }
        return null;
    }

    @Override
    public Empleado selectEmpleadoByCorreo(String correo) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getCorreo().equalsIgnoreCase(correo)) {
                return empleados.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Empleado> selectAllEmpleados() {
        return empleados;
    }

    @Override
    public Empleado updateEmpleado(Empleado empleado) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getIdUsuario() == empleado.getIdUsuario()) {
                empleados.set(i, empleado);
                return empleado;
            }
        }
        return null;
    }

    @Override
    public void deleteEmpleadoById(int id) {
        for (int i = 0; i < empleados.size(); i++) {
            if (empleados.get(i).getIdUsuario() == id) {
                empleados.remove(i);
                return;
            }
        }
    }

}
