package service.outputport;

import domain.Empleado;

import java.util.List;

public interface EmpleadoRepositoryPort {

    public Empleado createEmpleado(Empleado empleado);
    public Empleado selectEmpleadoById(int id);
    public Empleado selectEmpleadoByCorreo(String correo);
    public List<Empleado> selectAllEmpleados();
    public Empleado updateEmpleado(Empleado empleado);
    public void deleteEmpleadoById(int id);

}
