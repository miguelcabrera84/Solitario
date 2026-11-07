package com.example.solitario;

import solitaire.JuegoEightOff;
import solitaire.TableauDeck;
import DeckOfCards.CartaInglesa;
import EstructuraDeDatos.NodoSimple;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class TableroEightOff extends BorderPane {

    private JuegoEightOff motor;

    private VBox panelSuperior;
    private HBox panelTableaus;
    private HBox panelBotones;

    private int seleccionOrigenIdx = -1;
    private String tipoOrigen = "";

    public TableroEightOff() {
        motor = new JuegoEightOff();

        this.setStyle("-fx-background-color: #2e7d32;");
        this.setPadding(new Insets(15));

        inicializarComponentes();
        actualizarVista();
    }

    private void inicializarComponentes() {
        panelSuperior = new VBox(15);
        panelSuperior.setAlignment(Pos.CENTER);
        panelSuperior.setPadding(new Insets(10));
        this.setTop(panelSuperior);

        panelTableaus = new HBox(15);
        panelTableaus.setAlignment(Pos.CENTER);
        panelTableaus.setPadding(new Insets(20, 0, 0, 0));
        this.setCenter(panelTableaus);

        panelBotones = new HBox(15);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(10));

        Button btnDeshacer = new Button("Deshacer");
        btnDeshacer.setOnAction(e -> {
            motor.deshacerUltimoMovimiento();
            limpiarSeleccion();
            actualizarVista();
        });

        Button btnPista = new Button("Pista (Hint)");
        btnPista.setOnAction(e -> {
            String pista = motor.darPista();
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sugerencia de Movimiento");
            alerta.setHeaderText(null);
            alerta.setContentText(pista);
            alerta.showAndWait();
        });

        panelBotones.getChildren().addAll(btnDeshacer, btnPista);
        this.setBottom(panelBotones);
    }

    public void actualizarVista() {
        panelSuperior.getChildren().clear();
        panelTableaus.getChildren().clear();

        // RESERVAS Y FUNDACIONES
        VBox contenedorSup = new VBox(12);
        contenedorSup.setAlignment(Pos.CENTER);

        Label lblTituloSup = new Label("Reservas y Fundaciones");
        lblTituloSup.setTextFill(Color.WHITE);
        lblTituloSup.setFont(new Font("Arial", 14));

        //Fila de Reservas
        HBox boxReservas = new HBox(10);
        boxReservas.setAlignment(Pos.CENTER);
        for (int i = 0; i < 8; i++) {
            final int idxReserva = i;
            CartaInglesa c = motor.getReserva().verCarta(i);
            StackPane slot = c != null ? crearSlotCarta(c) : crearSlotVisual("R" + (i + 1));

            slot.setOnMouseClicked(e -> manejarClicReserva(idxReserva));
            boxReservas.getChildren().add(slot);
        }

        // Fila de Fundaciones
        HBox boxFundaciones = new HBox(15);
        boxFundaciones.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            final int idxFundacion = i;
            CartaInglesa c = motor.getFundaciones()[i].getUltimaCarta();
            StackPane slot = c != null ? crearSlotCarta(c) : crearSlotVisual("F" + (i + 1));

            slot.setOnMouseClicked(e -> manejarClicFundacion(idxFundacion));
            boxFundaciones.getChildren().add(slot);
        }

        contenedorSup.getChildren().addAll(lblTituloSup, boxReservas, boxFundaciones);
        panelSuperior.getChildren().add(contenedorSup);

        // DIBUJAR LAS 8 COLUMNAS (TABLEAUS)
        for (int i = 0; i < 8; i++) {
            final int idxTableau = i;

            VBox colVisual = new VBox(-38);
            colVisual.setAlignment(Pos.TOP_CENTER);
            colVisual.setPrefWidth(75);
            colVisual.setStyle("-fx-border-color: #1b5e20; -fx-border-width: 2px; -fx-background-color: rgba(0,0,0,0.1); -fx-padding: 5px;");

            Label lblTituloCol = new Label("T" + (i + 1));
            lblTituloCol.setTextFill(Color.WHITE);
            colVisual.getChildren().add(lblTituloCol);

            TableauDeck tDeck = motor.getTableaus()[i];
            if (tDeck.estaVacio()) {
                Label lblVacio = new Label("[Vacio]");
                lblVacio.setTextFill(Color.LIGHTGRAY);
                colVisual.getChildren().add(lblVacio);
            } else {
                java.util.List<CartaInglesa> listaCartas = new java.util.ArrayList<>();
                NodoSimple<CartaInglesa> actual = tDeck.getCartas().getTope();
                while (actual != null) {
                    listaCartas.add(0, actual.dato);
                    actual = actual.siguiente;
                }

                for (CartaInglesa c : listaCartas) {
                    StackPane cartaSlot = new StackPane();
                    cartaSlot.setPrefSize(65, 60);
                    cartaSlot.setStyle("-fx-background-color: white; -fx-border-color: #333; -fx-border-radius: 3px; -fx-background-radius: 3px;");

                    if (c != null) {
                        Label lblCarta = new Label(c.toString());
                        lblCarta.setFont(new Font("Arial", 12));
                        lblCarta.setTextFill(c.getColor().equals("rojo") ? Color.RED : Color.BLACK);

                        StackPane.setAlignment(lblCarta, Pos.TOP_LEFT);
                        cartaSlot.setPadding(new Insets(3, 0, 0, 4));
                        cartaSlot.getChildren().add(lblCarta);
                    }

                    colVisual.getChildren().add(cartaSlot);
                }
            }

            colVisual.setOnMouseClicked(e -> manejarClicTableau(idxTableau));
            panelTableaus.getChildren().add(colVisual);
        }
    }

    // MANEJO DE CLICS
    private void manejarClicTableau(int colIdx) {
        if (seleccionOrigenIdx == -1) {
            seleccionOrigenIdx = colIdx;
            tipoOrigen = "T";
            System.out.println("Seleccionaste la columna Tableau " + (colIdx + 1));
        } else {
            boolean exito = false;
            if (tipoOrigen.equals("T")) {
                exito = motor.moverDeTableauATableau(seleccionOrigenIdx, colIdx);
            } else if (tipoOrigen.equals("R")) {
                exito = motor.moverDeReservaATableau(seleccionOrigenIdx, colIdx);
            }
            limpiarSeleccion();
            actualizarVista();
        }
    }

    private void manejarClicReserva(int resIdx) {
        if (seleccionOrigenIdx == -1) {
            seleccionOrigenIdx = resIdx;
            tipoOrigen = "R";
            System.out.println("Seleccionaste la Reserva " + (resIdx + 1));
        } else {
            if (tipoOrigen.equals("T")) {
                motor.moverDeTableauAReserva(seleccionOrigenIdx, resIdx);
            }
            limpiarSeleccion();
            actualizarVista();
        }
    }

    private void manejarClicFundacion(int fundacionIdx) {
        if (seleccionOrigenIdx != -1) {
            boolean exito = false;
            if (tipoOrigen.equals("T")) {
                exito = motor.moverDeTableauAFundacion(seleccionOrigenIdx);
            } else if (tipoOrigen.equals("R")) {
                exito = motor.moverDeReservaAFundacion(fundacionIdx);
            }
            limpiarSeleccion();
            actualizarVista();
        }
    }

    private void limpiarSeleccion() {
        seleccionOrigenIdx = -1;
        tipoOrigen = "";
    }

    private StackPane crearSlotVisual(String texto) {
        StackPane pane = new StackPane();
        pane.setPrefSize(50, 70);
        pane.setStyle("-fx-border-color: white; -fx-border-radius: 5px; -fx-background-color: rgba(255,255,255,0.1);");
        Label lbl = new Label(texto);
        lbl.setTextFill(Color.WHITE);
        pane.getChildren().add(lbl);
        return pane;
    }

    private StackPane crearSlotCarta(CartaInglesa c) {
        StackPane pane = new StackPane();
        pane.setPrefSize(50, 70);
        pane.setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        Label lbl = new Label(c.toString());
        lbl.setFont(new Font("Arial", 14));
        lbl.setTextFill(c.getColor().equals("rojo") ? Color.RED : Color.BLACK);
        StackPane.setAlignment(lbl, Pos.TOP_LEFT);
        pane.setPadding(new Insets(4, 0, 0, 6));
        pane.getChildren().add(lbl);
        return pane;
    }
}