package com.z.nativejpablocking.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockAsyncContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.AsyncListener;
import java.util.List;
import java.util.Map;
import java.util.Random;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIntegrationTest {
    protected static final String DEFAULT_COUNTRY = "AR";
    protected static Map<String, List<String>> citiesByCountry = initCitiesByCountry();
    protected static Random r = new Random();

    protected String port;
    protected ObjectMapper mapper;
    protected MockMvc mockMvc;

    @BeforeAll
    void setup(@Autowired WebApplicationContext context, @Autowired ObjectMapper mapper, @Autowired Environment environment) {
        this.port = environment.getProperty("local.server.port");
        this.mapper = mapper;
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    private static Map<String, List<String>> initCitiesByCountry() {
        return Map.of(DEFAULT_COUNTRY, List.of(
                "Capital Federal", "Buenos Aires",
                "Catamarca", "Cordoba", "Corrientes",
                "Chaco", "Chubut", "Entre Rios",
                "Formosa", "Jujuy", "La Pampa",
                "La Rioja", "Mendoza", "Misiones",
                "Neuquen", "Rio Negro", "Salta",
                "San Juan", "San Luis", "Santa Cruz",
                "Santa Fe", "Santiago del Estero",
                "Tucuman", "Tierra del Fuego"
        ));
    }

    public static int randomNumberBetween(int low, int high) {
        return r.nextInt(high - low) + low;
    }

    @SneakyThrows
    protected ResultActions asyncDispatch(MvcResult result) {
        MockAsyncContext ctx = (MockAsyncContext) result.getRequest().getAsyncContext();
        assert ctx != null;
        for (AsyncListener listener : ctx.getListeners()) {
            listener.onTimeout(null);
        }

        return mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(result));
    }

    @SneakyThrows
    protected byte[] asByteArray(Object value) {
        return mapper.writeValueAsBytes(value);
    }

    @SneakyThrows
    protected <T> T map(MvcResult result, Class<T> classValue) {
        return mapper.readValue(result.getResponse().getContentAsByteArray(), classValue);
    }

    @SneakyThrows
    protected <T> List<T> mapList(MvcResult result, TypeReference<List<T>> typeReference) {
        return mapper.readValue(result.getResponse().getContentAsByteArray(), typeReference);
    }

    @SneakyThrows
    protected <T> RestResponsePage<T> mapPage(MvcResult result, TypeReference<RestResponsePage<T>> typeReference) {
        return mapper.readValue(result.getResponse().getContentAsByteArray(), typeReference);
    }
}
