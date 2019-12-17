package life;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Main main = new Main();
        main.start();
    }

    private void start() {

        long[] input = input();
        char[][] generation = createFirstGeneration((int) input[0], input[1]);
        //output(generation);

        for (int i = 0; i < input[2]; i++) {
            generation = Generator.nextGeneration(generation);
            // output(generation);
        }
        output(generation);
    }

    private long[] input() {
        Scanner scn = new Scanner(System.in);
        int n;  //size
        long s; //seed
        int m;  //number of generations
        while (true) {
            // System.out.println("Input: universe size(size = size * size), random number(seed), number of generations. Example: 10 10 10");
            n = scn.nextInt();
            s = scn.nextLong();
            m = scn.nextInt();
            if (n > 0 && m >= 0) {
                break;
            } else {
                System.out.println("Incorrect input, try again");
            }
        }
        return new long[]{n, s, m};
    }

    private char[][] createFirstGeneration(int size, long seed) {
        char[][] field = new char[size][size];
        Random random = new Random(seed);

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field.length; j++) {
                field[i][j] = random.nextBoolean() ? 'O' : ' ';
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
