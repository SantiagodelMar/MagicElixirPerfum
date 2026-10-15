package service;

import domain.Empleado;
import domain.enums.TipoDocumento;
import service.inputport.EmpleadoServiceInterface;
import service.outputport.EmpleadoRepositoryPort;

import java.util.List;

public class EmpleadoServiceImp implements EmpleadoServiceInterface {

    private final EmpleadoRepositoryPort empleadoRepository;

    public EmpleadoServiceImp(EmpleadoRepositoryPort empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public Empleado createEmpleado(int idEmpleado, String nombre, String apellido, String correo, TipoDocumento tipoDoc,
                                   String celular, String direccion, String fechaNacimiento, String role, String password) {

        Empleado empleado = new Empleado(idEmpleado, nombre, apellido, correo, tipoDoc, celular, direccion,
                fechaNacimiento, role, password);

        return empleadoRepository.createEmpleado(empleado);
    }

    @Override
    public Empleado selectEmpleadoById(int id) {
        return empleadoRepository.selectEmpleadoById(id);
    }

    @Override
    public List<Empleado> selectAllEmpleados() {
        return empleadoRepository.selectAllEmpleados();
    }

    @Override
    public Empleado updateEmpleado(int idEmpleado, String celular, String role) {
        Empleado empleado = empleadoRepository.selectEmpleadoById(idEmpleado);
        if (empleado == null) {
            return null;
        }

        empleado.setCelular(celular);
        empleado.setRole(role);

        return empleadoRepository.updateEmpleado(empleado);
    }

    @Override
    public void deleteEmpleadoById(int id) {
        empleadoRepository.deleteEmpleadoById(id);
    }

    // HU-01: solo entra si el correo y la contraseña coinciden y la cuenta esta activa
    @Override
    public Empleado iniciarSesion(String correo, String password) {
        Empleado empleado = empleadoRepository.selectEmpleadoByCorreo(correo);

        if (empleado == null || !empleado.isActivo()) {
            return null;
        }

        if (empleado.getPassword().equals(password)) {
            return empleado;
        }
        return null;
    }

    // HU-02: activar o desactivar la cuenta del empleado
    @Override
    public Empleado cambiarEstadoEmpleado(int id) {
        Empleado empleado = empleadoRepository.selectEmpleadoById(id);
        if (empleado == null) {
            return null;
        }

        empleado.setActivo(!empleado.isActivo());
        return empleadoRepository.updateEmpleado(empleado);
    }

}
