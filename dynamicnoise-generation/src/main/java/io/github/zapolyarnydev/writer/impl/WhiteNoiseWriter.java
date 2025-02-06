package io.github.zapolyarnydev.writer.impl;

import io.github.zapolyarnydev.info.WhiteNoiseInfo;
import io.github.zapolyarnydev.writer.NoiseWriter;

import java.util.Random;

public class WhiteNoiseWriter implements NoiseWriter {

    private final WhiteNoiseInfo noiseInfo;

    public WhiteNoiseWriter(WhiteNoiseInfo info) {
        this.noiseInfo = info;
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
        Random random = new Random(noiseInfo.seed());
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextDouble() * 2 - 1;
        }
    }

    private void write2D(double[][] array) {
        Random random = new Random(noiseInfo.seed());
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                array[x][y] = random.nextDouble() * 2 - 1;
            }
        }
    }

    private void write3D(double[][][] array) {
        Random random = new Random(noiseInfo.seed());
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[x].length; y++) {
                for (int z = 0; z < array[x][y].length; z++) {
                    array[x][y][z] = random.nextDouble() * 2 - 1;
                }
            }
        }
    }
}
