package com.tde.listasnaolinearescodigomorse;

import java.util.HashMap;
import java.util.Map;

public class MorseBST {
    private Node root;
    private Map<Character, String> morseMap;

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

    public void drawTree(javafx.scene.canvas.Canvas canvas) {
        javafx.scene.canvas.GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        gc.setLineWidth(2);
        drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4);
    }

    private void drawNode(javafx.scene.canvas.GraphicsContext gc, Node node, double x, double y, double xOffset) {
        if (node == null) return;

        gc.strokeOval(x - 15, y - 15, 30, 30);
        gc.strokeText(String.valueOf(node.letter), x - 5, y + 5);

        if (node.left != null) {
            double newX = x - xOffset;
            double newY = y + 80;
            gc.strokeLine(x, y + 15, newX, newY - 15);
            drawNode(gc, node.left, newX, newY, xOffset / 2);
        }
        if (node.right != null) {
            double newX = x + xOffset;
            double newY = y + 80;
            gc.strokeLine(x, y + 15, newX, newY - 15);
            drawNode(gc, node.right, newX, newY, xOffset / 2);
        }
    }

}