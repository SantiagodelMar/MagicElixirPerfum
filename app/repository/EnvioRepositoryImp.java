package repository;

import domain.Envio;
import service.outputport.EnvioRepositoryPort;

import java.util.ArrayList;
import java.util.List;

public class EnvioRepositoryImp implements EnvioRepositoryPort {

    // Lista en memoria mientras no tenemos base de datos
    private List<Envio> envios = new ArrayList<>();

    @Override
    public Envio createEnvio(Envio envio) {
        envios.add(envio);
        return envio;
    }

    @Override
    public Envio selectEnvioById(int id) {
        for (int i = 0; i < envios.size(); i++) {
            if (envios.get(i).getIdEnvio() == id) {
                return envios.get(i);
            }
        }
        return null;
    }

    @Override
    public Envio selectEnvioByGuia(String numeroDeGuia) {
        for (int i = 0; i < envios.size(); i++) {
            if (envios.get(i).getNumeroDeGuia().equalsIgnoreCase(numeroDeGuia)) {
                return envios.get(i);
            }
        }
        return null;
    }

    @Override
    public List<Envio> selectAllEnvios() {
        return envios;
    }

    @Override
    public Envio updateEnvio(Envio envio) {
        for (int i = 0; i < envios.size(); i++) {
            if (envios.get(i).getIdEnvio() == envio.getIdEnvio()) {
                envios.set(i, envio);
                return envio;
            }
        }
        return null;
    }

    @Override
    public void deleteEnvioById(int id) {
        for (int i = 0; i < envios.size(); i++) {
            if (envios.get(i).getIdEnvio() == id) {
                envios.remove(i);
                return;
            }
        }
    }

}
