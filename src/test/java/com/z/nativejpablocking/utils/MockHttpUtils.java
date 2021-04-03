package com.z.nativejpablocking.utils;

import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

public class MockHttpUtils {
    public static MockHttpServletRequestBuilder get(String url, MediaType mediaType) {
        return MockMvcRequestBuilders.get(url).contentType(mediaType);
    }

    public static MockHttpServletRequestBuilder get(String url) {
        return MockHttpUtils.get(url, MediaType.APPLICATION_JSON);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder post(String url, byte[] content) {
        return MockMvcRequestBuilders
                .post(new URI(url))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

    @SneakyThrows
    public static MockHttpServletRequestBuilder put(String url, byte[] content) {
        return MockMvcRequestBuilders
                .put(new URI(url))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }

}
