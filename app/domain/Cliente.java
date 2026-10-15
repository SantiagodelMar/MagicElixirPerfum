package domain;

import domain.enums.TipoDocumento;

public class Cliente extends Usuario {

    //Atributos

    private String tipoCliente;
    private String direccionEnvio;

    //Constructores

    public Cliente() {
        super();
    }

    public Cliente(int idUsuario, String nombre, String apellido, String correo, TipoDocumento tipoDoc, String celular, String direccion, String fechaNacimiento, String tipoCliente, String direccionEnvio) {
        super(idUsuario, nombre, apellido, correo, tipoDoc, celular, direccion, fechaNacimiento);
        this.tipoCliente = tipoCliente;
        this.direccionEnvio = direccionEnvio;
    }

    //Getters and Setters

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

}
