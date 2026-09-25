package domain;

public class Categoria {

    // Atributos
    private int idCategoria;
    private String nombre;
    private String descripcion;

    // Categorías válidas del catálogo de Instagram (HU-6)
    public static final String[] CATEGORIAS_INSTAGRAM = {
            "Perfumería Masculina",
            "Perfumería Femenina",
            "Árabe Masculina",
            "Árabe Femenina"
    };

    // Constructores
    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters and Setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Métodos (HU-6)
    public String mostrarDetalles() {
        return String.format("Categoría [%d]: %s - %s", idCategoria, nombre, descripcion);
    }

    public void create() {
        System.out.println("Categoría " + nombre + " creada.");
    }

    public void update() {
        System.out.println("Categoría " + nombre + " actualizada.");
    }

}// Fin public class