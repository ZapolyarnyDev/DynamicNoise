package io.github.zapolyarnydev.noise.value;

import io.github.zapolyarnydev.noise.Noise;
import lombok.Data;

import java.util.Random;

/**
 * Implementation of the {@link Noise} interface using Value Noise.
 *
 * Value Noise is a simple procedural noise algorithm that generates a smooth gradient-based noise
 * by interpolating between random values assigned to a fixed grid of points.
 * It is commonly used for generating textures, terrain height maps, and other procedural effects.
 *
 * <p>The behavior of the Value Noise generator can be customized with the following parameters:</p>
 * <ul>
 *     <li><strong>Seed</strong>: Determines the initial random values at grid points.</li>
 *     <li><strong>Scale</strong>: Controls the spacing between grid points; larger values produce smoother noise.</li>
 *     <li><strong>Octaves</strong>: Determines the number of layers of noise, adding finer details with each octave.</li>
 *     <li><strong>Lacunarity</strong>: Controls how the frequency changes between successive octaves.</li>
 *     <li><strong>Persistence</strong>: Determines how much influence each octave has on the final noise value.</li>
 * </ul>
 *
 * <p>This class provides a default constructor that initializes the noise generator with preset values
 * and allows modifying noise parameters dynamically.</p>
 */

@Data
public class ValueNoise implements Noise {

    private int seed;
    private int scale;
    private int octaves;
    private double lacunarity;
    private double persistence;
    private final Random random = new Random();

    /**
     * Default constructor initializing the Value noise generator with default parameters:
     * <ul>
     *     <li>Seed: Randomly generated</li>
     *     <li>Scale: 16</li>
     *     <li>Octaves: 3</li>
     *     <li>Lacunarity: 2.0</li>
     *     <li>Persistence: 0.5</li>
     * </ul>
     */
    public ValueNoise() {
        this.seed = random.nextInt();
        this.scale = 16;
        this.octaves = 3;
        this.lacunarity = 2;
        this.persistence = 0.5;
    }

    /**
     * Resets all parameters of the Value noise generator to their default values:
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
