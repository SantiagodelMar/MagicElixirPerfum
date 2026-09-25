package domain;

public class Perfume {

    // Atributos (HU-4: nombre, marca, mililitros, precio, notasOlfativas y categoría)
    private int idPerfume;
    private String nombre;
    private String marca;
    private double precio;
    private int mililitros;
    private String notasOlfativas;
    private Categoria categoria;

    // Constructores
    public Perfume() {
    }

    // HU-4: Permite registrar el perfume con todos sus datos completos
    public Perfume(int idPerfume, String nombre, String marca, double precio, int mililitros, String notasOlfativas, Categoria categoria) {
        this.idPerfume = idPerfume;
        this.nombre = nombre;
        this.marca = marca;
        setPrecio(precio);
        this.mililitros = mililitros;
        this.notasOlfativas = notasOlfativas;
        this.categoria = categoria;
    }

    // Getters and Setters
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
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
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

    // Métodos (Cumplimiento de HUs)

    // HU-5: Permite al administrador actualizar el precio según el proveedor
    public void actualizarPrecio(double nuevoPrecio) {
        setPrecio(nuevoPrecio);
        System.out.println("Precio actualizado con éxito a: $" + nuevoPrecio);
    }

    public void aplicarDescuento(double porcentaje) {
        if (porcentaje > 0 && porcentaje <= 100) {
            this.precio -= (this.precio * (porcentaje / 100));
        }
    }

    // HU-4: Retorna la ficha técnica completa del perfume registrado
    public String obtenerInformacionCompleta() {
        String nombreCat = (categoria != null) ? categoria.getNombre() : "Sin Categoría";
        return String.format("Perfume #%d: %s - %s (%d ml) | Cat: %s | Notas: %s | Precio: $%.2f",
                idPerfume, nombre, marca, mililitros, nombreCat, notasOlfativas, precio);
    }

    public void create() {
        System.out.println("Perfume " + nombre + " registrado en el catálogo.");
    }

    public void update() {
        System.out.println("Perfume " + nombre + " actualizado.");
    }

}// Fin public class