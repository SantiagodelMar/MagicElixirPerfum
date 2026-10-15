package service.inputport;

import domain.Envio;
import domain.enums.EstadoEnvio;

import java.util.List;

public interface EnvioServiceInterface {

    public Envio createEnvio(int idEnvio, int idPedido, String transportadora, String numeroDeGuia,
                             String direccionDestino, double costoEnvio);
    public Envio selectEnvioById(int id);
    public Envio selectEnvioByGuia(String numeroDeGuia);
    public List<Envio> selectAllEnvios();
    public Envio updateEnvio(int idEnvio, String transportadora, String numeroDeGuia, String direccionDestino, double costoEnvio);
    public Envio actualizarEstadoEnvio(int idEnvio, EstadoEnvio nuevoEstado);
    public void deleteEnvioById(int id);

}
