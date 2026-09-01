package domain;

import java.util.Date;

public class Envio {


    private int idEnvio;
    private String transportadora;
    private String numeroDeGuia;
    private String direccionDestino;
    private Date fechaEnvio;
    private Date fechaEntrega;
    private double costoEnvio;
    private String estadoEnvio;


    public Envio() { }


    public Envio(int idEnvio, String transportadora, String numeroDeGuia, String direccionDestino, Date fechaEnvio, Date fechaEntrega, double costoEnvio, String estadoEnvio) {

        this.idEnvio = idEnvio;
        this.transportadora = transportadora;
        this.numeroDeGuia = numeroDeGuia;
        this.direccionDestino = direccionDestino;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.costoEnvio = costoEnvio;
        this.estadoEnvio = estadoEnvio;
    }


    public String mostrarGuia() {

        System.out.println("Mostrando la guía del envío...");
        System.out.println("Número de guía: " + numeroDeGuia);

        public void actualizarEstado(String nuevoEstado) {

            if (nuevoEstado.equalsIgnoreCase("Creado")
                    || nuevoEstado.equalsIgnoreCase("En Camino")
                    || nuevoEstado.equalsIgnoreCase("Entregado")) {

                this.estadoEnvio = nuevoEstado;

                System.out.println("El estado del envío ha sido actualizado.");
                System.out.println("Nuevo estado: " + estadoEnvio);

            } else {

                System.out.println("Estado no válido.");
                System.out.println("Los estados permitidos son:");
                System.out.println("Creado, En Camino o Entregado.");
            }
        }


        public void rastrearEnvio() {

            System.out.println("Información del envío");
            System.out.println("Número de guía: " + numeroDeGuia);
            System.out.println("Estado actual: " + estadoEnvio);
        }


        public int getIdEnvio() {
            return idEnvio;
        }

        public void setIdEnvio(int idEnvio) {
            this.idEnvio = idEnvio;
        }

        public String getTransportadora() {
            return transportadora;
        }

        public void setTransportadora(String transportadora) {
            this.transportadora = transportadora;
        }

        public String getNumeroDeGuia() {
            return numeroDeGuia;
        }

        public void setNumeroDeGuia(String numeroDeGuia) {
            this.numeroDeGuia = numeroDeGuia;
        }

        public String getDireccionDestino() {
            return direccionDestino;
        }

        public void setDireccionDestino(String direccionDestino) {
            this.direccionDestino = direccionDestino;
        }

        public Date getFechaEnvio() {
            return fechaEnvio;
        }

        public void setFechaEnvio(Date fechaEnvio) {
            this.fechaEnvio = fechaEnvio;
        }

        public Date getFechaEntrega() {
            return fechaEntrega;
        }

        public void setFechaEntrega(Date fechaEntrega) {
            this.fechaEntrega = fechaEntrega;
        }

        public double getCostoEnvio() {
            return costoEnvio;
        }

        public void setCostoEnvio(double costoEnvio) {
            this.costoEnvio = costoEnvio;
        }

        public String getEstadoEnvio() {
            return estadoEnvio;
        }

        public void setEstadoEnvio(String estadoEnvio) {
            this.estadoEnvio = estadoEnvio;
        }
    }
