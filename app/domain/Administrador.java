package domain;

import domain.enums.TipoDocumento;

public class Administrador extends Usuario {

    //Atributos

    private String permissions;
    private String level;

    //Constructores

    public Administrador() {
        super();
    }

    public Administrador(int idUsuario, String nombre, String apellido, String correo, TipoDocumento tipoDoc, String celular, String direccion, String fechaNacimiento, String permissions, String level) {
        super(idUsuario, nombre, apellido, correo, tipoDoc, celular, direccion, fechaNacimiento);
        this.permissions = permissions;
        this.level = level;
    }

    //Getters and Setters

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

}
