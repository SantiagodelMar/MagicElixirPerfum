package domain;

public class Cliente extends Usuario{

    private String tipoCliente;
    private String direccionEnvio;

    public Cliente() {
        super();
    }

    public Cliente(int idUsuario, String nombre, String apellido, String correo, String tipoDoc, String celular, String direccion, String fechaNacimiento, String tipoCliente, String direccionEnvio) {
        super(idUsuario, nombre, apellido, correo, tipoDoc, celular, direccion, fechaNacimiento);
        this.tipoCliente = tipoCliente;
        this.direccionEnvio = direccionEnvio;
    }

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
