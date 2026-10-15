package view;

import domain.Administrador;
import domain.enums.TipoDocumento;
import service.helpers.SetUsuarioHelper;
import service.inputport.AdministradorServiceInterface;
import service.validations.FormTypeValidator;

import java.util.List;

public class AdministradorView {

    private final AdministradorServiceInterface administradorService;

    public AdministradorView(AdministradorServiceInterface administradorService) {
        this.administradorService = administradorService;
    }

    public void createAdministrador() {
        int id = FormTypeValidator.intValidator("Ingrese el id del administrador");

        if (administradorService.selectAdministradorById(id) != null) {
            System.out.println("Ya existe un administrador con ese id");
            return;
        }

        String nombre = FormTypeValidator.stringValidator("Ingrese el nombre del administrador");
        String apellido = FormTypeValidator.stringValidator("Ingrese el apellido del administrador");
        String correo = FormTypeValidator.stringValidator("Ingrese el correo del administrador");
        TipoDocumento tipoDoc = SetUsuarioHelper.getTipoDocumento();
        String celular = FormTypeValidator.stringValidator("Ingrese el celular del administrador");
        String direccion = FormTypeValidator.stringValidator("Ingrese la direccion del administrador");
        String fechaNacimiento = FormTypeValidator.stringValidator("Ingrese la fecha de nacimiento (yyyy-MM-dd)");
        String permissions = FormTypeValidator.stringValidator("Ingrese los permisos (Total, Inventario, Ventas...)");
        String level = FormTypeValidator.stringValidator("Ingrese el nivel (1, 2, 3...)");

        administradorService.createAdministrador(id, nombre, apellido, correo, tipoDoc, celular, direccion,
                fechaNacimiento, permissions, level);
        System.out.println("Administrador registrado");
    }

    public void selectAdministradorById() {
        int id = FormTypeValidator.intValidator("Ingrese el id del administrador a buscar");
        Administrador administrador = administradorService.selectAdministradorById(id);

        if (administrador == null) {
            System.out.println("No se encontro el administrador");
        } else {
            mostrarAdministrador(administrador);
        }
    }

    public void selectAllAdministradores() {
        List<Administrador> administradores = administradorService.selectAllAdministradores();

        if (administradores.isEmpty()) {
            System.out.println("No hay administradores registrados");
            return;
        }

        for (Administrador administrador : administradores) {
            mostrarAdministrador(administrador);
        }
    }

    public void updateAdministrador() {
        int id = FormTypeValidator.intValidator("Ingrese el id del administrador a actualizar");

        if (administradorService.selectAdministradorById(id) == null) {
            System.out.println("No se encontro el administrador");
            return;
        }

        String permissions = FormTypeValidator.stringValidator("Ingrese los nuevos permisos");
        String level = FormTypeValidator.stringValidator("Ingrese el nuevo nivel");

        administradorService.updateAdministrador(id, permissions, level);
        System.out.println("Administrador actualizado");
    }

    public void deleteAdministrador() {
        int id = FormTypeValidator.intValidator("Ingrese el id del administrador a eliminar");

        if (administradorService.selectAdministradorById(id) == null) {
            System.out.println("No se encontro el administrador");
            return;
        }

        administradorService.deleteAdministradorById(id);
        System.out.println("Administrador eliminado");
    }

    private void mostrarAdministrador(Administrador administrador) {
        System.out.println("Administrador #" + administrador.getIdUsuario()
                + " | " + administrador.getNombre() + " " + administrador.getApellido()
                + " | Correo: " + administrador.getCorreo()
                + " | Permisos: " + administrador.getPermissions()
                + " | Nivel: " + administrador.getLevel());
    }

}//Fin public class
