package service.outputport;

import domain.Envio;

import java.util.List;

public interface EnvioRepositoryPort {

    public Envio createEnvio(Envio envio);
    public Envio selectEnvioById(int id);
    public Envio selectEnvioByGuia(String numeroDeGuia);
    public List<Envio> selectAllEnvios();
    public Envio updateEnvio(Envio envio);
    public void deleteEnvioById(int id);

}
