package life;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Main main = new Main();
        main.start();
    }

    private void start() {
        int input = input();
        clearConsole();
        Generator generator = new Generator(input);

        char[][] generation = generator.createFirstGeneration();
        System.out.printf("Generation #%d\n", generator.getGeneration());
        System.out.printf("Alive: %d\n\n", generator.getAlive());
        output(generation);

        for (int i = 1; i < 10; i++) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Something went wrong");
            }
            clearConsole();
            generation = generator.nextGeneration(generation);
            System.out.printf("Generation #%d\n", generator.getGeneration());
            System.out.printf("Alive: %d\n\n", generator.getAlive());
            output(generation);
        }
    }

    private int input() {
        Scanner scn = new Scanner(System.in);
        int n;  //size
        //int m;  //number of generations
        while (true) {
            // System.out.println("Input: universe size(size = size * size), random number(seed), number of generations. Example: 10 10 10");
            n = scn.nextInt();
            //m = scn.nextInt();
            if (n > 0) {
                break;
            } else {
                System.out.println("Incorrect input, try again");
            }
        }
        return n;
    }

    private void output(char[][] field) {
        for (char[] l : field) {
            for (char r : l) {
                System.out.print(r);
            }
            System.out.println();
        }
    }

    private void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}
