package domain;

public class Empleado extends Usuario {

    private String role;

    public Empleado() {
        super();
    }

    public Empleado(int idUsuario, String nombre, String apellido, String correo, String tipoDoc, String celular, String direccion, String fechaNacimiento, String role) {
        super(idUsuario, nombre, apellido, correo, tipoDoc, celular, direccion, fechaNacimiento);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void create(){

    }

    public void selectAll(){

    }

    public void selectById(int idUsuario){

    }

    public void update(){

    }

    public void delete(){


    }








}
