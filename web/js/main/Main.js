/** Punto de entrada de la tienda web. Los datos los entrega el backend Java (Application.java). */
document.addEventListener("DOMContentLoaded", () => {
    const ventana = new VentanaPrincipal(document.getElementById("app"));
    ventana.mostrar("acceso");
});
