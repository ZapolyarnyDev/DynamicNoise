package io.github.zapolyarnydev.algorithm;

import java.util.Random;

public class ValueNoiseAlgorithm {

    private final int[] permutationTable;

    public ValueNoiseAlgorithm(Random random) {
        permutationTable = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }
        shufflePermutation(p, random);
        System.arraycopy(p, 0, permutationTable, 0, 256);
        System.arraycopy(p, 0, permutationTable, 256, 256);
    }

    private void shufflePermutation(int[] p, Random random) {
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    private int fastFloor(double value) {
        return value > 0 ? (int) value : (int) value - 1;
    }

    public double noise(double x, double y) {
        int X = fastFloor(x) & 255;
        int Y = fastFloor(y) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        double u = fade(xf);
        double v = fade(yf);

        int aa = permutationTable[X + permutationTable[Y]] & 255;
        int ab = permutationTable[X + permutationTable[Y + 1]] & 255;
        int ba = permutationTable[X + 1 + permutationTable[Y]] & 255;
        int bb = permutationTable[X + 1 + permutationTable[Y + 1]] & 255;

        double x1 = lerp(value(aa, xf, yf), value(ba, xf - 1, yf), u);
        double x2 = lerp(value(ab, xf, yf - 1), value(bb, xf - 1, yf - 1), u);

        return lerp(x1, x2, v);
    }

    private double value(int hash, double x, double y) {
        int h = hash & 15;
        return (h < 8 ? x : y);
    }

    private double fade(double value) {
        return value * value * value * (value * (value * 6 - 15) + 10);
    }
}
