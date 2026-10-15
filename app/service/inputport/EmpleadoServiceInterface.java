package service.inputport;

import domain.Empleado;
import domain.enums.TipoDocumento;

import java.util.List;

public interface EmpleadoServiceInterface {

    public Empleado createEmpleado(int idEmpleado, String nombre, String apellido, String correo, TipoDocumento tipoDoc,
                                   String celular, String direccion, String fechaNacimiento, String role, String password);
    public Empleado selectEmpleadoById(int id);
    public List<Empleado> selectAllEmpleados();
    public Empleado updateEmpleado(int idEmpleado, String celular, String role);
    public void deleteEmpleadoById(int id);
    public Empleado iniciarSesion(String correo, String password);
    public Empleado cambiarEstadoEmpleado(int id);

}
