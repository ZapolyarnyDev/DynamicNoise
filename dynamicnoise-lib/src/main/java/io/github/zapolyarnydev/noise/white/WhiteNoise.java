package io.github.zapolyarnydev.noise.white;

import io.github.zapolyarnydev.noise.Noise;
import lombok.Data;

import java.util.Random;

/**
 * Implementation of the {@link Noise} interface using White Noise.
 *
 * White Noise is a type of procedural noise where each value is generated independently,
 * resulting in a completely random, uncorrelated pattern. It is useful for various applications,
 * including audio synthesis, dithering, procedural textures, and randomness in simulations.
 *
 * <p>The behavior of the White Noise generator can be customized with the following parameters:</p>
 * <ul>
 *     <li><strong>Seed</strong>: Determines the sequence of random values.</li>
 *     <li><strong>Scale</strong>: Controls the spacing between sampled noise points.</li>
 *     <li><strong>Octaves</strong>: Determines the number of layers of noise, adding finer details with each octave.</li>
 *     <li><strong>Lacunarity</strong>: Controls how the frequency changes between successive octaves.</li>
 *     <li><strong>Persistence</strong>: Determines how much influence each octave has on the final noise value.</li>
 * </ul>
 *
 * <p>This class provides a default constructor that initializes the noise generator with preset values
 * and allows modifying noise parameters dynamically.</p>
 */

@Data
public class WhiteNoise implements Noise {


    private int seed;
    private int scale;
    private int octaves;
    private double lacunarity;
    private double persistence;
    private final Random random = new Random();

    /**
     * Default constructor initializing the White noise generator with default parameters:
     * <ul>
     *     <li>Seed: Randomly generated</li>
     *     <li>Scale: 16</li>
     *     <li>Octaves: 3</li>
     *     <li>Lacunarity: 2.0</li>
     *     <li>Persistence: 0.5</li>
     * </ul>
     */
    public WhiteNoise() {
        this.seed = random.nextInt();
        this.scale = 16;
        this.octaves = 3;
        this.lacunarity = 2;
        this.persistence = 0.5;
    }

    /**
     * Resets all parameters of the White noise generator to their default values:
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
