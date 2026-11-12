package repository.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Patron Singleton: solo existe UNA conexion a MySQL (XAMPP) para toda la aplicacion.
public class DataBaseConnectionMySql {

    private static DataBaseConnectionMySql instance;
    private final Connection connection;

    // XAMPP por defecto: puerto 3306, usuario root y sin contraseña
    private static final String URL = "jdbc:mysql://localhost:3306/magic_elixir_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // El constructor es privado: nadie mas puede hacer "new"
    private DataBaseConnectionMySql() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos. "
                    + "Revise: 1) MySQL prendido en XAMPP, 2) la base magic_elixir_db importada, "
                    + "3) el driver mysql-connector-j agregado al proyecto. Detalle: " + e.getMessage(), e);
        }
    }

    public static synchronized DataBaseConnectionMySql getInstance() {
        if (instance == null) {
            instance = new DataBaseConnectionMySql();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
