package io.github.zapolyarnydev.algorithm.impl;

import io.github.zapolyarnydev.algorithm.NoiseAlgorithm;

import java.util.Random;

public class PerlinNoiseAlgorithm implements NoiseAlgorithm {

    private final int[] permutationTable;
    private final int[] permutation;
    private final Random random;

    public PerlinNoiseAlgorithm(Random random) {
        this.random = random;
        permutationTable = new int[512];
        permutation = new int[256];
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }
        shufflePermutation();
        System.arraycopy(permutation, 0, permutationTable, 0, 256);
        System.arraycopy(permutation, 0, permutationTable, 256, 256);
    }

    private void shufflePermutation() {
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = tmp;
        }
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : (h == 12 || h == 14) ? x : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }

    @Override
    public double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);
        double u = fade(xf);
        double v = fade(yf);

        int aa = permutationTable[X + permutationTable[Y]];
        int ab = permutationTable[X + permutationTable[Y + 1]];
        int ba = permutationTable[X + 1 + permutationTable[Y]];
        int bb = permutationTable[X + 1 + permutationTable[Y + 1]];

        double x1 = lerp(grad(aa, xf, yf), grad(ba, xf - 1, yf), u);
        double x2 = lerp(grad(ab, xf, yf - 1), grad(bb, xf - 1, yf - 1), u);

        return lerp(x1, x2, v);
    }
}
