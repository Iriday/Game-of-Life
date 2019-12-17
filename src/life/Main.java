package life;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Main main = new Main();
        main.start();
    }

    private void start() {

        int[] input = input();
        char[][] field = createField(input[0], input[1]);
        output(field);
    }

    private int[] input() {
        Scanner scn = new Scanner(System.in);
        int n;
        int s;
        while (true) {
            n = scn.nextInt();
            s = scn.nextInt();
            if (n > 0) {
                break;
            } else {
                System.out.println("Incorrect input, try again");
            }
        }
        return new int[]{n, s};
    }

    private char[][] createField(int size, int seed) {
        char[][] field = new char[size][size];
        Random random = new Random(seed);
        char aliveCell = 'O';
        char space = ' ';
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = random.nextBoolean() ? aliveCell : space;
            }
        }
        return field;
    }

    private void output(char[][] field) {
        for (char[] l : field) {
            for (char r : l) {
                System.out.print(r);
            }
            System.out.println();
        }
    }
}
