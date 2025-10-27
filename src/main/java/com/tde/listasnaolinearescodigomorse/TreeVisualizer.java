package com.tde.listasnaolinearescodigomorse;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;

public class TreeVisualizer extends Application {

    private MorseBST bst;
    private Canvas canvas;
    private TextArea resultadoArea;
    private List<List<Node>> todosCaminhos = new ArrayList<>();
    private List<String> todasLetras = new ArrayList<>();
    private int letraAtual = 0;
    private int indiceAnimacao = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Árvore do Código Morse - TDE 2");
        bst = new MorseBST();

        BorderPane painelPrincipal = new BorderPane();
        painelPrincipal.setStyle("-fx-background-color: #f5f5f5;");

        Label titulo = new Label("🔤 Codificador/Decodificador Morse");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox topoBox = new VBox(titulo);
        topoBox.setAlignment(Pos.CENTER);
        topoBox.setPadding(new Insets(20));
        topoBox.setStyle("-fx-background-color: #3498db;");

        int height = bst.getHeight();
        int canvasHeight = 100 + height * 100;
        int canvasWidth = (int) Math.pow(2, height) * 40;

        canvas = new Canvas(canvasWidth, canvasHeight);
        bst.drawTree(canvas);

        ScrollPane scrollCanvas = new ScrollPane(canvas);
        scrollCanvas.setStyle("-fx-background: white;");
        scrollCanvas.setFitToWidth(true);
        scrollCanvas.setFitToHeight(true);

        VBox painelControles = new VBox(15);
        painelControles.setPadding(new Insets(20));
        painelControles.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");

        Label labelDecodificar = new Label("📥 Decodificar Morse para Texto:");
        labelDecodificar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox decodificarBox = new HBox(10);
        TextField campoMorse = new TextField();
        campoMorse.setPromptText("Ex: .... . .-.. .-.. --- / .-- --- .-. .-.. -..");
        campoMorse.setPrefWidth(400);

        Button botaoDecodificar = new Button("Decodificar");
        botaoDecodificar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");

        decodificarBox.getChildren().addAll(campoMorse, botaoDecodificar);
        decodificarBox.setAlignment(Pos.CENTER_LEFT);

        Label labelCodificar = new Label("📤 Codificar Texto para Morse:");
        labelCodificar.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox codificarBox = new HBox(10);
        TextField campoTexto = new TextField();
        campoTexto.setPromptText("Ex: HELLO WORLD");
        campoTexto.setPrefWidth(400);

        Button botaoCodificar = new Button("Codificar");
        botaoCodificar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");

        codificarBox.getChildren().addAll(campoTexto, botaoCodificar);
        codificarBox.setAlignment(Pos.CENTER_LEFT);

        Label labelResultado = new Label("💬 Resultado:");
        labelResultado.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        resultadoArea = new TextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setPrefHeight(80);
        resultadoArea.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New';");
        resultadoArea.setPromptText("Os resultados aparecerão aqui...");

        painelControles.getChildren().addAll(
                labelDecodificar, decodificarBox,
                new Separator(),
                labelCodificar, codificarBox,
                new Separator(),
                labelResultado, resultadoArea
        );

        botaoDecodificar.setOnAction(e -> {
            String morse = campoMorse.getText().trim();
            if (!morse.isEmpty()) {
                String resultado = bst.decode(morse);

                todosCaminhos.clear();
                todasLetras.clear();
                String[] words = morse.split(" / ");
                for (String word : words) {
                    String[] letters = word.split(" ");
                    for (String code : letters) {
                        if (!code.trim().isEmpty()) {
                            todosCaminhos.add(bst.getPath(code));
                            todasLetras.add(code);
                        }
                    }
                }

                resultadoArea.setText("📥 Morse: " + morse + "\n✅ Texto: " + resultado + "\n\n🎯 Animando caminho de cada letra na árvore...");
                letraAtual = 0;
                animarProximaLetra();
            } else {
                mostrarAlerta("Por favor, insira um código Morse!");
            }
        });

        botaoCodificar.setOnAction(e -> {
            String texto = campoTexto.getText().trim();
            if (!texto.isEmpty()) {
                String resultado = bst.encode(texto);

                todosCaminhos.clear();
                todasLetras.clear();
                for (char c : texto.toUpperCase().toCharArray()) {
                    if (c == ' ') continue;
                    String codigoMorse = bst.morseMap.get(c);
                    if (codigoMorse != null) {
                        todosCaminhos.add(bst.getPath(codigoMorse));
                        todasLetras.add(c + " (" + codigoMorse + ")");
                    }
                }

                resultadoArea.setText("📤 Texto: " + texto + "\n✅ Morse: " + resultado + "\n\n🎯 Animando caminho de cada letra na árvore...");
                letraAtual = 0;
                animarProximaLetra();
            } else {
                mostrarAlerta("Por favor, insira um texto!");
            }
        });

        painelPrincipal.setTop(topoBox);
        painelPrincipal.setCenter(scrollCanvas);
        painelPrincipal.setBottom(painelControles);

        Scene scene = new Scene(painelPrincipal, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void animarProximaLetra() {
        if (letraAtual >= todosCaminhos.size()) {
            Timeline resetTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> bst.drawTree(canvas))
            );
            resetTimeline.play();
            return;
        }

        List<Node> caminhoAtual = todosCaminhos.get(letraAtual);
        String letraInfo = todasLetras.get(letraAtual);

        String textoAtual = resultadoArea.getText().split("\n\n")[0];
        resultadoArea.setText(textoAtual + "\n\n🎯 Mostrando caminho: " + letraInfo + " [" + (letraAtual + 1) + "/" + todosCaminhos.size() + "]");

        indiceAnimacao = 0;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(400), e -> {
                    if (indiceAnimacao <= caminhoAtual.size()) {
                        List<Node> nosDestacados = caminhoAtual.subList(0, indiceAnimacao);
                        bst.drawTree(canvas, nosDestacados);
                        indiceAnimacao++;
                    } else {
                        Timeline pauseTimeline = new Timeline(
                                new KeyFrame(Duration.millis(500), ev -> {
                                    letraAtual++;
                                    animarProximaLetra();
                                })
                        );
                        pauseTimeline.play();
                    }
                })
        );
        timeline.setCycleCount(caminhoAtual.size() + 2);
        timeline.play();
    }

    private void mostrarAlerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atenção");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

}