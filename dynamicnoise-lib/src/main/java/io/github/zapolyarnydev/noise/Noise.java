package io.github.zapolyarnydev.noise;

/**
 * Interface for defining noise generators with configurable parameters.
 * This interface is designed to support various noise algorithms like Perlin, Simplex, or Value noise.
 */
public interface Noise {

    /**
     * Sets the seed for the noise generator.
     * The seed is used to initialize the pseudo-random number generator, ensuring reproducibility of the noise.
     * Using the same seed with the same noise algorithm and parameters will produce identical outputs.
     *
     * @param seed The seed value to use. The value can be any integer.
     */
    void setSeed(int seed);

    /**
     * Retrieves the current seed value used by the noise generator.
     * The seed determines the random pattern of the generated noise, allowing reproducibility when reused.
     *
     * @return The currently set seed value.
     */
    int getSeed();

    /**
     * Sets the scale of the noise.
     * The scale controls the "zoom level" of the noise: larger values result in smoother, larger features,
     * while smaller values make the features more frequent and detailed.
     *
     * @param scale The scale value to use. Must be greater than 0.
     *              A scale value that is too small may result in excessive detail, while very large values
     *              may produce overly smooth noise.
     */
    void setScale(int scale);

    /**
     * Retrieves the current scale value.
     * The scale determines the size and frequency of features in the generated noise.
     *
     * @return The current scale value.
     */
    int getScale();

    /**
     * Sets the number of octaves for fractal noise generation.
     * Octaves represent layers of noise at different frequencies and amplitudes.
     * Adding more octaves increases the detail in the noise, but also increases computational cost.
     *
     * @param octaves The number of octaves to use. Must be greater than or equal to 1.
     *                A small number of octaves produces smoother noise, while a larger number
     *                introduces more detail and complexity.
     */
    void setOctaves(int octaves);

    /**
     * Retrieves the current number of octaves used in noise generation.
     * Octaves add layers of detail to the noise, with each successive octave contributing finer details.
     *
     * @return The number of octaves currently set.
     */
    int getOctaves();

    /**
     * Sets the lacunarity of the noise.
     * Lacunarity controls the increase in frequency between successive octaves.
     * Values greater than 1.0 cause each octave to contribute higher-frequency details,
     * while values less than 1.0 reduce the frequency of higher octaves, making the noise smoother.
     *
     * @param lacunarity The lacunarity value to use.
     *                   Typical values are >= 1.0, but smaller values (e.g., 0.5) can produce smoother transitions.
     *                   A value of exactly 1.0 will make all octaves have the same frequency.
     */
    void setLacunarity(double lacunarity);

    /**
     * Retrieves the current lacunarity value.
     * Lacunarity determines how quickly the frequency of the noise increases across octaves.
     * Higher lacunarity values emphasize finer details, while lower values smooth out transitions.
     *
     * @return The current lacunarity value.
     */
    double getLacunarity();

    /**
     * Sets the persistence of the noise.
     * Persistence controls how much each successive octave contributes to the final noise.
     * A lower persistence value causes higher octaves to fade out more quickly, resulting in smoother noise.
     * Higher persistence values emphasize finer details by retaining the influence of higher octaves.
     *
     * @param persistence The persistence value to use.
     *                    Typical values range between 0.0 and 1.0.
     *                    Values greater than 1.0 can be used to amplify the contribution of higher octaves,
     *                    creating more pronounced details, though this may reduce naturalness.
     */
    void setPersistence(double persistence);

    /**
     * Retrieves the current persistence value.
     * Persistence determines how quickly the amplitude of successive octaves decreases,
     * affecting the balance between large and small details in the noise.
     *
     * @return The current persistence value.
     */
    double getPersistence();

    /**
     * Resets all parameters of the noise generator to their default values.
     * The default values are implementation-specific and depend on the type of noise algorithm being used.
     */
    void resetDefaults();
}
