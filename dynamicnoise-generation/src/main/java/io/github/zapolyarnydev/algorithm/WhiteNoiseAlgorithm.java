package io.github.zapolyarnydev.algorithm;

import java.util.Random;

public class WhiteNoiseAlgorithm {

    private final Random random;

    public WhiteNoiseAlgorithm(Random random) {
        this.random = random;
    }

    public double noise(double x, double y) {
        return random.nextDouble() * 2.0 - 1.0;
    }
}
