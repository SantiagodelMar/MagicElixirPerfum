package domain;

public class Perfume {

    //Atributos

    private int idPerfume;
    private String nombre;
    private String marca;
    private double precio;
    private int mililitros;
    private String notasOlfativas;
    private Categoria categoria;

    //Constructores

    public Perfume() {
    }

    public Perfume(int idPerfume, String nombre, String marca, double precio, int mililitros, String notasOlfativas, Categoria categoria) {
        this.idPerfume = idPerfume;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.mililitros = mililitros;
        this.notasOlfativas = notasOlfativas;
        this.categoria = categoria;
    }

    //Getters and Setters

    public int getIdPerfume() {
        return idPerfume;
    }

    public void setIdPerfume(int idPerfume) {
        this.idPerfume = idPerfume;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getMililitros() {
        return mililitros;
    }

    public void setMililitros(int mililitros) {
        this.mililitros = mililitros;
    }

    public String getNotasOlfativas() {
        return notasOlfativas;
    }

    public void setNotasOlfativas(String notasOlfativas) {
        this.notasOlfativas = notasOlfativas;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    //Metodos

    public void aplicarDescuento(){

    }

    public void obtenerInformacionCompleta(){

    }

    public void create(){

    }

    public void update(){

    }

}//Fin public class