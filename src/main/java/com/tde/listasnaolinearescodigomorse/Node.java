package com.tde.listasnaolinearescodigomorse;

public class Node {
    char letter;
    Node left;  // ponto (.)
    Node right; // traço (-)

    public Node(char letter) {
        this.letter = letter;
        this.left = null;
        this.right = null;
    }
}