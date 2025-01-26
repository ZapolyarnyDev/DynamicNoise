package io.github.zapolyarnydev.noise.simplex;

import lombok.Data;
import io.github.zapolyarnydev.noise.Noise;

import java.util.Random;

/**
 * Implementation of the {@link Noise} interface using Simplex noise.
 *
 * <p>Simplex noise is a gradient-based noise algorithm, an improvement over Perlin noise,
 * offering better computational efficiency and fewer artifacts in higher dimensions.
 * It is widely used in procedural generation for applications like terrain, texture synthesis, and animations.</p>
 *
 * <p>The behavior of the Simplex noise generator can be customized with the following parameters:
 * <ul>
 *     <li><strong>Seed</strong>: Determines the random pattern of the noise.</li>
 *     <li><strong>Scale</strong>: Controls the frequency of the noise; larger values result in smoother, larger features.</li>
 *     <li><strong>Octaves</strong>: Specifies the number of layers of noise, adding finer details with each octave.</li>
 *     <li><strong>Lacunarity</strong>: Controls the increase in frequency between successive octaves.</li>
 *     <li><strong>Persistence</strong>: Determines the amplitude of successive octaves.</li>
 * </ul>
 * </p>
 *
 * <p>This class includes a default constructor that initializes the noise generator with preset default values
 * and supports resetting parameters to defaults at any time.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 * SimplexNoise noise = new SimplexNoise();
 * noise.setSeed(12345); // Set a custom seed
 * noise.resetDefaults(); // Reset parameters to default values
 * }</pre>
 * </p>
 *
 */
@Data
public class SimplexNoise implements Noise {

    private int seed;
    private int scale;
    private int octaves;
    private double lacunarity;
    private double persistence;
    private final Random random = new Random();

    /**
     * Default constructor initializing the Simplex noise generator with default parameters:
     * <ul>
     *     <li>Seed: Randomly generated</li>
     *     <li>Scale: 16</li>
     *     <li>Octaves: 3</li>
     *     <li>Lacunarity: 2.0</li>
     *     <li>Persistence: 0.5</li>
     * </ul>
     */
    public SimplexNoise() {
        this.seed = random.nextInt();
        this.scale = 16;
        this.octaves = 3;
        this.lacunarity = 2;
        this.persistence = 0.5;
    }

    /**
     * Resets all parameters of the Simplex noise generator to their default values:
     * <ul>
     *     <li>Seed: Randomly generated</li>
     *     <li>Scale: 16</li>
     *     <li>Octaves: 3</li>
     *     <li>Lacunarity: 2.0</li>
     *     <li>Persistence: 0.5</li>
     * </ul>
     */
    @Override
    public void resetDefaults() {
        this.seed = random.nextInt();
        this.scale = 16;
        this.octaves = 3;
        this.lacunarity = 2;
        this.persistence = 0.5;
    }
}
