package io.github.zapolyarnydev.writer.impl;

import io.github.zapolyarnydev.info.ValueNoiseInfo;
import io.github.zapolyarnydev.writer.NoiseWriter;

import java.util.Random;

public class ValueNoiseWriter implements NoiseWriter {

    private final ValueNoiseInfo noiseInfo;
    private final Random random;
    private final int[] permutationTable;

    public ValueNoiseWriter(ValueNoiseInfo info) {
        this.noiseInfo = info;
        this.random = new Random(noiseInfo.seed());

        permutationTable = new int[512];
        int[] p = new int[256];
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }
        shufflePermutation(p);
        System.arraycopy(p, 0, permutationTable, 0, 256);
        System.arraycopy(p, 0, permutationTable, 256, 256);
    }

    private void shufflePermutation(int[] p) {
        for (int i = 255; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = p[i];
            p[i] = p[j];
            p[j] = tmp;
        }
    }

    @Override
    public void write(Object array) {
        if (array instanceof double[] doubleArray) {
            write1D(doubleArray);
        } else if (array instanceof double[][] doubleArray) {
            write2D(doubleArray);
        } else if (array instanceof double[][][] doubleArray) {
            write3D(doubleArray);
        }
    }

    private void write1D(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = generateNoise(i, 0, 0);
        }
    }

    private void write2D(double[][] array) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                array[x][y] = generateNoise(x, y, 0);
            }
        }
    }

    private void write3D(double[][][] array) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                for (int z = 0; z < array[x][y].length; z++) {
                    array[x][y][z] = generateNoise(x, y, z);
                }
            }
        }
    }

    private double generateNoise(int x, int y, int z) {
        double amplitude = 1.0;
        double frequency = 1.0 / noiseInfo.scale();
        double value = 0.0;
        double maxAmplitude = 0.0;

        for (int octave = 0; octave < noiseInfo.octaves(); octave++) {
            double sampleX = x * frequency;
            double sampleY = y * frequency;
            double sampleZ = z * frequency;

            double noiseValue = interpolateBicubic(sampleX, sampleY, sampleZ);
            value += noiseValue * amplitude;
            maxAmplitude += amplitude;

            amplitude *= noiseInfo.persistence();
            frequency *= noiseInfo.lacunarity();
        }

        return value / maxAmplitude;
    }

    private double interpolateBicubic(double x, double y, double z) {
        int X0 = fastFloor(x) & 255;
        int Y0 = fastFloor(y) & 255;
        int Z0 = fastFloor(z) & 255;

        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);
        double zf = z - Math.floor(z);

        double[] values = new double[4 * 4 * 4];
        for (int i = -1; i <= 2; i++) {
            for (int j = -1; j <= 2; j++) {
                for (int k = -1; k <= 2; k++) {
                    int X = (X0 + i) & 255;
                    int Y = (Y0 + j) & 255;
                    int Z = (Z0 + k) & 255;
                    values[(i + 1) * 16 + (j + 1) * 4 + (k + 1)] = value(permutationTable[X + permutationTable[Y + permutationTable[Z]]]);
                }
            }
        }

        return tricubicInterpolate(values, xf, yf, zf);
    }

    private double value(int hash) {
        return (hash & 255) / 255.0;
    }

    private double tricubicInterpolate(double[] values, double x, double y, double z) {
        double[] arr = new double[4];
        for (int i = 0; i < 4; i++) {
            double[] arr2 = new double[4];
            for (int j = 0; j < 4; j++) {
                double[] arr3 = new double[4];
                for (int k = 0; k < 4; k++) {
                    arr3[k] = values[i * 16 + j * 4 + k];
                }
                arr2[j] = cubicInterpolate(arr3, z);
            }
            arr[i] = cubicInterpolate(arr2, y);
        }
        return cubicInterpolate(arr, x);
    }

    private double cubicInterpolate(double[] values, double t) {
        double t2 = t * t;
        double t3 = t2 * t;
        return values[1] + 0.5 * t * (values[2] - values[0] + t * (2.0 * values[0] - 5.0 * values[1] + 4.0 * values[2] - values[3] + t * (3.0 * (values[1] - values[2]) + values[3] - values[0])));
    }

    private int fastFloor(double value) {
        return value > 0 ? (int) value : (int) value - 1;
    }
}