package repository.mysql;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

// Convierte los errores tecnicos de SQL en mensajes que se entienden
public class ErroresBD {

    public static RuntimeException traducir(String accion, SQLException e) {
        if (e instanceof SQLIntegrityConstraintViolationException) {
            return new RuntimeException("No se pudo " + accion
                    + ": el dato esta repetido o tiene registros relacionados. (" + e.getMessage() + ")", e);
        }
        return new RuntimeException("Error de base de datos al " + accion + ": " + e.getMessage(), e);
    }

}
