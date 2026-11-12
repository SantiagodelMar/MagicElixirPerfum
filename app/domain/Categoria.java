package domain;

public class Categoria {

    //Atributos

    private int idCategoria;
    private String nombre;
    private String descripcion;

    //Constructores

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    //Getters and Setters

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

    // Dos objetos son "el mismo" si tienen el mismo id (asi un objeto leido otra vez de la base de datos
    // se reconoce como igual al que ya estaba, por ejemplo en las listas desplegables)
    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (otro == null || getClass() != otro.getClass()) {
            return false;
        }
        return idCategoria == ((Categoria) otro).idCategoria;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idCategoria);
    }

}
