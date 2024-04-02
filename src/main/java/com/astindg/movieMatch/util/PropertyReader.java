package com.astindg.movieMatch.util;

import org.springframework.core.env.Environment;

import java.util.Objects;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

public class PropertyReader {
    public static String getProperty(Environment env, String propertyKey){
        return new String(
                Objects.requireNonNull(
                        env.getProperty(propertyKey)
                ).getBytes(ISO_8859_1), UTF_8
        );
    }
}
