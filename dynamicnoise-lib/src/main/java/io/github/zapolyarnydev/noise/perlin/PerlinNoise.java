package io.github.zapolyarnydev.noise.perlin;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.github.zapolyarnydev.noise.Noise;

import java.util.Random;

/**
 * Implementation of the {@link Noise} interface using Perlin noise.
 *
 * <p>Perlin noise is a gradient-based noise algorithm widely used in procedural generation
 * for tasks like terrain and texture creation. This implementation supports customization
 * through parameters such as seed, scale, octaves, lacunarity, and persistence.</p>
 *
 * <p>Key features:</p>
 * <ul>
 *     <li><strong>Seed</strong>: Determines the random pattern of the noise.</li>
 *     <li><strong>Scale</strong>: Controls the frequency of the noise features.</li>
 *     <li><strong>Octaves</strong>: Adds layers of detail by combining multiple noise frequencies.</li>
 *     <li><strong>Lacunarity</strong>: Governs the frequency increase between octaves.</li>
 *     <li><strong>Persistence</strong>: Controls the amplitude of successive octaves.</li>
 * </ul>
 *
 * <p>Includes a default constructor for preset initialization and a parameterized constructor for customization.</p>
 */


@Data
@AllArgsConstructor
public class PerlinNoise implements Noise {

    private int seed;
    private int scale;
    private int octaves;
    private double lacunarity;
    private double persistence;
    private final Random random = new Random();

    /**
     * Default constructor initializing the Perlin noise generator with default parameters:
     * <ul>
     *     <li>Seed: Randomly generated</li>
     *     <li>Scale: 16</li>
     *     <li>Octaves: 3</li>
     *     <li>Lacunarity: 2.0</li>
     *     <li>Persistence: 0.5</li>
     * </ul>
     */
    public PerlinNoise() {
        this.seed = random.nextInt();
        this.scale = 16;
        this.octaves = 3;
        this.lacunarity = 2;
        this.persistence = 0.5;
    }

    /**
     * Resets all parameters of the Perlin noise generator to their default values:
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
