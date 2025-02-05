package io.github.zapolyarnydev.generator;

import lombok.Data;
import io.github.zapolyarnydev.map.NoiseMap;
import io.github.zapolyarnydev.noise.Noise;
import io.github.zapolyarnydev.thread.VirtualThreadExecutor;
import io.github.zapolyarnydev.writer.NoiseWriter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * This class is responsible for generating noise and populating it into a noise map
 * or directly into an array. It supports 1D, 2D, and 3D arrays of type {@code double}.
 * The generator allows the use of customizable noise algorithms and range bounds.
 * <p>
 * It also provides asynchronous methods for noise generation, using virtual threads for optimal performance.
 */
@Data
public class NoiseGenerator {

    /**
     * The default noise generator to be used if no specific noise is provided.
     */
    private Noise defaultNoise;



    /**
     * The default lower bound for the noise values.
     */
    private double lowerBound = 0;

    /**
     * The default upper bound for the noise values.
     */
    private double upperBound = 128;

    /**
     * Constructs a noise generator with the specified default noise.
     *
     * @param defaultNoise The default noise generator.
     */
    public NoiseGenerator(Noise defaultNoise) {
        this.defaultNoise = defaultNoise;
    }

    /**
     * Constructs a noise generator with the specified default noise, lower and upper bounds.
     *
     * @param defaultNoise The default noise generator.
     * @param lowerBound The lower bound for the noise values.
     * @param upperBound The upper bound for the noise values.
     */
    public NoiseGenerator(Noise defaultNoise, double lowerBound, double upperBound) {
        this.defaultNoise = defaultNoise;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }


    /**
     * Generates noise for the specified noise map using default settings.
     * The {@link NoiseMap} dimensions and size must already be defined.
     *
     * @param map The {@link NoiseMap} to populate with noise.
     * @throws IllegalArgumentException If {@code defaultNoise} is not set.
     */
    public void generateForMap(NoiseMap map) {
        if (defaultNoise == null) {
            throw new IllegalArgumentException("Default noise generator is not set.");
        }
        NoiseWriter writer = WriterManager.getWriter(defaultNoise);
        writer.write(map.getDoubleArray());

    }


    /**
     * Generates noise for the specified noise map using custom noise and range bounds.
     *
     * @param map        The {@link NoiseMap} to populate with noise.
     * @param noise      The noise generator to use.
     * @param lowerBound The lower bound for noise values.
     * @param upperBound The upper bound for noise values.
     * @throws IllegalArgumentException If the bounds are invalid (e.g., {@code lowerBound > upperBound}).
     */
    public void generateForMap(NoiseMap map, Noise noise, double lowerBound, double upperBound) {
        if (noise == null) {
            throw new IllegalArgumentException("Noise generator cannot be null.");
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
        }
        NoiseWriter writer = WriterManager.getWriter(noise);
        writer.write(map.getDoubleArray());
        map.normalize(lowerBound, upperBound);
    }

    /**
     * Asynchronously generates noise for the specified noise map using default settings.
     * This method utilizes virtual threads for lightweight and scalable asynchronous execution.
     *
     * @param map The {@link NoiseMap} to populate with noise.
     * @return A {@link CompletableFuture} that completes when the noise generation is finished.
     * @throws IllegalArgumentException If {@code defaultNoise} is not set.
     */
    public CompletableFuture<Void> generateForMapAsync(NoiseMap map) {
        Executor executor = new VirtualThreadExecutor();;
        return CompletableFuture.runAsync(() -> generateForMap(map), executor);
    }

    /**
     * Asynchronously generates noise for the specified noise map using custom noise and range bounds.
     * This method utilizes virtual threads for lightweight and scalable asynchronous execution.
     *
     * @param map        The {@link NoiseMap} to populate with noise.
     * @param noise      The noise generator to use.
     * @param lowerBound The lower bound for noise values.
     * @param upperBound The upper bound for noise values.
     * @return A {@link CompletableFuture} that completes when the noise generation is finished.
     * @throws IllegalArgumentException If the bounds are invalid (e.g., {@code lowerBound > upperBound}).
     */
    public CompletableFuture<Void> generateForMapAsync(NoiseMap map, Noise noise, double lowerBound, double upperBound) {
        Executor executor = new VirtualThreadExecutor();
        return CompletableFuture.runAsync(() -> generateForMap(map, noise, lowerBound, upperBound), executor);
    }

    /**
     * Generates noise directly into the specified array using default settings.
     * The array must be of type {@code double[]}, {@code double[][]}, or {@code double[][][]}.
     *
     * @param array The array to populate with noise.
     * @throws IllegalArgumentException If {@code defaultNoise} is not set or the array type is unsupported.
     */
    public void generateForArray(Object array) {
        if (defaultNoise == null) {
            throw new IllegalArgumentException("Default noise generator is not set.");
        }

    }

    /**
     * Generates noise directly into the specified array using custom noise and range bounds.
     * The array must be of type {@code double[]}, {@code double[][]}, or {@code double[][][]}.
     *
     * @param array      The array to populate with noise.
     * @param noise      The noise generator to use.
     * @param lowerBound The lower bound for noise values.
     * @param upperBound The upper bound for noise values.
     * @throws IllegalArgumentException If the bounds are invalid (e.g., {@code lowerBound > upperBound}) or the array type is unsupported.
     */
    public void generateForArray(Object array, Noise noise, double lowerBound, double upperBound) {
        if (noise == null) {
            throw new IllegalArgumentException("Noise generator cannot be null.");
        }
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound.");
        }

    }

    /**
     * Asynchronously generates noise directly into the specified array using default settings.
     * This method utilizes virtual threads for lightweight and scalable asynchronous execution.
     *
     * @param array The array to populate with noise.
     * @return A {@link CompletableFuture} that completes when the noise generation is finished.
     * @throws IllegalArgumentException If {@code defaultNoise} is not set or the array type is unsupported.
     */
    public CompletableFuture<Void> generateForArrayAsync(Object array) {
        Executor executor = new VirtualThreadExecutor();
        return CompletableFuture.runAsync(() -> generateForArray(array), executor);
    }

    /**
     * Asynchronously generates noise directly into the specified array using custom noise and range bounds.
     * This method utilizes virtual threads for lightweight and scalable asynchronous execution.
     *
     * @param array      The array to populate with noise.
     * @param noise      The noise generator to use.
     * @param lowerBound The lower bound for noise values.
     * @param upperBound The upper bound for noise values.
     * @return A {@link CompletableFuture} that completes when the noise generation is finished.
     * @throws IllegalArgumentException If the bounds are invalid (e.g., {@code lowerBound > upperBound}) or the array type is unsupported.
     */
    public CompletableFuture<Void> generateForArrayAsync(Object array, Noise noise, double lowerBound, double upperBound) {
        Executor executor = new VirtualThreadExecutor();
        return CompletableFuture.runAsync(() -> generateForArray(array, noise, lowerBound, upperBound), executor);
    }
}
