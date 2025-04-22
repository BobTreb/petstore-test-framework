package com.petcircle.utils.TestUtils;

import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class TestResourceUtil {
    public static InputStream getResourceStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
