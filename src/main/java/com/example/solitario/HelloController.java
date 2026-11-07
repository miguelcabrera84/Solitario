package com.example.solitario;

import solitaire.JuegoEightOff;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class HelloController {


    private JuegoEightOff motorLogico;


    private int origenSeleccionado = -1; // -1 significa que no hay carta seleccionada
    private String tipoOrigen = ""; // Para saber si agarró de "TABLEAU" o de "RESERVA"

    @FXML
    public void initialize() {

        motorLogico = new JuegoEightOff();
        actualizarPantalla();
    }


    @FXML
    public void clicEnColumna(MouseEvent event) {

        int columnaClickeada = 0; // Supongamos que clickeó la columna 0

        if (origenSeleccionado == -1) {

            origenSeleccionado = columnaClickeada;
            tipoOrigen = "TABLEAU";
            System.out.println("Seleccionaste la columna " + columnaClickeada);



        } else {

            if (tipoOrigen.equals("TABLEAU")) {

                boolean exito = motorLogico.moverDeTableauATableau(origenSeleccionado, columnaClickeada);

                if (exito) {
                    System.out.println("¡Movimiento válido!");
                } else {
                    System.out.println("Movimiento inválido: No respeta el palo o el valor secuencial.");
                }
            }


            origenSeleccionado = -1;
            tipoOrigen = "";


            actualizarPantalla();
        }
    }

    @FXML
    public void clicBotonDeshacer() {
        motorLogico.deshacerUltimoMovimiento();
        actualizarPantalla();
    }

    private void actualizarPantalla() {

    }
}