package domain;

public class Administrador extends Usuario{

//Atributos

    private String permissions;
    private String level;

    public Administrador() {
        super();
    }

    public Administrador(int idUsuario, String nombre, String apellido, String correo, String tipoDoc, String celular, String direccion, String fechaNacimiento, String permissions, String level) {
        super(idUsuario, nombre, apellido, correo, tipoDoc, celular, direccion, fechaNacimiento);
        this.permissions = permissions;
        this.level = level;
    }

    public Administrador(String permissions, String level) {
        this.permissions = permissions;
        this.level = level;
    }

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

    //Metodos

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
