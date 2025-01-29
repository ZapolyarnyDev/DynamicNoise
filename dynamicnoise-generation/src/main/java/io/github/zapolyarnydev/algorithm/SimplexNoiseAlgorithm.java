package io.github.zapolyarnydev.algorithm;

import java.util.Random;

public class SimplexNoiseAlgorithm {

    private static final int[][] GRADIENTS = {
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1},
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    private static final double SKEW_FACTOR = 0.5 * (Math.sqrt(3.0) - 1.0);
    private static final double UNSKEW_FACTOR = (3.0 - Math.sqrt(3.0)) / 6.0;

    private final int[] permutation;

    public SimplexNoiseAlgorithm(Random random) {
        permutation = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }
        shufflePermutation(p, random);
        System.arraycopy(p, 0, permutation, 0, 256);
        System.arraycopy(p, 0, permutation, 256, 256);
    }

    private void shufflePermutation(int[] p, Random random) {
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
    }

    private static double dot(int[] grad, double x, double y) {
        return grad[0] * x + grad[1] * y;
    }

    private static int fastFloor(double value) {
        return value > 0 ? (int) value : (int) value - 1;
    }

    public double noise(double x, double y) {
        double s = (x + y) * SKEW_FACTOR;
        int i = fastFloor(x + s);
        int j = fastFloor(y + s);

        double t = (i + j) * UNSKEW_FACTOR;
        double x0 = x - (i - t);
        double y0 = y - (j - t);

        int i1, j1;
        if (x0 > y0) {
            i1 = 1; j1 = 0;
        } else {
            i1 = 0; j1 = 1;
        }

        double x1 = x0 - i1 + UNSKEW_FACTOR;
        double y1 = y0 - j1 + UNSKEW_FACTOR;
        double x2 = x0 - 1.0 + 2.0 * UNSKEW_FACTOR;
        double y2 = y0 - 1.0 + 2.0 * UNSKEW_FACTOR;

        int gi0 = permutation[(i + permutation[j & 255]) & 255] % 8;
        int gi1 = permutation[(i + i1 + permutation[(j + j1) & 255]) & 255] % 8;
        int gi2 = permutation[(i + 1 + permutation[(j + 1) & 255]) & 255] % 8;

        double t0 = 0.5 - x0 * x0 - y0 * y0;
        double n0;
        if (t0 < 0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * dot(GRADIENTS[gi0], x0, y0);
        }

        double t1 = 0.5 - x1 * x1 - y1 * y1;
        double n1;
        if (t1 < 0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * dot(GRADIENTS[gi1], x1, y1);
        }

        double t2 = 0.5 - x2 * x2 - y2 * y2;
        double n2;
        if (t2 < 0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * dot(GRADIENTS[gi2], x2, y2);
        }

        return 70.0 * (n0 + n1 + n2);
    }
}

