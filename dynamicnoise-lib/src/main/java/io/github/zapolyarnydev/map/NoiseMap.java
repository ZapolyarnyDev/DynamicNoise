package io.github.zapolyarnydev.map;

import lombok.Getter;
import io.github.zapolyarnydev.thread.VirtualThreadExecutor;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * This class represents a noise map with support for 1D, 2D, and 3D arrays.
 */
public class NoiseMap {

    private final Object map;

    /**
     * The dimension of the noise map. Must be between 1 and 3.
     *
     * @return The dimension of the noise map.
     */
    @Getter
    private final int dimensionSize;

    /**
     * The size of the noise map.
     *
     * @return The size of the noise map.
     */
    @Getter
    private final int mapSize;

    /**
     * Creates a new noise map from a given array.
     * The array must be one of the following types: double[], double[][], or double[][][].
     *
     * @param map The array to initialize the noise map with.
     * @throws IllegalArgumentException If the array is not of type double[], double[][], or double[][][].
     */
    public NoiseMap(Object map) throws IllegalArgumentException {
        switch (map) {
            case double[] doubles -> {
                this.map = map;
                this.dimensionSize = 1;
                this.mapSize = doubles.length;
            }
            case double[][] doubles -> {
                this.map = map;
                this.dimensionSize = 2;
                this.mapSize = doubles.length;
            }
            case double[][][] doubles -> {
                this.map = map;
                this.dimensionSize = 3;
                this.mapSize = doubles.length;
            }
            case null, default ->
                    throw new IllegalArgumentException("Invalid map type. Map must be of type double[], double[][], or double[][][]");
        }

        if (mapSize < 32) {
            throw new IllegalArgumentException("Invalid map size: " + mapSize + ". Map size must be 32 or more.");
        }
    }

    /**
     * Retrieves the noise map based on the dimension size.
     *
     * @param <T> The type of the returned map (either double[], double[][], or double[][][]).
     * @return The noise map array of the appropriate dimension.
     */
    @SuppressWarnings("unchecked")
    public <T> T getDoubleArray() {
        return (T) map;
    }

    /**
     * Normalizes the values in the map to the specified range.
     *
     * @param lowerBound The lower bound of the normalization range.
     * @param upperBound The upper bound of the normalization range.
     * @throws IllegalArgumentException If the lowerBound is greater than the upperBound.
     */
    public void normalize(double lowerBound, double upperBound) {
        if (lowerBound > upperBound) {
            throw new IllegalArgumentException("Lower bound must be less than upper bound.");
        }

        if (map instanceof double[]) {
            normalize1D(lowerBound, upperBound);
        } else if (map instanceof double[][]) {
            normalize2D(lowerBound, upperBound);
        } else if (map instanceof double[][][]) {
            normalize3D(lowerBound, upperBound);
        }
    }

    private void normalize1D(double lowerBound, double upperBound) {
        double[] map1D = (double[]) map;
        if (map1D.length == 0) return;

        double min = Arrays.stream(map1D).min().orElse(Double.MAX_VALUE);
        double max = Arrays.stream(map1D).max().orElse(Double.MIN_VALUE);

        if (min == max) {
            Arrays.fill(map1D, lowerBound);
            return;
        }

        for (int i = 0; i < map1D.length; i++) {
            map1D[i] = lowerBound + (map1D[i] - min) / (max - min) * (upperBound - lowerBound);
        }
    }

    private void normalize2D(double lowerBound, double upperBound) {
        double[][] map2D = (double[][]) map;
        if (map2D.length == 0) return;

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double[] row : map2D) {
            for (double value : row) {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }

        if (min == max) {
            for (double[] row : map2D) {
                Arrays.fill(row, lowerBound);
            }
            return;
        }

        for (int i = 0; i < map2D.length; i++) {
            for (int j = 0; j < map2D[i].length; j++) {
                map2D[i][j] = lowerBound + (map2D[i][j] - min) / (max - min) * (upperBound - lowerBound);
            }
        }
    }

    private void normalize3D(double lowerBound, double upperBound) {
        double[][][] map3D = (double[][][]) map;
        if (map3D.length == 0) return;

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double[][] slice : map3D) {
            for (double[] row : slice) {
                for (double value : row) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }
            }
        }

        if (min == max) {
            for (double[][] slice : map3D) {
                for (double[] row : slice) {
                    Arrays.fill(row, lowerBound);
                }
            }
            return;
        }

        for (int i = 0; i < map3D.length; i++) {
            for (int j = 0; j < map3D[i].length; j++) {
                for (int k = 0; k < map3D[i][j].length; k++) {
                    map3D[i][j][k] = lowerBound + (map3D[i][j][k] - min) / (max - min) * (upperBound - lowerBound);
                }
            }
        }
    }

    /**
     * Asynchronously normalizes the noise map using a virtual stream.
     *
     * @param lowerBound The lower bound of the normalization range.
     * @param upperBound The upper bound of the normalization range.
     * @return A CompletableFuture that completes when normalization is done.
     */
    public CompletableFuture<Void> normalizeAsync(double lowerBound, double upperBound) {
        Executor executor = new VirtualThreadExecutor();
        return CompletableFuture.runAsync(() -> normalize(lowerBound, upperBound), executor);
    }

    /**
     * Combines this noise map with another noise map using a specified weight.
     * The other noise map must have the same dimensions and size.
     *
     * @param other  The noise map to combine with.
     * @param weight The weight factor for combining. Can be negative or positive.
     * @throws IllegalArgumentException If the dimensions or sizes do not match.
     */
    public void combine(NoiseMap other, double weight) {
        if (this.dimensionSize != other.dimensionSize || this.mapSize != other.mapSize) {
            throw new IllegalArgumentException("Noise maps must have the same dimensions and size to combine.");
        }

        if (map instanceof double[] map1D && other.map instanceof double[] other1D) {
            for (int i = 0; i < map1D.length; i++) {
                map1D[i] += other1D[i] * weight;
            }
        } else if (map instanceof double[][] map2D && other.map instanceof double[][] other2D) {
            for (int i = 0; i < map2D.length; i++) {
                for (int j = 0; j < map2D[i].length; j++) {
                    map2D[i][j] += other2D[i][j] * weight;
                }
            }
        } else if (map instanceof double[][][] map3D && other.map instanceof double[][][] other3D) {
            for (int i = 0; i < map3D.length; i++) {
                for (int j = 0; j < map3D[i].length; j++) {
                    for (int k = 0; k < map3D[i][j].length; k++) {
                        map3D[i][j][k] += other3D[i][j][k] * weight;
                    }
                }
            }
        }
    }

    /**
     * Asynchronously combines this noise map with another noise map using a specified weight.
     * The other noise map must have the same dimensions and size.
     *
     * @param other  The noise map to combine with.
     * @param weight The weight factor for combining. Can be negative or positive.
     * @return A CompletableFuture that completes when combination is done.
     */
    public CompletableFuture<Void> combineAsync(NoiseMap other, double weight) {
        Executor executor = new VirtualThreadExecutor();
        return CompletableFuture.runAsync(() -> combine(other, weight), executor);
    }
}
