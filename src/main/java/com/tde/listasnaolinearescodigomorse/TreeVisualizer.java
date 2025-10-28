package com.tde.listasnaolinearescodigomorse;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeVisualizer extends Application {

    // Classe Node para a árvore binária de busca
    static class Node {
        char letter;
        Node left;  // ponto (.)
        Node right; // traço (-)

        public Node(char letter) {
            this.letter = letter;
            this.left = null;
            this.right = null;
        }
    }

    // Classe da árvore binária de busca
    static class MorseBST {
        private Node root;
        private Map<Character, String> morseMap;

        public MorseBST() {
            root = new Node(' ');
            morseMap = new HashMap<>();
            buildMorseTree();
        }

        private void buildMorseTree() {
            insert('A', ".-");
            insert('B', "-...");
            insert('C', "-.-.");
            insert('D', "-..");
            insert('E', ".");
            insert('F', "..-.");
            insert('G', "--.");
            insert('H', "....");
            insert('I', "..");
            insert('J', ".---");
            insert('K', "-.-");
            insert('L', ".-..");
            insert('M', "--");
            insert('N', "-.");
            insert('O', "---");
            insert('P', ".--.");
            insert('Q', "--.-");
            insert('R', ".-.");
            insert('S', "...");
            insert('T', "-");
            insert('U', "..-");
            insert('V', "...-");
            insert('W', ".--");
            insert('X', "-..-");
            insert('Y', "-.--");
            insert('Z', "--..");
        }

        public void insert(char letter, String morseCode) {
            Node current = root;
            for (char symbol : morseCode.toCharArray()) {
                if (symbol == '.') {
                    if (current.left == null) {
                        current.left = new Node(' ');
                    }
                    current = current.left;
                } else if (symbol == '-') {
                    if (current.right == null) {
                        current.right = new Node(' ');
                    }
                    current = current.right;
                }
            }
            current.letter = letter;
            morseMap.put(letter, morseCode);
        }

        public String decode(String morseMessage) {
            StringBuilder result = new StringBuilder();
            String[] words = morseMessage.trim().split(" / ");

            for (String word : words) {
                String[] letters = word.split(" ");
                for (String code : letters) {
                    if (!code.trim().isEmpty()) {
                        result.append(findLetter(code));
                    }
                }
                result.append(" ");
            }

            return result.toString().trim();
        }

        private char findLetter(String morseCode) {
            Node current = root;
            for (char symbol : morseCode.toCharArray()) {
                if (symbol == '.') {
                    current = current.left;
                } else if (symbol == '-') {
                    current = current.right;
                }
                if (current == null) return '?';
            }
            return current.letter;
        }

        public String encode(String text) {
            StringBuilder morse = new StringBuilder();
            for (char c : text.toUpperCase().toCharArray()) {
                if (c == ' ') {
                    morse.append("/ ");
                } else if (morseMap.containsKey(c)) {
                    morse.append(morseMap.get(c)).append(" ");
                }
            }
            return morse.toString().trim();
        }

        public List<Node> getPath(String morseCode) {
            List<Node> path = new ArrayList<>();
            Node current = root;
            path.add(current);

            for (char symbol : morseCode.toCharArray()) {
                if (symbol == '.') {
                    current = current.left;
                } else if (symbol == '-') {
                    current = current.right;
                }
                if (current != null) {
                    path.add(current);
                } else {
                    break;
                }
            }
            return path;
        }

        public Map<Character, String> getMorseMap() {
            return morseMap;
        }

        // Calcula a altura da árvore
        public int getHeight() {
            return getHeight(root);
        }

        private int getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }

        public void drawTree(Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setLineWidth(2);
            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4, null);
        }

        public void drawTree(Canvas canvas, List<Node> highlightedNodes) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setLineWidth(2);
            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4, highlightedNodes);
        }

        private void drawNode(GraphicsContext gc, Node node, double x, double y, double xOffset, List<Node> highlightedNodes) {
            if (node == null) {
                return;
            }

            boolean isHighlighted = highlightedNodes != null && highlightedNodes.contains(node);

            // Desenha linhas para os filhos primeiro
            if (node.left != null) {
                double newX = x - xOffset;
                double newY = y + 80;
                boolean childHighlighted = highlightedNodes != null && highlightedNodes.contains(node.left);
                gc.setStroke(isHighlighted && childHighlighted ? Color.GREEN : Color.BLACK);
                gc.setLineWidth(isHighlighted && childHighlighted ? 4 : 2);
                gc.strokeLine(x, y + 15, newX, newY - 15);

                // Desenha o símbolo "." na linha
                gc.setFill(Color.BLUE);
                gc.fillText(".", (x + newX) / 2 - 10, (y + newY) / 2);

                drawNode(gc, node.left, newX, newY, xOffset / 2, highlightedNodes);
            }

            if (node.right != null) {
                double newX = x + xOffset;
                double newY = y + 80;
                boolean childHighlighted = highlightedNodes != null && highlightedNodes.contains(node.right);
                gc.setStroke(isHighlighted && childHighlighted ? Color.GREEN : Color.BLACK);
                gc.setLineWidth(isHighlighted && childHighlighted ? 4 : 2);
                gc.strokeLine(x, y + 15, newX, newY - 15);

                // Desenha o símbolo "-" na linha
                gc.setFill(Color.RED);
                gc.fillText("-", (x + newX) / 2 + 5, (y + newY) / 2);

                drawNode(gc, node.right, newX, newY, xOffset / 2, highlightedNodes);
            }

            // Desenha o nó (círculo e letra)
            if (isHighlighted) {
                gc.setFill(Color.LIGHTGREEN);
                gc.fillOval(x - 15, y - 15, 30, 30);
            } else {
                gc.setFill(Color.WHITE);
                gc.fillOval(x - 15, y - 15, 30, 30);
            }

            gc.setStroke(isHighlighted ? Color.DARKGREEN : Color.BLACK);
            gc.setLineWidth(isHighlighted ? 3 : 2);
            gc.strokeOval(x - 15, y - 15, 30, 30);

            // Desenha a letra dentro do círculo
            gc.setFill(Color.BLACK);
            gc.fillText(String.valueOf(node.letter), x - 5, y + 5);
        }
    }

    // Variáveis da interface
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

        // Layout principal
        BorderPane painelPrincipal = new BorderPane();
        painelPrincipal.setStyle("-fx-background-color: #f5f5f5;");

        // TOPO
        Label titulo = new Label("🔤 Codificador/Decodificador Morse");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        VBox topoBox = new VBox(titulo);
        topoBox.setAlignment(Pos.CENTER);
        topoBox.setPadding(new Insets(20));
        topoBox.setStyle("-fx-background-color: #3498db;");

        // CANVAS
        int height = bst.getHeight();
        int canvasHeight = 100 + height * 100;
        int canvasWidth = (int) Math.pow(2, height) * 40;

        canvas = new Canvas(canvasWidth, canvasHeight);
        bst.drawTree(canvas);

        ScrollPane scrollCanvas = new ScrollPane(canvas);
        scrollCanvas.setStyle("-fx-background: white;");
        scrollCanvas.setFitToWidth(true);
        scrollCanvas.setFitToHeight(true);

        // CONTROLES
        VBox painelControles = new VBox(15);
        painelControles.setPadding(new Insets(20));
        painelControles.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");

        // Decodificação
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

        // Codificação
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

        // Área de resultado
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

        // EVENTOS
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
                    String codigoMorse = bst.getMorseMap().get(c);
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

        // Monta a interface
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
                        List<Node> nosDestacados = caminhoAtual.subList(0, Math.min(indiceAnimacao, caminhoAtual.size()));
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