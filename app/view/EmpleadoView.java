package view;

import domain.Empleado;
import domain.enums.TipoDocumento;
import service.helpers.SetUsuarioHelper;
import service.inputport.EmpleadoServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class EmpleadoView {

    private final EmpleadoServiceInterface empleadoService;

    public EmpleadoView(EmpleadoServiceInterface empleadoService) {
        this.empleadoService = empleadoService;
    }

    // HU-01: devuelve true si el empleado pudo entrar
    public boolean iniciarSesion() {
        String correo = FormTypeValidator.stringValidator("Ingrese su correo");
        String password = FormTypeValidator.stringValidator("Ingrese su contraseña");

        Empleado empleado = empleadoService.iniciarSesion(correo, password);

        if (empleado == null) {
            System.out.println("Correo o contraseña incorrectos, o la cuenta esta desactivada");
            return false;
        }

        System.out.println("Bienvenido " + empleado.getNombre() + " (" + empleado.getRole() + ")");
        return true;
    }

    public void createEmpleado() {
        int id = FormTypeValidator.intValidator("Ingrese el id del empleado");

        if (empleadoService.selectEmpleadoById(id) != null) {
            System.out.println("Ya existe un empleado con ese id");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nombre del empleado");
        String apellido = FormTypeValidator.stringValidator("Ingrese el apellido del empleado");
        String correo = FormTypeValidator.stringValidator("Ingrese el correo del empleado");
        TipoDocumento tipoDoc = SetUsuarioHelper.getTipoDocumento();
        String celular = FormTypeValidator.stringValidator("Ingrese el celular del empleado");
        String direccion = FormTypeValidator.stringValidator("Ingrese la direccion del empleado");
        String fechaNacimiento = FormTypeValidator.stringValidator("Ingrese la fecha de nacimiento (yyyy-MM-dd)");
        String role = FormTypeValidator.stringValidator("Ingrese el rol (Vendedor, Bodeguero...)");
        String password = FormTypeValidator.stringValidator("Ingrese la contraseña del empleado");

        empleadoService.createEmpleado(id, nombre, apellido, correo, tipoDoc, celular, direccion,
                fechaNacimiento, role, password);
        System.out.println("Empleado registrado");
    }

    public void selectEmpleadoById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del empleado a buscar");
        Empleado empleado = empleadoService.selectEmpleadoById(id);

        if (empleado == null) {
            System.out.println("No se encontro el empleado");
        } else {
            mostrarEmpleado(empleado);
        }
    }

    public void selectAllEmpleados() {
        List<Empleado> empleados = empleadoService.selectAllEmpleados();

        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados");
            return;
        }

        for (Empleado empleado : empleados) {
            mostrarEmpleado(empleado);
        }
    }

    public void updateEmpleado() {
        int id = FormTypeValidator.intValidator("Ingrese el id del empleado a actualizar");

        if (empleadoService.selectEmpleadoById(id) == null) {
            System.out.println("No se encontro el empleado");
            return;
        }

        String celular = FormTypeValidator.stringValidator("Ingrese el nuevo celular");
        String role = FormTypeValidator.stringValidator("Ingrese el nuevo rol");

        empleadoService.updateEmpleado(id, celular, role);
        System.out.println("Empleado actualizado");
    }

    public void deleteEmpleado() {
        int id = FormTypeValidator.intValidator("Ingrese el id del empleado a eliminar");

        if (empleadoService.selectEmpleadoById(id) == null) {
            System.out.println("No se encontro el empleado");
            return;
        }

        empleadoService.deleteEmpleadoById(id);
        System.out.println("Empleado eliminado");
    }

    // HU-02: activar o desactivar la cuenta
    public void cambiarEstadoEmpleado() {
        int id = FormTypeValidator.intValidator("Ingrese el id del empleado a activar/desactivar");
        Empleado empleado = empleadoService.cambiarEstadoEmpleado(id);

        if (empleado == null) {
            System.out.println("No se encontro el empleado");
        } else if (empleado.isActivo()) {
            System.out.println("La cuenta de " + empleado.getNombre() + " quedo activa");
        } else {
            System.out.println("La cuenta de " + empleado.getNombre() + " quedo desactivada");
        }
    }

    private void mostrarEmpleado(Empleado empleado) {
        System.out.println("Empleado #" + empleado.getIdUsuario()
                + " | " + empleado.getNombre() + " " + empleado.getApellido()
                + " | Correo: " + empleado.getCorreo()
                + " | Rol: " + empleado.getRole()
                + " | Activo: " + (empleado.isActivo() ? "Si" : "No"));
    }

}//Fin public class
