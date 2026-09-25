package domain;

public class Inventario {

    // Atributos
    private int idInventario;
    private Perfume perfume;
    private int stockDisponible;
    private String estadoDisponibilidad;

    // Constructores
    public Inventario() {
    }

    public Inventario(int idInventario, Perfume perfume, int stockDisponible, String estadoDisponibilidad) {
        this.idInventario = idInventario;
        this.perfume = perfume;
        this.stockDisponible = stockDisponible;
        this.estadoDisponibilidad = estadoDisponibilidad;
        actualizarEstado();
    }

    // Getters and Setters
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
        actualizarEstado();
    }

    public String getEstadoDisponibilidad() {
        return estadoDisponibilidad;
    }

    public void setEstadoDisponibilidad(String estadoDisponibilidad) {
        this.estadoDisponibilidad = estadoDisponibilidad;
    }

    // Métodos
    public void actualizarStock(int cantidad) {
        if (this.stockDisponible + cantidad < 0) {
            throw new IllegalArgumentException("Stock insuficiente.");
        }
        this.stockDisponible += cantidad;
        actualizarEstado();
    }

    public boolean verificarStockDisponibilidad() {
        return this.stockDisponible > 0;
    }

    private void actualizarEstado() {
        if (this.stockDisponible == 0) {
            this.estadoDisponibilidad = "Agotado";
        } else if (this.stockDisponible <= 5) {
            this.estadoDisponibilidad = "Bajo Stock";
        } else {
            this.estadoDisponibilidad = "Disponible";
        }
    }

    public void update() {
        System.out.println("Inventario del perfume actualizado.");
    }

}// Fin public class