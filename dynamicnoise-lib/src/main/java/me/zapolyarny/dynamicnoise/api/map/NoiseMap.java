package me.zapolyarny.dynamicnoise.api.map;

import lombok.Getter;
import me.zapolyarny.dynamicnoise.api.thread.VirtualThreadExecutor;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


/**
 * This class represents a noise map with support for 1D, 2D, and 3D arrays.
 * The map's dimension and size are configurable.
 */
public class NoiseMap {

    private Object map;

    /**
     * The dimension of the noise map. Must be between 1 and 3.
     *
     * @return The dimension of the noise map.
     */
    @Getter
    private final int dimensionSize;

    /**
     * The size of the noise map. Must be 32 or more.
     *
     * @return The size of the noise map.
     */
    @Getter
    private final int mapSize;

    /**
     * Creates a new noise map based on the given dimension size and map size.
     * The array dimension must be between 1 and 3, and the map size must be 32 or more.
     *
     * @param dimensionSize The dimension of the map (1, 2, or 3).
     * @param mapSize The size of the arrays (32 or more).
     * @throws IllegalArgumentException If the dimension size is not between 1 and 3, or if the map size is less than 32.
     */
    public NoiseMap(int dimensionSize, int mapSize) throws IllegalArgumentException {
        if (dimensionSize < 1 || dimensionSize > 3) {
            throw new IllegalArgumentException("Invalid map dimension: " + dimensionSize + ". Dimension must be between 1 and 3.");
        }
        if (mapSize < 32) {
            throw new IllegalArgumentException("Invalid map size: " + mapSize + ". Map size must be 32 or more.");
        }

        this.dimensionSize = dimensionSize;
        this.mapSize = mapSize;

        switch (dimensionSize) {
            case 1:
                map = new double[mapSize];
                break;
            case 2:
                map = new double[mapSize][mapSize];
                break;
            case 3:
                map = new double[mapSize][mapSize][mapSize];
                break;
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
     * <p>
     * If all values in the map are the same, they will be set to the lowerBound.
     *
     * @param lowerBound The lower bound of the normalization range.
     * @param upperBound The upper bound of the normalization range.
     * @throws IllegalArgumentException If the lowerBound is greater than the upperBound.
     */
    public void normalize(double lowerBound, double upperBound){
        if(lowerBound > upperBound){
            throw new IllegalArgumentException("Lower bound must be less than upper bound.");
        }

        if(map instanceof double[]) normalize1D(lowerBound, upperBound);
        else if (map instanceof double[][]) normalize2D(lowerBound, upperBound);
        else if (map instanceof double[][][]) normalize3D(lowerBound, upperBound);
    }

    private void normalize1D(double lowerBound, double upperBound){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double[] map1D = (double[]) map;

        if (map1D.length == 0) return;
        if (lowerBound == upperBound) {
            Arrays.fill(map1D, lowerBound);
            return;
        }

        for (double value : map1D) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        for (int i = 0; i < map1D.length; i++) {
            map1D[i] = lowerBound + (map1D[i] - min) / (max - min) * (upperBound - lowerBound);
        }
    }

    private void normalize2D(double lowerBound, double upperBound){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double[][] map2D = (double[][]) map;

        if (map2D.length == 0) return;
        if (lowerBound == upperBound) {
            for (double[] doubles : map2D) {
                Arrays.fill(doubles, lowerBound);
            }
            return;
        }

        for (double[] row : map2D) {
            for (double value : row) {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }

        for (int i = 0; i < map2D.length; i++) {
            for (int j = 0; j < map2D[i].length; j++) {
                map2D[i][j] = lowerBound + (map2D[i][j] - min) / (max - min) * (upperBound - lowerBound);
            }
        }
    }

    private void normalize3D(double lowerBound, double upperBound){
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double[][][] map3D = (double[][][]) map;

        if (map3D.length == 0) return;
        if (lowerBound == upperBound) {
            for (double[][] doubles : map3D) {
                for (double[] aDouble : doubles) {
                    Arrays.fill(aDouble, lowerBound);
                }
            }
            return;
        }

        for (double[][] slice : map3D) {
            for (double[] row : slice) {
                for (double value : row) {
                    min = Math.min(min, value);
                    max = Math.max(max, value);
                }
            }
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
     * Recommended for applications that require a high level of parallelism.
     * This method leverages Java Virtual Threads for lightweight concurrency
     *
     * @param lowerBound The lower bound of the normalization range.
     * @param upperBound The upper bound of the normalization range.
     * @return A CompletableFuture that completes when normalization is done.
     * @throws IllegalArgumentException If the lowerBound is greater than the upperBound.
     */

    public CompletableFuture<Void> normalizeAsync(double lowerBound, double upperBound) {
        Executor executor = new VirtualThreadExecutor();
        return CompletableFuture.runAsync(() -> normalize(lowerBound, upperBound), executor);
    }

}
