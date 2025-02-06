package io.github.zapolyarnydev.generator;

import io.github.zapolyarnydev.info.*;
import io.github.zapolyarnydev.noise.Noise;
import io.github.zapolyarnydev.noise.perlin.PerlinNoise;
import io.github.zapolyarnydev.noise.simplex.SimplexNoise;
import io.github.zapolyarnydev.noise.value.ValueNoise;
import io.github.zapolyarnydev.noise.white.WhiteNoise;
import io.github.zapolyarnydev.writer.NoiseWriter;
import io.github.zapolyarnydev.writer.impl.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

class WriterManager {

    private static final Map<Class<? extends Noise>, Class<? extends NoiseWriter>> registeredWriters = Map.of(
            PerlinNoise.class, PerlinNoiseWriter.class,
            SimplexNoise.class, SimplexNoiseWriter.class,
            ValueNoise.class, ValueNoiseWriter.class,
            WhiteNoise.class, WhiteNoiseWriter.class
    );

    private static final Map<Class<? extends Noise>, Class<?>> noiseInfoMap = Map.of(
            PerlinNoise.class, PerlinNoiseInfo.class,
            SimplexNoise.class, SimplexNoiseInfo.class,
            ValueNoise.class, ValueNoiseInfo.class,
            WhiteNoise.class, WhiteNoiseInfo.class
    );

    public static NoiseWriter getWriter(Noise noise) {
        Class<? extends NoiseWriter> writerClass = registeredWriters.get(noise.getClass());
        Class<?> infoClass = noiseInfoMap.get(noise.getClass());

        if (writerClass == null || infoClass == null) {
            return null;
        }

        Object info = createNoiseInfo(noise, infoClass);
        return instantiateWriter(writerClass, info);
    }

    private static Object createNoiseInfo(Noise noise, Class<?> infoClass) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("seed", noise.getSeed());
        params.put("scale", noise.getScale());
        params.put("octaves", noise.getOctaves());
        params.put("lacunarity", noise.getLacunarity());
        params.put("persistence", noise.getPersistence());


        return instantiateInfo(infoClass, params);
    }

    private static Object instantiateInfo(Class<?> infoClass, Map<String, Object> params) {
        try {
            for (Constructor<?> constructor : infoClass.getConstructors()) {
                if (constructor.getParameterCount() == params.size()) {
                    return constructor.newInstance(params.values().toArray());
                }
            }
            throw new RuntimeException("No matching constructor found for " + infoClass.getSimpleName());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create noise info instance", e);
        }
    }

    private static NoiseWriter instantiateWriter(Class<? extends NoiseWriter> writerClass, Object info) {
        try {
            return writerClass.getConstructor(info.getClass()).newInstance(info);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Failed to create noise writer instance", e);
        }
    }
}
