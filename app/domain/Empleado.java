package domain;

import domain.enums.TipoDocumento;

public class Empleado extends Usuario {

    //Atributos

    private String role;
    private String password;
    private boolean activo;

    //Constructores

    public Empleado() {
        super();
    }

    public Empleado(int idUsuario, String nombre, String apellido, String correo, TipoDocumento tipoDoc, String celular, String direccion, String fechaNacimiento, String role, String password) {
        super(idUsuario, nombre, apellido, correo, tipoDoc, celular, direccion, fechaNacimiento);
        this.role = role;
        this.password = password;
        this.activo = true;
    }

    //Getters and Setters

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}
