package com.z.nativejpablocking.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class MockHttpUtils {
    public static MockHttpServletRequestBuilder get(String url, MediaType mediaType) {
        return MockMvcRequestBuilders.get(url).contentType(mediaType);
    }

    public static MockHttpServletRequestBuilder get(String url) {
        return MockHttpUtils.get(url, MediaType.APPLICATION_JSON);
    }

}
