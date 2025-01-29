package io.github.zapolyarnydev.generator;

import io.github.zapolyarnydev.info.ValueNoiseInfo;
import io.github.zapolyarnydev.noise.Noise;
import io.github.zapolyarnydev.noise.perlin.PerlinNoise;
import io.github.zapolyarnydev.noise.simplex.SimplexNoise;
import io.github.zapolyarnydev.info.PerlinNoiseInfo;
import io.github.zapolyarnydev.info.SimplexNoiseInfo;
import io.github.zapolyarnydev.noise.value.ValueNoise;
import io.github.zapolyarnydev.writer.NoiseWriter;
import io.github.zapolyarnydev.writer.PerlinNoiseWriter;
import io.github.zapolyarnydev.writer.SimplexNoiseWriter;
import io.github.zapolyarnydev.writer.ValueNoiseWriter;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

class WriterManager {

    private static Map<Class<? extends Noise>, Class<? extends NoiseWriter>> registeredWriters = Map.ofEntries(
            Map.entry(PerlinNoise.class, PerlinNoiseWriter.class),
            Map.entry(SimplexNoise.class, SimplexNoiseWriter.class),
            Map.entry(ValueNoise.class, ValueNoiseWriter.class)
    );

    public static NoiseWriter getWriter(Noise noise) {
        if(noise instanceof PerlinNoise perlinNoise) {
            return getPerlinWriter(perlinNoise);
        } else if (noise instanceof SimplexNoise simplexNoise) {
            return getSimplexWriter(simplexNoise);
        } else if (noise instanceof ValueNoise valueNoise) {
            return getValueWriter(valueNoise);
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

    private static NoiseWriter getValueWriter(ValueNoise valueNoise){
        int seed = valueNoise.getSeed();
        int scale = valueNoise.getScale();
        int octaves = valueNoise.getOctaves();
        double lacunarity = valueNoise.getLacunarity();
        double persistence = valueNoise.getPersistence();

        ValueNoiseInfo info = new ValueNoiseInfo(seed, scale ,octaves, lacunarity, persistence);
        try {
            return registeredWriters.get(valueNoise.getClass()).getConstructor(ValueNoiseInfo.class).newInstance(info);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
