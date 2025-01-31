package io.github.zapolyarnydev.writer.impl;

import io.github.zapolyarnydev.algorithm.impl.PerlinNoiseAlgorithm;
import io.github.zapolyarnydev.info.PerlinNoiseInfo;
import io.github.zapolyarnydev.writer.NoiseWriter;

import java.util.Random;

public class PerlinNoiseWriter implements NoiseWriter {

    private final PerlinNoiseInfo noiseInfo;
    public PerlinNoiseWriter(PerlinNoiseInfo noiseInfo){
        this.noiseInfo = noiseInfo;
    }

    @Override
    public void write(Object array) {
        if(array instanceof double[] doubleArray) write1D(doubleArray);
        else if (array instanceof double[][] doubleArray) write2D(doubleArray);
        else if (array instanceof double[][][] doubleArray) write3D(doubleArray);
    }

    private void write1D(double[] array) {
        int length = array.length;

        Random random = new Random(noiseInfo.seed());
        PerlinNoiseAlgorithm perlinNoise = new PerlinNoiseAlgorithm(random);

        double scale = noiseInfo.scale();
        int octaves = noiseInfo.octaves();
        double lacunarity = noiseInfo.lacunarity();
        double persistence = noiseInfo.persistence();

        for (int x = 0; x < length; x++) {
            double noiseValue = 0;
            double amplitude = 1.0;
            double frequency = scale;

            for (int octave = 0; octave < octaves; octave++) {
                noiseValue += perlinNoise.noise((x + noiseInfo.seed()) / frequency, 0) * amplitude;
                amplitude *= persistence;
                frequency *= lacunarity;
            }

            array[x] = noiseValue;
        }
    }

    private void write2D(double[][] array) {
        int width = array.length;
        int height = array[0].length;

        Random random = new Random(noiseInfo.seed());
        PerlinNoiseAlgorithm perlinNoise = new PerlinNoiseAlgorithm(random);

        double scale = noiseInfo.scale();
        int octaves = noiseInfo.octaves();
        double lacunarity = noiseInfo.lacunarity();
        double persistence = noiseInfo.persistence();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double noiseValue = 0;
                double amplitude = 1.0;
                double frequency = scale;

                for (int octave = 0; octave < octaves; octave++) {
                    noiseValue += perlinNoise.noise((x + noiseInfo.seed()) / frequency, (y + noiseInfo.seed()) / frequency) * amplitude;
                    amplitude *= persistence;
                    frequency *= lacunarity;
                }

                array[x][y] = noiseValue;
            }
        }
    }

    private void write3D(double[][][] array) {
        int width = array.length;
        int height = array[0].length;
        int depth = array[0][0].length;

        Random random = new Random(noiseInfo.seed());
        PerlinNoiseAlgorithm perlinNoise = new PerlinNoiseAlgorithm(random);

        double scale = noiseInfo.scale();
        int octaves = noiseInfo.octaves();
        double lacunarity = noiseInfo.lacunarity();
        double persistence = noiseInfo.persistence();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    double noiseValue = 0;
                    double amplitude = 1.0;
                    double frequency = scale;

                    for (int octave = 0; octave < octaves; octave++) {
                        noiseValue += perlinNoise.noise((x + noiseInfo.seed()) / frequency, (y + noiseInfo.seed() + z) / frequency) * amplitude;
                        amplitude *= persistence;
                        frequency *= lacunarity;
                    }

                    array[x][y][z] = noiseValue;
                }
            }
        }
    }
}
