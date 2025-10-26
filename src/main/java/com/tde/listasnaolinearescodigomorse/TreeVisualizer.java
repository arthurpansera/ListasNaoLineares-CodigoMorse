package com.tde.listasnaolinearescodigomorse;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import java.util.Scanner;

public class TreeVisualizer extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Árvore do Código Morse");
        MorseBST bst = new MorseBST();


        int height = bst.getHeight();
        int canvasHeight = 100 + height * 100;
        int canvasWidth = (int) Math.pow(2, height) * 40;
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        bst.drawTree(canvas);
        Group root = new Group(canvas);
        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        Scanner sc = new Scanner(System.in);
        System.out.print("Digite o código morse (use / para separar palavras): ");
        String morseInput = sc.nextLine();
        System.out.println("Mensagem decodificada: " + bst.decode(morseInput));
        System.out.print("Digite uma palavra para codificar: ");
        String text = sc.nextLine();
        System.out.println("Código Morse: " + bst.encode(text));
    }

    public static void main(String[] args) {
        launch(args);
    }

}