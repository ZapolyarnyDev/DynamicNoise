package me.zapolyarny.dynamicnoise.lib.generator;

import me.zapolyarny.dynamicnoise.lib.noise.Noise;
import me.zapolyarny.dynamicnoise.lib.noise.perlin.PerlinNoise;
import me.zapolyarny.dynamicnoise.lib.noise.simplex.SimplexNoise;
import me.zapolyarny.dynamicnoise.core.info.PerlinNoiseInfo;
import me.zapolyarny.dynamicnoise.core.info.SimplexNoiseInfo;
import me.zapolyarny.dynamicnoise.core.writer.NoiseWriter;
import me.zapolyarny.dynamicnoise.core.writer.PerlinNoiseWriter;
import me.zapolyarny.dynamicnoise.core.writer.SimplexNoiseWriter;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

class WriterManager {

    private static Map<Class<? extends Noise>, Class<? extends NoiseWriter>> registeredWriters = Map.ofEntries(
            Map.entry(PerlinNoise.class, PerlinNoiseWriter.class),
            Map.entry(SimplexNoise.class, SimplexNoiseWriter.class)
    );

    public static NoiseWriter getWriter(Noise noise) {
        if(noise instanceof PerlinNoise perlinNoise) {
            return getPerlinWriter(perlinNoise);
        } else if (noise instanceof SimplexNoise simplexNoise) {
            return getSimplexWriter(simplexNoise);
        }
        return null;
    }

    private static NoiseWriter getPerlinWriter(PerlinNoise perlinNoise){
        int seed = perlinNoise.getSeed();
        int scale = perlinNoise.getScale();
        int octaves = perlinNoise.getOctaves();
        double lacunarity = perlinNoise.getLacunarity();
        double persistence = perlinNoise.getPersistence();

        PerlinNoiseInfo info = new PerlinNoiseInfo(seed, scale ,octaves, lacunarity, persistence);
        try {
            return registeredWriters.get(perlinNoise.getClass()).getConstructor(PerlinNoiseInfo.class).newInstance(info);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static NoiseWriter getSimplexWriter(SimplexNoise simplexNoise){
        int seed = simplexNoise.getSeed();
        int scale = simplexNoise.getScale();
        int octaves = simplexNoise.getOctaves();
        double lacunarity = simplexNoise.getLacunarity();
        double persistence = simplexNoise.getPersistence();

        SimplexNoiseInfo info = new SimplexNoiseInfo(seed, scale ,octaves, lacunarity, persistence);
        try {
            return registeredWriters.get(simplexNoise.getClass()).getConstructor(SimplexNoiseInfo.class).newInstance(info);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
