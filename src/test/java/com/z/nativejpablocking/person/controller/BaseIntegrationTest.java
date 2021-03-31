package com.z.nativejpablocking.person.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.z.nativejpablocking.person.dao.PersonDAO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIntegrationTest {
    protected ObjectMapper mapper;
    protected MockMvc webTestClient;
    protected PersonDAO personDAO;

    @BeforeAll
    void setup(@Autowired WebApplicationContext context, @Autowired ObjectMapper mapper, @Autowired PersonDAO personDAO) {
        this.mapper = mapper;
        this.personDAO = personDAO;
        this.webTestClient = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @SneakyThrows
    JsonNode toJsonNode(MvcResult result) {
        return mapper.readValue(result.getResponse().getContentAsByteArray(), JsonNode.class);
    }
}
