package io.github.zapolyarnydev.algorithm.impl;

import io.github.zapolyarnydev.algorithm.NoiseAlgorithm;

import java.util.Random;

public class WhiteNoiseAlgorithm implements NoiseAlgorithm {

    private final Random random;

    public WhiteNoiseAlgorithm(Random random) {
        this.random = random;
    }

    @Override
    public double noise(double x, double y) {
        return random.nextDouble() * 2.0 - 1.0;
    }
}
