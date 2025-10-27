package com.tde.listasnaolinearescodigomorse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MorseBST {

    Node root;
    Map<Character, String> morseMap;

    public MorseBST() {
        root = new Node(' ');
        morseMap = new HashMap<>();
        buildMorseTree();
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

    public String decode(String morseMessage) {
        StringBuilder result = new StringBuilder();
        String[] words = morseMessage.trim().split(" / ");

        for (String word : words) {
            String[] letters = word.split(" ");
            for (String code : letters) {
                result.append(findLetter(code));
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

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
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

    public void drawTree(javafx.scene.canvas.Canvas canvas) {
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.setLineWidth(2);
        drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4, null);
    }

    public void drawTree(javafx.scene.canvas.Canvas canvas, List<Node> highlightedNodes) {
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setLineWidth(2);
        drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4, highlightedNodes);
    }

    private void drawNode(javafx.scene.canvas.GraphicsContext gc, Node node, double x, double y, double xOffset, List<Node> highlightedNodes) {
        if (node == null) return;

        boolean isHighlighted = highlightedNodes != null && highlightedNodes.contains(node);

        if (node.left != null) {
            double newX = x - xOffset;
            double newY = y + 80;
            boolean childHighlighted = highlightedNodes != null && highlightedNodes.contains(node.left);
            gc.setStroke(isHighlighted && childHighlighted ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.BLACK);
            gc.setLineWidth(isHighlighted && childHighlighted ? 4 : 2);
            gc.strokeLine(x, y + 15, newX, newY - 15);
            drawNode(gc, node.left, newX, newY, xOffset / 2, highlightedNodes);
        }
        if (node.right != null) {
            double newX = x + xOffset;
            double newY = y + 80;
            boolean childHighlighted = highlightedNodes != null && highlightedNodes.contains(node.right);
            gc.setStroke(isHighlighted && childHighlighted ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.BLACK);
            gc.setLineWidth(isHighlighted && childHighlighted ? 4 : 2);
            gc.strokeLine(x, y + 15, newX, newY - 15);
            drawNode(gc, node.right, newX, newY, xOffset / 2, highlightedNodes);
        }

        if (isHighlighted) {
            gc.setFill(javafx.scene.paint.Color.LIGHTGREEN);
            gc.fillOval(x - 15, y - 15, 30, 30);
        }

        gc.setStroke(isHighlighted ? javafx.scene.paint.Color.DARKGREEN : javafx.scene.paint.Color.BLACK);
        gc.setLineWidth(isHighlighted ? 3 : 2);
        gc.strokeOval(x - 15, y - 15, 30, 30);

        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillText(String.valueOf(node.letter), x - 5, y + 5);
    }

}