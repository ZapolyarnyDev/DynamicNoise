package io.github.zapolyarnydev.writer.impl;

import java.util.Random;
import io.github.zapolyarnydev.info.SimplexNoiseInfo;
import io.github.zapolyarnydev.writer.NoiseWriter;

public class SimplexNoiseWriter implements NoiseWriter {

    private final Random random;
    private final double lacunarity;
    private final int octaves;
    private final double persistence;
    private final double scale;

    private final int[] perm;
    private final int[] p;

    public SimplexNoiseWriter(SimplexNoiseInfo noiseInfo) {
        this.random = new Random(noiseInfo.seed());
        this.lacunarity = noiseInfo.lacunarity();
        this.octaves = noiseInfo.octaves();
        this.persistence = noiseInfo.persistence();
        this.scale = noiseInfo.scale();

        perm = new int[512];
        p = new int[256];

        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }

        for (int i = 0; i < 512; i++) {
            perm[i] = p[i % 256];
        }
    }

    @Override
    public void write(Object array) {
        switch (array) {
            case double[] doubleArray -> write1D(doubleArray);
            case double[][] doubleArray -> write2D(doubleArray);
            case double[][][] doubleArray -> write3D(doubleArray);
            case null, default ->
                    throw new IllegalArgumentException("Unsupported array type: " + array.getClass().getName());
        }
    }

    private void write1D(double[] array) {
        for (int i = 0; i < array.length; i++) {
            double x = i / scale;
            array[i] = generateNoise(x, 0, 0);
        }
    }

    private void write2D(double[][] array) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                double nx = x / scale;
                double ny = y / scale;
                array[x][y] = generateNoise(nx, ny, 0);
            }
        }
    }

    private void write3D(double[][][] array) {
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                for (int z = 0; z < array[x][y].length; z++) {
                    double nx = x / scale;
                    double ny = y / scale;
                    double nz = z / scale;
                    array[x][y][z] = generateNoise(nx, ny, nz);
                }
            }
        }
    }

    private double generateNoise(double x, double y, double z) {
        double totalNoise = 0;
        double frequency = 1;
        double amplitude = 1;

        for (int i = 0; i < octaves; i++) {
            totalNoise += noise(x * frequency, y * frequency, z * frequency) * amplitude;
            frequency *= lacunarity;
            amplitude *= persistence;
        }

        return totalNoise;
    }

    private double noise(double x, double y, double z) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        int Z = (int) Math.floor(z) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);
        z -= Math.floor(z);

        double u = fade(x);
        double v = fade(y);
        double w = fade(z);

        int A = perm[X] + Y;
        int AA = perm[A] + Z;
        int AB = perm[A + 1] + Z;
        int B = perm[X + 1] + Y;
        int BA = perm[B] + Z;
        int BB = perm[B + 1] + Z;

        return lerp(w, lerp(v, lerp(u, grad(perm[AA], x, y, z), grad(perm[BA], x - 1, y, z)),
                        lerp(u, grad(perm[AB], x, y - 1, z), grad(perm[BB], x - 1, y - 1, z))),
                lerp(v, lerp(u, grad(perm[AA + 1], x, y, z - 1), grad(perm[BA + 1], x - 1, y, z - 1)),
                        lerp(u, grad(perm[AB + 1], x, y - 1, z - 1), grad(perm[BB + 1], x - 1, y - 1, z - 1))));
    }

    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y, double z) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
