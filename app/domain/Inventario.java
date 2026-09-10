package domain;

public class Inventario {

    //Atributos

    private int idInventario;
    private Perfume perfume;
    private int stockDisponible;
    private String estadoDisponibilidad;

    //Constructores

    public Inventario() {
    }

    public Inventario(int idInventario, Perfume perfume, int stockDisponible, String estadoDisponibilidad) {
        this.idInventario = idInventario;
        this.perfume = perfume;
        this.stockDisponible = stockDisponible;
        this.estadoDisponibilidad = estadoDisponibilidad;
    }

    //Getters and Setters

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public Perfume getPerfume() {
        return perfume;
    }

    public void setPerfume(Perfume perfume) {
        this.perfume = perfume;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public String getEstadoDisponibilidad() {
        return estadoDisponibilidad;
    }

    public void setEstadoDisponibilidad(String estadoDisponibilidad) {
        this.estadoDisponibilidad = estadoDisponibilidad;
    }

    //Metodos

    public void actualizarStock(){

    }

    public boolean verificarStockDisponibilidad(){
        return false;
    }

    public void update(){

    }

}//Fin public class